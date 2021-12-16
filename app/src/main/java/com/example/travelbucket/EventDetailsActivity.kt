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
        /*val eventTitle = bucketsDB.BucketsDAO().getEventTitle(eventId)
        supportActionBar?.setTitle(eventTitle)*/

        var event = bucketsDB.BucketsDAO().getEvent(eventId)
        var bucket = bucketsDB.BucketsDAO().getBucket(bucketId)

        //Set title in top bar
        setTitleInBar(bucketId)
        //Set content in view
        setContent(event)

        binding.btnEditEvent.setOnClickListener{
            val intent = Intent(this,EditEventActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("eventId", eventId)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnDeleteEvent.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete")
            builder.setMessage("Do you want to delete this event?")
            builder.setPositiveButton("Delete") { dialog, which ->
                Toast.makeText(applicationContext, "Event was deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,EventOverviewActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                bucketsDB.BucketsDAO().deleteEvent(eventId)
                setResult(RESULT_OK, intent)
                startActivity(intent)

            }
            builder.setNegativeButton("Cancel") { dialog, which ->

            }
            builder.show()
        }

        binding.btnShowLocation.setOnClickListener{
            var location = event.title
            var city = bucket.title
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=$city $location") }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.btnLinks.setOnClickListener{
            /*var url = event.links
            val webpage: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }*/
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
        supportActionBar?.setTitle(bucketTitle)
    }
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
        //super.onBackPressed()
        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")

        val intent = Intent(this,EventOverviewActivity::class.java)
        intent.putExtra("bucketId", bucketId)
        setResult(RESULT_OK, intent)
        startActivity(intent)
    }
}