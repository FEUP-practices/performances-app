package com.feup.mobilecomputing.firsttask.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.R.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.models.PerformanceType
import com.squareup.picasso.Picasso

class PerformancesListAdapter(performancesList: Array<PerformanceType>?) : RecyclerView.Adapter<PerformancesListAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mListener: OnItemClickListener
    private var pList: Array<PerformanceType> = performancesList ?: arrayOf()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun resetList() {
        pList = arrayOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.performace_card_title)
        val price: TextView = view.findViewById(R.id.performace_card_price)
        val date: TextView = view.findViewById(R.id.performace_card_date)
        val num: TextView = view.findViewById(R.id.performace_card_num)
        val image: ImageView = view.findViewById(R.id.performace_card_image)
        var id: String = ""

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.performance_card, viewGroup, false)
        return ViewHolder(view, mListener)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (id, imageUri, name, price, startDate, endDate, seatsLeft) = pList[position]
        holder.price.text = "${price.toString().replace(".", ",")} €"
        holder.title.text = name
        holder.date.text = startDate.split(" ")[0]
        holder.num.text = "x$seatsLeft"
        holder.id = id
        try {
            Picasso.with(context).load(imageUri).into(holder.image)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        if (seatsLeft <= 0) {
            holder.num.text = "Sold out"
            holder.price.setTextColor(Color.GRAY)
            holder.date.setTextColor(Color.GRAY)
            holder.num.setTextColor(Color.GRAY)
            holder.title.setTextColor(Color.GRAY)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.image.setColorFilter(
                    Color.argb(0.4F,0.0F,0.0F,0.0F),
                    PorterDuff.Mode.SRC_OVER
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return pList.size
    }

    fun addNewItems (newList: Array<PerformanceType>): Array<PerformanceType> {
        val fromRange =  pList.size - 1
        val list = pList.toMutableList()
        newList.forEach { list.add(it) }
        pList = list.toTypedArray()
        val itemsAdded = newList.size
        notifyItemRangeInserted(fromRange, itemsAdded)
        return pList
    }
}