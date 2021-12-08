package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelbucket.databinding.ActivityEventDetailsBinding
import com.example.travelbucket.databinding.ActivityMainBinding

class EventDetailsActivity : AppCompatActivity() {
    val binding by lazy { ActivityEventDetailsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnEditEvent.setOnClickListener{
            val intent = Intent(this,EditEventActivity::class.java)
            startActivity(intent)
        }
    }
}