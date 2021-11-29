package com.example.travelbucket

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelbucket.databinding.EventViewBinding

class EventAdapter(val mContext: Context, val myEvents:MutableList<Event>): RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = myEvents.get(position)
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return myEvents.size
    }

    inner class ViewHolder(val binding: EventViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.textTitle.text = event.title
            binding.textDuration.text = "Duration: " + event.duration
            binding.textNotes.text = "Notes: " + event.notes
        }
        init {
            binding.root.setOnClickListener {
                val intent = Intent(mContext,EventDetailsActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }
}