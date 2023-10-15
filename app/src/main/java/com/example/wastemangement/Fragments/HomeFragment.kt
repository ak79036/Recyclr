package com.example.wastemangement.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.wastemangement.Activities.MainActivity
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var title:TextView
    private lateinit var bioCount:TextView
    private lateinit var nonBioCount:TextView
    private lateinit var recCount:TextView
    private lateinit var ECount:TextView
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title=view.findViewById(R.id.titleText)

        bioCount=view.findViewById(R.id.BioCount)
        nonBioCount=view.findViewById(R.id.NonBioCount)
        recCount=view.findViewById(R.id.RecCount)
        ECount=view.findViewById(R.id.E_Count)

        animateText("Recyclr")
        val firebaseAuth= FirebaseAuth.getInstance()
        val dbrefuser =  FirebaseDatabase.getInstance().getReference("Users")
        val scanBtn = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView2)
        val count= view.findViewById<TextView>(R.id.CountDisposal)
        dbrefuser.child(firebaseAuth.uid.toString()).get().addOnSuccessListener {snap ->
            val user: users = snap.getValue(users::class.java)!!
            count.text=user.count.toString()
            bioCount.text=user.wasteCounts.bioCount.toString()
            nonBioCount.text=user.wasteCounts.nonBioCount.toString()
            recCount.text=user.wasteCounts.recCount.toString()
            ECount.text=user.wasteCounts.eCount.toString()
        }

        scanBtn.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("key", 69)
            startActivity(intent)
        }
    }
    private fun animateText(text: String) {
        if (i <= text.length) {
            val fetchtext: String = text.substring(0, i);
            title.text = fetchtext
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    i++;
                    animateText(text)
                }, 500
            )
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}