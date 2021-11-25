package ru.inversion.sbpapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.inversion.sbpapplication.database.AppDataBase
import ru.inversion.sbpapplication.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        lateinit var db : AppDataBase
        lateinit var viewModel: MainViewModel

        fun clearAll(){
        viewModel.clearResponseTable()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        db = viewModel.db
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel.clearResponseTable()
    }




}