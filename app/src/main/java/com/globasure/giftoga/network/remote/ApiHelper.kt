package com.globasure.giftoga.network.remote

import com.globasure.giftoga.network.request.AllowDailyTransferRequest
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.CardOtpRequest
import com.globasure.giftoga.network.request.CardPinRequest
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.ChargeFeesRequest
import com.globasure.giftoga.network.request.ChargeGiftCardRequest
import com.globasure.giftoga.network.request.ChargeWalletRequest
import com.globasure.giftoga.network.request.GetLinkForBulkRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.network.request.GetLinkForSalesRequest
import com.globasure.giftoga.network.request.LoginRequest
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.network.request.ResetPasswordRequest
import com.globasure.giftoga.network.request.SearchAllCasesRequest
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.network.request.SignUpOtherRequest
import com.globasure.giftoga.network.request.SignUpPersonalRequest
import com.globasure.giftoga.network.request.TransactionRequest
import com.globasure.giftoga.network.request.TransferCommissionRequest
import com.globasure.giftoga.network.request.UpdateAddressRequest
import com.globasure.giftoga.network.request.UpdateNotificationRequest
import com.globasure.giftoga.network.request.UpdateProfileRequest
import com.globasure.giftoga.network.request.ValidateBankAccountRequest
import com.globasure.giftoga.network.request.ValidateTokenRequest
import com.globasure.giftoga.network.response.AddCardResponse
import com.globasure.giftoga.network.response.AllCasesResponse
import com.globasure.giftoga.network.response.AllReplyResponse
import com.globasure.giftoga.network.response.BankListResponse
import com.globasure.giftoga.network.response.BuyGiftCardResponse
import com.globasure.giftoga.network.response.CardOtpResponse
import com.globasure.giftoga.network.response.CardPinResponse
import com.globasure.giftoga.network.response.CaseDetailResponse
import com.globasure.giftoga.network.response.CategoriesResponse
import com.globasure.giftoga.network.response.CategoryResponse
import com.globasure.giftoga.network.response.ChargeBankTransferResponse
import com.globasure.giftoga.network.response.ChargeCardResponse
import com.globasure.giftoga.network.response.ChargeFeesResponse
import com.globasure.giftoga.network.response.ChargePayPalResponse
import com.globasure.giftoga.network.response.ChargeWalletResponse
import com.globasure.giftoga.network.response.CloseCaseResponse
import com.globasure.giftoga.network.response.CountryListResponse
import com.globasure.giftoga.network.response.DepositResponse
import com.globasure.giftoga.network.response.ExportTransactionResponse
import com.globasure.giftoga.network.response.GiftcardDetailResponse
import com.globasure.giftoga.network.response.ListCardResponse
import com.globasure.giftoga.network.response.LoginResponse
import com.globasure.giftoga.network.response.MerchantDetailResponse
import com.globasure.giftoga.network.response.MerchantsResponse
import com.globasure.giftoga.network.response.MyGiftCardResponse
import com.globasure.giftoga.network.response.OpenCaseResponse
import com.globasure.giftoga.network.response.PayLinkResponse
import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.network.response.PurchaseBulkResponse
import com.globasure.giftoga.network.response.RedeemGiftCardResponse
import com.globasure.giftoga.network.response.RemoveCardResponse
import com.globasure.giftoga.network.response.ReplyCaseResponse
import com.globasure.giftoga.network.response.ReplyDetailResponse
import com.globasure.giftoga.network.response.ResetTokenResponse
import com.globasure.giftoga.network.response.SendGiftCardResponse
import com.globasure.giftoga.network.response.SignUpResponse
import com.globasure.giftoga.network.response.StateListResponse
import com.globasure.giftoga.network.response.TransactionResponse
import com.globasure.giftoga.network.response.TransferCommissionResponse
import com.globasure.giftoga.network.response.ValidateBankAccountResponse
import com.globasure.giftoga.network.response.ValidateBulkFileResponse
import com.globasure.giftoga.network.response.ValidateTokenResponse
import com.globasure.giftoga.network.response.VerificationResponse
import okhttp3.MultipartBody

interface ApiHelper {

    suspend fun signUpPersonal(request: SignUpPersonalRequest): SignUpResponse

    suspend fun signUpBusiness(request: SignUpOtherRequest): SignUpResponse

    suspend fun signUpAgent(request: SignUpOtherRequest): SignUpResponse

    suspend fun signUpMerchant(request: SignUpOtherRequest): SignUpResponse

    suspend fun login(request: LoginRequest): LoginResponse

    suspend fun refreshToken(refresh_token: String): LoginResponse

    suspend fun getAccountDetails(): ProfileResponse

    suspend fun requestResetToken(email: String): ResetTokenResponse

    suspend fun validateToken(request: ValidateTokenRequest): ValidateTokenResponse

    suspend fun resetPassword(request: ResetPasswordRequest): ResetTokenResponse

    suspend fun getCountryList(): CountryListResponse

