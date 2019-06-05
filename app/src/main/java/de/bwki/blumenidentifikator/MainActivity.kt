package de.bwki.blumenidentifikator
// TODO: Ändere Paketname zum Teamnamen
// TODO: Aufräumen ._.

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import de.bwki.blumenidentifikator.databinding.ActivityMainBinding
import java.io.File

//lateinit var binding : ActivityMainBinding

// Drawer
private lateinit var drawerLayout: DrawerLayout
private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var prefs: SharedPreferences
private lateinit var imageClassification: ImageClassification

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

    interface Images {
        var image : File

        fun setFileVar(file: File){
            this.image = file
        }

        fun getFileVar():File{
            return image
        }
    }
}
