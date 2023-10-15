package com.example.wastemangement.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemangement.DataClass.collectionRequest
import com.example.wastemangement.R

class collectionAdapter (private val List: ArrayList<collectionRequest>) :
    RecyclerView.Adapter<collectionAdapter.ItemViewHolder>(){

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val city : TextView = itemView.findViewById(R.id.city)
        val bioWL : TextView = itemView.findViewById(R.id.biowasteList)
        val nonBioWL : TextView = itemView.findViewById(R.id.nonbiowasteList)
        val recWL : TextView = itemView.findViewById(R.id.recwasteList)
        val eWL : TextView = itemView.findViewById(R.id.ewasteList)
        val lat : TextView = itemView.findViewById(R.id.latitude)
        val long : TextView = itemView.findViewById(R.id.longitude)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.collection_view, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.city.text = List[position].city

        val bioWaste = List[position].biowastelist.toString()
        val nonBioWaste = List[position].nonbiowastelist.toString()
        val recWaste = List[position].recwastelist.toString()
        val eWaste = List[position].ewastelist.toString()


        holder.bioWL.text = bioWaste
        holder.nonBioWL.text = nonBioWaste
        holder.recWL.text = recWaste
        holder.eWL.text = eWaste
        holder.lat.text = List[position].lat.toString()
        holder.long.text = List[position].long.toString()

    }

    override fun getItemCount(): Int {
        return List.size
    }

}