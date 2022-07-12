package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.local.room.ProductEntity
import binar.lima.satu.secondhand.data.utils.AppExecutors
import binar.lima.satu.secondhand.data.utils.OnlineChecker
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.view.adapter.CategoryAdapter
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.view.adapter.ProductDbAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var category = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connected = OnlineChecker.isOnline(requireContext())
        if (!connected) {
            Toast.makeText(requireContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_LONG).show()
            binding.apply {
                rvProduct.visibility = View.VISIBLE
                progressCircular.visibility = View.GONE
            }
            apiViewModel.getProductDb().observe(viewLifecycleOwner){
                val adapter = ProductDbAdapter()
                adapter.submitData(it)
                binding.apply {
                    rvProduct.adapter = adapter
                    rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
                }
            }
        }else{
            apiViewModel.getSellerBanner().observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        val data = it.data!!
                        val list = ArrayList<SlideModel>()

                        for (img in data) {
                            list.add(SlideModel(img.imageUrl, ScaleTypes.FIT))
                        }

                        binding.imgSlider.setImageList(list)
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
            apiViewModel.getAllCategory().observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        val listImg = mutableListOf<Int>()
                        listImg.add(R.drawable.ic_category_18)
                        listImg.add(R.drawable.ic_category_1)
                        listImg.add(R.drawable.ic_category_2)
                        listImg.add(R.drawable.ic_category_3)
                        listImg.add(R.drawable.ic_category_4)
                        listImg.add(R.drawable.ic_category_5)
                        listImg.add(R.drawable.ic_category_6)
                        listImg.add(R.drawable.ic_category_7)
                        val adapter = CategoryAdapter(requireContext(), listImg) { data ->
                            category = data.id

                        }
                        val listCat = mutableListOf<GetSellerCategoryResponseItem>()

                        val category = it.data!!
                        listCat.add(category[18])

                        for (i in 1..6) {
                            listCat.add(category[i])
                        }

                        listCat.add(GetSellerCategoryResponseItem("", 0, "Lainnya", ""))

                        adapter.submitData(listCat)


                        binding.apply {
                            rvKategory.layoutManager = GridLayoutManager(requireContext(), 4)
                            rvKategory.adapter = adapter
                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
            getData()
            getProfile()

            binding.etSearch.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
    }

    private fun getProfile() {
        userViewModel.getToken().observe(viewLifecycleOwner){ token ->
            if (token != ""){
                apiViewModel.getLoginUser(token).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            val data = it.data!!

                            Glide.with(requireView()).load(data.imageUrl).into(binding.imgProfile)
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

    private fun getData() {
        apiViewModel.getAllProduct(status = "available").observe(viewLifecycleOwner) { product ->
            when (product.status) {
                SUCCESS -> {
                    val dataProduct = product.data!!
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE

                    val list = mutableListOf<GetProductResponseItem>()
                    var j = 0
                    for(data in dataProduct){
                        if (j == 20) break
                        else list.add(data)
                        j++
                    }

                    val adapter = ProductAdapter { data ->
                        val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
                    }
                    adapter.submitData(list)
                    binding.apply {
                        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        layoutManager.isMeasurementCacheEnabled = false
                        rvProduct.layoutManager = layoutManager
                        rvProduct.adapter = adapter
                        rvProduct.addOnScrollListener(object  : RecyclerView.OnScrollListener(){
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                super.onScrolled(recyclerView, dx, dy)
                                layoutManager.requestLayout()
                            }
                        })
                    }
                    val listProduct = ArrayList<ProductEntity>()
                    val appExecutors = AppExecutors()
                    var i = 0
                    appExecutors.diskIO.execute {
                        for (dp in dataProduct) {
                            if (i == 15) break
                            else {
                                var category = ""
                                var k = 1
                                for (cat in dp.categories){
                                    category += if (j != dp.categories.size){
                                        "${cat.name}, "
                                    }else{
                                        cat.name
                                    }
                                    k++
                                }
                                val productEntity = ProductEntity(
                                    null,
                                    dp.imageUrl,
                                    dp.name,
                                    category,
                                    dp.basePrice.toString()
                                )
                                listProduct.add(productEntity)
                            }
                            i++
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            apiViewModel.deleteAllProduct()
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            apiViewModel.addProduct(listProduct)
                        }
                    }
                }
                ERROR -> {

                }
                LOADING -> {
                    binding.rvProduct.visibility = View.INVISIBLE
                    binding.progressCircular.visibility = View.VISIBLE
                }
            }
        }
    }


}