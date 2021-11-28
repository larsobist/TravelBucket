package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myBuckets = mutableListOf<Bucket>()
        myBuckets.add(Bucket("Seoul", R.drawable.seoul))
        myBuckets.add(Bucket("Berlin", R.drawable.berlin))
        myBuckets.add(Bucket("Rome", R.drawable.rome))
        myBuckets.add(Bucket("Tokyo", R.drawable.tokyo))

        val bucketAdapter = BucketAdapter(myBuckets)
        binding.recyclerBuckets.adapter = bucketAdapter
        binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)

        binding.btnAddBucket.setOnClickListener{
            val intent = Intent(this,AddBucketActivity::class.java)
            startActivity(intent)
        }
    }
}