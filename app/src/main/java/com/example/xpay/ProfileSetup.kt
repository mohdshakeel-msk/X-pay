package com.example.xpay

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.xpay.databinding.ActivityProfileSetupBinding
import com.example.xpay.modle.User
import com.example.xpay.modle.UserModleClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.Date

class ProfileSetup : AppCompatActivity() {
    private val binding by lazy {
        ActivityProfileSetupBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        Firebase.database.reference.child("Users")
            .child(auth.currentUser?.phoneNumber.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        startActivity(Intent(this@ProfileSetup, Home::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error: ${error.message}")
                }
            })

        binding.userImg.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.saveProfile.setOnClickListener {
            if (binding.userName.text.isEmpty()){
                Toast.makeText(this,"Please enter your Name",Toast.LENGTH_SHORT).show()
            }
            else if (selectedImg == null){
                Toast.makeText(this,"Please select your Profile Image",Toast.LENGTH_SHORT).show()
            }
            else uploadData()
        }

    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener {
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        //creating a receivers data node in Firebase that use at the time of send money
        /*database.reference.child("receivers")
            .child("receiver")
            .setValue(auth.currentUser?.phoneNumber)*/

        //creating a Users node that containing the user Profile Data
        val user = UserModleClass(auth.uid.toString(), binding.userName.text.toString(),  auth.currentUser?.phoneNumber.toString(), imgUrl )
        database.reference.child("Users")
            .child(auth.currentUser?.phoneNumber.toString())
            .setValue(user)
            .addOnCompleteListener {
                startActivity(Intent(this, Home::class.java))
                finish()
            }
    }

    // Old code here we create a new nod in Users nod as (Firebase.auth.currentUser!!.uid)

    /*private fun uploadInfo(imgUrl: String) {
        val user = UserModleClass(auth.uid.toString(), binding.userName.text.toString(),  auth.currentUser?.phoneNumber.toString(), imgUrl )
        database.reference.child("Users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnCompleteListener {
                startActivity(Intent(this, Home::class.java))
                finish()
            }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.data != null){
            selectedImg = data.data!!

            binding.userImg.setImageURI(selectedImg)
        }
    }
}