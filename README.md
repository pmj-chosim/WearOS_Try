# WearOS_Try

 `! C:~\Sleeping\Sleeping\wear\src\main\java\com\dacslab\android\sleeping\wear` 내부 파일 수정함. 해당 경로 안의 파일들을 수정한 것!

### 추가 1. model/HealthServiceManager.kt(43~47line)
```kotlin
//추가추가추가추가 PPG 지원여부 확인 메소스
fun isPPGSupported(): Boolean{
   val capability = healthTrackingService?.trackingCapability ?: return false
   return capability.supportHealthTrackerTypes.contains(HealthTrackerType.PPG_ON_DEMAND)
```

### 추가 2. PpgMeasurement.kt,  PpgTrackerListener.kt 파일 새로 작성

### 추가 3.model/di/AppModule
```kotlin
    @Provides
    @Singleton
    fun providePpgTrackerListener(): PpgTrackerListener {
        // 필요하다면 여기서 콜백 람다를 기본 설정할 수도 있음
        val listener = PpgTrackerListener()
        listener.onPermissionError = {
            // TODO: 권한 오류 시 처리할 로직 (Toast, 로그 등)
        }
        listener.onSdkPolicyError = {
            // TODO: SDK 정책 오류 시 처리할 로직
        }
        return listener
    }
```
58~71 line 추가
