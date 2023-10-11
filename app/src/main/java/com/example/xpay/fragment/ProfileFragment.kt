package com.example.xpay.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.ContentInfoCompat.Flags
import com.bumptech.glide.Glide
import com.example.xpay.Home
import com.example.xpay.LoginPage
import com.example.xpay.R
import com.example.xpay.databinding.ActivityProfileSetupBinding
import com.example.xpay.databinding.FragmentProfileBinding
import com.example.xpay.modle.User
import com.example.xpay.modle.UserModleClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.io.File


class ProfileFragment : Fragment() {
    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Firebase.database.reference.child("Users")
            .child(auth.currentUser?.phoneNumber.toString())
            .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (snapshot1 in snapshot.children){
                            val user = snapshot.getValue(UserModleClass::class.java)
                            if (user!!.uid == FirebaseAuth.getInstance().uid){

                                binding.userName.text = user.name
                                binding.username.text = user.name
                                binding.userNumber.text = user.phoneNumber
                                Glide.with(context!!).load(user.profileImage).into(binding.userImg)
                                //binding.userImg.setImageURI(user.profileImage!!?.toUri())

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )


        // Old code here we called in Users nod as (Firebase.auth.currentUser!!.uid)

        /*Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (snapshot1 in snapshot.children){
                            val user = snapshot.getValue(UserModleClass::class.java)
                            if (user!!.uid == FirebaseAuth.getInstance().uid){

                                binding.userName.text = user.name
                                binding.username.text = user.name
                                binding.userNumber.text = user.phoneNumber
                                Glide.with(context!!).load(user.profileImage).into(binding.userImg)
                                //binding.userImg.setImageURI(user.profileImage!!?.toUri())

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )*/

        binding.logOut.setOnClickListener {
            requireActivity().run{
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this@ProfileFragment.requireContext(),"Successfully Log Out",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ProfileFragment.requireContext(), LoginPage::class.java))
                finish()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}