package com.dsc.grocerymanagement.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dsc.grocerymanagement.R;
import com.dsc.grocerymanagement.model.offermodel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OfferActivity extends AppCompatActivity {

    RecyclerView recview;
    TextView c1, c2, c3, offerdesc, terms;
    ImageView ofimg;
    androidx.appcompat.widget.Toolbar toolbar;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    offermodel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        recview = (RecyclerView) findViewById(R.id.firestore_list);
        toolbar = findViewById(R.id.of_Toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Offer Details");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseFirestore = FirebaseFirestore.getInstance();
        offerdesc = findViewById(R.id.offerdesc);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        terms = findViewById(R.id.terms);
        ofimg = findViewById(R.id.of_img);
        Intent intent = getIntent();
        String desc = intent.getStringExtra("offerdesc");
        String cn1 = intent.getStringExtra("c1");
        String cn2 = intent.getStringExtra("c2");
        String cn3 = intent.getStringExtra("c3");
        String image = intent.getExtras().getString("offerimage");

        offerdesc.setText(desc);
        terms.setPaintFlags(terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        c1.setText(cn1);
        c2.setText(cn2);
        c3.setText(cn3);
        Glide.with(getApplicationContext()).load(image).into(ofimg);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}