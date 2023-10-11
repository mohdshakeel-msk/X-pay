package com.example.xpay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.xpay.fragment.HomeFragment
import com.example.xpay.fragment.ProfileFragment
import com.example.xpay.fragment.TransactionsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)*/

        val homeView = HomeFragment()
        val transactionView = TransactionsFragment()
        val profileView = ProfileFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeView)

        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homeFragment -> replaceFragment(homeView)
                R.id.transactionsFragment -> replaceFragment(transactionView)
                R.id.profileFragment -> replaceFragment(profileView)
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val change = supportFragmentManager.beginTransaction()
            change.replace(R.id.fragmentContainerView, fragment)
            change.commit()
        }
    }
}

