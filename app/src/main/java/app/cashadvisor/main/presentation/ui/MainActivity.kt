package app.cashadvisor.main.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import app.cashadvisor.R
import app.cashadvisor.authorization.data.dto.CredentialsDto
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.databinding.ActivityMainBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var storage: CredentialsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.tag("MainActivity").d("Debug log")
        Timber.tag("MainActivity").i("Info log")
        Timber.tag("MainActivity").w("Warning log")
        Timber.tag("MainActivity").e("Error log")

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

        val logAndCrashButton = Button(this).apply {
            text = "Log and Crash"
            setOnClickListener {
                Timber.tag("MainActivity").d("Debug log")
                Timber.tag("MainActivity").i("Info log")
                Timber.tag("MainActivity").w("Warning log")
                Timber.tag("MainActivity").e("Error log")
                throw RuntimeException("This is a test crash.")
                FirebaseCrashlytics.getInstance().log("This is a test crash sended from button.")
            }
        }
        binding.root.addView(logAndCrashButton)


        this.lifecycleScope.launch {
            viewModel.state.collect {
                Timber.tag("MainActivity").d("AccountInformation: $it")
            }
        }

        val storageButton = Button(this).apply {
            text = "Storage"
            setOnClickListener {
                viewModel.saveCredentials( "test", "test")
            }
        }
        var layoutParams =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        with(binding.root as ConstraintLayout) {
            addView(storageButton, layoutParams)
        }

        val deleteStorageButton = Button(this).apply {
            text = "Logout"
            setOnClickListener {
                viewModel.logout()
            }
        }
        layoutParams =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        with(binding.root as ConstraintLayout) {
            addView(deleteStorageButton, layoutParams)
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
