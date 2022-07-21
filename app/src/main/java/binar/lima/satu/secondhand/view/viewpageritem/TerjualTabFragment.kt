package binar.lima.satu.secondhand.view.viewpageritem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentTerjualTabBinding
import binar.lima.satu.secondhand.view.adapter.TerjualAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class TerjualTabFragment : Fragment() {

    private var _binding: FragmentTerjualTabBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTerjualTabBinding.inflate(inflater, container, false)
        getDataTerjual()
        return binding.root
    }

    private fun getDataTerjual() {
        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            apiViewModel.getSellerOrder(token, "accepted").observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        val data = it.data!!
                        binding.progressCircular.visibility = View.GONE

                        val adapter = TerjualAdapter {

                        }
                        adapter.submitData(data)
                        binding.apply {
                            rvTerjual.adapter = adapter
                            rvTerjual.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                    ERROR -> {
                        binding.progressCircular.visibility = View.GONE
                    }
                    LOADING -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}