package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.FragmentDaftarJualSayaBinding
import binar.lima.satu.secondhand.view.adapter.ProductAdapter

import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class DaftarJualSayaFragment : Fragment() {
    private var _binding: FragmentDaftarJualSayaBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_jual_saya, container, false)
    }

    private fun initRV(){
        val adapter = ProductAdapter{ data ->
            val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
        }
    }
}