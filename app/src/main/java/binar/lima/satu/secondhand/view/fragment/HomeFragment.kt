package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.local.room.ProductEntity
import binar.lima.satu.secondhand.data.utils.AppExecutors
import binar.lima.satu.secondhand.data.utils.OnlineChecker
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.model.product.Category
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.view.activity.MainActivity
import binar.lima.satu.secondhand.view.adapter.CategoryAdapter
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.view.adapter.ProductDbAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as MainActivity).getBadge()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connected = OnlineChecker.isOnline(requireContext())

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetCategory)

        if (!connected) {
            Toast.makeText(requireContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_LONG)
                .show()
            binding.apply {
                rvProduct.visibility = View.VISIBLE
                progressCircular.visibility = View.GONE
            }
            apiViewModel.getProductDb().observe(viewLifecycleOwner) {
                val adapter = ProductDbAdapter()
                adapter.submitData(it)
                binding.apply {
                    rvProduct.adapter = adapter
                    rvProduct.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
            }
        } else {

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
                        val category = it.data!!
                        val listImg = mutableListOf<Int>()
                        listImg.add(R.drawable.ic_category_18)
                        listImg.add(R.drawable.ic_category_1)
                        listImg.add(R.drawable.ic_category_2)
                        listImg.add(R.drawable.ic_category_3)
                        listImg.add(R.drawable.ic_category_4)
                        listImg.add(R.drawable.ic_category_5)
                        listImg.add(R.drawable.ic_category_6)
                        listImg.add(R.drawable.ic_category_lainnya)

                        val listFullImg = mutableListOf<Int>()
                        listFullImg.add(R.drawable.ic_category_0)
                        listFullImg.add(R.drawable.ic_category_1)
                        listFullImg.add(R.drawable.ic_category_2)
                        listFullImg.add(R.drawable.ic_category_3)
                        listFullImg.add(R.drawable.ic_category_4)
                        listFullImg.add(R.drawable.ic_category_5)
                        listFullImg.add(R.drawable.ic_category_6)
                        listFullImg.add(R.drawable.ic_category_7)
                        listFullImg.add(R.drawable.ic_category_8)
                        listFullImg.add(R.drawable.ic_category_9)
                        listFullImg.add(R.drawable.ic_category_10)
                        listFullImg.add(R.drawable.ic_category_10)
                        listFullImg.add(R.drawable.ic_category_12)
                        listFullImg.add(R.drawable.ic_category_12)
                        listFullImg.add(R.drawable.ic_category_14)
                        listFullImg.add(R.drawable.ic_category_15)
                        listFullImg.add(R.drawable.ic_category_16)
                        listFullImg.add(R.drawable.ic_category_17)
                        listFullImg.add(R.drawable.ic_category_18)
                        listFullImg.add(R.drawable.ic_category_19)
                        listFullImg.add(R.drawable.ic_category_20)
                        listFullImg.add(R.drawable.ic_category_21)
                        listFullImg.add(R.drawable.ic_category_22)
                        listFullImg.add(R.drawable.ic_category_23)

                        val adapterCategory =
                            CategoryAdapter(requireContext(), listFullImg) { it1 ->
                                val mBundle = bundleOf(ProductFragment.EXTRA_ID_CATEGORY to it1.id)
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_homeFragment_to_productFragment, mBundle)
                            }.apply {
                                submitData(category)
                            }


                        val adapter = CategoryAdapter(requireContext(), listImg) { data ->
                            val categoryId = data.id
                            if (categoryId == -1) {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                            } else {
                                val mBundle =
                                    bundleOf(ProductFragment.EXTRA_ID_CATEGORY to categoryId)
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_homeFragment_to_productFragment, mBundle)
                            }
                        }
                        val listCat = mutableListOf<GetSellerCategoryResponseItem>()


                        listCat.add(category[18])

                        for (i in 1..6) {
                            listCat.add(category[i])
                        }

                        listCat.add(GetSellerCategoryResponseItem("", -1, "Lainnya", ""))

                        adapter.submitData(listCat)

                        binding.apply {
                            rvKategory.layoutManager = GridLayoutManager(requireContext(), 4)
                            rvKategoryFull.layoutManager = GridLayoutManager(requireContext(), 4)
                            rvKategory.adapter = adapter
                            rvKategoryFull.adapter = adapterCategory
                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        binding.apply {
                            bg.visibility = View.GONE
                        }
                    } else binding.bg.setOnClickListener {
                        binding.bg.visibility = View.GONE
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.bg.visibility = View.VISIBLE
                }

            })

            userViewModel.getToken().observe(viewLifecycleOwner) { token ->

                apiViewModel.getBuyerWishlist(token).observe(viewLifecycleOwner) {
                    when (it.status) {
                        SUCCESS -> {
                            val data = it.data!!

                            binding.apply {
                                if (data.isEmpty()) {
                                    cvBadge.visibility = View.VISIBLE
                                    tvWishlist.text = 0.toString()
                                } else {
                                    cvBadge.visibility = View.VISIBLE
                                    tvWishlist.text = data.size.toString()
                                }
                            }
                        }
                        ERROR -> {

                        }
                        LOADING -> {

                        }
                    }
                }

            }
            getData()

            binding.etSearch.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_searchFragment)
            }

            binding.btnWishlist.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_wishlistFragment)
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
                    for (data in dataProduct) {
                        if (j == 15) break
                        else list.add(data)
                        j++
                    }

                    val adapter = ProductAdapter { data ->
                        if(data.id == -2){
                            val mBundle =
                                bundleOf(ProductFragment.EXTRA_ID_CATEGORY to 0)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_homeFragment_to_productFragment, mBundle)
                        }else{
                            val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
                        }
                    }

                    list.add(
                        GetProductResponseItem(
                            0,
                            listOf(Category(1, "tes")),
                            "s",
                            -2,
                            "s",
                            "s",
                            "s",
                            "s",
                            "s",
                            -2
                        )
                    )
                    adapter.submitData(list)

                    Log.d("ini data", list.toString())

                    val listProduct = mutableListOf<ProductEntity>()
                    val appExecutors = AppExecutors()

                    appExecutors.diskIO.execute {
                        for (dp in list) {
                            if (dp.id == -2) break

                            var category = ""
                            var k = 1
                            for (cat in dp.categories) {
                                category += if (k != dp.categories.size) {
                                    "${cat.name}, "
                                } else {
                                    cat.name
                                }
                                k++
                            }
                            val productEntity = ProductEntity(
                                null,
                                dp.imageUrl,
                                dp.name,
                                category,
                                dp.basePrice.toString(),
                                dp.location
                            )
                            listProduct.add(productEntity)
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            apiViewModel.deleteAllProduct()
                            apiViewModel.addProduct(listProduct)
                        }
                    }

                    binding.apply {
                        val layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rvProduct.layoutManager = layoutManager
                        rvProduct.adapter = adapter
                        rvProduct.setItemViewCacheSize(list.size)
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