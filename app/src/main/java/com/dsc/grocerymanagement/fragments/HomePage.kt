package com.dsc.grocerymanagement.fragments

import android.app.SearchManager
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.adapter.HomeRecyclerAdapter
import com.dsc.grocerymanagement.model.groceryModel
import com.dsc.grocerymanagement.util.IOnBackPressed
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class HomePage : Fragment(), IOnBackPressed {
    private lateinit var recview: RecyclerView
    private lateinit var searchView: SearchView
    lateinit var progressLayout: RelativeLayout
    private lateinit var recyclerAdapter: HomeRecyclerAdapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var size = 0
    private val groceryArrayList = arrayListOf<groceryModel>()
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_items_page, container, false)
        setHasOptionsMenu(true)
        progressLayout = view.findViewById(R.id.progressLayout)
        recview = view.findViewById(R.id.firestore_list)
        progressLayout.visibility = View.GONE
        // searchView=(SearchView) findViewById(R.id.app_bar_search);
        firebaseFirestore = FirebaseFirestore.getInstance()
        //Query
        firebaseFirestore.collection("grocery").get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {

            } else {
                val types: List<groceryModel> = querySnapshot.toObjects(groceryModel::class.java)
                // Add all to your list

                groceryArrayList.addAll(types)
                size = groceryArrayList.size
                //println("sizing $size")
                //println("check1435 ${groceryArrayList[0].name}")
            }

        }.addOnFailureListener {
            Toast.makeText(activity as Context, "Error getting data!!!", Toast.LENGTH_SHORT).show()
        }
//        println("check ${mArrayList[0].name}")
        val query: Query = FirebaseFirestore.getInstance()
                .collection("grocery")
        /*Handler().postDelayed({
            println("size $size")
            recyclerAdapter=HomeRecyclerAdapter(groceryArrayList)
            recview.layoutManager = LinearLayoutManager(activity as Context)
            recview.adapter = recyclerAdapter
            recyclerAdapter.notifyDataSetChanged()
        }, 3000)*/
        recview.setHasFixedSize(true)
        getList(query)

        // Inflate the layout for this fragment
        return view
    }

    private fun getList(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<groceryModel>()
                .setQuery(query, groceryModel::class.java)
                .build()
        adapter = object : FirestoreRecyclerAdapter<groceryModel, GroceryViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
                val view2 = LayoutInflater.from(parent.context).inflate(R.layout.activity_listitem_single, parent, false)
                return GroceryViewHolder(view2)
            }

            override fun onBindViewHolder(holder: GroceryViewHolder, position: Int, model: groceryModel) {
                holder.name.text = model.name
                holder.price.text = model.price
                holder.save.text = model.save
                holder.price0.text = model.price0
                holder.price0.paintFlags = holder.price0.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(holder.img.context).load(model.img).into(holder.img)
                progressLayout.visibility = View.GONE
            }
        }
        adapter.notifyDataSetChanged()
        recview.setHasFixedSize(true)
        recview.layoutManager = LinearLayoutManager(activity as Context)
        recview.adapter = adapter
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.app_bar_search)
        searchView = item.actionView as SearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                if (s.trim { it <= ' ' }.isNotEmpty()) {
                    processSearch(s)
                }
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (s.trim { it <= ' ' }.isNotEmpty()) {
                    adapter.stopListening()
                    processSearch(s)
                }
                return false
            }
        })
        searchView.setOnCloseListener {

            val query: Query = FirebaseFirestore.getInstance()
                    .collection("grocery")
            getList(query)
            adapter.startListening()
            false
        }
    }

    private fun processSearch(s: String) {
        var s1 = s
        if (s.isNotEmpty())
            //s1 = s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT)
            s1=s.toLowerCase(Locale.ROOT)
        //println("lastly ${groceryArrayList.size}")
        val grocery = arrayListOf<groceryModel>()
        grocery.clear()
        for (i in 0 until groceryArrayList.size) {
            if (groceryArrayList[i].name.toLowerCase(Locale.ROOT).contains(s1)) {
                val groceryObject = groceryModel(
                        groceryArrayList[i].name,
                        groceryArrayList[i].save,
                        groceryArrayList[i].price,
                        groceryArrayList[i].img,
                        groceryArrayList[i].price0)
                grocery.add(groceryObject)
            }
                recyclerAdapter = HomeRecyclerAdapter(grocery)
                recview.layoutManager = LinearLayoutManager(activity as Context)
                recview.adapter = recyclerAdapter
                recyclerAdapter.notifyDataSetChanged()
        }
        /*val query = FirebaseFirestore.getInstance()
                .collection("grocery").orderBy("name").startAt(s1).endAt(s1 + "\uf8ff")
        getList(query)
        adapter.startListening()
        recview.adapter = adapter*/
    }

    override fun onBackPressed(): Boolean {
        return if (!searchView.isIconified) {
            searchView.isIconified = true
            searchView.onActionViewCollapsed()
            val query: Query = FirebaseFirestore.getInstance()
                    .collection("grocery")
            getList(query)
            adapter.startListening()
            true
        } else {
            false
        }
    }

}