package binar.lima.satu.secondhand.view.viewpageritem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentDetailBinding
import binar.lima.satu.secondhand.databinding.FragmentDiminatiTabBinding
import binar.lima.satu.secondhand.databinding.FragmentProductTabBinding
import binar.lima.satu.secondhand.model.seller.order.GetSellerOrderResponseItem
import binar.lima.satu.secondhand.view.adapter.SellerOrderAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class DiminatiTabFragment : Fragment() {

    private var _binding: FragmentDiminatiTabBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiminatiTabBinding.inflate(inflater, container, false)
        getDataDiminati()
        return binding.root
    }

    private fun getDataDiminati() {
        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            apiViewModel.getSellerOrder(token).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val data = it.data

                        val adapter = SellerOrderAdapter {

                        }
                        val list = mutableListOf<GetSellerOrderResponseItem>()

                        for (order in data!!) {
                            if (order.status == "pending" && order.product.status == "available") {
                                list.add(order)
                            }
                        }

                        adapter.submitData(list)
                        binding.apply {
                            rvDiminati.adapter = adapter
                            rvDiminati.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }

}