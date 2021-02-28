package io.sparkled.e2e.api.endpoint.v1.dev

import io.sparkled.e2e.spec.E2eSpec

class DbTransactionE2eTest : E2eSpec() {

// TODO
//    init {
//        "Exception in @Transactional method causes DB transaction to roll back" {
//            db.query(GetAffiliateLinksQuery()).size shouldBe 0
//
//            val (errorResponse) = http.getErrorResponse { doRequest(rollback = true) }
//            errorResponse.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
//            db.query(GetAffiliateLinksQuery()).size shouldBe 0
//
//            val (response) = doRequest(rollback = false)
//            response.status shouldBe HttpStatus.OK
//            db.query(GetAffiliateLinksQuery()).size shouldBe 5
//        }
//    }
//
//    private fun doRequest(rollback: Boolean): Pair<HttpResponse<Any>, Any> {
//        return http.getResponseAndObject(
//            HttpRequest.GET(
//                "/api/dev/transaction"
//            )
//        )
//    }
}
