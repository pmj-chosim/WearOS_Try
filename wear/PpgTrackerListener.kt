package com.dacslab.android.sleeping.wear.model

import android.util.Log
import androidx.annotation.NonNull
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.ValueKey
import java.security.KeyStore.TrustedCertificateEntry
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PpgTrackerListener @Inject constructor() : HealthTracker.TrackerEventListener {
    companion object {
        private const val APP_TAG = "PPG Monitor" // 로그 태그
        private const val STATUS_NO_CONTACT = -3 // 센서가 피부에서 떨어진 상태를 나타내는 값 //넣어야 하는데 모르겠음
        private const val STATUS_SIGNAL_WEAK = -8 // PPG 신호가 약한 상태를 나타내는 값 //넣어야 하는데 모르겠음
        private const val STATUS_SIGNAL_TOO_WEAK = -10 // PPG 신호가 너무 약한 상태를 나타내는 값 //넣어야 하는데 모르겠음
    }

    private val avgPpgGreen = AtomicReference(0f) // GREEN PPG 평균값
    private val avgPpgRed = AtomicReference(0f) // RED PPG 평균값
    private val avgPpgIr = AtomicReference(0f) // IR PPG 평균값
    private val leadOff = AtomicBoolean(true) // 센서 접촉 상태

    // 오류 발생 시 호출될 콜백 함수들
    var onPermissionError: () -> Unit = {}
    var onSdkPolicyError: () -> Unit = {}

    /**
     * PPG 데이터를 수신했을 때 호출되는 메서드
     * - 수신된 데이터에서 각 PPG 채널(GREEN, RED, IR)의 평균값을 계산
     * - 각 채널의 상태를 확인하여 센서 접촉 여부를 판단
     */
    override fun onDataReceived(@NonNull list: List<DataPoint>) {
        if (list.isEmpty()) return

        // 첫 번째 데이터 포인트에서 상태 받음. 데이터 상태가 올바른지 판단해서 isLeadOffValue에 저장.
        var dump=list[0].getValue(ValueKey.PpgSet.Status)
        val isLeadOnorOff = (dump != STATUS_NO_CONTACT && dump != STATUS_SIGNAL_WEAK && dump != STATUS_SIGNAL_TOO_WEAK)
        ppgStatus.set(isLeadOnorOff)



        // 각 채널의 PPG 값 합계를 저장할 변수들
        var sumGreen = 0f
        var sumRed = 0f
        var sumIr = 0f

        // 각 채널의 상태를 저장할 변수들
        var greenStatus = 0
        var redStatus = 0
        var irStatus = 0

        list.forEach { dataPoint ->
            sumGreen += dataPoint.getValue(ValueKey.PpgSet.PPG_GREEN)
            sumRed += dataPoint.getValue(ValueKey.PpgSet.PPG_RED)
            sumIr += dataPoint.getValue(ValueKey.PpgSet.PPG_IR)

            greenStatus = dataPoint.getValue(ValueKey.PpgSet.GREEN_STATUS)
            redStatus = dataPoint.getValue(ValueKey.PpgSet.RED_STATUS)
            irStatus = dataPoint.getValue(ValueKey.PpgSet.IR_STATUS)
        }

        // 각 채널의 평균값 계산
        avgPpgGreen.set(sumGreen / list.size)
        avgPpgRed.set(sumRed / list.size)
        avgPpgIr.set(sumIr / list.size)


        // 로그 출력
        Log.i(APP_TAG, "PPG GREEN mean: ${avgPpgGreen.get()}, Status: $greenStatus")
        Log.i(APP_TAG, "PPG RED mean: ${avgPpgRed.get()}, Status: $redStatus")
        Log.i(APP_TAG, "PPG IR mean: ${avgPpgIr.get()}, Status: $irStatus")
        Log.i(APP_TAG, "Sensor contact status: ${if (ppgStatus.get()) "Off" else "On Contact"}")
    }

    /**
     * 데이터 플러시(초기화)가 완료되었을 때 호출되는 메서드
     * - 현재는 단순히 로그 출력
     */
    override fun onFlushCompleted() {
        Log.i(APP_TAG, "onFlushCompleted called of ECG")
    }

    /**
     * 오류 발생 시 호출되는 메서드
     * - 발생한 오류 유형에 따라 적절한 콜백 함수를 실행
     */
    override fun onError(trackerError: HealthTracker.TrackerError) {
        Log.i(APP_TAG, "onError called: $trackerError")
        when (trackerError) {
            HealthTracker.TrackerError.PERMISSION_ERROR -> onPermissionError()
            HealthTracker.TrackerError.SDK_POLICY_ERROR -> onSdkPolicyError()
            else -> {}
        }
    }

    /**
     * 현재 GREEN PPG 평균값을 반환하는 함수
     */
    fun getCurrentPpgGreenAverage(): Float = avgPpgGreen.get()

    /**
     * 현재 RED PPG 평균값을 반환하는 함수
     */
    fun getCurrentPpgRedAverage(): Float = avgPpgRed.get()

    /**
     * 현재 IR PPG 평균값을 반환하는 함수
     */
    fun getCurrentPpgIrAverage(): Float = avgPpgIr.get()

    /**
     * 현재 센서 접촉 상태를 반환하는 함수
     */
    fun isLeadOff(): Boolean = leadOff.get()

}