package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.inversion.sbpapplication.databinding.FragmentConnectTerminalBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee
import android.widget.ArrayAdapter
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.pojo.Terminal


class ConnectTerminalFragmen : Fragment() {

    private var binding: FragmentConnectTerminalBinding? = null
    private var employee: Employee? = null
    private var customer: Customer? = null


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
        binding = FragmentConnectTerminalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = context?.let { ArrayAdapter.createFromResource(it, R.array.bankName,R.layout.spiner_layout) }
        adapter?.setDropDownViewResource(R.layout.spiner_dropdown_layout)
        binding?.spinner?.adapter = adapter

        binding?.tvCustomerHeaderText?.text = customer?.name


        binding?.tvBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.btNext?.setOnClickListener {
            MainActivity.viewModel.retData.put("merchantID",Terminal(bankId = 256987, merchantId = binding?.inputMerchantID?.text.toString().toLong()))
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}