package com.example.travelbucket

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.travelbucket.databinding.BucketViewBinding

lateinit var mListener: BucketAdapter.onItemClickListener

class BucketAdapter(var mContext: Context, val myBuckets:MutableList<Bucket>): RecyclerView.Adapter<BucketAdapter.ViewHolder>() {
    val bucketsDB: BucketsDB by lazy { BucketsDB.getInstance(mContext) } //binding of DB

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemListener(listener: onItemClickListener) {
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BucketViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, mListener)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bucket = myBuckets.get(position)
        holder.bind(bucket, position)
    }
    override fun getItemCount(): Int {
        return myBuckets.size
    }
    fun getSum(bucketId : Int) : Int{
        var bucketEvents = bucketsDB.BucketsDAO().getEventsOfBucket(bucketId) as MutableList<Event>
        var sumCosts = 0
        for (event in bucketEvents){
            var costs = event.costs
            sumCosts = sumCosts + costs
        }
        return sumCosts
    }

    inner class ViewHolder(val binding:BucketViewBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        //inner class ViewHolder(val binding:BucketViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(bucket: Bucket, position: Int) {
            binding.textBucket.text = bucket.title

            var bucketId = bucket.bucketId
            if(bucketsDB.BucketsDAO().getEventsOfBucket(bucketId) as MutableList<Event> != emptyList<Event>()){
                var price = getSum(bucketId)
                binding.textBucketPrice.text = "Total cost: " + price + " ₩"
            }else{
                binding.textBucketPrice.text = "Total cost: 0 ₩"
            }

            binding.textBucketDescription.text = bucket.description

            binding.cardView.setCardBackgroundColor(Color.parseColor(bucket.color))

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
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}