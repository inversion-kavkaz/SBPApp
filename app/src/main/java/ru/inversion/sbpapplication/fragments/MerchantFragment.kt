package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.MainActivity.Companion.viewModel
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.adapters.TrnInfoAdapter
import ru.inversion.sbpapplication.api.ApiFactory
import ru.inversion.sbpapplication.databinding.FragmentMerchantBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.pojo.Employee
import ru.inversion.sbpapplication.pojo.Transaction

class MerchantFragment : Fragment() {

    private var binding: FragmentMerchantBinding? = null
    private val db = MainActivity.db
    private var customer: Customer? = null
    private var employee: Employee? = null
    lateinit var adapter : TrnInfoAdapter
    var priceList : LiveData<List<Transaction>> = db.trnInfoDao().getTrnList()
    private val compositeDisposable = CompositeDisposable()
    var isLoading : Boolean = false

    init {
//        loadData(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee = it.get("employee") as Employee?
            customer = it.get("customer")?.let { it as Customer }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMerchantBinding.inflate(inflater, container, false)

        adapter = TrnInfoAdapter(this)
        binding?.rvTrnPriceList?.adapter = adapter
        binding?.rvTrnPriceList?.addItemDecoration(DividerItemDecoration(binding?.rvTrnPriceList?.context, LinearLayout.VERTICAL))
        priceList.observe(this, {
            adapter.trnInfoList = it
        })

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        employee?.id?.let { viewModel.getEmployeeById(it).subscribe({
            employee = it
            inittialize()
            },{}) }
    }

    private fun inittialize() {
        binding?.tvProgressBar?.visibility = View.GONE
        setEmployeeText()

        binding?.exitImage?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.qrButton?.setOnClickListener {
            findNavController().navigate(
                R.id.action_merchantFragment_to_createQRFragment,
                bundleOf("employee" to employee, "customer" to customer)
            )
        }

        viewModel.getTrnSum().observe(this, {
            if(it == null) binding?.tvPaySumNumber?.text = "0.00"
            else
            binding?.tvPaySumNumber?.text = it.toString()
        })

        binding?.cvEmployeeProfile?.setOnClickListener {
            findNavController().navigate(
                R.id.action_merchantFragment_to_employeeFragment,
                bundleOf("employee" to employee)
            )
        }

            //binding?.ivAlarm?.visibility = if(employee?.isChanged != null && employee?.isChanged!! < 1) View.VISIBLE else View.GONE
    }

    private fun setEmployeeText() {
        GlobalScope.launch(Dispatchers.Main){
            binding?.tvCustomerHeaderText?.text = "${employee?.firstName} ${employee?.secondName}"
            binding?.tvAbreviatura?.text = "${if(employee?.firstName?.isNullOrEmpty() == true) "" else employee?.firstName?.first()?.uppercase()}" +
                    "${if(employee?.secondName?.isNullOrEmpty() == true) "" else employee?.secondName?.first()?.uppercase()}"
        }
    }

    fun loadData(pageNym : Int = 1) {
        isLoading = true
        val disposable = ApiFactory.apiService.getPageTrn(pageNym, 10)
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.trnInfoDao().insertTrnList(it)
                //sum += "%.2f".format(it.sumOf { it.mtrnsum!! }).toDouble()
                Log.d("TEST_OF_LOADING_DATA", it.toString())
                finishRequest()
            }, {
                Log.d("TEST_OF_LOADING_DATA", it.message.toString())
                finishRequest()
            })

        compositeDisposable.add(disposable)
    }

    fun onCleared() {
        compositeDisposable.dispose()
    }


    fun finishRequest(){
        isLoading = false
        binding?.tvProgressBar?.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
        compositeDisposable.dispose()
    }
}