package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        var editText = findViewById<TextInputEditText>(R.id.textInputEditText)

        binding.textInputEditText.doOnTextChanged { text, start, before, count ->
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
            if (binding.textInputEditText.text!!.isEmpty()) {
                binding.textInputLayout.error = "Title required!"
            } else {
                var title = (editText.text).toString()
                //val bild = editText.text

                Log.d("ITM", "Title: $title")
               // Log.d("ITM", "Bild: $bild")


                val item = Bucket(0, title, 1) //warum ein int?
                GlobalScope.launch(Dispatchers.IO){ //insert it to the DB
                    bucketsDB.BucketsDAO().insert(item)
                    //bucketsDB.BucketsDAO().nukeTable()
                }

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            binding.imgBucket.setImageURI(data?.data)
        }
    }

}