package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.OnlineChecker
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentDaftarJualSayaBinding
import binar.lima.satu.secondhand.view.adapter.DaftarJualPageAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DaftarJualSayaFragment : Fragment() {
    private var _binding: FragmentDaftarJualSayaBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarJualSayaBinding.inflate(inflater, container, false)
        val connected = OnlineChecker.isOnline(requireContext())
        if (!connected){
            Toast.makeText(requireContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DaftarJualPageAdapter(requireParentFragment())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
            tab.setIcon(IMAGE_LIST[position])
        }.attach()

        val tabs = binding.tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount ) {
            val tab = tabs.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = 16
            layoutParams.marginStart = 16
            tab.layoutParams = layoutParams
            binding.tabLayout.requestLayout()
        }
        userViewModel.getToken().observe(viewLifecycleOwner) {
            if (it == "") {
                Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_daftarJualSayaFragment_to_loginFragment)
            }else{
                apiViewModel.getLoginUser(it).observe(viewLifecycleOwner){ user ->
                    when(user.status){
                        SUCCESS -> {
                            val data = user.data!!

                            binding.apply {
                                if (data.city != "temp"){
                                    tvSellerCity.text = data.city
                                }
                                tvSellerName.text = data.fullName
                                Glide.with(requireView()).load(data.imageUrl).into(binding.imgSeller)
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
        binding.btnEdit.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_daftarJualSayaFragment_to_editProfileFragment)
        }
    }


    companion object{
        private val TAB_TITLES = mutableListOf<String>(
            "Produk",
            "Diminati",
            "Terjual"
        )

        private val IMAGE_LIST = mutableListOf<Int>(
            R.drawable.ic_box,
            R.drawable.ic_baseline_favorite_border_24,
            R.drawable.ic_dollar
        )
    }
}