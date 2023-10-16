# SecureTextView 사용

개인정보 보호를 위해 신용카드 데이터는 일반 문자열이 아닌 SecureString 객체로 반환됩니다.
신용카드 인식 정보를 String 객체로 생성하여 사용하면 보안에 취약하며, 데이터를 화면에 표시하기 위해 SecureTextView를 사용할 수 있습니다.

<br>

## SecureTextView

```xml
<com.nhncloud.android.ocr.SecureTextView
    android:id="@+id/credit_card_first_numbers_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:textColor="#ffffff"
    app:textSize="15sp"
    app:textStyle="bold"/>
```

SecureTextView의 setText 메서드를 통해 표시할 텍스트를 설정합니다.
```kotlin
val firstCardNumbers = creditCard.cardNumbers[0]

val creditCardFirestNumbersView = findViewById<SecureTextView>(credit_card_first_numbers_view)
creditCardFirestNumbersView.setText(firstCardNumbers)
```