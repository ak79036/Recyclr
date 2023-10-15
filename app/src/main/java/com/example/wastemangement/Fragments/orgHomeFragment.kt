package com.example.wastemangement.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemangement.Adapters.collectionAdapter
import com.example.wastemangement.DataClass.collectionRequest
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class orgHomeFragment : Fragment() {

    private lateinit var collectionRequestList : ArrayList<collectionRequest>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_org_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectionRequestList = arrayListOf()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCollection)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        getItemInfo(recyclerView)

    }

    private fun getItemInfo(recyclerView: RecyclerView) {

        val uid = FirebaseAuth.getInstance().uid

        val database: DatabaseReference = Firebase.database(
            "https://wastemangement-ced8c-default-rtdb.firebaseio.com/"
        ).reference

//        if(view!=null){
//            val orgName : TextView = requireView().findViewById(R.id.orgNameTV)
//            var name ="Organization name"
//            database.child("Organization").child("biode").child(uid!!)
//                .child("name").get().addOnSuccessListener {
//                if(it.toString().isNotEmpty()){
//                    name = it.value.toString()
//                }
//            }
//            database.child("Organization").child("ewaste").child(uid!!)
//                .child("name").get().addOnSuccessListener {
//                if(it.toString().isNotEmpty()){
//                    name = it.value.toString()
//                }
//            }
//            database.child("Organization").child("nonbiode").child(uid!!)
//                .child("name").get().addOnSuccessListener {
//                if(it.toString().isNotEmpty()){
//                    name = it.value.toString()
//                }
//            }
//            database.child("Organization").child("recyclabe").child(uid!!)
//                .child("name").get().addOnSuccessListener {
//                if(it.toString().isNotEmpty()){
//                    name = it.value.toString()
//                }
//            }
//            orgName.text = name
//
//        }

            database.ref.child("CollectionRequests").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val requestInfo = itemSnapshot.getValue(collectionRequest::class.java)
                        if (requestInfo != null) {
                            collectionRequestList.add(requestInfo)
                        }
                    }

                    val adapter = collectionAdapter(collectionRequestList)
                    recyclerView.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


}