package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    var myBuckets = mutableListOf<Bucket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
/*
        myBuckets.add(Bucket(1,"Seoul", R.drawable.seoul))
        myBuckets.add(Bucket(2,"Berlin", R.drawable.berlin))
        myBuckets.add(Bucket(3,"Rome", R.drawable.rome))
        myBuckets.add(Bucket(4,"Tokyo", R.drawable.tokyo))

 */
        myBuckets.add(Bucket(4,"Example"))

        val bucketAdapter = BucketAdapter(this, myBuckets)
        binding.recyclerBuckets.adapter = bucketAdapter
        binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)

        binding.btnAddBucket.setOnClickListener{
            val intent = Intent(this,AddBucketActivity::class.java)
            startActivity(intent)
        }

    }

    fun init() {
        //GlobalScope.launch(Dispatchers.IO) { //get all the data saved in the DB
            myBuckets = bucketsDB.BucketsDAO().getAll() as MutableList<Bucket>
            val bucketAdapter = BucketAdapter(this, myBuckets) //tell the numbersAdapter
            binding.recyclerBuckets.adapter = bucketAdapter //display the data
        //}
        //binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)
    }
}