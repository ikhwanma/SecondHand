package binar.lima.satu.secondhand.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.local.room.ProductEntity
import binar.lima.satu.secondhand.data.utils.AppExecutors
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.ActivityMainBinding
import binar.lima.satu.secondhand.model.product.Category
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.view.fragment.DetailFragment
import binar.lima.satu.secondhand.view.fragment.ProductFragment
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiViewModel: ApiViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                if (destination.id == R.id.homeFragment || destination.id == R.id.profileFragment || destination.id == R.id.notificationFragment || destination.id == R.id.daftarJualSayaFragment) {
                    navView.visibility = View.VISIBLE
                } else {
                    navView.visibility = View.GONE
                }
            }
        }

        getBadge()
    }

    fun getBadge() {
        userViewModel.getToken().observe(this) { token ->

            apiViewModel.getNotification(token).observe(this) {
                when (it.status) {
                    SUCCESS -> {
                        val data = it.data!!
                        Log.d("ini Data", data.toString())

                        var num = 0

                        for (notif in data) {
                            if (!notif.read) num++
                        }
                        if(num == 0){
                            binding.navView.removeBadge(R.id.notificationFragment)
                        }else{
                            binding.navView.getOrCreateBadge(R.id.notificationFragment).number = num
                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }
    }
}