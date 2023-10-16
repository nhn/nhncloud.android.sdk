# 신분증 진위 확인 수동 입력 API 가이드

사용자가 진위 확인할 신분증 데이터를 직접 입력하여 사용할 수 있습니다.<br>

<br>

## 사전 준비
1. [NHN Cloud Console](https://console.nhncloud.com)에서 [AI Service > OCR] 서비스를 활성화합니다.
2. OCR 콘솔에서 AppKey와 SecretKey를 확인합니다.

<br>

## 지원 환경
NHN Cloud ID Card Authenticator는 Android 5.0 이상(API level 21 이상)에서 동작합니다.

<br>

## 프로젝트 설정

### 의존성 추가

앱의 build.gradle 파일에 nhncloud-idcard-recognizer 혹은 nhncloud-idcard-authenticator 의존성을 추가합니다.

```groovy
dependencies {
    ...
    // NHN Cloud ID Card Recognizer
    implementation 'com.nhncloud.android:nhncloud-idcard-recognizer:1.7.0'
    // NHN Cloud ID Card Authenticator
    implementation 'com.nhncloud.android:nhncloud-idcard-authenticator:1.7.0'
}
```

<br>

## IdCardAuthenticityService 인스턴스 생성
NhnCloudOcrServices 혹은 IdCardAuthenticators를 통해 IdCardAuthenticityService 인스턴스를 생성합니다. 

### NhnCloudOcrServices 사용 
```kotlin
val authenticityService = NhnCloudOcrServices.newBuilder(context)
        .appKey(APP_KEY)
        .secretKey(SECRET_KEY)
        .createIdCardAuthenticityService()
```

### IdCardAuthenticators 사용 
```kotlin
val authenticator = IdCardAuthenticators.newBuilder(context)
        .appKey(APP_KEY)
        .secretKey(SECRET_KEY)
        .createIdCardAuthenticityService()        
```
<br>

## IdCardAuthenticator 인스턴스 생성 
NhnCloudOcr를 사용하는 경우 아래와 같이 IdCardAuthenticator 인스턴스를 생성할 수 있습니다. 

### NhnCloudOcr 사용 
```kotlin
val authenticator = NhnCloudOcr.newBuilder(context)
        .appKey(APP_KEY)
        .secretKey(SECRET_KEY)
        .createIdCardAuthenticator()
```

<br>

## 신분증 진위 확인 
신분증 데이터를 직접 입력하여 진위 확인을 요청할 수 있습니다.

### SecureString 생성
개인정보 보호를 위해 신분증 데이터는 일반 문자열이 아닌 SecureString 객체로 전달해야 하며, 
SecureString 객체 생성 후 원본 byte 배열은 반드시 초기화해야 합니다.  

```kotlin
//plainText is ByteArray.
val securePlainText = SecureByteArray.wrap(plainText)
val secureString = Secures.asSecureString(securePlainText)
securePlainText.clear()
```

<br>

### 진위 확인 요청 데이터
| Parameter | Type | Descriptions | idType |
| --- | --- | --- | --- |
| name | SecureString | 이름 | 공통 |
| residentNumber | SecureString | 주민등록번호<br>- resident(주민등록증)의 경우 주민등록번호 숫자 13자리<br>- driver(운전면허증)의 경우 주민등록번호 앞 6자리와 뒤 첫 번째 1자리를 조합한 숫자 7자리<br> | 공통 |
| issueDate | SecureString | 주민등록증 발급 일자(YYYYMMDD) | resident |
| driverLicenseNumber | SecureString | 12자리 운전면허번호 | driver |
| serialNum | SecureString | 5~6자리 암호 일련번호 | driver |
| apiKey | String | API Gateway에서 발급 받은 Primary API Key 또는 Secondary API Key | 공통 |


<br>

### 진위 확인 
진위 확인을 요청할 신분증 타입에 따라 authenticateResidentTypeWithApiKey 혹은 authenticateDriverTypeWithApiKey 메서드를 호출합니다. 
신분증 진위 확인 결과는 Boolean 타입으로 반환됩니다.

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    try {
        //Use authenticity result
        isAuthenticity = idCardAuthenticityService.authenticateResidentTypeWithApiKey(
                        name,
                        residentNumber,
                        issueData,
                        apiKey)
    } catch (e : OcrException) {
        //Authenticity Error
    }
}
```

authenticateAsync 메서드로 호출을 원할 경우 IdCardAuthenticityCallback을 구현하여 결과를 받을 수 있습니다.

```kotlin
idCardAuthenticityService.authenticateResidentTypeWithApiKeyAsync(
    name,
    residentNumber,
    issueData,
    apiKey
){ result, isAuthenticity ->
    if (result.isSuccess) {
        //Use authenticity result
    } else {
        //Authenticity Error
    }
}
```