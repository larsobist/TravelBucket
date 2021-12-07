package com.example.travelbucket

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.travelbucket.databinding.BucketViewBinding
import kotlinx.coroutines.CoroutineScope


class BucketAdapter(var mContext: Context, val myBuckets:MutableList<Bucket>): RecyclerView.Adapter<BucketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BucketViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bucket = myBuckets.get(position)
        holder.bind(bucket)
    }
    override fun getItemCount(): Int {
        return myBuckets.size
    }
    inner class ViewHolder(val binding:BucketViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(bucket: Bucket) {
            binding.textBucket.text = bucket.title
            //binding.imgBucket.setImageResource(bucket.image)
        }
        init {
            binding.root.setOnClickListener {
                val intent = Intent(mContext,EventOverviewActivity::class.java)
                mContext.startActivity(intent)



            }
        }

    }
}