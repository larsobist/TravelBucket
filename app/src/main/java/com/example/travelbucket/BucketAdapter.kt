package com.example.travelbucket

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.travelbucket.databinding.BucketViewBinding
import kotlinx.coroutines.CoroutineScope


class BucketAdapter(var mContext: Context, val myBuckets:MutableList<Bucket>, private val showDeleteMenu: (Boolean) -> Unit): RecyclerView.Adapter<BucketAdapter.ViewHolder>() {
    private var smthSelected = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BucketViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bucket = myBuckets.get(position)
        holder.bind(bucket, position)
    }
    override fun getItemCount(): Int {
        return myBuckets.size
    }
    inner class ViewHolder(val binding:BucketViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(bucket: Bucket,  position: Int) {
            binding.textBucket.text = bucket.title
            //binding.imgBucket.setImageResource(bucket.image)

            binding.cardView.setOnLongClickListener {
                if (!smthSelected){
                    binding.cardView.strokeWidth = 8
                    bucket.selected = true
                    smthSelected = true
                    showDeleteMenu(true)
                }
                true
            }

            binding.cardView.setOnClickListener {
                if (bucket.selected){
                    binding.cardView.strokeWidth = 0
                    bucket.selected = false
                    smthSelected = false
                    showDeleteMenu(false)
                }else if (!smthSelected){
                    val intent = Intent(mContext,EventOverviewActivity::class.java)
                    mContext.startActivity(intent)
                }
            }

            // modify card view constraints
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayout)
            // make card view top constraint correct
            if (position == 0) {
                constraintSet.connect(binding.cardView.id, ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP, 24.toDp(mContext))
            }else {
                constraintSet.connect(binding.cardView.id, ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP, 12.toDp(mContext))
            }
            // make card view bottom constraint correct
            if (position == myBuckets.size-1) {
                constraintSet.connect(binding.cardView.id, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM, 24.toDp(mContext))
            }else {
                constraintSet.connect(binding.cardView.id, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM, 12.toDp(mContext))
            }
            // apply constraints to layout
            constraintSet.applyTo(binding.constraintLayout)
        }
/*        init {
            binding.root.setOnClickListener {
                val intent = Intent(mContext,EventOverviewActivity::class.java)
                mContext.startActivity(intent)



            }
        }*/

    }
}