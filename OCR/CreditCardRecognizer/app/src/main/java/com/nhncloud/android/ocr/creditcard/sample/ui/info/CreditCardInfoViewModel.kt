package com.nhncloud.android.ocr.creditcard.sample.ui.info

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhncloud.android.ocr.creditcard.CreditCardRecognitionData
import com.nhncloud.android.ocr.security.SecureByteArray
import com.nhncloud.android.ocr.security.SecureString
import com.nhncloud.android.ocr.security.Secures

class CreditCardInfoViewModel : ViewModel() {
    private val _originBitmap = MutableLiveData<Bitmap>()
    val originBitmap: LiveData<Bitmap> = _originBitmap

    private val _cardNumbers = MutableLiveData<SecureString?>()
    val cardNumbers: LiveData<SecureString?> = _cardNumbers

    private val _expirationDate = MutableLiveData<CreditCardRecognitionData.ExpirationDate?>()
    val expirationDate: LiveData<CreditCardRecognitionData.ExpirationDate?> = _expirationDate

    fun setCreditCardRecognitionData(data: CreditCardRecognitionData) {
        // Credit card images.
        _originBitmap.value = data.originBitmap

        // Credit card recognition data.
        val values = data.cardNumbers.map { it.value }
        _cardNumbers.value = createCardNumbersWithSpacing(values)
        _expirationDate.value = data.expirationDate
    }

    private fun createCardNumbersWithSpacing(cardNumbers: List<SecureString>?): SecureString? {
        var fullCardNumber : SecureString? = null
        val spacing = Secures.asSecureString(SecureByteArray.wrap(" ".toByteArray()))
        cardNumbers?.forEachIndexed { index, cardNumber ->
            if (index == 0) {
                fullCardNumber = cardNumber
                return@forEachIndexed
            }
            fullCardNumber = fullCardNumber?.concat(spacing)
            fullCardNumber = fullCardNumber?.concat(cardNumber)
        }
        return fullCardNumber
    }
}