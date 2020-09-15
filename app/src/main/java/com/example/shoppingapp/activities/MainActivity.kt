package com.example.shoppingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.UI.Account.AccountFragment
import com.example.shoppingapp.activities.UI.Home.HomeFragment
import com.example.shoppingapp.activities.UI.More.MoreFragment
import com.example.shoppingapp.activities.UI.Notifications.NotificationsFragment
import com.example.shoppingapp.activities.UI.Settings.SettingsFragment
import com.example.shoppingapp.activities.UI.Shop.ShopFragment
import com.example.shoppingapp.activities.UI.Wallet.WalletFragment
import com.example.shoppingapp.activities.UI.Wishlist.WishlistFragment
import com.example.shoppingapp.activities.UI.history.HistoryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var HeaderLayout : View
    var selectedFragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        HeaderLayout=  nav_view.getHeaderView(0)
        nav_view.setNavigationItemSelectedListener(this)

        HeaderLayout.header_backButton.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            HomeFragment()).commit()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottomNav_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        HomeFragment()).commit()
                    true
                }
                R.id.bottomNav_more -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        MoreFragment()).commit()
                    true
                }
                R.id.bottomNav_shop -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        ShopFragment()).commit()
                    true
                }
                R.id.bottomNav_wallet -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        WalletFragment()).commit()
                    true
                }

                else -> false
            }
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout,
        toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                HomeFragment()).commit()
            }
            R.id.nav_account ->{
                bottom_navigation.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    AccountFragment()).commit()

            }
            R.id.nav_history ->{
                bottom_navigation.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    HistoryFragment()).commit()
            }
            R.id.nav_notifications ->{
                bottom_navigation.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    NotificationsFragment()).commit()
            }
            R.id.nav_wishlist -> {
                bottom_navigation.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    WishlistFragment()).commit()
            }
            R.id.nav_setting -> {
                bottom_navigation.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    SettingsFragment()).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}






