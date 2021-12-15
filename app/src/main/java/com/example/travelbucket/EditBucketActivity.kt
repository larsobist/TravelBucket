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
        supportActionBar?.setTitle("Edit Bucket")

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTitle)
        var editDescription = findViewById<TextInputEditText>(R.id.textInputEditDescription)

        setContent(bucketId)

        binding.textInputEditTitle.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else {
                binding.textInputLayout.error = null
            }
        }

        binding.btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent,100)
        }

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener {
            if (binding.textInputEditTitle.text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else {
                var title = (editTitle.text).toString()
                var description = (editDescription.text).toString()

                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    //bucketsDB.BucketsDAO().updateEvent(eventId, title, costs, date, location, notes, links, duration)
                    bucketsDB.BucketsDAO().updateBucket(bucketId, title, description)
                }

                val intent = Intent(this,EventOverviewActivity::class.java)
                intent.putExtra("bucketId", bucketId)
                setResult(RESULT_OK, intent)
                startActivity(intent)
            }
        }
    }
    fun setContent(bucketId: Int){
        var bucket = bucketsDB.BucketsDAO().getBucket(bucketId)
        binding.textInputEditTitle.setText(bucket.title)
        binding.textInputEditDescription.setText(bucket.description)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            binding.imgBucket.setImageURI(data?.data)
        }
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