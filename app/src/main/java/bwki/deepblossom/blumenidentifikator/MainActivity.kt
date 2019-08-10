package bwki.deepblossom.blumenidentifikator

// TODO: Aufräumen ._.

import android.content.SharedPreferences
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import bwki.deepblossom.blumenidentifikator.databinding.ActivityMainBinding
import com.parse.Parse

// Drawer
private lateinit var drawerLayout: DrawerLayout
private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var prefs: SharedPreferences
private lateinit var asset: AssetManager
lateinit var res: Resources

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        val navController = this.findNavController(R.id.coreNavHostFragment)
        prefs = getSharedPreferences("de.bwki.blumenidentifikator_preferences", MODE_PRIVATE)
        asset = assets
        res = resources

        drawerLayout = binding.drawerLayout
        NavigationUI.setupActionBarWithNavController(
            this, navController,
            drawerLayout
        )

        // Drawer Config
        appBarConfiguration = AppBarConfiguration(
            navController.graph,
            drawerLayout
        )
        NavigationUI.setupWithNavController(binding.navView, navController)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("k6uZC17WtPcnUvcZV6g5D6hYIEXDrj6oGg0eIEDd")
                .clientKey("hdQ2JZyMpUvYj8dfXJAxXomkU9C2ZtCQPYU18Tvc")
                .server("https://parseapi.back4app.com")
                .build()
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.coreNavHostFragment)
        return NavigationUI.navigateUp(
            navController,
            drawerLayout
        )
    }

    // Methoden für Fragments
    interface GlobalMethods {
        fun getPrefs(): SharedPreferences {
            return prefs
        }

        fun lockDrawer() {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

        fun getAsset(): AssetManager {
            return asset
        }
    }
}
