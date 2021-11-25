package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentDepatmentListBinding
import ru.inversion.sbpapplication.databinding.FragmentProfileBinding
import ru.inversion.sbpapplication.pojo.Customer

class ProfileFragment : Fragment() {
    private var bindin: FragmentProfileBinding? = null
    private var customer: Customer? = null

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
        bindin = FragmentProfileBinding.inflate(inflater, container, false)
        return bindin?.root

    }

    override fun onDestroy() {
        bindin = null
        customer = null
        super.onDestroy()
    }
}