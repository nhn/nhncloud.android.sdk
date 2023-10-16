# SecureTextView 사용

개인정보 보호를 위해 신분증 데이터는 일반 문자열이 아닌 SecureString 객체로 반환됩니다.
신분증 인식 정보를 String 객체로 생성하여 사용하면 보안에 취약하며, 데이터를 화면에 표시하기 위해 SecureTextView를 사용할 수 있습니다.

<br>

## SecureTextView

```xml
<com.nhncloud.android.ocr.SecureTextView
    android:id="@+id/id_card_name_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:textColor="#ffffff"
    app:textSize="15sp"
    app:textStyle="bold"/>
```

SecureTextView의 setText 메서드를 통해 표시할 텍스트를 설정합니다.
```kotlin
val name = idCardData.name

val idCardNameView = findViewById<SecureTextView>(id_card_name_view)
idCardNameView.setText(name)
```

## SecureTextGroup
여러 줄의 텍스트를 표시해야 한다면 SecureTextGroup을 사용할 수 있습니다.
```xml
<com.nhncloud.android.ocr.SecureTextGroup
    android:id="@+id/id_card_license_type_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:textColor="#ffffff"
    app:textSize="15sp"
    app:textStyle="bold"/>
```

SecureTextGroup의 addTextViews 메서드는 배열을 파라미터로 받아 하나의 요소마다 한 줄의 텍스트로 설정합니다.
```kotlin
//The license type is a SecureString array.
val licenseType = idCardData.idCardLicenseType.split('/')

val idCardLicenseTypeView = findViewById<SecureTextGroup>(id_card_license_type_view)
idCardLicenseTypeView.addTextViews(licenseType)
```