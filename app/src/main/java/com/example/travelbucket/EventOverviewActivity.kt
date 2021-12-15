package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityEventOverviewBinding
import java.util.*

class EventOverviewActivity : AppCompatActivity() {
    val binding by lazy { ActivityEventOverviewBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    var bucketEvents = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")
        val bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        supportActionBar?.setTitle(bucketTitle)

        init(bucketId)

        val myEvents = mutableListOf<Event>()
        val event1 = Event(0,0,"Gyeongbokgung Palace", 3000, Date(2022, 1, 16), "Jung-Gu", "Rent a hanbok", "www.google.de",3)
        val event2 = Event(0,0,"War Memorial of Korea", 0, Date(2022, 1, 17), "Seoul", "only go if the weather is bad", "www.google.de",2)
        val event3 = Event(0,1,"Changdeokgung Palace", 4000, Date(2022, 2, 16), "Jung-Gu", "Rent a hanbok", "www.google.de",5)
        val event4 = Event(0,1,"National Museum of Korea", 0, Date(2022, 1, 16), "Seoul", "only go if the weather is bad", "www.google.de",1)
        myEvents.add(event1)
        myEvents.add(event2)
        myEvents.add(event3)
        myEvents.add(event4)
        myEvents.add(event1)
        myEvents.add(event2)
        myEvents.add(event3)
        myEvents.add(event4)

        var currentDate = getFirstDate(myEvents)
        bindDate(currentDate)

        var displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
        //val eventAdapter = EventAdapter(this, displayedEvents)
        val eventAdapter = EventAdapter(this, bucketEvents)
        binding.recyclerEvents.adapter = eventAdapter
        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)

        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(this,AddEventActivity::class.java)
            startActivity(intent)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("bucketTitle", bucketTitle)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            bucketsDB.BucketsDAO().deleteBucket(bucketId)
            bucketsDB.BucketsDAO().deleteEventsFromDeletedBucket(bucketId)
            startActivity(intent)
        }

        binding.btnEdit.setOnClickListener{
            val intent = Intent(this,EditBucketActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            currentDate = prevDate(currentDate)
            bindDate(currentDate)

            displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
            eventAdapter.update(displayedEvents)
        }

        binding.btnForward.setOnClickListener {
            currentDate = nextDate(currentDate)
            bindDate(currentDate)

            displayedEvents = getDisplayedEvents(currentDate, bucketEvents)
            eventAdapter.update(displayedEvents)
        }
    }

    fun init(bucketId: Int) {
        //GlobalScope.launch(Dispatchers.IO) { //get all the data saved in the DB
        bucketEvents = bucketsDB.BucketsDAO().getEventsOfBucket(bucketId) as MutableList<Event>
        val eventsAdapter = EventAdapter(this, bucketEvents)
        binding.recyclerEvents.adapter = eventsAdapter //display the data
        eventsAdapter.setOnItemListener(object : EventAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@EventOverviewActivity, EventDetailsActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                intent.putExtra("eventId", bucketEvents[position].eventId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        })

        dayPrice()
        dayDuration()
    }

    fun getFirstDate(myEvents: MutableList<Event>) : Date{
        var firstEvent = myEvents[0]
        for (i in myEvents) {
            if (i.date.year <= firstEvent.date.year) {
                if (i.date.month <= firstEvent.date.month) {
                    if (i.date.date <= firstEvent.date.date) {
                        firstEvent = i
                    }
                }
            }
        }
        return firstEvent.date
    }

    fun bindDate(currentDate: Date) {
        var yearStr = "${currentDate.year}"
        var monthStr = if (currentDate.month  < 10) "0${currentDate.month}" else "${currentDate.month}"
        var dayStr = if (currentDate.day  < 10) "0${currentDate.day}" else "${currentDate.day}"
        binding.date.text = "$monthStr / $dayStr / $yearStr"
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

    fun dayPrice(){
        var sumCosts = 0
        for (event in bucketEvents){
            var costs = event.costs
            sumCosts = sumCosts + costs
        }
        Log.d("ITM", "SumCosts: $sumCosts")
    }
    fun dayDuration(){
        var sumDuration = 0
        for (event in bucketEvents){
            var duration = event.duration
            sumDuration = sumDuration + duration
        }
        Log.d("ITM", "SumDuration: $sumDuration")
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}