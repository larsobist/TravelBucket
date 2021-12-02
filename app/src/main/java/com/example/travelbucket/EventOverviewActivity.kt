package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelbucket.databinding.ActivityAddBucketBinding.inflate
import com.example.travelbucket.databinding.ActivityEventOverviewBinding
import com.example.travelbucket.databinding.ActivityMainBinding

class EventOverviewActivity : AppCompatActivity() {
    val binding by lazy { ActivityEventOverviewBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myEvents = mutableListOf<Event>()
        val event1 = Event(0,"Gyeongbokgung Palace", "3000 Won", "12.12.2021", "Jung-Gu", "Rent a hanbok", "www.google.de","3h")
        val event2 = Event(0,"War Memorial of Korea", "0 Won", "12.12.2021", "Seoul", "only go if the weather is bad", "www.google.de","2h")
        val event3 = Event(0,"Changdeokgung Palace", "4000 Won", "12.12.2021", "Jung-Gu", "Rent a hanbok", "www.google.de","5h")
        val event4 = Event(0,"National Museum of Korea", "0 Won", "12.12.2021", "Seoul", "only go if the weather is bad", "www.google.de","1h")
        myEvents.add(event1)
        myEvents.add(event2)
        myEvents.add(event3)
        myEvents.add(event4)
        myEvents.add(event1)
        myEvents.add(event2)
        myEvents.add(event3)
        myEvents.add(event4)

        val eventAdapter = EventAdapter(this, myEvents)
        binding.recyclerEvents.adapter = eventAdapter
        binding.recyclerEvents.layoutManager = LinearLayoutManager(this)

        binding.btnAddEvent.setOnClickListener {
            val intent = Intent(this,AddEventActivity::class.java)
            startActivity(intent)
        }
    }
}