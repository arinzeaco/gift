package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.DisputeRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.network.request.SearchAllCasesRequest
import com.globasure.giftoga.network.response.*

class DisputeRepositoryImpl(
    private val apiHelper: ApiHelper
) : DisputeRepository {
    override suspend fun openCase(request: OpenCaseRequest): OpenCaseResponse {
        return apiHelper.openCase(request)
    }

    override suspend fun getCaseDetail(case_id: String): CaseDetailResponse {
        return apiHelper.getCaseDetail(case_id)
    }

    override suspend fun replyCase(case_id: String, comment: String): ReplyCaseResponse {
        return apiHelper.replyCase(case_id, comment)
    }

    override suspend fun getReplyDetail(case_id: String, comment_id: String): ReplyDetailResponse {
        return apiHelper.getReplyDetail(case_id, comment_id)
    }

    override suspend fun getAllReply(case_id: String): AllReplyResponse {
        return apiHelper.getAllReply(case_id)
    }

    override suspend fun closeCase(case_id: String): CloseCaseResponse {
        return apiHelper.closeCase(case_id)
    }

    override suspend fun searchAllCases(request: SearchAllCasesRequest): AllCasesResponse {
        return apiHelper.searchAllCases(request)
    }

}