package com.example.travelbucket

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.travelbucket.databinding.EventViewBinding

class EventAdapter(val mContext: Context, val myEvents:MutableList<Event>): RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = myEvents.get(position)
        holder.bind(event, position)
    }

    override fun getItemCount(): Int {
        return myEvents.size
    }

    inner class ViewHolder(val binding: EventViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, position: Int) {
            binding.textTitle.text = event.title
            binding.textDuration.text = "Duration: " + event.duration
            binding.textNotes.text = "Notes: " + event.notes

            // make line start at right position of first item
            if (position == 0) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.constraintLayout)
                constraintSet.connect(binding.line.id, ConstraintSet.TOP, binding.circle.id, ConstraintSet.TOP)
                constraintSet.applyTo(binding.constraintLayout)
            }

            // make line end at right position and add margin of last item
            else if (position == myEvents.size-1) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.constraintLayout)
                constraintSet.connect(binding.line.id, ConstraintSet.BOTTOM, binding.circle.id, ConstraintSet.BOTTOM)
                constraintSet.connect(binding.textNotes.id, ConstraintSet.BOTTOM, PARENT_ID, ConstraintSet.BOTTOM, 32.toDp(mContext))
                constraintSet.applyTo(binding.constraintLayout)
            }

        }
        init {
            binding.root.setOnClickListener {
                val intent = Intent(mContext,EventDetailsActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }
}

fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
).toInt()