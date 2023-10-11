package com.example.xpay.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import com.example.xpay.ConfirmSendMoneyDetails
import com.example.xpay.databinding.FragmentSendMoneyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SendMoneyFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSendMoneyBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentSendMoneyBinding.inflate(inflater, container, false)
        val enteredNumber = binding.receiverNumber
        //val receiverNumber = "+91" + binding.receiverNumber
        val amount = binding.amount



        binding.sendMoney.setOnClickListener {
            if (enteredNumber.text.toString().trim().isNotEmpty()) {
                if ((enteredNumber.text.toString().trim()).length == 10) {
                    if (amount.text.toString().trim().isNotEmpty()) {

                        val currentUser = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
                        if ("+91"+enteredNumber.text.toString() == currentUser){
                            Toast.makeText(
                                this@SendMoneyFragment.requireContext(),
                                "You can't sent money on your number",
                                Toast.LENGTH_SHORT 
                            ).show()
                        }else{
                            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                            val receiverNumber = enteredNumber.text.toString()
                            val reference: DatabaseReference = database.reference.child("Users").child("+91$receiverNumber")

                            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(this@SendMoneyFragment.requireContext(), "User Exist", Toast.LENGTH_SHORT).show()

                                        requireActivity().run {

                                            val intent = Intent(this@SendMoneyFragment.requireContext(), ConfirmSendMoneyDetails::class.java)
                                            //intent.putExtra("+91receiverNumber", receiverNumber)
                                            intent.putExtra("enteredNumber", enteredNumber.text.toString())
                                            intent.putExtra("amount", amount.text.toString())
                                            this@SendMoneyFragment.startActivity(intent)
                                        }
                                        //showReceiverDetails(receiverNumber)

                                    } else {
                                        Toast.makeText(
                                            this@SendMoneyFragment.requireContext(), "User not Exist",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    println("Error: ${error.message}")
                                }
                            })
                        }

                        /*requireActivity().run{

                            val intent = Intent(this@SendMoneyFragment.requireContext(), ConfirmSendMoneyDetails::class.java)
                            intent.putExtra("+91receiverNumber", receiverNumber)
                            intent.putExtra("enteredNumber", enteredNumber.text.toString())
                            intent.putExtra("amount", amount.text.toString())
                            this@SendMoneyFragment.startActivity(intent)
                        }*/
                    } else {
                        makeText(
                            this@SendMoneyFragment.requireContext(),
                            "Please enter Amount",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    makeText(
                        this@SendMoneyFragment.requireContext(),
                        "Please Enter 10 digit Correct number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                makeText(
                    this@SendMoneyFragment.requireContext(),
                    "Please enter Number",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /*Firebase.database.reference.child("Users")
            .child(receiverNumber.toString())
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (snapshot1 in snapshot.children){
                            val user = snapshot.getValue(UserModleClass::class.java)
                            if (user!!.uid == FirebaseAuth.getInstance().uid){



                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )*/


        return binding.root
    }


    companion object {

    }
}