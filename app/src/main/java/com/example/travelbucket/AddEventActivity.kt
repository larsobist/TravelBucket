package com.example.travelbucket

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.example.travelbucket.databinding.ActivityAddEventBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEventActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddEventBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB

    var btnDate: Button? = null
    var textDate: TextView? = null
    var cal = Calendar.getInstance()
    var date: Date = Date(-1, -1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")
        val bucketTitle = bundle!!.getString("bucketTitle")

        setTitleInBar(bucketId)

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTextTitle)
        var editCosts = findViewById<TextInputEditText>(R.id.textInputEditTextCosts)
        var editLocation = findViewById<TextInputEditText>(R.id.textInputEditTextLocation)
        var editNotes = findViewById<TextInputEditText>(R.id.textInputEditTextNotes)
        var editLinks = findViewById<TextInputEditText>(R.id.textInputEditTextLinks)
        var editDuration = findViewById<TextInputEditText>(R.id.textInputEditTextDuration)

        // get the references from layout file
        textDate = binding.textDate
        btnDate = binding.btnDate

        textDate!!.text = "--/--/----"

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.getTime()
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        btnDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@AddEventActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })


        binding.btnCancelEvent.setOnClickListener {
            val intent = Intent(this,EventOverviewActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("bucketTitle", bucketTitle)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnSaveEvent.setOnClickListener {
            // TODO implement validation of inputfields
            if (binding.textInputEditTextTitle.text!!.isEmpty()) {
                binding.textInputLayoutTitle.error = "Title required!"
            } else {
                Log.d("ITM", "Title: ${date.toString()}")
                var title = (editTitle.text).toString()
                var costs = (editCosts.text).toString()
                var location = (editLocation.text).toString()
                var notes = (editNotes.text).toString()
                var links = (editLinks.text).toString()
                var duration = (editDuration.text).toString()

                val item = Event(0, bucketId,title, costs.toInt(), date, location, notes, links, duration.toInt())
                Log.d("ITM", "$item")
                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    bucketsDB.BucketsDAO().insertEvent(item)
                }

                val intent = Intent(this,EventOverviewActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        }
    }
    fun setTitleInBar(bucketId :Int){
        var bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        supportActionBar?.setTitle("$bucketTitle: Add Event")
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textDate!!.text = sdf.format(cal.getTime())
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
