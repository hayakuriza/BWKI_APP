package de.bwki.blumenidentifikator
// TODO: Ändere Paketname zum Teamnamen

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import de.bwki.blumenidentifikator.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil.setContentView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

//lateinit var binding : ActivityMainBinding

// Drawer
private lateinit var drawerLayout: DrawerLayout
private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var prefs: SharedPreferences

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.coreNavHostFragment)
        prefs = getPreferences(MODE_PRIVATE)


        Log.i("MainActivity", "passed")
        drawerLayout = binding.drawerLayout
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        // Drawer Config
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.coreNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    // Methoden für Fragments
    interface GlobalMethods {
        fun getPrefs(): SharedPreferences{
            return prefs
        }

        fun lockDrawer() {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }
}
