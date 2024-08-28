@file:Suppress("DEPRECATION")

package com.example.api

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Initialize Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        // Set up your ImageView click listeners
        findViewById<ImageView>(R.id.carpent)?.setOnClickListener {
            startActivity(Intent(this, Carpenter::class.java))
        }

        findViewById<ImageView>(R.id.plumb)?.setOnClickListener {
            startActivity(Intent(this, Plumber::class.java))
        }

        findViewById<ImageView>(R.id.tail)?.setOnClickListener {
            startActivity(Intent(this, Tailor::class.java))
        }

        findViewById<ImageView>(R.id.shoemake)?.setOnClickListener {
            startActivity(Intent(this, Shoe::class.java))
        }

        findViewById<ImageView>(R.id.lock)?.setOnClickListener {
            startActivity(Intent(this, Locksmith::class.java))
        }

        findViewById<ImageView>(R.id.painter)?.setOnClickListener {
            startActivity(Intent(this, Painter::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                startActivity(Intent(this, Account::class.java))
            }
            R.id.nav_dashboard -> {
                startActivity(Intent(this, Dashboard::class.java))
            }
            R.id.nav_logout -> {
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
