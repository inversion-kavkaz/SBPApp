package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.MainActivity.Companion.viewModel
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentMainBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.utils.AppConstants.tempOGRN
import ru.inversion.sbpapplication.utils.AppConstants.tempPIN

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val db = MainActivity.db
    private var customer: Customer? = null
    private var employee: Employee? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.get("employee") as Employee?
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentCustomer()
        setCounters()
        setButtonClickNavigation()
        //updateEmployee()


        binding.exitImage.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_pinFragment)
        }
        viewModel.getTerminalCount().subscribe({
            binding?.tvTerminalsCounter?.text = it.toString()
        },{})



        /**Для тестовой работы*/
        binding.btClearData.setOnClickListener{
            MainActivity.clearAll()
        }

    }

    private fun updateEmployee() {
        employee?.id?.let {
            viewModel.getEmployeeById(it)
                .subscribe({
                   employee = it
                    if(employee?.isChanged!! < 1)
                        setMessage(binding.cvProfile, binding.tvProfileMessage,"Смените ПИН-код пользователя.")
                },{Log.d("ERROR", it.message.toString())})
        }
    }

    private fun setCounters() {
        viewModel.getEmployeeCount()
            .subscribe({
                if(it == 0){
                    employee = Employee(pinCode = tempPIN, permissionGroupName = "ADMIN", isChanged = 0)
                    viewModel.saveEmployee(employee)
                        .subscribe({
                            employee?.id = it
                            updateEmployee()
                       },
                            {Log.d("ERROR_OF_SET_FLAG", it.message.toString())})
                }else {
                    binding?.tvEmployeeCounter?.text = (it - 1).toString()
                    updateEmployee()
                }
                setEmployeeText()
            },{})
    }

    private fun setButtonClickNavigation() {
        binding.cvCustomer.setOnClickListener {
            var bundle = bundleOf("customer" to customer)
            findNavController().navigate(R.id.action_mainFragment_to_customerInfoFragment, bundle)
        }
        binding.cvDepartment.setOnClickListener {
            var bundle = bundleOf("customer" to customer)
            findNavController().navigate(R.id.action_mainFragment_to_depatmentListFragment, bundle)
        }
        binding.cvEmployee.setOnClickListener {
            var bundle = bundleOf("customer" to customer)
            findNavController().navigate(R.id.action_mainFragment_to_employeeListFragment, bundle)
        }
        binding.cvTerminal.setOnClickListener {
            var bundle = bundleOf("customer" to customer)
            findNavController().navigate(R.id.action_mainFragment_to_terminalListFragment, bundle)
        }
        binding.cvProfile.setOnClickListener {
            var bundle = bundleOf("customer" to customer,"employee" to employee)
            findNavController().navigate(R.id.action_mainFragment_to_employeeFragment, bundle)
        }
    }


    private fun setEmployeeText() {
        GlobalScope.launch(Dispatchers.Main){
        binding?.tvCustomerHeaderText.text = "${employee?.firstName} ${employee?.secondName}"
        binding?.tvAbreviatura.text = "${if(employee?.firstName?.isNullOrEmpty() == true) "" else employee?.firstName?.first()?.uppercase()}" +
                "${if(employee?.secondName?.isNullOrEmpty() == true) "" else employee?.secondName?.first()?.uppercase()}"
            }
    }

    private fun getCurrentCustomer() {
        db.customersDao().getCustomerByOGRN(tempOGRN)
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    customer = it[0]
                    if(customer?.acc.isNullOrEmpty())
                    setMessage(binding.cvCustomer, binding.tvCustomerMessage,"Не зарегистирован счет клиента")
            }, {Log.d("ERROR_OF_LOADING_CUSTOMER", it.message.toString())})
    }

    private fun setMessage(card: MaterialCardView,textView : TextView, message: String? ="") {
            GlobalScope.launch(Dispatchers.Main){
                card.strokeColor = resources.getColor(R.color.color_accent)
                textView.setTextColor(resources.getColor(R.color.color_accent))
                textView.text = message
        }

    }
}