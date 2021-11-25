package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentFirstEnterBinding
import ru.inversion.sbpapplication.pojo.Customer
import ru.inversion.sbpapplication.utils.AppConstants.tempOGRN

class FirstEnterFragment : Fragment() {

    private lateinit var bindin: FragmentFirstEnterBinding
    private val db = MainActivity.db
    private var tickedId : String = ""
    private var respCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindin = FragmentFirstEnterBinding.inflate(inflater,container, false)
        return bindin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                System.exit(0)
            }
        })

        bindin.pbRegiteredWait.visibility = View.INVISIBLE
        bindin.btNewAccaunt.setOnClickListener{
            respCount++
            if(tickedId.isEmpty())
                findNavController().navigate(R.id.action_firstEnterFragment_to_registerOGRNFragment)
            else{/**Тута делаем запрос статуса регистрации по ticketId*/
                if(respCount == 2){
                    bindin.pbRegiteredWait.visibility = View.VISIBLE
                    GlobalScope.launch(Dispatchers.IO){
                        delay(3000)
                        GlobalScope.launch(Dispatchers.Main){
                            bindin.accauntText.text = String.format(resources.getText(R.string.request_accaunts_status_in_process_text).toString(),tickedId)
                            bindin.pbRegiteredWait.visibility = View.INVISIBLE
                        }
                    }
                }
                if(respCount >= 3){
                    /**Записываем тестовое предприятие в бд*/
                    val newCustomer = Customer(legalId = "LF0000000001", name = "OOO Ромашка", ogrn = tempOGRN, inn = "3664069397")
                    db.customersDao().insertCustomer(newCustomer).subscribeOn(Schedulers.io()).subscribe(
                        {Log.d("SUCCESS_OF_LOADING_DATA","")},
                        {Log.d("ERROR_OF_LOADING_DATA", it.message.toString())}
                    )
                    bindin.pbRegiteredWait.visibility = View.VISIBLE
                    GlobalScope.launch(Dispatchers.IO){
                        delay(3000)
                        GlobalScope.launch(Dispatchers.Main){
                            bindin.accauntText.text = String.format(resources.getText(R.string.request_accaunts_status_is_created_text).toString(),tickedId)
                            bindin.btNewAccaunt.visibility = View.GONE
                            bindin.pbRegiteredWait.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }

        bindin.btSignIn.setOnClickListener{
            findNavController().navigate(R.id.action_firstEnterFragment_to_firstPinEnteredFragment)
        }
        getWaitedResponse()
    }

    private fun getWaitedResponse() {
        db.responseInfoDao().getResponseList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.isNotEmpty()){
                    it.filter{ it.keyName.equals("ticketId")}.first()
                        ?.let {
                            bindin.accauntText.text = String.format(resources.getText(R.string.request_accaunts_text).toString(), it.getFormatedTime(),it.KeyValue)
                            bindin.btNewAccaunt.text = resources.getText(R.string.reRegister_new_accaunt_button_text).toString()
                            tickedId = it.KeyValue.toString()
                        }
                }
            },{
                Log.d("ERROR_OF_LOADING_DATA", it.message.toString())
            })
    }


}