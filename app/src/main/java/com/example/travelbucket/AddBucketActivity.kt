package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.travelbucket.databinding.ActivityAddBucketBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddBucketActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddBucketBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTitle)
        var editDescription = findViewById<TextInputEditText>(R.id.textInputEditDescription)

        binding.textInputEditTitle.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else {
                binding.textInputLayout.error = null
            }
        }

        binding.textInputEditDescription.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.textInputLayoutDescription.error = "Description required!"
            } else {
                binding.textInputLayoutDescription.error = null
            }
        }

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener {
            if (binding.textInputEditTitle.text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else if (binding.textInputEditDescription.text!!.isEmpty()) {
                binding.textInputLayoutDescription.error = "Description required!"
            } else {
                var title = (editTitle.text).toString()
                var description = (editDescription.text).toString()


               // val item = Bucket(0, title, 1)
                val item = Bucket(0, title, description)
                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    bucketsDB.BucketsDAO().insertBucket(item)
                }

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}