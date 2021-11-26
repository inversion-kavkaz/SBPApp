package ru.inversion.sbpapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.rxjava3.core.Single
import ru.inversion.sbpapplication.pojo.Employee


@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployee(employee: Employee) : Single<Long>

    @Query("SELECT * FROM employee")
    fun getEmployeeList(): LiveData<List<Employee>>

    @Query("SELECT count(*) FROM employee")
    fun getEmployeeCount(): Single<Int>

    @Query("delete from employee")
    fun clearEmployeeTable()

    @Query("SELECT * FROM employee where pinCode = :pin")
    fun getEmployeeByPIN(pin: String): Single<Employee>

    @Query("SELECT * FROM employee where id = :id")
    fun getEmployeeById(id: Long): Single<Employee>

    @Delete
    fun delete(employee: Employee?) : Single<Unit>




}