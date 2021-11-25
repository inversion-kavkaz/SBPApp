package ru.inversion.sbpapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.inversion.sbpapplication.pojo.Department

@Dao
interface DepartmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(department: Department) : Single<Long>

    @Query("SELECT * FROM department")
    fun getCustomerList(): LiveData<List<Department>>

}