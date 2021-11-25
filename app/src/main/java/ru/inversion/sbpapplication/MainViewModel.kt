package ru.inversion.sbpapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.database.AppDataBase
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.pojo.Terminal
import ru.inversion.sbpapplication.pojo.Transaction
import java.math.BigDecimal

class MainViewModel(application: Application): AndroidViewModel(application) {

    val db = AppDataBase.getInstance(application)
    var retData: MutableMap<String,Any> = mutableMapOf()


    init{
        loadDate()
    }

    fun loadDate() {

    }
    /**Employee----------------------------------------------------------------------------------*/
    fun saveEmployee(employee: Employee?): Single<Long>{
        if (employee == null) return Single.just(0)
        return db.employeeDao().insertEmployee(employee)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getEmployeeList(): LiveData<List<Employee>>{
        return db.employeeDao().getEmployeeList()
    }

    fun getEmployeeCount() : Single<Int>{
        return db.employeeDao().getEmployeeCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getEmployeeById(id: Long): Single<Employee>{
        return db.employeeDao().getEmployeeById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getEmployeeByPin(pin: String): Single<Employee>{
        return db.employeeDao().getEmployeeByPIN(pin)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**-------------------------------------------------------------------------------------------*/
    fun clearResponseTable(){
        viewModelScope.launch(Dispatchers.IO){
            db.responseInfoDao().clearResponseTable()
            db.customersDao().clearCustomerTable()
            db.employeeDao().clearEmployeeTable()
            db.trnInfoDao().deleteAllTransaction()
            db.terminaltDao().clearTerminalTable()
        }
    }

    fun getTerminalByID(id: Long) :LiveData<Terminal>{
        return  db.terminaltDao().getTerminalByID(id)
    }


    fun saveTerminal(terminal: Terminal): Single<Long>{
        return db.terminaltDao().insertTerminal(terminal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    /**Transaction ------------------------------------------------------------------------------*/
    fun clearTrnBD(){
        db.trnInfoDao().deleteAllTransaction()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{})
    }

    fun insertTransaction(transaction: Transaction) : Single<Long>{
        return db.trnInfoDao().insertTrn(transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTrnSum() : LiveData<Double>{
        return db.trnInfoDao().getTrnTodaySum()
    }
    /**Terminals -----------------------------------------------------------------------------------*/

    fun getTerminalCount() : Single<Long>{
        return db.terminaltDao().getTerminalCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}