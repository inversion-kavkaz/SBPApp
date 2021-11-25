package ru.inversion.sbpapplication.fragments.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.reactivex.rxjava3.functions.BiConsumer
import ru.inversion.sbpapplication.utils.AppConstants.permissionList

class RoleDialogFragment : DialogFragment() {

    private val roleNames = permissionList.map { it.value }.toTypedArray()
    private val checkedItems = booleanArrayOf(false, false, true)
    private var okCall: BiConsumer<BooleanArray, String>? = null
    private var roleList: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите роль сотрудника")
                .setMultiChoiceItems(roleNames, checkedItems) { dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                    val name = roleNames[which]
                }
                .setPositiveButton("Готово") { dialog, id ->
                    for (i in checkedItems.indices) {
                        roleList += if(checkedItems[i]) roleNames[i] + "," else ""
                    }
                    okCall?.accept(checkedItems, roleList.dropLast(1))
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun okClicked(cons: BiConsumer<BooleanArray, String>) {
        okCall = cons
    }
}