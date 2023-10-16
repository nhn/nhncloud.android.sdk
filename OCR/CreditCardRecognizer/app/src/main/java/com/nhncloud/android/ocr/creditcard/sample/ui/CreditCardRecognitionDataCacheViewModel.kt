package com.nhncloud.android.ocr.creditcard.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhncloud.android.ocr.creditcard.CreditCardRecognitionData

class CreditCardRecognitionDataCacheViewModel : ViewModel() {
    private val _creditCardRecognitionData = MutableLiveData<CreditCardRecognitionData?>()
    val creditCardRecognitionData: LiveData<CreditCardRecognitionData?> = _creditCardRecognitionData

    fun setData(data: CreditCardRecognitionData?) {
        _creditCardRecognitionData.value = data
    }
}