package com.example.resume2.logout

import android.view.View
import com.example.resume2.logout.RecyclerAdapter2

interface OnInterviewItemClickListener {
    fun onInterviewClick(holder: RecyclerAdapter2.ViewHolder, view: View?, position: Int)
}

