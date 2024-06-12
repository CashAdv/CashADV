package app.cashadvisor.main.presentation.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import app.cashadvisor.R
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.databinding.ActivityMainBinding
import com.google.firebase.appdistribution.FirebaseAppDistribution
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
        setStatusBarColor()
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


        this.lifecycleScope.launch {
            viewModel.state.collect {
                Timber.tag("MainActivity").d("AccountInformation: $it")
            }
        }

    }
    override fun onResume() {
        super.onResume()
        val firebaseAppDistribution = FirebaseAppDistribution.getInstance()
        firebaseAppDistribution.updateIfNewReleaseAvailable()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Метод определяет теккущую тему устройства (тёмная/светлая) и относительно этого устанавливает цвета в статус баре
     */
    private fun setStatusBarColor() {
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (uiMode == Configuration.UI_MODE_NIGHT_NO) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.decorView.systemUiVisibility = 0
            }
        }
    }


}
