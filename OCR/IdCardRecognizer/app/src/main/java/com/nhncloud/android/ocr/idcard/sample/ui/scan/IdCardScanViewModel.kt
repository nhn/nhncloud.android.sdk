package com.nhncloud.android.ocr.idcard.sample.ui.scan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nhncloud.android.ocr.NhnCloudOcrServices
import com.nhncloud.android.ocr.OcrResult
import com.nhncloud.android.ocr.idcard.IdCardDriverRecognitionData
import com.nhncloud.android.ocr.idcard.IdCardRecognitionData
import com.nhncloud.android.ocr.idcard.IdCardRecognitionData.IdCardValue
import com.nhncloud.android.ocr.idcard.IdCardRecognitionListener
import com.nhncloud.android.ocr.idcard.IdCardResidentRecognitionData
import com.nhncloud.android.ocr.idcard.view.IdCardRecognitionCameraPreview
import com.nhncloud.android.ocr.idcard.sample.R
import com.nhncloud.android.ocr.idcard.sample.ui.Event
import java.io.IOException

class IdCardScanViewModel(
    application: Application
) : AndroidViewModel(application), IdCardRecognitionListener {
    private val ocrServices = NhnCloudOcrServices.newBuilder(application)
        .appKey(application.getString(R.string.app_key))
        .secretKey(application.getString(R.string.secret_key))
        .build()

    private val mIdCardRecognitionService =
        ocrServices.createIdCardRecognitionService().apply {
            setIdCardRecognitionListener(this@IdCardScanViewModel)
        }

    private val _recognizedEvent = MutableLiveData<Event<IdCardRecognitionData>>()
    val recognizedEvent: LiveData<Event<IdCardRecognitionData>> = _recognizedEvent

    fun startRecognizer(cameraPreview: IdCardRecognitionCameraPreview) {
        try {
            mIdCardRecognitionService.start(cameraPreview)
        } catch (e: IOException) {
            // Camera is not available (in use or does not exist)
        }
    }

    fun stopRecognizer() {
        mIdCardRecognitionService.stop()
    }

    fun releaseRecognizer() {
        mIdCardRecognitionService.release()
    }

    override fun onIdCardRecognized(result: OcrResult, data: IdCardRecognitionData?) {
        if (result.isSuccess && isConfident(data!!)) {
            mIdCardRecognitionService.stop()
            _recognizedEvent.value = Event(data)
        }
    }

    private fun isConfident(vararg idCardValues: IdCardValue): Boolean {
        return idCardValues.all { it.confidence >= 0.4 }
    }

    private fun isConfident(data: IdCardRecognitionData): Boolean {
        //Returns success if resident number's format is "123456-1234567"
        //and the all confidences is 0.4 or higher
        when (data) {
            is IdCardResidentRecognitionData -> {
                val residentNumbers = data.residentNumber.value.split('-')
                if (residentNumbers.size != 2 ||
                    residentNumbers[0].length != 6 ||
                    residentNumbers[1].length != 7
                ) {
                    return false
                }

                return isConfident(data.name, data.residentNumber, data.issueDate, data.issuer)
            }

            is IdCardDriverRecognitionData -> {
                val residentNumbers = data.residentNumber.value.split('-')
                if (residentNumbers.size != 2 ||
                    residentNumbers[0].length != 6 ||
                    residentNumbers[1].length != 7
                ) {
                    return false
                }

                //driver license number has the format of "12-12-123456-78".
                val driverLicenseNumbers = data.driverLicenseNumber.value.split('-')
                if (driverLicenseNumbers.size != 4 ||
                    driverLicenseNumbers[0].length != 2 ||
                    driverLicenseNumbers[1].length != 2 ||
                    driverLicenseNumbers[2].length != 6 ||
                    driverLicenseNumbers[3].length != 2
                ) {
                    return false
                }

                //The driver type additionally checks the driver license number, serial number, and license type.
                //condition is not checked because there is a case where the value does not exist.
                return isConfident(
                    data.name,
                    data.residentNumber,
                    data.issueDate,
                    data.issuer,
                    data.driverLicenseNumber,
                    data.licenseType,
                    data.serialNumber
                )
            }

            else -> error("Invalid data.")
        }
    }
}