package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.inversion.sbpapplication.MainActivity
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.database.AppDataBase
import ru.inversion.sbpapplication.databinding.FragmentRegisterOGRNBinding
import ru.inversion.sbpapplication.pojo.ResponseResult
import ru.inversion.sbpapplication.utils.AppConstants
import ru.inversion.sbpapplication.utils.Extension.Companion.closeKeyboard
import ru.inversion.sbpapplication.utils.Extension.Companion.openKeyboard
import java.util.*
import java.util.concurrent.TimeUnit

class RegisterOGRNFragment : Fragment() {

    private lateinit var bindin: FragmentRegisterOGRNBinding
    private val db = MainActivity.db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindin = FragmentRegisterOGRNBinding.inflate(inflater,container, false)
        return bindin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindin.tvResponseText.visibility  = View.INVISIBLE
        bindin.waitProgress.visibility = View.INVISIBLE
        bindin.itInputOGRN.requestFocus()
        bindin.itInputOGRN.openKeyboard(requireActivity(), )
        bindin.btRequestOGRN.setEnabled(false)
        bindin.itInputOGRN.addTextChangedListener(textWatcher())
        bindin.btRequestOGRN.setOnClickListener{
            bindin.itInputOGRN.closeKeyboard(requireActivity())
            bindin.itInputOGRN.isEnabled = false
            bindin.btRequestOGRN.setEnabled(false)
            bindin.waitProgress.visibility = View.VISIBLE
            waitResponse()
        }

    }

    private fun textWatcher() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            bindin.btRequestOGRN.text = resources.getText(R.string.request_new_customer)
            if(p0 != null && p0.length == 13)
                bindin.btRequestOGRN.text = resources.getText(R.string.request_new_customer_org)
            if(p0 != null && p0.length == 15)
                bindin.btRequestOGRN.text = resources.getText(R.string.request_new_customer_ip)

            bindin.btRequestOGRN.setEnabled(p0 != null && (p0.length == 13 || p0.length == 15))
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

        /**
         * Здесь ждем ответ от сервера о регистрации заявки на подключение и сохраняем полученный ticketId в БД.
         * Теоретически к устройству можно подцепить несколько юрлиц.
         * */
    fun waitResponse(){
        val newResponse = ResponseResult(
            keyName = "ticketId",
            KeyValue = "b5ffebc7ee9b4c3bb8865eaec4e6ab05",
            responseDate =  (Calendar.getInstance().timeInMillis + AppConstants.plusTimeZone) / 1000
        )
        db.responseInfoDao().insertResponse(newResponse)
            .delay(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bindin.waitProgress.visibility = View.INVISIBLE
                bindin.tvResponseText.text = String.format(resources.getText(R.string.request_new_responce).toString(),bindin.itInputOGRN.text)
                bindin.tvResponseText.visibility  = View.VISIBLE
            },{
                Toast.makeText(context, "Ошибка записи в БД ${it.message.toString()}", Toast.LENGTH_SHORT).show();
                Log.d("ERROR_OF_WRITE_DATA", it.message.toString())
            })
    }




}


