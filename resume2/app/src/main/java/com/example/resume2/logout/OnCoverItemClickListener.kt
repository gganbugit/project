package com.example.resume2.logout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resume2.R

interface OnCoverItemClickListener {
    fun onCoverClick(holder:RecyclerAdapter.ViewHolder?, view: View?, position: Int)

}

