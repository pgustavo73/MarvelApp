package com.pgustavo.marvelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.pgustavo.marvelapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragmentNav()
    }

    private fun initFragmentNav() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentMainView) as NavHostFragment
        val _navController = navHostFragment.navController

        binding.bottomNavigation.apply {
            setupWithNavController(_navController)
            setOnNavigationItemReselectedListener {  }
        }
    }
}