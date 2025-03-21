# WearOS_Try

 ! `C:~\Sleeping\Sleeping\wear\src\main\java\com\dacslab\android\sleeping\wear\model` 내부 파일 수정함. 해당 경로 안의 파일들을 수정한 것!

---
<br> <br>

### 추가 1. model/HealthServiceManager.kt(43~47line)
[HealthServiceManager.kt](https://github.com/pmj-chosim/WearOS_Try/blob/main/wear/model/HealthServiceManager.kt)
  
```kotlin
//추가 #Feature: PPG_Green Support 여부, PPG는 디멘드랑 컨티뉴어스 둘 다 확인해 줘야 함
    fun isPPG_Green_Supported(): Boolean{
        val capability = healthTrackingService?.trackingCapability ?: return false
        return capability.supportHealthTrackerTypes.contains(HealthTrackerType.PPG_CONTINUOUS)
    }
    // 추가 #Feature: PPG_IR, PPG_Red Support 여부
    fun isPPG_Green_Supported(): Boolean{
        val capability = healthTrackingService?.trackingCapability ?: return false
        return capability.supportHealthTrackerTypes.contains(HealthTrackerType.PPG_ON_DEMAND)
    }
    //추가 #Feature: ACC Support 여부
    fun isAACSupported(): Boolean{
        val capability = healthTrackingService?.trackingCapability ?: return false
        return capability.supportHealthTrackerTypes.contains(HealthTrackerType.ACCELEROMETER_CONTINUOUS)
    }
```


 ![image](https://github.com/user-attachments/assets/8c08837c-e521-47e2-9715-59882d5835fd)  
 ![image](https://github.com/user-attachments/assets/ff66aeac-2558-442a-a420-3dad09f47d35)  



삼성 헬스 문서에 PPG 지원 확인법 PPG_ON_DEMAND / PPG_CONTINUOUS 로 나와 있음.  
ACC는 ACCELEROMETER_CONTINUOUS로 나와 있음
  
<br> 

### 추가 2. PpgMeasurement.kt,  PpgTrackerListener.kt 파일 새로 작성
- [PpgMeasurement.kt](https://github.com/pmj-chosim/WearOS_Try/blob/main/wear/model/PpgMeasurement.kt) : PPG 데이터 클래스 정의
- [PpgTrackerListener.kt](https://github.com/pmj-chosim/WearOS_Try/blob/main/wear/model/PpgTrackerListener.kt): PPG 데이터 받고 저장 및 값 로그로 찍기
  
> 참고 정보1:  
> [카이스트랩 코드](https://github.com/Kaist-ICLab/android-tracker/blob/085d74019af84b633a111af4b8d6d5bbcaa61c37/tracker-library/src/main/java/kaist/iclab/tracker/sensor/galaxywatch/PPGSensor.kt#L62)  
> 참고 정보2: 삼성 헬스 문서  
> ![image](https://github.com/user-attachments/assets/e33f5197-5d50-4111-8357-65a53c8bf038)

  
<br>

### 추가 3.model/di/AppModule
[di/AppModule.kt](https://github.com/pmj-chosim/WearOS_Try/blob/main/wear/model/di/AppModule.kt)
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

<br>
<br>


--- 

wear 코드 관련 추가 참고 사항이 필요할 시
[참고](https://docs.google.com/document/d/1Pscza2ya4whP6zy3GDtIvgyv-L02UGgF80bUyyWoGGo/edit?usp=sharing) 가능.

