package com.example.mybackgroundthread.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mybackgroundthread.model.MyViewModel
import com.example.mybackgroundthread.databinding.ActivityMyViewModelBinding

class MyViewModelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyViewModelBinding
//    private lateinit var viewModel: MyViewModel
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyViewModelBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        displayResult()
        binding.btnCalculate.setOnClickListener {
            val width = binding.edtWidth.text.toString()
            val height = binding.edtHeight.text.toString()
            val length = binding.edtLength.text.toString()
            when {
                width.isEmpty() -> {
                    binding.edtWidth.error = "Masih kosong"
                }
                height.isEmpty() -> {
                    binding.edtHeight.error = "Masih kosong"
                }
                length.isEmpty() -> {
                    binding.edtLength.error = "Masih kosong"
                }
                else -> {
                    viewModel.calculate(width, height, length)
                    displayResult()
                }
            }
        }
    }

    private fun displayResult() {
        binding.tvResult.text = viewModel.result.toString()
    }
}
