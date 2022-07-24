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
import binar.lima.satu.secondhand.databinding.FragmentOrderBinding
import binar.lima.satu.secondhand.model.buyer.wishlist.GetWishlistResponseItem
import binar.lima.satu.secondhand.model.seller.order.GetSellerOrderResponseItem
import binar.lima.satu.secondhand.view.adapter.OrderAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeContainer.setOnRefreshListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_orderFragment_self)
        }

        userViewModel.getToken().observe(viewLifecycleOwner){ token ->
            binding.apply {
                apiViewModel.getBuyerOrder(token).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            progressCircular.visibility = View.GONE
                            val data = it.data!!

                            val listData = mutableListOf<GetSellerOrderResponseItem>()

                            for (ld in data){
                                if (ld.status != "accepted"){
                                    listData.add(ld)
                                }
                            }
                            getData(listData, token)

                        }
                        ERROR -> {
                            progressCircular.visibility = View.GONE
                        }
                        LOADING -> {
                            progressCircular.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    fun getData(data: List<GetSellerOrderResponseItem>, token: String) {
        binding.apply {
            if (data.isEmpty()){
                llListKosong.visibility = View.VISIBLE
            }else{
                llListKosong.visibility = View.GONE
            }

            val adapter = OrderAdapter(
                apiViewModel, token, viewLifecycleOwner,
                data as MutableList<GetSellerOrderResponseItem>
            ){ it1 ->
                val mBundle = bundleOf(DetailFragment.EXTRA_ID to it1.productId)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_orderFragment_to_detailFragment, mBundle)
            }
            adapter.submitData(data)

            rvOrder.adapter = adapter
            rvOrder.layoutManager = LinearLayoutManager(requireContext())
        }

    }

}