    suspend fun getStatesByCountry(country: String): StateListResponse

    suspend fun requestConfirmAccount(security_token: String): SignUpResponse

    suspend fun updateProfile(request: UpdateProfileRequest): ProfileResponse

    suspend fun updateAddress(request: UpdateAddressRequest): ProfileResponse

    suspend fun updateAvatar(avatar: MultipartBody.Part?): ProfileResponse

    suspend fun updateNotification(request: UpdateNotificationRequest): ProfileResponse

    suspend fun getBankList(): BankListResponse

    suspend fun validateBankAccount(request: ValidateBankAccountRequest): ValidateBankAccountResponse

    suspend fun allowDailyTransfer(request: AllowDailyTransferRequest): ProfileResponse

    suspend fun verifyBusiness(
        document_file: MultipartBody.Part?,
        document_type: String,
        bvn: String,
        rc_number: String
    ): VerificationResponse

    suspend fun verifyCustomer(
        document_file: MultipartBody.Part?,
        document_type: String,
        bvn: String
    ): VerificationResponse

    suspend fun getVerificationStatus(): VerificationResponse

    suspend fun getPayLinkForSales(request: GetLinkForSalesRequest): PayLinkResponse

    suspend fun getPayLinkForBulk(request: GetLinkForBulkRequest): PayLinkResponse

    suspend fun getLinkForPayment(forPaymentRequest: GetLinkForPaymentRequest): PayLinkResponse

    suspend fun chargeWallet(request: ChargeWalletRequest): ChargeWalletResponse

    suspend fun chargeGiftCard(request: ChargeGiftCardRequest): ChargeWalletResponse

    suspend fun chargeCard(request: ChargeCardRequest): ChargeCardResponse

    suspend fun chargeFees(request: ChargeFeesRequest): ChargeFeesResponse

    suspend fun cardPin(request: CardPinRequest): CardPinResponse

    suspend fun cardOtp(request: CardOtpRequest): CardOtpResponse

    suspend fun chargeBankTransfer(pay_link: String): ChargeBankTransferResponse

    suspend fun confirmBankTransfer(reference: String): ChargeBankTransferResponse

    suspend fun chargePayPal(pay_link: String): ChargePayPalResponse

    suspend fun confirmPayPal(reference: String, order_id: String): ChargePayPalResponse

    suspend fun deposit(payment_token: String): DepositResponse

    suspend fun getAllTransaction(request: TransactionRequest): TransactionResponse

    suspend fun getTransactionDetail(reference: String): DepositResponse

    suspend fun exportTransaction(request: TransactionRequest): ExportTransactionResponse

    suspend fun transferCommission(request: TransferCommissionRequest): TransferCommissionResponse

    suspend fun transferRedeem(request: TransferCommissionRequest): TransferCommissionResponse

    suspend fun getBanks(country: String = "Nigeria"): BankListResponse

    suspend fun validateBankAccount(
        paymentMethod: String = "bank",
        bankCode: String,
        accountId: String
    ): ValidateBankAccountResponse

    suspend fun getAllMerchants(request: MerchantsRequest): MerchantsResponse

    suspend fun getMerchantDetail(merchant_id: String): MerchantDetailResponse

    suspend fun getMerchantByCategoryId(category_id: String): MerchantsResponse

    suspend fun getAllCategories(): CategoriesResponse

    suspend fun getCategoryDetail(category_id: String): CategoryResponse

    suspend fun getGiftCardDetail(giftcard: String): GiftcardDetailResponse

    suspend fun buyGiftCard(request: BuyGiftcardRequest): BuyGiftCardResponse

    suspend fun redeemGiftCard(giftcard: String, amount: String): RedeemGiftCardResponse

    suspend fun getMyGiftCard(giftcard: MyGiftCardRequest): MyGiftCardResponse

    suspend fun sendMyGiftCard(request: SendGiftCardRequest): SendGiftCardResponse

    suspend fun validateBulkFile(merchant: String, excelFile: MultipartBody.Part?): ValidateBulkFileResponse

    suspend fun purchasesBulk(payment_token: String): PurchaseBulkResponse

    suspend fun addBankCard(payment_token: String): AddCardResponse

    suspend fun getListBankCards(order_by: String, page: Int, per_page: Int): ListCardResponse

    suspend fun deleteBankCard(card_token: String): RemoveCardResponse

    suspend fun openCase(request: OpenCaseRequest): OpenCaseResponse

    suspend fun getCaseDetail(case_id: String): CaseDetailResponse

    suspend fun replyCase(case_id: String, comment: String): ReplyCaseResponse

    suspend fun getReplyDetail(case_id: String, comment_id: String): ReplyDetailResponse

    suspend fun getAllReply(case_id: String): AllReplyResponse

    suspend fun closeCase(case_id: String): CloseCaseResponse

    suspend fun searchAllCases(request: SearchAllCasesRequest): AllCasesResponse
}