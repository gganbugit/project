package com.example.resume2.logout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resume2.R

class RecyclerAdapter2(val saveInterList: ArrayList<TTSMember>, val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter2.ViewHolder>() {

    lateinit var listener2: OnInterviewItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.myinterviewui, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(saveInterList[position], context)
    }

    override fun getItemCount(): Int {
        return saveInterList.count()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        init {
            itemView?.setOnClickListener {
                listener2?.onInterviewClick(this, itemView, adapterPosition)
            }
        }

        val myInterviewQuestion = itemView?.findViewById<TextView>(R.id.myInterviewQuestion)
        val myInterviewAnswer = itemView?.findViewById<TextView>(R.id.myInterviewAnswer)
        val myInterviewId = itemView?.findViewById<TextView>(R.id.myInterviewId)

        fun bind(itemInterview: TTSMember, context: Context) {
            myInterviewQuestion?.text = itemInterview?.question
            myInterviewAnswer?.text = itemInterview?.answer
            myInterviewId?.text = itemInterview?.interview_id.toString()
        }

    }
}