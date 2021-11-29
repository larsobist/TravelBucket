package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.travelbucket.databinding.ActivityAddEventBinding
import com.example.travelbucket.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEventActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddEventBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCancelEvent.setOnClickListener {
            val intent = Intent(this,EventOverviewActivity::class.java)
            startActivity(intent)
        }

        binding.btnSaveEvent.setOnClickListener {
            // TODO implement validation of inputfields
        }
    }
}