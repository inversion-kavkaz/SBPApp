package ru.inversion.sbpapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.inversion.sbpapplication.pojo.*

@Database(entities = [Transaction::class, ResponseResult::class, Customer::class,Employee::class,Department::class,Terminal::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    companion object{
        private var db: AppDataBase? =  null
        private const val DB_NAME = "sbp5.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDataBase {
            synchronized(LOCK){
                db?.let { return it }
                val instance = Room.databaseBuilder(context, AppDataBase::class.java, DB_NAME).build()
                db = instance
                return instance
            }
        }

    }

    abstract fun trnInfoDao(): TrnInfoDao
    abstract fun responseInfoDao(): ResponseDao
    abstract fun customersDao(): CustomerDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun terminaltDao(): TerminalDao
}