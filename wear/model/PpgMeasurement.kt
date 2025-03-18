package com.dacslab.android.sleeping.wear.model


data class PpgMeasurement(
    val avgPpgGreen: Float, // GREEN PPG 평균 값
    val avgPpgRed: Float, // RED PPG 평균 값
    val avgPpgIr: Float, // IR PPG 평균 값
    val ppgStatus: Boolean   // 리드 오프 상태
)