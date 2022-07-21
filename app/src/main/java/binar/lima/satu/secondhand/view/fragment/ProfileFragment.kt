package binar.lima.satu.secondhand.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.OnlineChecker
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentProfileBinding
import binar.lima.satu.secondhand.view.activity.MainActivity
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val connected = OnlineChecker.isOnline(requireContext())
        if (!connected){
            Toast.makeText(requireContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show()
        }
        (activity as MainActivity).getBadge()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner) {
            if (it == ""){
                Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment)
            }else{
                apiViewModel.getLoginUser(it).observe(viewLifecycleOwner) { it1 ->
                    when (it1.status) {
                        Status.SUCCESS -> {
                            val data = it1.data!!
                            if (data.imageUrl.isNotEmpty()){
                                Glide.with(requireView()).load(data.imageUrl).into(binding.imgUser)
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

        binding.btnUbahAkun.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Logout")
                .setMessage("Apakah Anda Yakin?")
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton("Iya") { _, _ ->
                    AlertDialog.Builder(requireContext()).setTitle("Simpan data login")
                        .setMessage("Apakah Anda Ingin Menyimpan Data Login Anda?")
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton("Iya") { _, _ ->
                            userViewModel.getToken().observe(viewLifecycleOwner){
                                userViewModel.setBiometric(it)
                            }
                            userViewModel.setToken("")
                            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment)
                        }.setNegativeButton("Tidak") { _, _ ->
                            userViewModel.setToken("")
                            userViewModel.setBiometric("")
                            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment)
                        }
                        .show()
                }.setNegativeButton("Tidak") { _, _ ->

                }
                .show()
        }

        binding.btnUbahPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_ubahPasswordFragment)
        }
    }
}