package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityEventOverviewBinding

class EventOverviewActivity : AppCompatActivity() {
    val binding by lazy { ActivityEventOverviewBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    var placeEvents = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        val myEvents = mutableListOf<Event>()
        val event1 = Event(0,"Gyeongbokgung Palace", "3000 Won", "12.12.2021", "Jung-Gu", "Rent a hanbok", "www.google.de","3h")
        myEvents.add(event1)
       /*
        val event2 = Event(0,"War Memorial of Korea", "0 Won", "12.12.2021", "Seoul", "only go if the weather is bad", "www.google.de","2h")
        val event3 = Event(0,"Changdeokgung Palace", "4000 Won", "12.12.2021", "Jung-Gu", "Rent a hanbok", "www.google.de","5h")
        val event4 = Event(0,"National Museum of Korea", "0 Won", "12.12.2021", "Seoul", "only go if the weather is bad", "www.google.de","1h")

        myEvents.add(event2)
        myEvents.add(event3)
        myEvents.add(event4)
        myEvents.add(event1)
        myEvents.add(event2)
        myEvents.add(event3)
        myEvents.add(event4)

        */

        val eventAdapter = EventAdapter(this, myEvents)
        binding.recyclerEvents.adapter = eventAdapter
        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)

        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(this,AddEventActivity::class.java)
            startActivity(intent)
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
}