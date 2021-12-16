package com.example.travelbucket

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.travelbucket.databinding.ActivityEditEventBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditEventActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditEventBinding.inflate(layoutInflater) }
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
        val eventId = bundle!!.getInt("eventId")

        //Set title in top bar
        setTitleInBar(bucketId)
        //Set content in view
        setContent(eventId)

        // get the references from layout file
        textDate = binding.textDate
        btnDate = binding.btnDate

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTextTitle)
        var layoutTitle = findViewById<TextInputLayout>(R.id.textInputLayoutTitle)
        var editCosts = findViewById<TextInputEditText>(R.id.textInputEditTextCosts)
        var layoutCosts = findViewById<TextInputLayout>(R.id.textInputLayoutCosts)
        var editNotes = findViewById<TextInputEditText>(R.id.textInputEditTextNotes)
        var editLinks = findViewById<TextInputEditText>(R.id.textInputEditTextLinks)
        var editDuration = findViewById<TextInputEditText>(R.id.textInputEditTextDuration)
        var layoutDuration = findViewById<TextInputLayout>(R.id.textInputLayoutDuration)

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
                    this@EditEventActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        editTitle.addTextChangedListener {
            if (editTitle.text!!.isEmpty()) {
                layoutTitle.error = "Title required!"
            } else{
                layoutTitle.error = null
            }
        }

        editCosts.addTextChangedListener {
            if (editCosts.text!!.isEmpty()) {
                layoutCosts.error = "Costs required!"
            } else{
                layoutCosts.error = null
            }
        }

        editDuration.addTextChangedListener {
            if (editDuration.text!!.isEmpty()) {
                layoutDuration.error = "Duration required!"
            } else{
                layoutDuration.error = null
            }
        }

        binding.btnCancelEdit.setOnClickListener {
            val intent = Intent(this,EventDetailsActivity::class.java)
            intent.putExtra("bucketId", bucketId)
            intent.putExtra("eventId", eventId)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        binding.btnSaveEdit.setOnClickListener {
            if (editTitle.text!!.isEmpty()) {
                layoutTitle.error = "Title required!"
            } else if (editCosts.text!!.isEmpty()) {
                layoutCosts.error = "Costs required!"
            } else if (editDuration.text!!.isEmpty()) {
                layoutDuration.error = "Duration required!"
            } else {
                var title = (editTitle.text).toString()
                var costs = (editCosts.text).toString()
                var notes = (editNotes.text).toString()
                var links = (editLinks.text).toString()
                var duration = (editDuration.text).toString()

                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    //bucketsDB.BucketsDAO().updateEvent(eventId, title, costs.toInt(), date, notes, links, duration.toInt())
                    bucketsDB.BucketsDAO().updateEvent(eventId, title, costs.toInt(), notes, links, duration.toInt())
                }

                val intent = Intent(this,EventDetailsActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                intent.putExtra("eventId", eventId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        }
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textDate!!.text = sdf.format(date)
    }

    fun setTitleInBar(bucketId :Int){
        var bucketTitle = bucketsDB.BucketsDAO().getBucketTitle(bucketId)
        supportActionBar?.setTitle("$bucketTitle: Edit Event")
    }
    fun setContent(eventId: Int){
        var event = bucketsDB.BucketsDAO().getEvent(eventId)
        date = event.date
        updateDateInView()
        binding.textInputEditTextTitle.setText(event.title)
        binding.textInputEditTextDuration.setText(event.duration.toString())
        binding.textInputEditTextCosts.setText(event.costs.toString())
        binding.textInputEditTextNotes.setText(event.notes)
        binding.textInputEditTextLinks.setText(event.links)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")
        val eventId = bundle!!.getInt("eventId")

        val intent = Intent(this,EventDetailsActivity::class.java)
        intent.putExtra("bucketId", bucketId)
        intent.putExtra("eventId", eventId)
        setResult(RESULT_OK, intent)
        startActivity(intent)
    }
}