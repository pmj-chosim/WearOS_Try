package com.dacslab.android.sleeping.wear.model.di

import android.app.Activity
import android.content.Context
import com.dacslab.android.sleeping.wear.model.EcgTrackerListener
import com.dacslab.android.sleeping.wear.model.HealthServiceConnectionListener
import com.dacslab.android.sleeping.wear.model.HealthServiceManager
import com.jetbrains.exported.JBRApi.Provides
import com.samsung.android.service.health.tracking.ConnectionListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConnectionListener(
        @ApplicationContext activity: Activity
    ): ConnectionListener {
        return HealthServiceConnectionListener(activity)
    }

    @Provides
    @Singleton
    fun provideHealthServiceManager(
        @ApplicationContext context: Context,
        connectionListener: ConnectionListener
    ): HealthServiceManager {
        return HealthServiceManager(context, connectionListener)
    }

    /**
     * EcgTrackerListener를 싱글톤으로 제공한다.
     * 생성자 파라미터가 없는 경우, 직접 new 해서 반환.
     * 또는 @Inject constructor 만으로도 가능하나, 여기서는 명시적 @Provides 사용.
     */
    @Provides
    @Singleton
    fun provideEcgTrackerListener(): EcgTrackerListener {
        // 필요하다면 여기서 콜백 람다를 기본 설정할 수도 있음
        val listener = EcgTrackerListener()
        listener.onPermissionError = {
            // TODO: 권한 오류 시 처리할 로직 (Toast, 로그 등)
        }
        listener.onSdkPolicyError = {
            // TODO: SDK 정책 오류 시 처리할 로직
        }
        return listener
    }

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


}