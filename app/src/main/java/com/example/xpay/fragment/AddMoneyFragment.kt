package com.example.xpay.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.xpay.LoginPage
import com.example.xpay.R
import com.example.xpay.TransactionCompleted
import com.example.xpay.databinding.FragmentAddMoneyBinding
import com.example.xpay.databinding.FragmentSendMoneyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AddMoneyFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddMoneyBinding
    var currentBalance: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMoneyBinding.inflate(inflater, container, false)


        val amount = binding.amount
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        database.reference.child("User Balance").child(auth.currentUser?.phoneNumber.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentBalance = snapshot.getValue() as Long
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.addMoney.setOnClickListener{
            if (amount.text.toString().trim().isNotEmpty()){
                database.reference.child("User Balance").child(auth.currentUser?.phoneNumber.toString())
                    .setValue(currentBalance + amount.text.toString().toDouble())

                Toast.makeText(this@AddMoneyFragment.requireContext(),"Money added Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddMoneyFragment.requireContext(), TransactionCompleted::class.java))
                //requireActivity().finish()
            }
            else{
                Toast.makeText(
                    this@AddMoneyFragment.requireContext(), "Please Enter Amount", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {

    }
}