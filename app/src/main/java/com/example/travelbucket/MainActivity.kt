package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    var myBuckets = mutableListOf<Bucket>()
    private lateinit var bucketAdapter: BucketAdapter
    private var mainMenu: Menu? = null

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
        myBuckets.add(Bucket(4,"Example", false))

        bucketAdapter = BucketAdapter(this, myBuckets){ show -> showDeleteMenu(show) }
        binding.recyclerBuckets.adapter = bucketAdapter
        binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)

        binding.btnAddBucket.setOnClickListener{
            val intent = Intent(this,AddBucketActivity::class.java)
            startActivity(intent)
        }

    }

    fun init() {
        //GlobalScope.launch(Dispatchers.IO) { //get all the data saved in the DB
        myBuckets = bucketsDB.BucketsDAO().getAllBuckets() as MutableList<Bucket>
        val bucketAdapter = BucketAdapter(this, myBuckets){ show -> showDeleteMenu(show) } //tell the numbersAdapter
        binding.recyclerBuckets.adapter = bucketAdapter //display the data
        bucketAdapter.setOnItemListener(object : BucketAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Log.d("ITM","Item $position clicked")
                val intent = Intent(this@MainActivity, EventOverviewActivity::class.java)
                intent.putExtra("id", myBuckets[position].bucketId)
                intent.putExtra("title", myBuckets[position].title)
                Log.d("ITM","${myBuckets[position].title}")
                setResult(RESULT_OK, intent)
                startActivity(intent)

            }

        })
        //}
        //binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)
    }

    fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.btnDeleteBucket)?.isVisible = show
        mainMenu?.findItem(R.id.btnEditBucket)?.isVisible = show
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.bucket_menu, mainMenu)
        showDeleteMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnDeleteBucket -> { delete() }
            R.id.btnEditBucket -> {
                val intent = Intent(this,EditBucketActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this bucket?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
        builder.setPositiveButton("Delete") { dialog, which ->
            bucketAdapter.smthSelected = false
            /*Toast.makeText(applicationContext, "Bucket was deleted", Toast.LENGTH_SHORT).show()
            showDeleteMenu(false)*/
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            /*
            // unselect bucket
            bucketAdapter.binding.cardView.strokeWidth = 0
            bucketAdapter.bucket.selected = false
            bucketAdapter.smthSelected = false
            showDeleteMenu(false)*/
        }
        builder.show()

    }
}