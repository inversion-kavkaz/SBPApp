package ru.inversion.sbpapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.rxjava3.core.Single
import ru.inversion.sbpapplication.pojo.Transaction
import java.math.BigDecimal

@Dao
interface TrnInfoDao {
    @Query("SELECT * FROM trn_list ORDER BY dat desc")
    fun getTrnList(): LiveData<List<Transaction>>

    @Query("SELECT a.* FROM trn_list a where a.dat > :timestamp ORDER BY a.dat desc")
    fun getTrnListToday(timestamp : Long): LiveData<List<Transaction>>

    @Query("SELECT SUM(a.mtrnsum) FROM trn_list a")
    fun getTrnTodaySum(): LiveData<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrnList(priceList: List<Transaction>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrn(trn: Transaction) : Single<Long>

    @Query("delete from trn_list")
    fun deleteAllTransaction()



}