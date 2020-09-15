package com.example.shoppingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var HeaderLayout : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        HeaderLayout=  nav_view.getHeaderView(0)
        nav_view.setNavigationItemSelectedListener(this)

        HeaderLayout.header_backButton.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        bottom_navigation.setOnNavigationItemSelectedListener()

        val toggle = ActionBarDrawerToggle(this, drawer_layout,
        toolbar, R.string.open_drawer, R.string.close_drawer)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
            R.id.nav_account ->{
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
            R.id.nav_history ->{
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
            R.id.nav_notifications ->{
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
            R.id.nav_wishlist -> {
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

private fun BottomNavigationView.setOnNavigationItemSelectedListener() {

}


