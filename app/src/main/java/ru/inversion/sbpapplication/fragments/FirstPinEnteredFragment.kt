package ru.inversion.sbpapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.inversion.sbpapplication.R
import ru.inversion.sbpapplication.databinding.FragmentFirstPinEnteredBinding
import ru.inversion.sbpapplication.utils.AppConstants
import ru.inversion.sbpapplication.utils.AppConstants.tempOGRN
import ru.inversion.sbpapplication.utils.AppConstants.tempPIN

class FirstPinEnteredFragment : Fragment() {

    private lateinit var binding: FragmentFirstPinEnteredBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstPinEnteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btSignIn2.isEnabled = false
        binding.itInputOGRN.addTextChangedListener(textWatcher())
        binding.itInputPIN.addTextChangedListener(textWatcher())

        binding.btSignIn2.setOnClickListener{
            /**Берем логин пароль и отправляем на сервер для проверки
             * Если косячные извиняемся и говорим пошел нахуй!
             * Если норм то входим на основную страницу*/
            if(binding.itInputOGRN.text.toString() == tempOGRN && binding.itInputPIN.text.toString() == tempPIN){
                findNavController().navigate(R.id.action_firstPinEnteredFragment_to_mainFragment)
            }
        }


    }

    private fun textWatcher() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.btSignIn2.isEnabled = (binding.itInputOGRN.text?.length!! > 0 && binding.itInputPIN.text?.length!! > 0)
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

}