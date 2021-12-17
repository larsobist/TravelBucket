package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    var myBuckets = mutableListOf<Bucket>()
    private lateinit var bucketAdapter: BucketAdapter
    private var mainMenu: Menu? = null
    // store buckets in reversed order
    var reversedBuckets = myBuckets.reversed().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        // display buckets
        bucketAdapter = BucketAdapter(this, reversedBuckets)
        binding.recyclerBuckets.adapter = bucketAdapter
        binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)

        // if add bucket button is clicked, go to add bucket activity
        binding.btnAddBucket.setOnClickListener{
            val intent = Intent(this,AddBucketActivity::class.java)
            startActivity(intent)
        }
    }

    fun init() {
        // get all buckets from DB
        myBuckets = bucketsDB.BucketsDAO().getAllBuckets() as MutableList<Bucket>
        // store buckets in reversed order
        reversedBuckets= myBuckets.reversed().toMutableList()
        val bucketAdapter = BucketAdapter(this, reversedBuckets) //tell the numbersAdapter
        binding.recyclerBuckets.adapter = bucketAdapter //display the data
        // if item is clicked, go to event overview activity
        bucketAdapter.setOnItemListener(object : BucketAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, EventOverviewActivity::class.java)
                // pass bucketId with intent
                intent.putExtra("bucketId", reversedBuckets[position].bucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        //nothing should happen
    }
}