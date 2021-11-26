package ru.inversion.sbpapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_employee_info.view.*
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.fragments.EmployeeListFragment
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.utils.AppConstants.permissionList

class EmployeeAdapter(private val context: EmployeeListFragment) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {
    var employeeList: List<Employee> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_employee_info, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val empl = employeeList[position]
        with(holder) {
            with(empl) {
                tvEName.text = "$firstName $secondName"
                tvEmployeeRole.text = permissionList[permissionGroupName]
                delBut.setOnClickListener {
                    MainActivity.viewModel.deleteEmployee(this)
                }
                constraynt.setOnClickListener {
                    context.findNavController().navigate(R.id.action_employeeListFragment_to_employeeFragment, bundleOf("employee" to this))
                }
            }
        }
    }

    override fun getItemCount(): Int = employeeList.size

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEName = itemView.tvEmployeeName
        val tvEmployeeRole = itemView.tvEmployeeRole
        val delBut = itemView.delButton
        val constraynt = itemView.constraintLayout
    }

}