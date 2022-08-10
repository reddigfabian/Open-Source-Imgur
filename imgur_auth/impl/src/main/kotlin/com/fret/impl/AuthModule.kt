package com.fret.impl

import android.app.Application
import android.net.Uri
import com.fret.constants.Constants
import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import net.openid.appauth.*

@Module
@ContributesTo(AppScope::class)
object AuthModule {

    @Provides
    @SingleIn(AppScope::class)
    fun providesAuthorizationServiceConfiguration(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            Uri.parse(Constants.IMGUR_OAUTH2_AUTH_CODE),
            Uri.parse(Constants.IMGUR_OAUTH2_ACCESS_TOKEN)
        )
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideAuthorizationRequest(authConfig: AuthorizationServiceConfiguration): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            authConfig,
            BuildConfig.IMGUR_CLIENT_ID,
            ResponseTypeValues.TOKEN,
            Uri.parse(Constants.AUTH_REDIRECT)
        ).build()
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideAuthState(authConfig: AuthorizationServiceConfiguration): AuthState {
        return AuthState(authConfig)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideAuthService(application: Application): AuthorizationService {
        return AuthorizationService(application)
    }
}