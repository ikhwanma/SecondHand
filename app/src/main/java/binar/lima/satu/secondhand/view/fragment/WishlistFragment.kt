package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentWishlistBinding
import binar.lima.satu.secondhand.model.buyer.wishlist.GetWishlistResponseItem
import binar.lima.satu.secondhand.view.adapter.WishlistAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner){ token ->
            apiViewModel.getBuyerWishlist(token).observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        val data = it.data

                        getData(data, token)
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }


    }

    fun getData(data: List<GetWishlistResponseItem>?, token: String) {
        val adapter = WishlistAdapter(apiViewModel, token, viewLifecycleOwner,
            data as MutableList<GetWishlistResponseItem>?
        ){
            val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.productId)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_wishlistFragment_to_detailFragment, mBundle)
        }.apply {
            submitData(data)
        }

        binding.apply {
            rvWishlist.adapter = adapter
            rvWishlist.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}