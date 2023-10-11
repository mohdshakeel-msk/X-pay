package com.example.xpay.adaptor

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xpay.databinding.HistoryItemsBinding
import com.example.xpay.modle.HistoryModleClass
import java.sql.Date
import java.sql.Timestamp

class HistoryAdaptor(var historyList:ArrayList<HistoryModleClass>) : RecyclerView.Adapter<HistoryAdaptor.HistoryViewHolder>() {
    class HistoryViewHolder(var binding: HistoryItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(HistoryItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        var timestamp = Timestamp(historyList.get(position).dateAndTime.toLong())
        holder.binding.dateTime.text = Date(timestamp.time).toString()
        holder.binding.sendToOrReceivedFrom.text = if (historyList.get(position).isReceived){"Received from "} else{"Sent to "}
        holder.binding.status.text = if (historyList.get(position).isReceived){"+ ₹ "} else{"- ₹ "}
        holder.binding.amount.text = historyList[position].amount
        holder.binding.userName.text = historyList[position].userName
    }
}