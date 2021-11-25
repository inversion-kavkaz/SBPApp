package ru.inversion.sbpapplication.database

import androidx.room.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE
import ru.inversion.sbpapplication.pojo.ResponseResult

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResponse(response: ResponseResult) : Single<Long>

    @Query("SELECT * FROM response_result ORDER BY responseDate desc")
    fun getResponseList(): Single<List<ResponseResult>>

    @Query("delete from response_result")
    fun clearResponseTable()
}