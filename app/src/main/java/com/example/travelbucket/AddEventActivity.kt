package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.travelbucket.databinding.ActivityAddEventBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEventActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddEventBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTextTitle)
        var editCosts = findViewById<TextInputEditText>(R.id.textInputEditTextCosts)
        var editDate = findViewById<TextInputEditText>(R.id.textInputEditTextDate)
        var editLocation = findViewById<TextInputEditText>(R.id.textInputEditTextLocation)
        var editNotes = findViewById<TextInputEditText>(R.id.textInputEditTextNotes)
        var editLinks = findViewById<TextInputEditText>(R.id.textInputEditTextLinks)
        var editDuration = findViewById<TextInputEditText>(R.id.textInputEditTextDuration)


        binding.btnCancelEvent.setOnClickListener {
            val intent = Intent(this,EventOverviewActivity::class.java)
            startActivity(intent)
        }

        binding.btnSaveEvent.setOnClickListener {
            // TODO implement validation of inputfields
            if (binding.textInputEditTextTitle.text!!.isEmpty()) {
                binding.textInputLayoutTitle.error = "Title required!"
            } else {
                var title = (editTitle.text).toString()
                Log.d("ITM", "Title: $title")
                var costs = (editCosts.text).toString()
                Log.d("ITM", "costs: $costs")
                var date = (editDate.text).toString()
                Log.d("ITM", "date: $date")
                var location = (editLocation.text).toString()
                Log.d("ITM", "location: $location")
                var notes = (editNotes.text).toString()
                Log.d("ITM", "notes: $notes")
                var links = (editLinks.text).toString()
                Log.d("ITM", "links: $links")
                var duration = (editDuration.text).toString()
                Log.d("ITM", "duration: $duration")

                val item = Event(0, title, costs, date, location, notes, links, duration)
                Log.d("ITM", "$item")
                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    bucketsDB.EventsDAO().insert(item)
                    //bucketsDB.BucketsDAO().nukeTable()
                }

                val intent = Intent(this,EventOverviewActivity::class.java)
                startActivity(intent)
            }
        }
    }
}