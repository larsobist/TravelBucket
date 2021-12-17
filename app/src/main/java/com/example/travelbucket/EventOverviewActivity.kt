package com.example.travelbucket

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        // store bucket id in public variable
        publicBucketId = bucketId

        val bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        val bucketColor = bucketsDB.BucketsDAO().getBucketColor(bucketId)

        // set background color to bucket color
        binding.constraintLayout.setBackgroundColor(Color.parseColor(bucketColor))

        //set action bar title to bucket title
        supportActionBar?.setTitle(bucketTitle)

        init(bucketId)

        // show edit and delete button in app bar
        showDeleteMenu(true)

        // set current date to date of first event
        var currentDate = getFirstDate(bucketEvents)

        // display correct date in view
        bindDate(currentDate)

        // save events of currentDate in displayedEvents
        displayedEvents = getDisplayedEvents(currentDate, bucketEvents)

        // display events of currentDate in view
        val eventAdapter = EventAdapter(this, displayedEvents)
        binding.recyclerEvents.adapter = eventAdapter
        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)

        // display daily cost and duration
        binding.sumPrice.text = "Daily cost: " + dayPrice().toString() +" ₩"
        binding.sumDuration.text =  "Daily duration: " + dayDuration().toString() +"h"

        // if add event button is clicked, go to add event activity
        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(this,AddEventActivity::class.java)
            startActivity(intent)
            // pass bucketId, bucketTitle and date with intent
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("bucketTitle", bucketTitle)
            intent.putExtra("date", currentDate.getTime())
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        // go to previous day
        binding.btnBack.setOnClickListener {
            // change currentDate
            currentDate = prevDate(currentDate)

            //update displayed date
            bindDate(currentDate)

            // update displayed events
            displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
            eventAdapter.update(displayedEvents)

            // update daily cost and duration
            binding.sumPrice.text = "Daily cost: " + dayPrice().toString() +" ₩"
            binding.sumDuration.text =  "Daily duration: " + dayDuration().toString() +"h"
        }

        // go to next day
        binding.btnForward.setOnClickListener {
            // change currentDate
            currentDate = nextDate(currentDate)

            //update displayed date
            bindDate(currentDate)

            // update displayed events
            displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
            eventAdapter.update(displayedEvents)

            // update daily cost and duration
            binding.sumPrice.text = "Daily cost: " + dayPrice().toString() +" ₩"
            binding.sumDuration.text =  "Daily duration: " + dayDuration().toString() +"h"
        }
    }

    fun init(bucketId: Int) {
        bucketEvents = bucketsDB.BucketsDAO().getEventsOfBucket(bucketId) as MutableList<Event>
        val eventsAdapter = EventAdapter(this, displayedEvents)
        binding.recyclerEvents.adapter = eventsAdapter //display the data
        // if item is clicked, got to event details activity
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
        // if bucket has no events, set current date as first date
        if (myEvents.isEmpty()){
            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime()
        }else{
            // find first event
            var firstEvent = myEvents[0]
            for (i in 0 until myEvents.size) {
                if (myEvents[i].date.before(firstEvent.date)) {
                    firstEvent = myEvents[i]
                }
            }
            // return date of first event
            return firstEvent.date
        }
    }

    fun bindDate(currentDate: Date) {
        // display date in correct format
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.date.text = sdf.format(currentDate)
    }

    // make list of all events with currentDate and return it
    fun getDisplayedEvents(currentDate: Date, myEvents: MutableList<Event>) : MutableList<Event> {
        val displayedEvents : MutableList<Event> = mutableListOf()
        for (i in myEvents) {
            if (i.date == currentDate){
                displayedEvents.add(i)
            }
        }
        return displayedEvents
    }

    // change date to one day before
    fun prevDate(currentDate: Date) : Date {
        val c = Calendar.getInstance()
        c.time = currentDate
        c.add(Calendar.DATE, -1)
        return c.time
    }

    // change date to next day
    fun nextDate(currentDate: Date) : Date {
        val c = Calendar.getInstance()
        c.time = currentDate
        c.add(Calendar.DATE, 1)
        return c.time
    }

    // calculate price of all events on current day
    fun dayPrice() : Int{
        var sumCosts = 0
        for (event in displayedEvents){
            var costs = event.costs
            sumCosts = sumCosts + costs
        }
        return sumCosts
    }

    // calculate duration of all events on current day
    fun dayDuration() : Int {
        var sumDuration = 0
        for (event in displayedEvents){
            var duration = event.duration
            sumDuration = sumDuration + duration
        }
        return sumDuration
    }


    override fun onBackPressed() {
        // on back press go back to main activity
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun showDeleteMenu(show: Boolean) {
        // show edit and delete button in app bar
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
            // if delete button is clicked, call delete method
            R.id.btnDeleteBucket -> { delete() }

            // if edit button is clicked, go to edit bucket activity
            R.id.btnEditBucket -> {
                val intent = Intent(this,EditBucketActivity::class.java)
                // pass bucketId with intent
                intent.putExtra("bucketId", publicBucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete() {
        // create alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this bucket?")
        builder.setPositiveButton("Delete") { dialog, which ->
            // display toast
            Toast.makeText(applicationContext, "Bucket was deleted", Toast.LENGTH_SHORT).show()
            // delete bucket from DB
            val intent = Intent(this,MainActivity::class.java)
            bucketsDB.BucketsDAO().deleteBucket(publicBucketId)
            bucketsDB.BucketsDAO().deleteEventsFromDeletedBucket(publicBucketId)
            startActivity(intent)

        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // close dialog
        }
        // display dialog
        builder.show()
    }
}