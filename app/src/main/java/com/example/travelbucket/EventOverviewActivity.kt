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
        val event1 = Event("Gyeongbokgung Palace", "3h", "you can rent hanbok")
        val event2 = Event("War Memorial of Korea", "2h", "go when weather is good")
        val event3 = Event("Changdeokgung Palace", "3h", "be there before 5pm")
        val event4 = Event("National Museum of Korea", "3h", "there is a group discount")
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