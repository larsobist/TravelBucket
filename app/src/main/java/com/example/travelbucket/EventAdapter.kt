package com.example.travelbucket

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.travelbucket.databinding.EventViewBinding

lateinit var bListener : EventAdapter.onItemClickListener

class EventAdapter(val mContext: Context, var myEvents:MutableList<Event>): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemListener(listener: onItemClickListener) {
        bListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, bListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = myEvents.get(position)
        holder.bind(event, position)
    }

    override fun getItemCount(): Int {
        return myEvents.size
    }

    fun update(updatedList:MutableList<Event>){
        myEvents = updatedList
        this!!.notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:EventViewBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, position: Int) {
            binding.textTitle.text = event.title
            binding.textDuration.text = "Duration: " + event.duration + "h"
            binding.textCosts.text = "Cost: " + event.costs

            // modify line constraints
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayout)
            // make line start at right position of first item
            if (position == 0) {
                constraintSet.connect(binding.line.id, ConstraintSet.TOP, binding.circle.id, ConstraintSet.TOP)
            }else {
                constraintSet.connect(binding.line.id, ConstraintSet.TOP, PARENT_ID, ConstraintSet.TOP)
            }
            // make line end at right position and add margin of last item
            if (position == myEvents.size-1) {
                constraintSet.connect(binding.line.id, ConstraintSet.BOTTOM, binding.circle.id, ConstraintSet.BOTTOM)
                constraintSet.connect(binding.textCosts.id, ConstraintSet.BOTTOM, PARENT_ID, ConstraintSet.BOTTOM, 32.toDp(mContext))
            }else {
                constraintSet.connect(binding.line.id, ConstraintSet.BOTTOM, PARENT_ID, ConstraintSet.BOTTOM)
                constraintSet.connect(binding.textCosts.id, ConstraintSet.BOTTOM, PARENT_ID, ConstraintSet.BOTTOM, 0.toDp(mContext))
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

fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
).toInt()