package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentCustomerInfoBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.utils.Extension.Companion.closeKeyboard

class CustomerInfoFragment : Fragment() {

    var bindin: FragmentCustomerInfoBinding? = null
    private var customer: Customer? = null
    private val db = MainActivity.db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customer = it.get("customer") as Customer?
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindin = FragmentCustomerInfoBinding.inflate(inflater, container, false)
        return bindin?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindin?.cvAccRegistered?.visibility = View.GONE
        bindin?.tvAccRegistered?.visibility = View.GONE

        setProperty()
        if (customer?.acc.isNullOrEmpty())
            showAccRegistration()

        bindin?.exitImage?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showAccRegistration() {
        bindin?.cvAccRegistered?.visibility = View.VISIBLE
        bindin?.btRequestAcc?.isEnabled = false
        bindin?.waitProgress?.visibility = View.GONE
        bindin?.inputAcc?.addTextChangedListener(textWatcher())
        bindin?.btRequestAcc?.setOnClickListener {
            bindin?.inputAcc?.closeKeyboard(requireActivity())
            bindin?.waitProgress?.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.Main) {
                delay(3000)
                bindin?.cvAccRegistered?.visibility = View.GONE
                bindin?.tvAccRegistered?.visibility = View.VISIBLE
                bindin?.tvAccRegistered?.text = String.format(resources.getString(R.string.acc_registered_success),bindin?.inputAcc?.text)
                bindin?.tvAcc?.text = bindin?.inputAcc?.text
                customer?.acc = bindin?.inputAcc?.text.toString()
                saveAccToDb(customer)
                delay(3000)
                bindin?.tvAccRegistered?.visibility = View.GONE
            }
            }
        }

    private fun saveAccToDb(customer: Customer?) {
        if(customer != null)
        db.customersDao().insertCustomer(customer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("WRITE_DATA_SUCCESS", "")
            }, {
                Toast.makeText(
                    context,
                    "Ошибка записи в БД ${it.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show();
                Log.d("ERROR_OF_WRITE_DATA", it.message.toString())
            })
    }

    private fun setProperty() {
        bindin?.tvName?.text = customer?.name
        bindin?.tvOGRN?.text = customer?.ogrn
        bindin?.tvINN?.text = customer?.inn
        bindin?.tvAcc?.text = customer?.acc
    }

    private fun textWatcher() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            bindin?.btRequestAcc?.isEnabled = (p0 != null && p0.length > 0)
        }

        override fun afterTextChanged(p0: Editable?) {}
    }


    override fun onDestroy() {
        bindin = null
        customer = null
        super.onDestroy()
    }
}


