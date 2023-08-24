package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.budgetapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

            navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
                if(destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment) {
                    bottomNavigationView.visibility = View.GONE
                } else {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

}