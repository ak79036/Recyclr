package com.example.wastemangement.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.wastemangement.Activities.AboutActivity
import com.example.wastemangement.Activities.LoginScreen
import com.example.wastemangement.R
import com.example.wastemangement.databinding.FragmentProfileBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var iv_profile: ImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbrefuser: DatabaseReference
    private val pfViewModel : ProfileFragmentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        firebaseAuth=FirebaseAuth.getInstance()
    }

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
        val addrBtn = view.findViewById<LinearLayout>(R.id.addr)
        val addrLayout = view.findViewById<TextInputLayout>(R.id.addrInputLayout)
        val pfAddr = view.findViewById<EditText>(R.id.addrEntryField)
        addrBtn.setOnClickListener {
            if(addrLayout.visibility == View.VISIBLE){
                addrLayout.visibility = View.GONE
            }else{
                addrLayout.visibility = View.VISIBLE
                pfAddr.setText(pfViewModel.address.value)
            }
        }

        val aboutBtn = view.findViewById<LinearLayout>(R.id.about)
        aboutBtn.setOnClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
        }

        dbrefuser =  FirebaseDatabase.getInstance().getReference("Users")

        val pfName = view.findViewById<TextView>(R.id.idProfileName)
        val pfPic = view.findViewById<ImageView>(R.id.idProfilePic)

        val name : String? = pfViewModel.name.value
        pfName.text = name

        val address : String? = pfViewModel.address.value
        pfAddr.setText(address)

        val image : String? = pfViewModel.image.value
        Glide
            .with(view)
            .load(image)
            .placeholder(R.drawable.baseline_account_circle_24)
            .centerCrop()
            .circleCrop()
            .into(pfPic);
    }
}