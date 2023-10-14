package com.example.wastemangement.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.wastemangement.Activities.LoginScreen
import com.example.wastemangement.R
import com.example.wastemangement.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var iv_profile:ImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbrefuser: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutBtn = view.findViewById<LinearLayout>(R.id.logout)

        firebaseAuth=FirebaseAuth.getInstance()

        logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(activity, "You've signed out successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginScreen::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        val dltAcctBtn = view.findViewById<LinearLayout>(R.id.deleteAcc)
        dltAcctBtn.setOnClickListener {
            firebaseAuth.currentUser?.delete()?.addOnSuccessListener {
                Toast.makeText(activity, "Your account has been deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, LoginScreen::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }?.addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        }


        iv_profile = view.findViewById(R.id.idProfilePic)
        dbrefuser =  FirebaseDatabase.getInstance().getReference("Users")

        dbrefuser.child("${firebaseAuth.uid}").get()
            .addOnSuccessListener {
                    snapshot->
                val imageURL = snapshot.child("image").value.toString()
                Glide.with(this@ProfileFragment)
                    .load(imageURL)
                    .placeholder(R.drawable.profilevector)
                    .centerCrop()
                    .circleCrop()
                    .into(iv_profile)

                Toast.makeText(activity,imageURL,Toast.LENGTH_SHORT).show()
            }




    }
}