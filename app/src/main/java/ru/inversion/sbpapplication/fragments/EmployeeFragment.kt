package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.functions.BiConsumer
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.MainActivity.Companion.viewModel
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentEmployeeBinding
import ru.inversion.sbpapplication.fragments.dialog.RoleDialogFragment
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.pojo.Terminal
import ru.inversion.sbpapplication.utils.AppConstants.permissionList

class EmployeeFragment : Fragment() {
    private var employee: Employee? = null
    private var customer: Customer? = null
    private  var infoFields: Map<String, TextInputEditText>? = null
    private  var infoButtons: Map<String, MaterialCardView>? = null

    private var isEditMode: Boolean = false
    private var binding : FragmentEmployeeBinding? = null
    private var rolesList: String = permissionList["MERCHANT"].toString()
    private var rolesListForDb: String = ""
    private var employeeTerminal: Terminal? = null
    private var isCreate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.get("employee")?.let { it as Employee }
            customer = it.get("customer")?.let { it as Customer }
            isCreate = false
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvRolesText?.text = rolesList
        binding?.tvCustomerHeaderText?.text = when(employee != null){
            true  -> resources.getString(R.string.update_customer)
            false -> resources.getString(R.string.create_customer)
        }

        binding?.exitImage?.setOnClickListener { findNavController().popBackStack() }
        initTerminal()
          initViews()
        setRolesDialog()
        initField(employee)
        binding?.cvChangeTerminal?.setOnClickListener {
            var bundle = bundleOf("customer" to customer,"employee" to employee)
            viewModel.retData.remove("merchantID")
            findNavController().navigate(R.id.action_employeeFragment_to_terminalFragment, bundle)
        }


    }

    private fun initTerminal() {
        employee?.let {
            if(it.terminalId != null){
                viewModel.getTerminalByID(it.terminalId!!)
                    .observe(this,{it1 ->
                        employeeTerminal = it1
                        initField(it)
                    })
            }
        }
    }


    private fun initField(employee: Employee?) {

        if (viewModel.retData["merchantID"] != null) {
            employeeTerminal = viewModel.retData["merchantID"] as Terminal
            binding?.tvTerminalText?.text = "Банк:${employeeTerminal?.bankId} Мерчант: ${employeeTerminal?.merchantId}"
        } else if(employee == null) rolesListForDb ="MERCHANT"

        employee?.let {
            with(it) {
                binding?.inputCustomerName?.setText(firstName)
                binding?.inputCustomerFam?.setText(secondName)
                binding?.inputCustomerPhone?.setText(phone)
                binding?.inputCustomerLogin?.setText(login)
                val pmText = permissionGroupName
                    ?.split(",")
                    ?.map { p -> permissionList[p] }
                    ?.toTypedArray()
                    ?.joinToString(separator = ",")
                binding?.tvRolesText?.text = if (pmText.isNullOrEmpty()) "" else pmText
                binding?.tvTerminalText?.text =
                    if (employeeTerminal == null) "" else "Банк:${employeeTerminal?.bankId} Мерчант: ${employeeTerminal?.merchantId}"
            }
        }
    }

    private fun setRolesDialog(){
        binding?.cvChangeRole?.setOnClickListener {
            if (isEditMode) {
                val roleDialog = RoleDialogFragment()
                val manager: FragmentManager = childFragmentManager
                val transition: FragmentTransaction = manager.beginTransaction()
                roleDialog.show(transition, "dialog")
                roleDialog.okClicked(BiConsumer { t1, t2 ->
                    binding?.tvRolesText?.text = t2
                    rolesListForDb = ""
                    rolesListForDb += if (t1[0]) "ADMIN" + "," else ""
                    rolesListForDb += if (t1[1]) "DEPARTMENTBOSS" + "," else ""
                    rolesListForDb += if (t1[2]) "MERCHANT" + "," else ""
                    if (rolesListForDb.isNotEmpty())
                        rolesListForDb = rolesListForDb.dropLast(1)
                })
            }
        }
    }

    private fun initViews() {
        infoFields = mapOf(
            "firstName" to binding?.inputCustomerName,
            "lastName" to binding?.inputCustomerFam,
            "login" to binding?.inputCustomerLogin,
            "phone" to binding?.inputCustomerPhone,
            "pin" to binding?.inputCustomerPIN,
            "dpin" to binding?.inputCustomerDPIN,
        ) as Map<String, TextInputEditText>

        infoButtons = mapOf(
            "terminal" to binding?.cvChangeTerminal,
            "department" to binding?.cvChangeDepartment,
            "role" to binding?.cvChangeRole,
        ) as Map<String, MaterialCardView>

        binding?.editButton?.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }
        showCurrentMode(isEditMode)
    }


    private fun createEmployee(terminalID: Long? = null) : Employee{
        return Employee(
            id = if(employee?.id != null) employee?.id else null,
            firstName = binding?.inputCustomerName?.text?.trim().toString(),
            secondName = binding?.inputCustomerFam?.text?.trim().toString(),
            phone = binding?.inputCustomerPhone?.text?.trim().toString(),
            login = binding?.inputCustomerLogin?.text?.trim().toString(),
            permissionGroupName = if(rolesListForDb.isEmpty()) employee?.permissionGroupName else rolesListForDb,
            isChanged = if(employee != null && !binding?.inputCustomerPIN?.text.toString().isEmpty()) 1 else 0,
            terminalId = terminalID,
            pinCode = if(binding?.inputCustomerPIN?.text?.trim().toString().isEmpty())
                employee?.pinCode?.trim().toString()
            else
                binding?.inputCustomerPIN?.text?.trim().toString()
        )

    }

    private fun saveProfileInfo() {
        if(employeeTerminal != null){
            viewModel.saveTerminal(employeeTerminal!!)
                .subscribe({
                    SaveEmployee(createEmployee(it))
                },{  toastError(it)})
        }
        else
            SaveEmployee(createEmployee())
    }

    private fun SaveEmployee(employee: Employee){
        viewModel.saveEmployee(employee)
            .subscribe({
                if(isCreate)
                    Toast.makeText(context, "Запись сохранена ", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Запись изменена ", Toast.LENGTH_SHORT).show();
                Thread.sleep(3000)
                viewModel.retData.remove("merchantID")
                findNavController().popBackStack()

            },{
                toastError(it)
            })
    }

    private fun toastError(it: Throwable) {
        Toast.makeText(context, "Ошибка записи в БД ${it.message.toString()}", Toast.LENGTH_SHORT).show();
        Log.d("ERROR_OF_WRITE_DATA", it.message.toString())
    }

    private fun showCurrentMode(isEdit: Boolean) {
        infoFields?.let {
            for ((_, v) in it) {
                v.isFocusable = isEdit
                v.isFocusableInTouchMode = isEdit
                v.isEnabled = isEdit
                v.background.alpha = if (isEdit) 255 else 0
            }
        }
        infoButtons?.let{
            for((_,v) in it) {
                v.isEnabled = isEdit
                v.setCardBackgroundColor(if(isEdit) resources.getColor(R.color.white) else  resources.getColor(R.color.button_disabled))
            }
        }

        with(binding?.editButton) {
            this?.setImageDrawable(if (isEdit) resources.getDrawable(R.drawable.ic_save_white_24dp ) else resources.getDrawable(R.drawable.ic_edit_day_night))
        }
    }

        override fun onDestroy() {
        super.onDestroy()
        binding = null
        infoButtons = null
        infoFields = null
    }
}