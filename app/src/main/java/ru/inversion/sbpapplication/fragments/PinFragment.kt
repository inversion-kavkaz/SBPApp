package ru.inversion.sbpapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hanks.passcodeview.PasscodeView
import ru.inversion.sbpapplication.databinding.FragmentPinBinding
import ru.inversion.sbpapplication.utils.AppConstants.tempPIN
import android.R
import android.widget.AdapterView

import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import io.reactivex.rxjava3.functions.Consumer
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.fragments.dialog.EmployeeDialog
import ru.inversion.sbpapplication.fragments.dialog.RoleDialogFragment
import ru.inversion.sbpapplication.pojo.Employee


class PinFragment : Fragment() {

    private var binding: FragmentPinBinding? = null
    private var currentEmployee : Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {System.exit(0)}})
        var employeeDialog: EmployeeDialog? = null
        var transition: FragmentTransaction? = null


        MainActivity.viewModel.getEmployeeList()
            .observe(this, {
                if(it.size == 1) {
                    currentEmployee = it[0]
                    initPinCode(currentEmployee)
                }
                employeeDialog = EmployeeDialog()
                employeeDialog?.employeeNames = it
                val manager: FragmentManager = childFragmentManager
                transition = manager.beginTransaction()
                employeeDialog?.okClicked(Consumer {
                    initPinCode(it)
                })
            })

        binding?.cvChangeEmployee?.setOnClickListener {
            transition?.let { it1 -> employeeDialog?.show(it1, "dialog") }

        }
    }

    private fun initPinCode(it: Employee?) {
        currentEmployee = it
        currentEmployee?.pinCode?.let {
            binding?.Employee?.text = "${currentEmployee?.firstName} ${currentEmployee?.secondName}"
            binding?.passcodeView?.setPasscodeLength(4)
                ?.setLocalPasscode(currentEmployee?.pinCode)
                ?.listener = object : PasscodeView.PasscodeViewListener {
                override fun onFail() {
                    Toast.makeText(context, "PIN код неверен!", Toast.LENGTH_SHORT).show();
                }

                override fun onSuccess(number: String?) {
                    findNavController().navigate(
                        if (currentEmployee?.permissionGroupName?.contains("MERCHANT") == true)
                            ru.inversion.sbpapplication.R.id.action_pinFragment_to_merchantFragment
                        else
                            ru.inversion.sbpapplication.R.id.action_pinFragment_to_mainFragment,
                        bundleOf("employee" to currentEmployee)
                    )
                }
            }
        }
    }

    private fun setupEmployeeDialog() {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

 }