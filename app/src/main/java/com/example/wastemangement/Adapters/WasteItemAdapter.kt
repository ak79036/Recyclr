package com.example.wastemangement.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.R

class WasteItemAdapter(val context: Context, var list: ArrayList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(context).inflate(com.example.wastemangement.R.layout.wasteitem,parent,false)
        return WasteItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        val viewholder: WasteItemViewHolder = holder as WasteItemViewHolder

        viewholder.label.text=item.uppercase()
        viewholder.delButton.setOnClickListener {
            list.removeAll(listOf(item).toSet())
            notifyItemRemoved(position)
        }
    }
    fun sendList(): ArrayList<String> {
        return list
    }

    class WasteItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var label: TextView =itemView.findViewById(com.example.wastemangement.R.id.itemText)
        var delButton: ImageView = itemView.findViewById(com.example.wastemangement.R.id.deleteButton)

    }
}