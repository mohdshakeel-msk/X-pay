package com.example.xpay.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.xpay.R
import com.example.xpay.databinding.FragmentHomeBinding
import com.example.xpay.databinding.FragmentProfileBinding
import com.example.xpay.modle.UserModleClass
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    var currentBalance: Long = 0L

    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        binding.balance.text = currentBalance.toString()
        database.reference.child("User Balance").child(auth.currentUser?.phoneNumber.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentBalance = snapshot.getValue() as Long
                        binding.balance.text = currentBalance.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        Firebase.database.reference.child("Users")
            .child(auth.currentUser?.phoneNumber.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snapshot1 in snapshot.children){
                        val user = snapshot.getValue(UserModleClass::class.java)
                        if (user!!.uid == FirebaseAuth.getInstance().uid){

                            binding.userName.text = user.name
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            )

        binding.pay.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = SendMoneyFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"send Money")
            bottomSheetDialog.enterTransition
        }

        binding.balance.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = AddMoneyFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "add Money")
            bottomSheetDialog.enterTransition
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }

}