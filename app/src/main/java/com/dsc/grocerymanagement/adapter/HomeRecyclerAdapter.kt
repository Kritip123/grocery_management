package com.dsc.grocerymanagement.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.model.groceryModel

class HomeRecyclerAdapter(private val itemList: ArrayList<groceryModel>) :
        RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.nametext)
        val save: TextView = itemView.findViewById(R.id.savetext)
        val price: TextView = itemView.findViewById(R.id.pricetext)
        val price0: TextView = itemView.findViewById(R.id.actualprice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view2 = LayoutInflater.from(parent.context).inflate(R.layout.activity_listitem_single, parent, false)

        return HomeViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val grocery = itemList[position]
        println("grocey ${grocery.name}")
        holder.name.text = grocery.name
        holder.price.text = grocery.price
        holder.save.text = grocery.save
        holder.price0.text = grocery.price0
        holder.price0.paintFlags = holder.price0.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        Glide.with(holder.img.context).load(grocery.img).into(holder.img)
    }
}