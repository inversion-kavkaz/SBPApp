package ru.inversion.sbpapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.ResponseResult

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(customer: Customer) : Single<Long>

    @Query("SELECT * FROM customer")
    fun getCustomerList(): Single<List<Customer>>

    @Query("UPDATE customer  SET firstEnter = 1 where ogrn = :ogrn")
    fun setEnteredFlag(ogrn: String): Single<Unit>

    @Query("SELECT * FROM customer where ogrn = :ogrnnum")
    fun getCustomerByOGRN(ogrnnum: String): Single<List<Customer>>

    @Query("delete from customer")
    fun clearCustomerTable()


}