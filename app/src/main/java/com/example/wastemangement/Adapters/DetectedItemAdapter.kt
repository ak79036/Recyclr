package com.example.wastemangement.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.R

class DetectedItemAdapter(val context: Context, var list: ArrayList<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view= LayoutInflater.from(context).inflate(com.example.wastemangement.R.layout.recitem,parent,false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = list[position]
        val viewholder:ItemViewHolder = holder as ItemViewHolder
        val k="${position+1}.$label"
        viewholder.title.text=k
    }
    fun updateList(newList:ArrayList<String>)
    {
        list=newList
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var title: TextView =itemView.findViewById(com.example.wastemangement.R.id.text)

    }
}