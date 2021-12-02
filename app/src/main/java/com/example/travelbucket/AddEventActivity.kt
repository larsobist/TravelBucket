package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import com.example.travelbucket.databinding.ActivityAddEventBinding
import com.example.travelbucket.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEventActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddEventBinding.inflate(layoutInflater) }
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



                //val item = Bucket(0, title, 1) //warum ein int?
                //GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                //    bucketsDB.BucketsDAO().insert(item)
                    //bucketsDB.BucketsDAO().nukeTable()
                //}

                val intent = Intent(this,EventOverviewActivity::class.java)
                startActivity(intent)
            }
        }
    }
}