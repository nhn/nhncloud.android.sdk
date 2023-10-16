package com.nhncloud.android.ocr.creditcard.sample.ui.scan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nhncloud.android.ocr.NhnCloudOcrServices
import com.nhncloud.android.ocr.OcrResult
import com.nhncloud.android.ocr.creditcard.CreditCardRecognitionData
import com.nhncloud.android.ocr.creditcard.CreditCardRecognitionListener
import com.nhncloud.android.ocr.creditcard.CreditCardScanOrientation
import com.nhncloud.android.ocr.creditcard.sample.R
import com.nhncloud.android.ocr.creditcard.sample.ui.Event
import com.nhncloud.android.ocr.creditcard.view.CreditCardRecognitionCameraPreview
import java.io.IOException

class CreditCardScanViewModel(
    application: Application
) : AndroidViewModel(application), CreditCardRecognitionListener {
    private val ocrServices = NhnCloudOcrServices.newBuilder(application)
        .appKey(application.getString(R.string.app_key))
        .secretKey(application.getString(R.string.secret_key))
        .build()

    private val creditCardRecognitionService =
        ocrServices.createCreditCardRecognitionService().apply {
            setCreditCardRecognitionListener(this@CreditCardScanViewModel)
        }

    private val _recognizedEvent = MutableLiveData<Event<CreditCardRecognitionData>>()
    val recognizedEvent: LiveData<Event<CreditCardRecognitionData>> = _recognizedEvent

    private val _scanOrientation = MutableLiveData(CreditCardScanOrientation.HORIZONTAL)
    val scanOrientation: LiveData<Int> = _scanOrientation

    private val _torch = MutableLiveData(false)
    val torch: LiveData<Boolean> = _torch

    fun startRecognitionService(cameraPreview: CreditCardRecognitionCameraPreview) {
        try {
            creditCardRecognitionService.start(cameraPreview)
        } catch (e: IOException) {
            // Camera is not available (in use or does not exist)
        }
    }

    fun stopRecognitionService() {
        creditCardRecognitionService.stop()
        _torch.value = false
    }

    fun releaseRecognitionService() {
        creditCardRecognitionService.release()
    }

    fun toggleScanOrientation() {
        creditCardRecognitionService.apply {
            scanOrientation = if (scanOrientation == CreditCardScanOrientation.HORIZONTAL) {
                CreditCardScanOrientation.VERTICAL
            } else CreditCardScanOrientation.HORIZONTAL
        }
        _scanOrientation.value = creditCardRecognitionService.scanOrientation
    }

    fun toggleTorch() {
        creditCardRecognitionService.apply {
            isTorchModeEnabled = !isTorchModeEnabled
        }
        _torch.value = creditCardRecognitionService.isTorchModeEnabled
    }

    override fun onCreditCardRecognized(result: OcrResult, data: CreditCardRecognitionData?) {
        if (result.isSuccess && isConfident(data!!)) {
            creditCardRecognitionService.stop()
            _recognizedEvent.value = Event(data)
            _torch.value = false
        }
    }

    private fun isConfident(data: CreditCardRecognitionData): Boolean {
        // Returns success if the number of card numbers is greater than or equal to 3
        // and the confidence is greater than or equal to 0.4.
        // American Express is in the format 1234-123456-12345.
        with (data.cardNumbers) {
            if (size < 3) {
                return false
            }
            for (cardNumber in this) {
                if (cardNumber.confidence < 0.4) {
                    return false
                }
            }
        }

        if (data.fullCardNumber.length < 15) {
            return false
        }

        return data.expirationDate?.let {
            !(it.value.isEmpty || it.confidence < 0.4)
        } ?: return false
    }
}