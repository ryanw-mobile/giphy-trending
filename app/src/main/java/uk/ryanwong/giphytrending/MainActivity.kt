package uk.ryanwong.giphytrending

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.ryanwong.giphytrending.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}