package com.dsc.grocerymanagement.fragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.activities.DashboardActivity
import com.dsc.grocerymanagement.model.grocerymodel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CategoriesPage : Fragment() {
    private lateinit var recview: RecyclerView
    lateinit var searchView: SearchView
    lateinit var progressLayout: RelativeLayout
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_items_page, container, false)
        recview = view.findViewById(R.id.firestore_list)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        // searchView=(SearchView) findViewById(R.id.app_bar_search);
        firebaseFirestore = FirebaseFirestore.getInstance()
        val collection: String = DashboardActivity().getCollect()
        println("check $collection")
        //Query
        val query: Query = FirebaseFirestore.getInstance()
                .collection(collection)
        //RecyclerOption
        //RecyclerOption
        val options = FirestoreRecyclerOptions.Builder<grocerymodel>()
                .setQuery(query, grocerymodel::class.java)
                .build()
        adapter = object : FirestoreRecyclerAdapter<grocerymodel, GroceryViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
                val view2 = LayoutInflater.from(parent.context).inflate(R.layout.activity_listitem_single, parent, false)
                return GroceryViewHolder(view2)
            }

            override fun onBindViewHolder(holder: GroceryViewHolder, position: Int, model: grocerymodel) {
                holder.name.text = model.name
                holder.price.text = model.price
                holder.save.text = model.save
                holder.price0.text = model.price0
                holder.price0.paintFlags = holder.price0.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(holder.img.context).load(model.img).into(holder.img)
                progressLayout.visibility=View.GONE
            }
        }
        recview.setHasFixedSize(true)
        recview.layoutManager = LinearLayoutManager(activity as Context)
        recview.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    private class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.nametext)
        val save: TextView = itemView.findViewById(R.id.savetext)
        val price: TextView = itemView.findViewById(R.id.pricetext)
        val price0: TextView = itemView.findViewById(R.id.actualprice)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}