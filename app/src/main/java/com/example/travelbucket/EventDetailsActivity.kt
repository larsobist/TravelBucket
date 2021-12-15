package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.travelbucket.databinding.ActivityEventDetailsBinding
import com.example.travelbucket.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsActivity : AppCompatActivity() {
    val binding by lazy { ActivityEventDetailsBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")
        val eventId = bundle!!.getInt("eventId")

        //Set title in top bar
        setTitleInBar(bucketId)
        //Set content in view
        setContent(eventId)

        binding.btnEditEvent.setOnClickListener{
            val intent = Intent(this,EditEventActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("eventId", eventId)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnDeleteEvent.setOnClickListener{
            val intent = Intent(this,EventOverviewActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            bucketsDB.BucketsDAO().deleteEvent(eventId)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }
    }

    fun setTitleInBar(bucketId :Int){
        var bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        supportActionBar?.setTitle(bucketTitle)
    }
    fun setContent(eventId: Int){
        var event = bucketsDB.BucketsDAO().getEvent(eventId)
        binding.textViewTitle.text = event.title
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textViewDate.text = sdf.format(event.date)
        binding.textViewDuration.text = event.duration.toString()+"h"
        binding.textViewLocation.text = event.location
        binding.textViewCosts.text = event.costs.toString()+" ₩"
        binding.textViewNotes.text = event.notes
        binding.textViewLinks.text = event.links
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")

        val intent = Intent(this,EventOverviewActivity::class.java)
        intent.putExtra("bucketId", bucketId)
        setResult(RESULT_OK, intent)
        startActivity(intent)
    }
}