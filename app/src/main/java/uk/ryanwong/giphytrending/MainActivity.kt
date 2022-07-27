package uk.ryanwong.giphytrending

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import uk.ryanwong.giphytrending.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the splash screen transition.
        installSplashScreen()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.navView.let {
            NavigationUI.setupWithNavController(
                it,
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
            )
        }
    }
}