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
import com.example.travelbucket.databinding.ActivityEditEventBinding
import com.example.travelbucket.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditEventActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditEventBinding.inflate(layoutInflater) }

    var btnDate: Button? = null
    var textDate: TextView? = null
    var cal = Calendar.getInstance()
    var date: Date = Date(-1, -1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                    this@EditEventActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })



        binding.btnCancelEdit.setOnClickListener {
            val intent = Intent(this,EventOverviewActivity::class.java)
            startActivity(intent)
        }

        binding.btnSaveEdit.setOnClickListener {
            // TODO implement validation of inputfields
            if (binding.textInputEditTextTitle.text!!.isEmpty()) {
                binding.textInputLayoutTitle.error = "Title required!"
            } else {
                val intent = Intent(this,EventDetailsActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textDate!!.text = sdf.format(cal.getTime())
    }
}