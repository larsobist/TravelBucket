package com.example.travelbucket

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityEventOverviewBinding
import java.text.SimpleDateFormat
import java.util.*

class EventOverviewActivity : AppCompatActivity() {
    val binding by lazy { ActivityEventOverviewBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    var bucketEvents = mutableListOf<Event>()
    var displayedEvents = mutableListOf<Event>()
    private var mainMenu: Menu? = null
    var publicBucketId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")
        publicBucketId = bucketId
        val bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        val bucketColor = bucketsDB.BucketsDAO().getBucketColor(bucketId)
        binding.constraintLayout.setBackgroundColor(Color.parseColor(bucketColor))
        supportActionBar?.setTitle(bucketTitle)

        init(bucketId)

        showDeleteMenu(true)

        var currentDate = getFirstDate(bucketEvents)
        bindDate(currentDate)

        displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
        val eventAdapter = EventAdapter(this, displayedEvents)
        binding.recyclerEvents.adapter = eventAdapter
        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)

        binding.sumPrice.text = "Daily cost: " + dayPrice().toString() +" ₩"
        binding.sumDuration.text =  "Daily duration: " + dayDuration().toString() +"h"

        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(this,AddEventActivity::class.java)
            startActivity(intent)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("bucketTitle", bucketTitle)
            intent.putExtra("date", currentDate.getTime())
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            currentDate = prevDate(currentDate)
            bindDate(currentDate)

            displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
            eventAdapter.update(displayedEvents)

            binding.sumPrice.text = "Daily cost: " + dayPrice().toString() +" ₩"
            binding.sumDuration.text =  "Daily duration: " + dayDuration().toString() +"h"
        }

        binding.btnForward.setOnClickListener {
            currentDate = nextDate(currentDate)
            bindDate(currentDate)

            displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
            eventAdapter.update(displayedEvents)

            binding.sumPrice.text = "Daily cost: " + dayPrice().toString() +" ₩"
            binding.sumDuration.text =  "Daily duration: " + dayDuration().toString() +"h"
        }
    }

    fun init(bucketId: Int) {
        //GlobalScope.launch(Dispatchers.IO) { //get all the data saved in the DB
        bucketEvents = bucketsDB.BucketsDAO().getEventsOfBucket(bucketId) as MutableList<Event>
        val eventsAdapter = EventAdapter(this, displayedEvents)
        binding.recyclerEvents.adapter = eventsAdapter //display the data
        eventsAdapter.setOnItemListener(object : EventAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@EventOverviewActivity, EventDetailsActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                intent.putExtra("eventId", displayedEvents[position].eventId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        })

    }

    fun getFirstDate(myEvents: MutableList<Event>) : Date{
        if (myEvents.isEmpty()){
            var cal = Calendar.getInstance()
            return cal.getTime()
        }else{
            var firstEvent = myEvents[0]
            for (i in 0 until myEvents.size) {
                if (myEvents[i].date.before(firstEvent.date)) {
                    firstEvent = myEvents[i]
                }
            }
            return firstEvent.date
        }
    }

    fun bindDate(currentDate: Date) {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.date.text = sdf.format(currentDate)
    }

    fun getDisplayedEvents(currentDate: Date, myEvents: MutableList<Event>) : MutableList<Event> {
        val displayedEvents : MutableList<Event> = mutableListOf()
        for (i in myEvents) {
            if (i.date == currentDate){
                displayedEvents.add(i)
            }
        }
        return  displayedEvents
    }

    fun prevDate(currentDate: Date) : Date {
        val c = Calendar.getInstance()
        c.time = currentDate
        c.add(Calendar.DATE, -1)
        return c.time
    }

    fun nextDate(currentDate: Date) : Date {
        val c = Calendar.getInstance()
        c.time = currentDate
        c.add(Calendar.DATE, 1)
        return c.time
    }

    fun dayPrice() : Int{
        var sumCosts = 0
        for (event in displayedEvents){
            var costs = event.costs
            sumCosts = sumCosts + costs
        }
        Log.d("ITM", "SumCosts: $sumCosts")
        return sumCosts
    }

    fun dayDuration() : Int {
        var sumDuration = 0
        for (event in displayedEvents){
            var duration = event.duration
            sumDuration = sumDuration + duration
        }
        Log.d("ITM", "SumDuration: $sumDuration")
        return sumDuration
    }


    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.btnDeleteBucket)?.isVisible = show
        mainMenu?.findItem(R.id.btnEditBucket)?.isVisible = show
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.bucket_menu, mainMenu)
        showDeleteMenu(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnDeleteBucket -> { delete() }
            R.id.btnEditBucket -> {
                val intent = Intent(this,EditBucketActivity::class.java)
                intent.putExtra("bucketId", publicBucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this bucket?")
        builder.setPositiveButton("Delete") { dialog, which ->
            Toast.makeText(applicationContext, "Bucket was deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            bucketsDB.BucketsDAO().deleteBucket(publicBucketId)
            bucketsDB.BucketsDAO().deleteEventsFromDeletedBucket(publicBucketId)
            startActivity(intent)

        }
        builder.setNegativeButton("Cancel") { dialog, which ->

        }
        builder.show()
    }
}