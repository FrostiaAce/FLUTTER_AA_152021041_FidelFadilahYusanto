package com.miftah.moviecatalog.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.miftah.moviecatalog.R
import com.miftah.moviecatalog.databinding.ActivityMainBinding
import com.miftah.moviecatalog.ui.ViewModelFactory
import com.miftah.moviecatalog.ui.oboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()

        val user = viewModel.firebaseAuth().currentUser
        if (user == null) {
            Log.d("TAG", "is not login")
            Intent(this, OnboardingActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.fragment_container_main)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}