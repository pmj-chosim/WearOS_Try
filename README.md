# WearOS_Try

## ! C:~\Sleeping\Sleeping\wear\src\main\java\com\dacslab\android\sleeping\wear 내부 파일 수정

### 추가 1. model/HealthServiceManager.kt(43~47line)
```kotlin
//추가추가추가추가 PPG 지원여부 확인 메소스
fun isPPGSupported(): Boolean{
   val capability = healthTrackingService?.trackingCapability ?: return false
   return capability.supportHealthTrackerTypes.contains(HealthTrackerType.PPG_ON_DEMAND)
```

### 추가 2. PpgMeasurement.kt,  PpgTrackerListener.kt 파일 새로 작성

### 추가 3.model/di/AppModule
58~71 line 추가
