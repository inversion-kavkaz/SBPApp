package ru.inversion.sbpapplication.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.inversion.sbpapplication.pojo.Transaction
import io.reactivex.rxjava3.core.Single

interface ApiService {

    @GET("api/payments")
    fun getPageTrn(
        @Query(QUERY_PARAM_PAGE_NUM) pageNum: Int,
        @Query(QUERY_PARAM_PAGE_SIZE) pageSize: Int = 50
        ): Single<List<Transaction>>


    @GET("api/qr_code")
    fun getQrCode(
        @Query(QUERY_PARAM_GET_QR_SUM) sum: Double,
        @Query(QUERY_PARAM_GET_QR_BANK) bank: Int = 12365478,
        @Query(QUERY_PARAM_GET_QR_CUR) cur: String = "RUR"
        ): Single<Transaction>

    @GET("api/qr_pay")
    fun getQrPay(
        @Query(QUERY_PARAM_GET_QR_ID) drId: String,
        ): Single<Transaction>

    @GET("api/timestamp")
    fun getTodayTimestamp(): Single<Long>



    companion object{
        private const val QUERY_PARAM_PAGE_NUM = "pageNum"
        private const val QUERY_PARAM_PAGE_SIZE = "pageSize"

        private const val QUERY_PARAM_GET_QR_SUM = "sum"
        private const val QUERY_PARAM_GET_QR_TYPE = "type"
        private const val QUERY_PARAM_GET_QR_BANK = "bank"
        private const val QUERY_PARAM_GET_QR_CUR = "cur"
        private const val QUERY_PARAM_GET_QR_CRC = "crc"

        private const val QUERY_PARAM_GET_QR_ID = "qrId"
    }
}