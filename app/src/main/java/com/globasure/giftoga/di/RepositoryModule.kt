package com.globasure.giftoga.di

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.domain.repository.BulkRepository
import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.domain.repository.GiftCardRepository
import com.globasure.giftoga.domain.repository.PaymentTokenRepository
import com.globasure.giftoga.domain.repository.SettingRepository
import com.globasure.giftoga.domain.repository.TransactionRepository
import com.globasure.giftoga.domain.repository.TransferRepository
import com.globasure.giftoga.domain.repository.VerificationRepository
import com.globasure.giftoga.domain.repository.WalletRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.repository.AuthRepositoryImpl
import com.globasure.giftoga.network.repository.BulkRepositoryImpl
import com.globasure.giftoga.network.repository.DisputeRepositoryImpl
import com.globasure.giftoga.network.repository.GiftCardRepositoryImpl
import com.globasure.giftoga.network.repository.PaymentTokenRepositoryImpl
import com.globasure.giftoga.network.repository.SettingRepositoryImpl
import com.globasure.giftoga.network.repository.TransactionRepositoryImpl
import com.globasure.giftoga.network.repository.TransferRepositoryImpl
import com.globasure.giftoga.network.repository.VerificationRepositoryImpl
import com.globasure.giftoga.network.repository.WalletRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthenticateRepository(apiHelper: ApiHelper): AuthRepository {
        return AuthRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideBulkRepository(apiHelper: ApiHelper): BulkRepository {
        return BulkRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideDisputeRepository(apiHelper: ApiHelper): DisputeRepository {
        return DisputeRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideGiftCardRepository(apiHelper: ApiHelper): GiftCardRepository {
        return GiftCardRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun providePaymentTokenRepository(apiHelper: ApiHelper): PaymentTokenRepository {
        return PaymentTokenRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideSettingRepository(apiHelper: ApiHelper): SettingRepository {
        return SettingRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(apiHelper: ApiHelper): TransactionRepository {
        return TransactionRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideTransferRepository(apiHelper: ApiHelper): TransferRepository {
        return TransferRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideVerificationRepository(apiHelper: ApiHelper): VerificationRepository {
        return VerificationRepositoryImpl(apiHelper)
    }

    @Provides
    @Singleton
    fun provideWalletRepository(apiHelper: ApiHelper): WalletRepository {
        return WalletRepositoryImpl(apiHelper)
    }

}