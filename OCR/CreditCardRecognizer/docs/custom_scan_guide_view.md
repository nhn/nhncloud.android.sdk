# 스캔 가이드 뷰 사용자 정의

신용카드 스캔 가이드 뷰를 사용자 정의하여 사용합니다.<br>

<br>

## 백그라운드 색상 변경

스캔 가이드 영역을 제외한 영역은 반투명하게 보여집니다.<br>
이 영역의 색상을 "app:guideBackgroundColor" 속성을 사용하여 설정합니다.<br>

```xml
<com.nhncloud.android.ocr.creditcard.view.CreditCardRecognitionCameraPreview
    android:id="@+id/camera_preview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:guideBackgroundColor="#33000000" />
```

<br>

## 스캔 가이드 뷰 사용자 정의

스캔 가이드 뷰를 CreditCardRecognitionCameraPreview의 하위 뷰로 배치하여 자유롭게 정의할 수 있습니다.<br>
사용자 정의한 가이드 뷰는 "app:guideView" 속성을 사용하여 설정합니다.<br>

> CreditCardRecognitionCameraPreview는 ConstraintLayout을 상속 구현되어있습니다.

스캔 가이드 뷰의 사이즈는 자동으로 조정됩니다.<br>

```xml
<com.nhncloud.android.ocr.creditcard.view.CreditCardRecognitionCameraPreview
    android:id="@+id/camera_preview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:guideView="@id/guide_view">

    <View
        android:id="@+id/guide_view"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginTop="80dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent">

</com.nhncloud.android.ocr.creditcard.view.CreditCardRecognitionCameraPreview>
```

<br>

## 신용카드 검출 시 가이드 뷰 변경

신용카드가 검출되었을 때 스캔 가이드 뷰의 색상 또는 모양을 변경할 수 있습니다.<br>
CreditCardDetectable 인터페이스를 상속 구현하여 setDetected(Boolean)으로 전달되는 값에 따라 가이드 뷰의 색상 또는 모양을 변경합니다.<br>

```kotlin
class CustomGuideView(
    context: Context, attrs: AttributeSet?
): View(context, attrs), CreditCardDetectable {
    override fun setDetected(detected: Boolean) {
        if (detected) {
            color = Color.GREEN
        } else {
            color = Color.WHITE
        }
    }

    ...
}
```
