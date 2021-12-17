package com.example.travelbucket

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.travelbucket.databinding.ActivityEventDetailsBinding
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

        // get event and bucket
        var event = bucketsDB.BucketsDAO().getEvent(eventId)
        var bucket = bucketsDB.BucketsDAO().getBucket(bucketId)

        //Set title in top bar
        setTitleInBar(bucketId)
        //Set content in view
        setContent(event)

        // if edit button is clicked go to edit event activity
        binding.btnEditEvent.setOnClickListener{
            val intent = Intent(this,EditEventActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("eventId", eventId)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        // if delete button is clicked, delete the event
        binding.btnDeleteEvent.setOnClickListener{
            // create alert dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete")
            builder.setMessage("Do you want to delete this event?")
            builder.setPositiveButton("Delete") { dialog, which ->
                // display toast
                Toast.makeText(applicationContext, "Event was deleted", Toast.LENGTH_SHORT).show()
                // delete event from DB
                val intent = Intent(this,EventOverviewActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                bucketsDB.BucketsDAO().deleteEvent(eventId)
                setResult(RESULT_OK, intent)
                startActivity(intent)

            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                // close dialog
            }
            // display dialog
            builder.show()
        }

        // open event in map
        binding.btnShowLocation.setOnClickListener{
            var location = event.title
            var city = bucket.title
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=$city $location") }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        // open link in browser
        binding.btnLinks.setOnClickListener{
            var url = event.links
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, url )
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    fun setTitleInBar(bucketId :Int){
        var bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        // set action bar title to bucket title
        supportActionBar?.setTitle(bucketTitle)
    }

    // display content from DB
    fun setContent(event: Event){
        binding.textViewTitle.text = event.title
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textViewDate.text = sdf.format(event.date)
        binding.textViewDuration.text = event.duration.toString()+"h"
        binding.textViewCosts.text = event.costs.toString()+" ₩"
        binding.textViewNotes.text = event.notes
        binding.btnLinks.text = event.links
    }

    override fun onBackPressed() {
        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")

        // on back press go back to event overview activity
        val intent = Intent(this,EventOverviewActivity::class.java)
        intent.putExtra("bucketId", bucketId)
        setResult(RESULT_OK, intent)
        startActivity(intent)
    }
}