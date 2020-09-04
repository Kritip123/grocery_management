package com.dsc.grocerymanagement.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dsc.grocerymanagement.R;
import com.dsc.grocerymanagement.model.grocerymodel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomePageRecyclerViewActivity extends AppCompatActivity {

    RecyclerView recview;
    SearchView searchView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_recycleview);

        recview = findViewById(R.id.firestore_list);
       // searchView=(SearchView) findViewById(R.id.app_bar_search);


        firebaseFirestore=FirebaseFirestore.getInstance();

        //Query
        Query query = FirebaseFirestore.getInstance()
                .collection("grocery");
        //RecyclerOption
        FirestoreRecyclerOptions<grocerymodel> options = new FirestoreRecyclerOptions.Builder<grocerymodel>()
                .setQuery(query, grocerymodel.class)
                .build();


        adapter= new FirestoreRecyclerAdapter<grocerymodel,GroceryViewHolder>(options) {
            @NonNull
            @Override
            public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listitem_single,parent,false);
                return new GroceryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull GroceryViewHolder holder, int position, @NonNull grocerymodel model) {

                holder.name.setText(model.getName());
                holder.price.setText(model.getPrice());
                holder.save.setText(model.getSave());
                holder.price0.setText(model.getPrice0());
                holder.price0.setPaintFlags(holder.price0.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Glide.with(holder.img.getContext()).load(model.getImg()).into(holder.img);
                //String url=model.getImage();
                //Picasso.get().load(model.getImage()).into(holder.img);
            }
        };

        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapter);
    }

    //ViewHolder
    private static class GroceryViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name,save,price,price0;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);

            img= itemView.findViewById(R.id.imageView);
            name= itemView.findViewById(R.id.nametext);
            save= itemView.findViewById(R.id.savetext);
            price= itemView.findViewById(R.id.pricetext);
            price0= itemView.findViewById(R.id.actualprice);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
