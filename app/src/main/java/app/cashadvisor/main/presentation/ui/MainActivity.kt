package app.cashadvisor.main.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import app.cashadvisor.R
import app.cashadvisor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{  _, destination, _ ->

            when(destination.id){
                R.id.analyticsFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.horizontalDivider.visibility = View.VISIBLE
                }
                R.id.trackerFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.horizontalDivider.visibility = View.VISIBLE
                }
                R.id.featuresAndSettingsFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.horizontalDivider.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.horizontalDivider.visibility = View.GONE
                }
            }
        }


        // Creates a button that mimics a crash when pressed
        /*val crashButton = TextView(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))*/



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}