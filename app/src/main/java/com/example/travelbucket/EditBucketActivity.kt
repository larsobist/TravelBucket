package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.travelbucket.databinding.ActivityEditBucketBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditBucketActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBucketBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle?= intent.extras
        val bucketId = bundle!!.getInt("bucketId")
        // customize app bar title
        supportActionBar?.setTitle("Edit Bucket")

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTitle)
        var editDescription = findViewById<TextInputEditText>(R.id.textInputEditDescription)

        // display current data of event in input fields
        setContent(bucketId)

        // show error if title field is empty
        binding.textInputEditTitle.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else {
                binding.textInputLayout.error = null
            }
        }

        // show error if description field is empty
        binding.textInputEditDescription.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.textInputLayoutDescription.error = "Description required!"
            } else {
                binding.textInputLayoutDescription.error = null
            }
        }

        // return to event overview activity if cancel button is clicked
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this,EventOverviewActivity::class.java)
            startActivity(intent)
        }

        // save bucket to DB
        binding.btnSave.setOnClickListener {
            // don not save while required fields are empty
            if (binding.textInputEditTitle.text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else if (binding.textInputEditDescription.text!!.isEmpty()) {
                binding.textInputLayoutDescription.error = "Description required!"
            } else {
                // get tile and description from input fields
                var title = (editTitle.text).toString()
                var description = (editDescription.text).toString()

                // insert item into DB
                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    //bucketsDB.BucketsDAO().updateEvent(eventId, title, costs, date, location, notes, links, duration)
                    bucketsDB.BucketsDAO().updateBucket(bucketId, title, description)
                }

                // go back to event overview activity
                val intent = Intent(this,EventOverviewActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        }
    }
    fun setContent(bucketId: Int){
        // get content from DB
        var bucket = bucketsDB.BucketsDAO().getBucket(bucketId)

        //display content in input fields
        binding.textInputEditTitle.setText(bucket.title)
        binding.textInputEditDescription.setText(bucket.description)
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