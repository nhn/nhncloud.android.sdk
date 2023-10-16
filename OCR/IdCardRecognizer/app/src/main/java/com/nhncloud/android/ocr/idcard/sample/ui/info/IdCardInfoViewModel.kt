package com.nhncloud.android.ocr.idcard.sample.ui.info

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nhncloud.android.ocr.NhnCloudOcrServices
import com.nhncloud.android.ocr.OcrException
import com.nhncloud.android.ocr.idcard.IdCardDriverRecognitionData
import com.nhncloud.android.ocr.idcard.IdCardRecognitionData
import com.nhncloud.android.ocr.idcard.IdCardResidentRecognitionData
import com.nhncloud.android.ocr.idcard.sample.R
import com.nhncloud.android.ocr.idcard.sample.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class IdCardInfoViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val ocrServices = NhnCloudOcrServices.newBuilder(application)
        .appKey(application.getString(R.string.app_key))
        .secretKey(application.getString(R.string.secret_key))
        .build()

    private var idCardRecognitionData: IdCardRecognitionData? = null
    private var isAuthenticity: Boolean? = null

    private val _originBitmap = MutableLiveData<Bitmap>()
    val originBitmap: LiveData<Bitmap> = _originBitmap

    private val _detectedBitmap = MutableLiveData<Bitmap>()
    val detectedBitmap: LiveData<Bitmap> = _detectedBitmap

    private val _residentRecognitionData = MutableLiveData<IdCardResidentRecognitionData>()
    val residentRecognitionData: LiveData<IdCardResidentRecognitionData> = _residentRecognitionData

    private val _driverRecognitionData = MutableLiveData<IdCardDriverRecognitionData>()
    val driverRecognitionData: LiveData<IdCardDriverRecognitionData> = _driverRecognitionData

    private val _alertEvent = MutableLiveData<Event<String>>()
    val alertEvent: LiveData<Event<String>> = _alertEvent

    fun setIdCardRecognitionData(idCardRecognitionData: IdCardRecognitionData) {
        // ID card images.
        _originBitmap.value = idCardRecognitionData.originBitmap
        _detectedBitmap.value = idCardRecognitionData.detectedBitmap

        when (idCardRecognitionData) {
            is IdCardResidentRecognitionData -> {
                _residentRecognitionData.value = idCardRecognitionData
            }
            is IdCardDriverRecognitionData -> {
                _driverRecognitionData.value = idCardRecognitionData
            }
        }
        this.idCardRecognitionData = idCardRecognitionData
    }

    fun authenticate() {
        isAuthenticity?.let {
            _alertEvent.value = Event("isAuthenticity : $it")
            return
        }

        idCardRecognitionData?.let {
            val service = ocrServices.createIdCardAuthenticityService()
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    isAuthenticity = service.authenticate(it)
                    _alertEvent.postValue(Event("isAuthenticity : $isAuthenticity"))
                } catch (e : OcrException) {
                    _alertEvent.postValue(Event("error : ${e.message}"))
                }
            }
        }
    }
}