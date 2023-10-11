package com.example.xpay.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xpay.R
import com.example.xpay.adaptor.HistoryAdaptor
import com.example.xpay.databinding.FragmentTransactionsBinding
import com.example.xpay.modle.HistoryModleClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Collections

class TransactionsFragment : Fragment() {

    var currentBalance: Long = 0L

    val binding by lazy {
        FragmentTransactionsBinding.inflate(layoutInflater)
    }

    lateinit var adaptor:HistoryAdaptor
    private var historyList = ArrayList<HistoryModleClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.database.reference.child("Transaction History")
            .child(Firebase.auth.currentUser?.phoneNumber.toString())
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyList.clear()
                    var historyList1 = ArrayList<HistoryModleClass>()
                    for (datasnapshot in snapshot.children){
                        var data = datasnapshot.getValue(HistoryModleClass::class.java)
                        historyList1.add(data!!)
                    }
                    Collections.reverse(historyList1)
                    historyList.addAll(historyList1)
                    adaptor.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        //historyList.clear()
        //historyList.add(HistoryModleClass("Shakeel","02:15:19", "-₹ 500"))
        //historyList.add(HistoryModleClass("Raja","03:25:23", "-₹ 700"))
        //historyList.add(HistoryModleClass("Shariq","05:19:136", "-₹ 200"))
        //historyList.add(HistoryModleClass("Ateeq","06:05:12", "-₹ 1000"))

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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adaptor = HistoryAdaptor(historyList)
        binding.recyclerView.adapter = adaptor
        binding.recyclerView.setHasFixedSize(true)


        binding.pay.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = SendMoneyFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"send Money")
            bottomSheetDialog.enterTransition
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}