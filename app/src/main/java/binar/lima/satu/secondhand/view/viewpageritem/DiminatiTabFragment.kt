package binar.lima.satu.secondhand.view.viewpageritem

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
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentDiminatiTabBinding
import binar.lima.satu.secondhand.model.seller.order.GetSellerOrderResponseItem
import binar.lima.satu.secondhand.view.adapter.SellerOrderAdapter
import binar.lima.satu.secondhand.view.fragment.InfoPenawarFragment
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
        binding.swipeContainer.setOnRefreshListener {
            getDataDiminati()
            binding.swipeContainer.visibility = View.INVISIBLE
        }
        getDataDiminati()
        return binding.root
    }

    private fun getDataDiminati() {
        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            apiViewModel.getSellerOrder(token, "").observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val data = it.data
                        binding.progressCircular.visibility = View.GONE

                        val adapter = SellerOrderAdapter { order ->
                            val mBundle = bundleOf(InfoPenawarFragment.EXTRA_ORDER_ID to order.id)
                            Navigation.findNavController(requireView()).navigate(
                                R.id.action_daftarJualSayaFragment_to_infoPenawarFragment,
                                mBundle
                            )
                        }
                        val list = mutableListOf<GetSellerOrderResponseItem>()

                        for (order in data!!) {
                            if (order.status == "pending" && order.product.status == "available" || order.status == "success" && order.product.status == "available") {
                                list.add(order)
                            }
                        }

                        binding.ivListKosong.visibility = if (list.isEmpty()) {
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }

                        binding.tvListKosong.visibility = if (list.isEmpty()) {
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }

                        adapter.submitData(list)
                        binding.apply {
                            rvDiminati.adapter = adapter
                            rvDiminati.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                    Status.ERROR -> {
                        binding.progressCircular.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}