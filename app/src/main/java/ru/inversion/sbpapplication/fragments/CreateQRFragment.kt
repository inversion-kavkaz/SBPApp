package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.api.ApiFactory
import ru.inversion.sbpapplication.databinding.FragmentCreateQRBinding
import ru.inversion.sbpapplication.databinding.FragmentMainBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.utils.Extension.Companion.hideKeyboard


class CreateQRFragment : Fragment() {

    private var binding: FragmentCreateQRBinding? = null
    private var customer: Customer? = null
    private var employee: Employee? = null
    private var sum : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.get("employee")?.let { it as Employee }
            customer = it.get("customer")?.let { it as Customer }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateQRBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEmployeeText()
        binding?.progressBar?.visibility = View.INVISIBLE
        setTextListener()
        setButtonEnabled(false)
        val adapter = context?.let { ArrayAdapter.createFromResource(it, R.array.serviceName,R.layout.spiner_layout) }
        adapter?.setDropDownViewResource(R.layout.spiner_dropdown_layout)
        binding?.spinner?.adapter = adapter

        binding?.exitImage?.setOnClickListener {
            findNavController().popBackStack()
            //findNavController().navigate(R.id.action_createQRFragment_to_merchantFragment, bundleOf("employee" to employee, "customer" to customer))
        }

        binding?.cvCreateQR?. setOnClickListener{
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.etSum?.hideKeyboard()
            setButtonEnabled(false)
            ApiFactory.apiService.getQrCode(sum)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("TEST_OF_LOADING_DATA", it.toString())
                    AndroidSchedulers.mainThread().scheduleDirect {
                        findNavController().navigate(R.id.action_createQRFragment_to_showQRFragment,
                            bundleOf("transaction" to it, "customer" to customer, "employee" to employee))
                        binding?.etSum?.text = null
                    }
                }
                    ,{
                        Log.d("TEST_OF_LOADING_DATA", it.message.toString())
                        setButtonEnabled(true)
                        binding?.progressBar?.visibility = View.INVISIBLE
                    })
        }
        }

    private fun setTextListener(){
        binding?.etSum?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setButtonEnabled(!(p0 != null && (p0.length < 1 || p0.toString().toDouble() <= 0)))
                sum = if(p0 != null && p0?.length > 0) p0.toString().toDouble() else 0.0
                }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setButtonEnabled(idEnabled : Boolean) {
        binding?.cvCreateQR?.isEnabled = idEnabled
    }

    private fun setEmployeeText() {
        GlobalScope.launch(Dispatchers.Main){
            binding?.tvCustomerHeaderText?.text = "${employee?.firstName} ${employee?.secondName}"
            binding?.tvAbreviatura?.text = "${if(employee?.firstName?.isNullOrEmpty() == true) "" else employee?.firstName?.first()?.uppercase()}" +
                    "${if(employee?.secondName?.isNullOrEmpty() == true) "" else employee?.secondName?.first()?.uppercase()}"
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}