package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.myapplication.view.main.MainActivityFragment
import com.example.myapplication.R
import com.example.myapplication.view.experiments.MainBroadcastReceiver
import com.example.myapplication.view.experiments.ThreadsFragment
import com.example.myapplication.view.history.HistoryFragment

private val broadcastReceiver = MainBroadcastReceiver()

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view_tag, MainActivityFragment.newInstance())
                .commit()

        registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_threads -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.fragment_container_view_tag, ThreadsFragment.newInstance())
                        .addToBackStack("")
                        .commit()
                }
                true
            }
            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction().replace(
                        R.id.fragment_container_view_tag,
                        HistoryFragment.newInstance()
                    ).addToBackStack("").commit()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}