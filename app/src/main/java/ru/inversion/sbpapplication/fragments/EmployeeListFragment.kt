package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_employee_list.*
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.adapters.EmployeeAdapter
import ru.inversion.sbpapplication.databinding.FragmentEmployeeListBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee


class EmployeeListFragment : Fragment() {
    private var bindin: FragmentEmployeeListBinding? = null
    private var customer: Customer? = null
    lateinit var adapter : EmployeeAdapter
    var employeeList : LiveData<List<Employee>> = MainActivity.db.employeeDao().getEmployeeList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customer = it.get("customer") as Customer
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindin = FragmentEmployeeListBinding.inflate(inflater, container, false)

        adapter = EmployeeAdapter(this)
        bindin?.rvEmployeeList?.adapter = adapter
        bindin?.rvEmployeeList?.addItemDecoration(DividerItemDecoration(bindin?.rvEmployeeList?.context, LinearLayout.VERTICAL))
        employeeList.observe(this,{
            adapter.employeeList = it.filter { e -> e.permissionGroupName?.contains("ADMIN") != true}
        })

        return bindin?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindin?.exitImage?.setOnClickListener {
            findNavController().popBackStack()
        }

        bindin?.fbAddEmployee?.setOnClickListener {
            findNavController().navigate(R.id.action_employeeListFragment_to_employeeFragment, bundleOf("customer" to customer,"employee" to null))
        }

    }
    override fun onDestroy() {
        bindin = null
        customer = null
        super.onDestroy()
    }
}