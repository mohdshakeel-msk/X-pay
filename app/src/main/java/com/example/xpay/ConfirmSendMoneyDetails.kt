package com.example.xpay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.xpay.modle.HistoryModleClass
import com.example.xpay.modle.UserModleClass
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConfirmSendMoneyDetails : AppCompatActivity() {
    /*val receiverImg: ImageView = findViewById<ImageView>(R.id.receiverImg)
    val receiverName: TextView = findViewById<TextView>(R.id.receiverName)
    val receiverNumberTextView: TextView = findViewById<TextView>(R.id.receiverNumber)
    val amountTextView: TextView = findViewById<TextView>(R.id.enterAmount)

    val enteredNumber : String = intent.getStringExtra("enteredNumber").toString()
    val receiverNumber : String = intent.getStringExtra("+91receiverNumber").toString()
    val amount : String = intent.getStringExtra("amount").toString()*/
    var currentBalance: Long = 0L
    var receiverCurrentBalance: Long = 0L
    var senderName: String = ""
    var receiverName: String = ""
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_send_money_details)

        val receiverImg = findViewById<ImageView>(R.id.receiverImg)
        val showReceiverName = findViewById<TextView>(R.id.receiverName)
        val receiverNumberTextView = findViewById<TextView>(R.id.receiverNumber)
        val amountTextView = findViewById<TextView>(R.id.enterAmount)
        val confirmAndPay = findViewById<Button>(R.id.confirm)

        val enteredNumber : String = intent.getStringExtra("enteredNumber").toString()
        val receiverNumber : String = intent.getStringExtra("+91receiverNumber").toString()
        val amount : String = intent.getStringExtra("amount").toString()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference.child("Users").child("+91$enteredNumber")


        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snapshot1 in snapshot.children){
                    val user = snapshot.getValue(UserModleClass::class.java)
                    Glide.with(receiverImg).load(user?.profileImage).into(receiverImg)
                    showReceiverName.text = user?.name
                    receiverNumberTextView.text = enteredNumber
                    amountTextView.text = amount
                    receiverName = user?.name.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

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

        database.reference.child("User Balance").child("+91$enteredNumber")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        receiverCurrentBalance = snapshot.getValue() as Long
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

                            senderName = user.name.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            )

        confirmAndPay.setOnClickListener{
            //val database: FirebaseDatabase = FirebaseDatabase.getInstance()

            if (currentBalance >= amount.toDouble()){
                //val transactoinId = database.reference.push().toString()

                database.reference.child("User Balance").child(auth.currentUser?.phoneNumber.toString())
                    .setValue(currentBalance - amount.toDouble())
                    .addOnSuccessListener {
                        val sentTransaction = HistoryModleClass(false, receiverName,
                            System.currentTimeMillis().toString(),amount)

                        database.reference.child("Transaction History")
                            .child(auth.currentUser?.phoneNumber.toString())
                            .push()
                            //.child(transactoinId)
                            .setValue(sentTransaction)
                    }

                database.reference.child("User Balance").child("+91$enteredNumber")
                    .setValue(receiverCurrentBalance + amount.toDouble())
                    .addOnSuccessListener {
                        val receivedTransaction = HistoryModleClass(true, senderName,
                            System.currentTimeMillis().toString(),amount)

                        database.reference.child("Transaction History")
                            .child("+91$enteredNumber")
                            .push()
                            //.child(transactoinId)
                            .setValue(receivedTransaction)
                    }

                Toast.makeText(this@ConfirmSendMoneyDetails,"Payment Successful",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ConfirmSendMoneyDetails, TransactionCompleted::class.java))
                finish()

            }
            else{
                Toast.makeText(
                    this@ConfirmSendMoneyDetails, "You have Insufficient wallet balance",
                    Toast.LENGTH_SHORT).show()
            }
        }



        /*val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference.child("Users").child("+91"+enteredNumber)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(this@ConfirmSendMoneyDetails,"User Exist",
                        Toast.LENGTH_SHORT).show()

                    //showReceiverDetails(receiverNumber)

                } else {
                    Toast.makeText(this@ConfirmSendMoneyDetails,"User not Exist",
                        Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
            }
        })*/

        /*Firebase.database.reference.child("Users")
            //.child(receiverNumber)
            .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        //if (snapshot.child(receiverNumber).exists()){
                            for (snapshot1 in snapshot.children){
                                val user = snapshot.getValue(UserModleClass::class.java)

                                Glide.with(receiverImg).load(user?.profileImage).into(receiverImg)
                                receiverName.text = user?.name
                                receiverNumberTextView.text = enteredNumber
                                amountTextView.text = amount
                            }
                        /*}
                        else{
                            Toast.makeText(this@ConfirmSendMoneyDetails,"Please enter Amount",
                                Toast.LENGTH_SHORT).show()
                        }*/

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )*/
    }

    /*private fun showReceiverDetails(receiverNumber: String) {
        Firebase.database.reference.child("Users")
            .child(receiverNumber)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snapshot1 in snapshot.children){
                        val user = snapshot.getValue(UserModleClass::class.java)
                        if (user!!.phoneNumber == receiverNumber){

                            Glide.with(receiverImg).load(user?.profileImage).into(receiverImg)
                            receiverName.text = user?.name
                            receiverNumberTextView.text = enteredNumber
                            amountTextView.text = amount
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            )

    }*/

}