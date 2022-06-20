package binar.lima.satu.secondhand.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            binding.apply {
                if (destination.id == R.id.homeFragment || destination.id == R.id.profileFragment) {
                    navView.visibility = View.VISIBLE
                }else{
                    navView.visibility = View.GONE
                }
            }
        }

    }
}