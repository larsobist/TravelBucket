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

var colorCount = 0
class AddBucketActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddBucketBinding.inflate(layoutInflater) }
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(this) } //binding of DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var editTitle = findViewById<TextInputEditText>(R.id.textInputEditTitle)
        var editDescription = findViewById<TextInputEditText>(R.id.textInputEditDescription)
        var color: String

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

        // return to main activity if cancel button is clicked
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
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

                // select a color for the bucket
                when (colorCount){
                    0 -> color = "#D3CBF5"
                    1 -> color = "#BCEBAE"
                    2 -> color = "#FFF6AB"
                    else -> color = "#FFBE9F"
                }

                // change color count variable
                if (colorCount == 3){
                    colorCount = 0
                } else{
                    colorCount ++
                }

               // insert item into DB
                val item = Bucket(0, title, description, color)
                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    bucketsDB.BucketsDAO().insertBucket(item)
                }

                // go back to main activity
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        // on back press go back to main activity
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}