package ru.inversion.sbpapplication.fragments.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.reactivex.rxjava3.functions.Consumer
import ru.inversion.sbpapplication.pojo.Employee

class EmployeeDialog : DialogFragment()  {

    var employeeNames: List<Employee>? = null
    private var okCall: Consumer<Employee>? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val employeeNameList : Array<String?>? = employeeNames?.map{"${it.firstName} ${it.secondName}"}?.toTypedArray()

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите сотрудника")
                .setItems(employeeNameList) { dialog, which ->
                    okCall?.accept(employeeNames?.get(which))
                    Toast.makeText(activity, employeeNameList?.get(which), Toast.LENGTH_LONG).show()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun okClicked(cons: Consumer<Employee>) {
        okCall = cons
    }
}