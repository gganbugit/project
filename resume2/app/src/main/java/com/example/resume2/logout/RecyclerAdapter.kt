package com.example.resume2.logout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resume2.R

class RecyclerAdapter(val postList: ArrayList<cover>, val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    lateinit var listener: OnCoverItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.mycoverui, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position], context)
    }

    override fun getItemCount(): Int {
        return postList.count()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        init {
            itemView?.setOnClickListener {
                listener?.onCoverClick(this, itemView, adapterPosition)
            }
        }

        val mysubject = itemView?.findViewById<TextView>(R.id.myCoverSubject)
        val mycontent = itemView?.findViewById<TextView>(R.id.myCoverCon)
        val mycoverid = itemView?.findViewById<TextView>(R.id.myCoverId)

        fun bind(itemCover: cover, context: Context) {
            mysubject?.text = itemCover?.subject
            mycontent?.text = itemCover?.content
            mycoverid?.text = itemCover?.cover_id.toString()

        }
    }
}