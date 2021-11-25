package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentMainBinding
import ru.inversion.sbpapplication.databinding.FragmentRegisterTerminalBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee

class RegisterTerminalFragment : Fragment() {

    private var binding: FragmentRegisterTerminalBinding? = null
    private val db = MainActivity.db
    private var customer: Customer? = null
    private var employee: Employee? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.get("employee") as Employee?
            customer = it.get("customer") as Customer?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterTerminalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}