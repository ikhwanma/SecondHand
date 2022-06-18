package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentEditProfileBinding
import binar.lima.satu.secondhand.databinding.FragmentProfileBinding
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner){
            if (it != ""){
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
        binding.btnSimpan.setOnClickListener(this)

        binding.btnPanah.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_simpan -> {
                binding.apply {
                    val image = btnGambarEditProfile
                    val nama = etNamaEditProfile.text.toString()
                    val kota = etPilihKotaEditProfile.text.toString()
                    val alamat = etAlamatEditProfile.text.toString()
                    val handphone = etNoHandphoneEditProfile.text.toString().toLong()

                    userViewModel.getToken().observe(viewLifecycleOwner){
                        apiViewModel.getLoginUser(it).observe(viewLifecycleOwner){
                            when(it.status){
                                Status.SUCCESS -> {
                                    val registerBody = RegisterBody(kota, it.data!!.email, nama, image, it.data.password, handphone)
                                    updateUser(registerBody)
                                }
                                Status.ERROR -> {
                                    if(it.message!!.contains("401")){
                                        Toast.makeText(requireContext(), "Update User Gagal", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Status.LOADING -> {
                                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateUser(registerBody: RegisterBody){
        apiViewModel.registerUser(registerBody).observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    userViewModel.setToken(it.data!!.accessToken)
                    Navigation.findNavController(requireView()).navigate(R.id.action_editProfileFragment_to_profileFragment)
                    Toast.makeText(requireContext(), "Update User Berhasil", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    if(it.message!!.contains("401")){
                        Toast.makeText(requireContext(), "Update User Gagal", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}