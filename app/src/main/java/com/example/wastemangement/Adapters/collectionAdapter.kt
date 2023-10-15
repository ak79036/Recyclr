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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.collection_view, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.city.text = List[position].city

        var bioWaste = ""
        for((index,i) in List[position].biowastelist.withIndex()){
            if(index==0){
                bioWaste += List[position].biowastelist
            }else{
                bioWaste += ", " + List[position].biowastelist
            }
        }

        var nonBioWaste = ""
        for((index,i) in List[position].nonbiowastelist.withIndex()){
            if(index==0){
                nonBioWaste += List[position].nonbiowastelist
            }else{
                nonBioWaste += ", " + List[position].nonbiowastelist
            }
        }

        var recWaste = ""
        for((index,i) in List[position].recwastelist.withIndex()){
            if(index==0){
                recWaste += List[position].recwastelist
            }else{
                recWaste += ", " + List[position].recwastelist
            }
        }

        var eWaste = ""
        for((index,i) in List[position].ewastelist.withIndex()){
            if(index==0){
                eWaste += List[position].ewastelist
            }else{
                eWaste += ", " + List[position].ewastelist
            }
        }

        holder.bioWL.text = bioWaste
        holder.nonBioWL.text = nonBioWaste
        holder.recWL.text = recWaste
        holder.eWL.text = eWaste

    }

    override fun getItemCount(): Int {
        return List.size
    }

}