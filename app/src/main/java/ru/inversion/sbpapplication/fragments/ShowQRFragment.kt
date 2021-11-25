package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.api.ApiFactory
import ru.inversion.sbpapplication.databinding.FragmentCreateQRBinding
import ru.inversion.sbpapplication.databinding.FragmentShowQRBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.pojo.Transaction

class ShowQRFragment : Fragment() {
    private var binding: FragmentShowQRBinding? = null
    private var customer: Customer? = null
    private var employee: Employee? = null
    private  var transaction: Transaction? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.get("employee")?.let { it as Employee }
            customer = it.get("customer")?.let { it as Customer }
            transaction = it.get("transaction")?.let { it as Transaction }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowQRBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.successCheck?.visibility = View.INVISIBLE

        setParams(transaction)
        binding?.cvCreateQR?.setOnClickListener { findNavController().popBackStack() }

        if (transaction?.qrId != null) {
            var disposable = ApiFactory.apiService.getQrPay(transaction?.qrId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    MainActivity.viewModel.insertTransaction(it)
                        .subscribe({
                            binding?.successCheck?.visibility = View.VISIBLE
                            binding?.progressBar?.visibility = View.INVISIBLE
                            binding?.tvWaitText?.visibility = View.INVISIBLE
                            GlobalScope.launch {
                                delay(2000)
                                GlobalScope.launch(Dispatchers.Main) {
                                    findNavController().popBackStack()
                            }
                            }
                        }, {
                            Log.d("TEST_OF_LOADING_DATA", it.message.toString())
                        })
                    //findNavController().navigate(R.id.action_SecondFragment_to_success)
                    Log.d("TEST_OF_LOADING_DATA", it.toString())
                }, {
                    Log.d("TEST_OF_LOADING_DATA", it.message.toString())
                })

            compositeDisposable.add(disposable)
        }

        setEmployeeText()
    }

    private fun setParams(newTrn: Transaction?) {
        binding?.tvOrder?.text = String.format(
            context?.resources?.getString(R.string.order_text).toString(),
            newTrn?.orderNum
        )
        binding?.tvPaySummNumber?.text = String.format(
            context?.resources?.getString(R.string.paySumNum).toString(),
            newTrn?.mtrnsum.toString()
        )
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
        compositeDisposable.dispose()
    }
}