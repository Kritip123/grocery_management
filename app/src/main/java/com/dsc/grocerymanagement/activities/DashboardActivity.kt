package com.dsc.grocerymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.dsc.grocerymanagement.R
import com.squareup.picasso.Picasso

class DashboardActivity : AppCompatActivity() {
    lateinit var img: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        img = findViewById(R.id.img)
        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/grocery-management-5eed8.appspot.com/o/Ashirvaad.jpg?alt=media&token=96b34bda-be16-441e-bb73-76efec708d37")
                .error(R.drawable.mainlogo)
                .into(img)
    }
}