package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentTerminalBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee

class TerminalFragment : Fragment() {
    private var bindin: FragmentTerminalBinding? = null
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
        bindin = FragmentTerminalBinding.inflate(inflater, container, false)
        return bindin?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindin?.btYes?.setOnClickListener {
            var bundle = bundleOf("customer" to customer,"employee" to employee)
            findNavController().navigate(R.id.action_terminalFragment_to_connectTerminalFragmen, bundle)
        }
        bindin?.btNo?.setOnClickListener {
            var bundle = bundleOf("customer" to customer,"employee" to employee)
            findNavController().navigate(R.id.action_terminalFragment_to_registerTerminalFragment, bundle)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        employee =  null
    }
}