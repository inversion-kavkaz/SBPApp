package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.inversion.sbpapplication.database.AppDataBase
import ru.inversion.sbpapplication.databinding.FragmentSplashBinding
import ru.inversion.sbpapplication.utils.convertTimestampToTime
import ru.inversion.sbpapplication.utils.AppConstants.plusTimeZone

import java.util.*



class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private var  db: AppDataBase = MainActivity.db
    private var employeeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**Здесь нужно проверить  если в БД есть текущий  ticketId - значит был запрос на регистрацию клиента.
         * Нужно запросить статус регистрации. Если статус вернул CREATED то запросить данные клиента  по legalId и забить их в БД
         * наличие текущего клиента в БД
         * */

        //findNavController().navigate(R.id.action_splashFragment_to_employeeFragment)

        val time = Calendar.getInstance().timeInMillis / 1000
        println("date = ${convertTimestampToTime(time)}")

        db.employeeDao().getEmployeeCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       employeeCount = it
            },{ Log.d("ERROR_OF_LOADING_DATA", it.message.toString())})

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            if(employeeCount < 1)
                findNavController().navigate(R.id.action_splashFragment_to_firstEnterFragment)
            else
                findNavController().navigate(R.id.action_splashFragment_to_pinFragment)
        }
    }
}