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
    var reversedBuckets = myBuckets.reversed().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        bucketAdapter = BucketAdapter(this, reversedBuckets){ show -> showDeleteMenu(show) }
        binding.recyclerBuckets.adapter = bucketAdapter
        binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)

        binding.btnAddBucket.setOnClickListener{
            val intent = Intent(this,AddBucketActivity::class.java)
            startActivity(intent)
        }
    }

    fun init() {
        myBuckets = bucketsDB.BucketsDAO().getAllBuckets() as MutableList<Bucket>
        reversedBuckets= myBuckets.reversed().toMutableList()
        val bucketAdapter = BucketAdapter(this, reversedBuckets){ show -> showDeleteMenu(show) } //tell the numbersAdapter
        binding.recyclerBuckets.adapter = bucketAdapter //display the data
        bucketAdapter.setOnItemListener(object : BucketAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, EventOverviewActivity::class.java)
                intent.putExtra("bucketId", reversedBuckets[position].bucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        })
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
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}