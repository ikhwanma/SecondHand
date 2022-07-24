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
import binar.lima.satu.secondhand.databinding.FragmentHistoryTabBinding
import binar.lima.satu.secondhand.view.adapter.HistoryAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class HistoryTabFragment : Fragment() {

    private var _binding: FragmentHistoryTabBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner){ token ->
            apiViewModel.getHistory(token).observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        binding.progressCircular.visibility = View.GONE
                        val data = it.data!!

                        if (data.isEmpty()){
                            binding.llListKosong.visibility = View.VISIBLE
                        }

                        val adapter = HistoryAdapter()
                        adapter.submitData(data)

                        binding.apply {
                            rvHistory.adapter = adapter
                            rvHistory.layoutManager = LinearLayoutManager(requireContext())
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

