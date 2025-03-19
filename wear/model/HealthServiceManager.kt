package com.dacslab.android.sleeping.wear.model

import android.content.Context
import android.util.Log
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTrackingService
import com.samsung.android.service.health.tracking.data.HealthTrackerType

// HealthServiceManager 클래스는 헬스케어 서비스를 관리하는 역할.
// 이 클래스는 서비스에 연결하고, 연결을 끊고, ECG 추적이 가능한지 확인하는 기능 제공.
class HealthServiceManager(
    private val context: Context, // Context는 앱의 환경을 제공하며, 서비스와의 연결을 위해 필요.
    private val listener: ConnectionListener // 연결 상태를 처리할 리스너
) {
    // HealthTrackingService 객체를 저장할 변수
    private var healthTrackingService: HealthTrackingService? = null

    // 헬스케어 서비스에 연결하는 메서드
    fun connectService() {
        try {
            // HealthTrackingService 객체를 생성하고 연결 시도.
            healthTrackingService = HealthTrackingService(listener, context)
            healthTrackingService?.connectService() // 서비스 연결
        } catch (t: Throwable) {
            // 연결 과정에서 오류가 발생한 경우 로그로 출력
            Log.e("HealthServiceManager", "Error connecting service: ${t.message}")
        }
    }

    // 헬스케어 서비스의 연결을 끊는 메서드
    fun disconnectService() {
        healthTrackingService?.disconnectService() // 서비스 연결 해제
    }

    // ECG(심전도) 추적 기능이 지원되는지 확인하는 메서드
    fun isEcgSupported(): Boolean {
        // 서비스가 연결되어 있고 추적 기능이 제공되는지 확인
        val capability = healthTrackingService?.trackingCapability ?: return false
        // ECG_ON_DEMAND 기능이 지원되는지 확인
        return capability.supportHealthTrackerTypes.contains(HealthTrackerType.ECG_ON_DEMAND)
    }

    //추가추가추가추가 PPG 지원여부 확인 메소스 PPG_DEMAND -> PPG_CONTINUOUS
    fun isPPGSupported(): Boolean{
        val capability = healthTrackingService?.trackingCapability ?: return false
        return capability.supportHealthTrackerTypes.contains(HealthTrackerType.PPG_CONTINUOUS)
    }
    //추가
    fun isAACSupported(): Boolean{
        val capability = healthTrackingService?.trackingCapability ?: return false
        return capability.supportHealthTrackerTypes.contains(HealthTrackerType.ACCELEROMETER_CONTINUOUS)
    }
}
