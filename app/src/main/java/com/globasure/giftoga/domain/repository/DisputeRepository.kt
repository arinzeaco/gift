package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.network.request.SearchAllCasesRequest
import com.globasure.giftoga.network.response.*

interface DisputeRepository {
    suspend fun openCase(request: OpenCaseRequest): OpenCaseResponse

    suspend fun getCaseDetail(case_id: String): CaseDetailResponse

    suspend fun replyCase(case_id: String, comment: String): ReplyCaseResponse

    suspend fun getReplyDetail(case_id: String, comment_id: String): ReplyDetailResponse

    suspend fun getAllReply(case_id: String): AllReplyResponse

    suspend fun closeCase(case_id: String): CloseCaseResponse

    suspend fun searchAllCases(request: SearchAllCasesRequest): AllCasesResponse
}