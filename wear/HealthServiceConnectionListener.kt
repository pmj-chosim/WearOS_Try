package com.dacslab.android.sleeping.wear.model

import android.app.Activity
import android.widget.Toast
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTrackerException

// HealthServiceConnectionListener는 헬스케어 서비스와의 연결 상태를 처리하는 리스너 클래스.
class HealthServiceConnectionListener(
    private val activity: Activity // 연결 상태에 따라 UI 업데이트를 위해 Activity를 전달받음.
) : ConnectionListener {

    // 연결이 성공했을 때 호출되는 메서드
    override fun onConnectionSuccess() {
        // 연결이 성공했을 때 필요한 처리를 여기에 추가 가능.
        // 예를 들어, 서비스와 연결되었을 때 UI 업데이트 등
    }

    // 연결이 종료되었을 때 호출되는 메서드
    override fun onConnectionEnded() {
        // 연결이 종료되었을 때 필요한 처리를 여기에 추가 가능.
        // 예를 들어, 연결 끊어짐에 따른 UI 업데이트 등
    }

    // 연결 실패 시 호출되는 메서드
    override fun onConnectionFailed(e: HealthTrackerException) {
        // 연결 실패 시 발생한 오류를 처리

        // 특정 오류 코드에 대해 사용자에게 알림을 표시
        if (e.errorCode == HealthTrackerException.OLD_PLATFORM_VERSION ||
            e.errorCode == HealthTrackerException.PACKAGE_NOT_INSTALLED) {
            // 헬스케어 플랫폼 버전이 오래되었거나 설치되지 않은 경우
            Toast.makeText(
                activity, // Activity를 사용하여 화면에 토스트 메시지 표시
                "Health Platform version is outdated or not installed", // 메시지 내용
                Toast.LENGTH_LONG // 메시지 길이 (오래 표시)
            ).show() // 토스트 메시지를 화면에 표시
        }

        // 오류가 해결할 방법이 있는지 확인
        if (e.hasResolution()) {
            // 해결 방법이 있다면, 해당 방법을 자동으로 실행
            e.resolve(activity) // Activity를 사용하여 해결을 시도
        }
    }
}
