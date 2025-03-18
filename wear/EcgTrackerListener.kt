package com.dacslab.android.sleeping.wear.model

import android.util.Log
import androidx.annotation.NonNull
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.ValueKey
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EcgTrackerListener @Inject constructor() : HealthTracker.TrackerEventListener {

    companion object {
        private const val APP_TAG = "ECG Monitor" // 로그에 사용할 앱 태그
        private const val NO_CONTACT = 5 // 리드 오프 상태를 나타내는 값 (ECG 장치가 피부에서 떨어졌을 때)
    }

    // 평균 ECG 값과 리드 오프 상태를 저장할 변수
    private val avgEcg = AtomicReference(0f)  // 평균 ECG 값을 저장하는 변수
    private val leadOff = AtomicBoolean(true) // 리드 오프 상태를 저장하는 변수 (초기값: true)

    // 오류 콜백 함수 (기본적으로 빈 람다로 설정)
    var onPermissionError: () -> Unit = {}
    var onSdkPolicyError: () -> Unit = {}

    /**
     * ECG 데이터가 수신될 때 호출되는 메서드.
     * 리스트에 포함된 데이터 포인트를 기반으로 평균 ECG 값과 리드 오프 상태를 계산.
     */
    override fun onDataReceived(@NonNull list: List<DataPoint>) {
        // 데이터 포인트가 비어 있으면 함수 종료
        if (list.isEmpty()) return

        // 첫 번째 데이터 포인트에서 리드 오프 상태를 확인 (리드 오프 값: 5는 피부에서 떨어졌다는 의미)
        val isLeadOffValue = list[0].getValue(ValueKey.EcgSet.LEAD_OFF)

        // 리드 오프 상태가 NO_CONTACT(5)이면 리드 오프 상태를 true로 설정
        if (isLeadOffValue == NO_CONTACT) {
            leadOff.set(true)  // 리드 오프 상태로 설정
            return  // 데이터를 처리할 필요 없음
        } else {
            leadOff.set(false) // 리드 오프 상태가 아니면 false로 설정
        }

        // ECG 값들의 평균을 계산
        var sum = 0f
        list.forEach { dataPoint ->
            sum += dataPoint.getValue(ValueKey.EcgSet.ECG_MV)  // ECG 값들을 모두 더함
        }
        avgEcg.set(sum / list.size)  // 평균 값을 avgEcg에 저장
    }

    /**
     * 데이터 플러시가 완료되면 호출되는 메서드.
     * 현재는 단순히 로그를 출력.
     */
    override fun onFlushCompleted() {
        Log.i(APP_TAG, "onFlushCompleted called")  // 데이터 플러시 완료 로그 출력
    }

    /**
     * 오류가 발생했을 때 호출되는 메서드.
     * 발생한 오류에 따라 적절한 콜백 함수를 실행.
     */
    override fun onError(trackerError: HealthTracker.TrackerError) {
        Log.i(APP_TAG, "onError called: $trackerError")  // 오류가 발생하면 로그 출력
        when (trackerError) {
            // 권한 오류가 발생하면 onPermissionError 콜백을 실행
            HealthTracker.TrackerError.PERMISSION_ERROR -> onPermissionError()
            // SDK 정책 오류가 발생하면 onSdkPolicyError 콜백을 실행
            HealthTracker.TrackerError.SDK_POLICY_ERROR -> onSdkPolicyError()
            else -> {}  // 그 외의 오류는 특별히 처리하지 않음
        }
    }

    /**
     * 외부에서 현재 평균 ECG 값을 가져올 수 있는 함수.
     */
    fun getCurrentEcgAverage(): Float = avgEcg.get()  // 평균 ECG 값 반환

    /**
     * 외부에서 리드 오프 상태를 확인할 수 있는 함수.
     */
    fun isLeadOff(): Boolean = leadOff.get()  // 리드 오프 상태 반환
}
