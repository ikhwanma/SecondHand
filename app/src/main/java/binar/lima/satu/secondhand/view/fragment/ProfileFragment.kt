package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.FragmentLoginBinding
import binar.lima.satu.secondhand.databinding.FragmentProfileBinding
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            userViewModel.setToken("")
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment)
        }
    }
}