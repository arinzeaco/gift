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
import com.globasure.giftoga.network.request.GetLinkForCardRequest
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
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface API {

    /***************** Authentication *****************/
    @POST("token")
    suspend fun signIn(@Body request: LoginRequest): Response<LoginResponse>

    @POST("token/refresh")
    suspend fun refreshToken(@Body body: RequestBody): Response<LoginResponse>

    @GET("account/details")
    suspend fun getAccountDetails(): Response<ProfileResponse>

    @POST("reset/password/token")
    suspend fun requestResetToken(@Body body: RequestBody): Response<ResetTokenResponse>

    @POST("reset/password/token-validate")
    suspend fun validateToken(@Body request: ValidateTokenRequest): Response<ValidateTokenResponse>

    @POST("reset/password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetTokenResponse>

    @GET("validate/country")
    suspend fun getCountryList(): Response<CountryListResponse>

    @POST("validate/country-state")
    suspend fun getStatesByCountry(@Body body: RequestBody): Response<StateListResponse>

    @POST("signup/personal")
    suspend fun signUp(@Body request: SignUpPersonalRequest): Response<SignUpResponse>

    @POST("signup/business")
    suspend fun signUpBusiness(@Body request: SignUpOtherRequest): Response<SignUpResponse>

    @POST("signup/agent")
    suspend fun signUpAgent(@Body request: SignUpOtherRequest): Response<SignUpResponse>

    @POST("signup/merchant")
    suspend fun signUpMerchant(@Body request: SignUpOtherRequest): Response<SignUpResponse>

    @POST("account-confirm")
    suspend fun requestConfirmAccount(@Body body: RequestBody): Response<SignUpResponse>

    /***************** Account Settings *****************/

    @POST("account/setting/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ProfileResponse>

    @POST("account/setting/address")
    suspend fun updateAddress(@Body request: UpdateAddressRequest): Response<ProfileResponse>

    @POST("account/setting/avatar")
    @Multipart
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part?): Response<ProfileResponse>

    @POST("account/setting/notification")
    suspend fun updateNotification(@Body request: UpdateNotificationRequest): Response<ProfileResponse>

    @GET("validate/bank-list") //USING FOR MERCHANT
    suspend fun getBankList(): Response<BankListResponse>

    @POST("validate/bank-account") //USING FOR MERCHANT
    suspend fun validateBankAccount(@Body request: ValidateBankAccountRequest): Response<ValidateBankAccountResponse>

    @POST("account/setting/daily-transfer") //USING FOR MERCHANT
    suspend fun allowDailyTransfer(@Body request: AllowDailyTransferRequest): Response<ProfileResponse>

    /***************** Account Verification *****************/

    @POST("business/verification/request")
    @Multipart
    suspend fun verifyBusiness(
        @Part document_file: MultipartBody.Part?,
        @Part("document_type") document_type: RequestBody,
        @Part("bvn") bvn: RequestBody,
        @Part("rc_number") rc_number: RequestBody
    ): Response<VerificationResponse>

    @POST("customer/verification/request")
    @Multipart
    suspend fun verifyCustomer(
        @Part document_file: MultipartBody.Part?,
        @Part("document_type") document_type: RequestBody,
        @Part("bvn") bvn: RequestBody
    ): Response<VerificationResponse>

    @GET("account/verification/status")
    suspend fun getVerificationStatus(): Response<VerificationResponse>

    /***************** Payment token generate *****************/

    @POST("charge/intent")
    suspend fun getPayLinkForSales(@Body request: GetLinkForSalesRequest): Response<PayLinkResponse>

    @POST("charge/intent")
    suspend fun getPayLinkForBulk(@Body request: GetLinkForBulkRequest): Response<PayLinkResponse>

    @POST("charge/intent")
    suspend fun getPayLinkForCard(@Body request: GetLinkForCardRequest): Response<PayLinkResponse>

    @POST("charge/intent")
    suspend fun getLinkForPayment(@Body Request: GetLinkForPaymentRequest): Response<PayLinkResponse>

    @POST("charge/wallet")
    suspend fun chargeWallet(@Body request: ChargeWalletRequest): Response<ChargeWalletResponse>

    @POST("charge/sdk-pay") //pay_link
    suspend fun chargePayPalPayStack(@Body request: RequestBody): Response<ChargePayPalResponse>

    @POST("confirm/sdk-payment") //reference, order_id
    suspend fun confirmPayPalPayStack(@Body request: RequestBody): Response<ChargePayPalResponse>

    @POST("charge/giftcard")
    suspend fun chargeGiftCard(@Body request: ChargeGiftCardRequest): Response<ChargeWalletResponse>

    @POST("charge/card")
    suspend fun chargeCard(@Body request: ChargeCardRequest): Response<ChargeCardResponse>

    @POST("charge/cardpin")
    suspend fun cardPin(@Body request: CardPinRequest): Response<CardPinResponse>

    @POST("charge/cardotp")
    suspend fun cardOtp(@Body request: CardOtpRequest): Response<CardOtpResponse>

    @POST("charge/banktransfer") //pay_link
    suspend fun chargeBankTransfer(@Body request: RequestBody): Response<ChargeBankTransferResponse>

    @POST("confirm/banktransfer") //reference
    suspend fun confirmBankTransfer(@Body request: RequestBody): Response<ChargeBankTransferResponse>

    @POST("charge/fees")
    suspend fun chargeFees(@Body request: ChargeFeesRequest): Response<ChargeFeesResponse>

    /***************** Transaction *****************/

    @POST("deposit") //payment_token - String
    suspend fun deposit(@Body request: RequestBody): Response<DepositResponse>

    @POST("transaction")
    suspend fun getAllTransaction(@Body request: TransactionRequest): Response<TransactionResponse>

    @POST("transaction-details") //reference - String
    suspend fun getTransactionDetail(@Body request: RequestBody): Response<DepositResponse>

    @POST("transaction/csv")
    suspend fun exportTransaction(@Body request: TransactionRequest): Response<ExportTransactionResponse>

    /***************** My gift card *****************/

    @POST("my-giftcard")
    suspend fun myGiftCard(@Body request: MyGiftCardRequest): Response<MyGiftCardResponse>

    @POST("my-giftcard/send")
    suspend fun sendMyGiftCard(@Body request: SendGiftCardRequest): Response<SendGiftCardResponse>

    /***************** My bank card *****************/

    @POST("wallet/card/add") //payment_token
    suspend fun addBankCard(@Body request: RequestBody): Response<AddCardResponse>

    @POST("wallet/card/list") //order_by: "desc", per_page: 10, page: 1
    suspend fun getListBankCards(@Body request: RequestBody): Response<ListCardResponse>

    @HTTP(method = "DELETE", path = "wallet/card/remove", hasBody = true) //card_token
    suspend fun deleteBankCard(@Body request: RequestBody): Response<RemoveCardResponse>

    /***************** Transfer Payment *****************/

    @POST("agent/transfer")
    suspend fun transferCommission(@Body request: TransferCommissionRequest): Response<TransferCommissionResponse>

    @POST("merchant/transfer")
    suspend fun transferRedeem(@Body request: TransferCommissionRequest): Response<TransferCommissionResponse>

    @POST("bank/list") //country - Nigeria
    suspend fun getBanks(@Body request: RequestBody): Response<BankListResponse>

    @POST("bank/validate-account") //payment_method = bank, bank_code, account_id
    suspend fun validateBankAccount(@Body request: RequestBody): Response<ValidateBankAccountResponse>

    /***************** Giftcard Purchases *****************/

    @POST("validate/merchants")
    suspend fun getAllMerchants(@Body request: MerchantsRequest): Response<MerchantsResponse>

    @POST("validate/merchant-details") //merchant_id - String
    suspend fun getMerchantDetail(@Body request: RequestBody): Response<MerchantDetailResponse>

    @POST("validate/merchantby-category") //category_id - String
    suspend fun getMerchantByCategoryId(@Body request: RequestBody): Response<MerchantsResponse>

    @POST("validate/categories")
    suspend fun getAllCategories(): Response<CategoriesResponse>

    @POST("validate/category-details") //category_id - String
    suspend fun getCategoryDetail(@Body request: RequestBody): Response<CategoryResponse>

    @POST("validate/giftcard") //giftcard - String
    suspend fun getGiftCardDetail(@Body request: RequestBody): Response<GiftcardDetailResponse>

    @POST("purchases")
    suspend fun buyGiftCard(@Body request: BuyGiftcardRequest): Response<BuyGiftCardResponse>

    @POST("redeem") //giftcard - String, amount - String
    suspend fun redeemGiftCard(@Body request: RequestBody): Response<RedeemGiftCardResponse>

    /***************** Bulk Purchases *****************/

    @POST("validate/bulkfile")
    @Multipart
    suspend fun validateBulkFile(
        @Part("merchant") merchant: RequestBody,
        @Part excelFile: MultipartBody.Part?
    ): Response<ValidateBulkFileResponse>

    @POST("purchases/bulk") //payment_token - String
    suspend fun purchasesBulk(@Body request: RequestBody): Response<PurchaseBulkResponse>

    /***************** Dispute center *****************/

    @POST("dispute/case/open")
    suspend fun openCase(@Body request: OpenCaseRequest): Response<OpenCaseResponse>

    @POST("dispute/case/case-details") //case_id -string
    suspend fun getCaseDetail(@Body request: RequestBody): Response<CaseDetailResponse>

    @POST("dispute/case/reply") //case_id - String, comment - String
    suspend fun replyCase(@Body request: RequestBody): Response<ReplyCaseResponse>

    @POST("dispute/case/reply-details") //case_id - String, comment_id - String
    suspend fun getReplayDetail(@Body request: RequestBody): Response<ReplyDetailResponse>

    @POST("dispute/case/all-reply") //case_id - String
    suspend fun getAllReply(@Body request: RequestBody): Response<AllReplyResponse>

    @POST("dispute/case/close") //case_id - String
    suspend fun closeCase(@Body request: RequestBody): Response<CloseCaseResponse>

    @POST("dispute/case/all")
    suspend fun searchAllCases(@Body request: SearchAllCasesRequest): Response<AllCasesResponse>
}

