package com.dsc.grocerymanagement.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.fragments.HomePage
import com.dsc.grocerymanagement.fragments.ItemsPage
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class DashboardActivity : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var auth: FirebaseAuth

    companion object {
        var collection: String = "as"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        drawerLayout = findViewById(R.id.DrawerLayout)
        navigationView = findViewById(R.id.NavigationView)
        navigationView.menu.getItem(0).isChecked = true
        toolbar = findViewById(R.id.Toolbar)
        auth = FirebaseAuth.getInstance()
        setUpToolbar()
        setMenuIcons(navigationView.menu)
        val header = navigationView.getHeaderView(0)
        val numberField: TextView = header.findViewById(R.id.txtMobile)
        numberField.text = auth.currentUser!!.phoneNumber.toString()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
                this@DashboardActivity,
                drawerLayout,
                R.string.Open_Drawer,
                R.string.Close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        openFragment(HomePage(), "All items", "grocery")
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.hOme -> {
                    openFragment(HomePage(), "All items", "grocery")
                }
                R.id.spices -> {

                }
                R.id.pulses -> {
                    openFragment(ItemsPage(), "Pulses", "Pulse")
                }
                /*R.id.rice -> {
                    openFragment(ItemsPage(), "Rice", "Rice")
                }*/
                R.id.dairy -> {

                }
                R.id.hCare -> {

                }
                R.id.bCare -> {

                }
                R.id.beverages -> {

                }
                R.id.pCare -> {

                }
                R.id.snacks -> {

                }
                R.id.noodles -> {

                }
                R.id.oil -> {

                }
                R.id.logout -> {
                    logoutUser()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFragment(fragment: Fragment, title: String, collect: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.FrameLayout, fragment).commit()
        supportActionBar?.title = title
        collection = collect
        drawerLayout.closeDrawers()
    }

    private fun setMenuIcons(menu: Menu) {

        for (i in 1..10) {
            val imageView = ImageView(this@DashboardActivity)
            val item = menu.getItem(i)
            when (i) {
                1 -> {
                    imageView.setImageResource(R.mipmap.spice)
                }
                2 -> {
                    imageView.setImageResource(R.mipmap.pulses)
                }
                /*3 -> {
                    imageView.setImageResource(R.mipmap.rice)
                }*/
                3 -> {
                    imageView.setImageResource(R.mipmap.dairy)
                }
                4 -> {
                    imageView.setImageResource(R.mipmap.home)
                }
                5 -> {
                    imageView.setImageResource(R.mipmap.baby)
                }
                6 -> {
                    imageView.setImageResource(R.mipmap.bevarages)
                }
                7 -> {
                    imageView.setImageResource(R.mipmap.personal)
                }
                8 -> {
                    imageView.setImageResource(R.mipmap.snacks)
                }
                9 -> {
                    imageView.setImageResource(R.mipmap.noodles)
                }
                10 -> {
                    imageView.setImageResource(R.mipmap.oil)
                }
            }
            item.actionView = imageView
        }
    }

    private fun logoutUser() {
        val confirmLogout = AlertDialog.Builder(this@DashboardActivity)
        confirmLogout.setTitle("Sure to Logout??")
                .setMessage("You have to login again next time!!")
                .setPositiveButton("Confirm") { _, _ ->
                    auth.signOut()
                    startActivity(Intent(this@DashboardActivity, PhoneLoginActivity::class.java))
                    Toast.makeText(this@DashboardActivity, "You have been successfully Logged Out",
                            Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    navigationView.menu.getItem(0).isChecked = true
                }
                .create()
                .show()
    }

    fun getCollect(): String {
        return collection
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.FrameLayout)) {
            !is HomePage -> {
                openFragment(
                        HomePage(), "Home", "grocery"
                )
                navigationView.menu.getItem(0).isChecked = true
            }
            else -> super.onBackPressed()
        }
    }

}