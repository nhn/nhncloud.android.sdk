package com.nhncloud.android.ocr.idcard.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhncloud.android.ocr.idcard.IdCardRecognitionData

class IdCardDataCacheViewModel : ViewModel() {
    private val _idCardRecognitionData = MutableLiveData<IdCardRecognitionData?>()
    val idCardRecognitionData: LiveData<IdCardRecognitionData?> = _idCardRecognitionData

    fun setData(data: IdCardRecognitionData?) {
        _idCardRecognitionData.value = data
    }
}