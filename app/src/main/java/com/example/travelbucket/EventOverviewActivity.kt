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
    var placeEvents = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        val myEvents = mutableListOf<Event>()
        val event1 = Event(0,"Gyeongbokgung Palace", "3000 Won", Date(2022, 1, 16), "Jung-Gu", "Rent a hanbok", "www.google.de","3h")
        val event2 = Event(0,"War Memorial of Korea", "0 Won", Date(2022, 1, 17), "Seoul", "only go if the weather is bad", "www.google.de","2h")
        val event3 = Event(0,"Changdeokgung Palace", "4000 Won", Date(2022, 2, 16), "Jung-Gu", "Rent a hanbok", "www.google.de","5h")
        val event4 = Event(0,"National Museum of Korea", "0 Won", Date(2022, 1, 16), "Seoul", "only go if the weather is bad", "www.google.de","1h")
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

        var displayedEvents = getDisplayedEvents(currentDate, myEvents)
        val eventAdapter = EventAdapter(this, displayedEvents)
        binding.recyclerEvents.adapter = eventAdapter
        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)

        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(this,AddEventActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            currentDate = prevDate(currentDate)
            bindDate(currentDate)

            displayedEvents = getDisplayedEvents(currentDate, myEvents)
            eventAdapter.update(displayedEvents)
        }

        binding.btnForward.setOnClickListener {
            currentDate = nextDate(currentDate)
            bindDate(currentDate)

            displayedEvents = getDisplayedEvents(currentDate, myEvents)
            eventAdapter.update(displayedEvents)
        }


    }

    fun init() {
        //GlobalScope.launch(Dispatchers.IO) { //get all the data saved in the DB
        placeEvents = bucketsDB.EventsDAO().getAll() as MutableList<Event>
        Log.d("ITM", "Title: $placeEvents")
        //val eventAdapter = EventAdapter(this, placeEvents) //tell the numbersAdapter
        //binding.recyclerEvents.adapter = eventAdapter //display the data
        //}
        //binding.recyclerBuckets.layoutManager = LinearLayoutManager(this)
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
}