package com.example.travelbucket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import com.example.travelbucket.databinding.ActivityAddBucketBinding
import com.example.travelbucket.databinding.ActivityEditBucketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditBucketActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBucketBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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