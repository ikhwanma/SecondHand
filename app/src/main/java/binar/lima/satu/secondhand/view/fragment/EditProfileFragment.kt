package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentEditProfileBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token : String
    private lateinit var user : GetLoginResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimpan.setOnClickListener(this)

        userViewModel.getToken().observe(viewLifecycleOwner){
            setToken(it)
            apiViewModel.getLoginUser(it).observe(viewLifecycleOwner){ user ->
                when(user.status){
                    SUCCESS -> {
                        binding.apply {
                            val data = user.data!!
                            etNamaEditProfile.setText(data.fullName)
                            etAlamatEditProfile.setText(data.address)
                            etNoHandphoneEditProfile.setText(data.phoneNumber)
                            etPilihKotaEditProfile.setText(data.city)

                            setUser(data)
                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }



        binding.btnPanah.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
    }

    private fun setUser(data: GetLoginResponse) {
        user = data
    }

    private fun setToken(it: String) {
        this.token = it
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

                    val requestBody = RegisterBody(alamat, user.email, nama, "sdasda", user.password, handphone, kota)

                    apiViewModel.updateUser(token, requestBody).observe(viewLifecycleOwner){
                        when(it.status){
                            SUCCESS -> {
                                Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                                Navigation.findNavController(requireView()).navigate(R.id.action_editProfileFragment_to_profileFragment)
                            }
                            ERROR -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                            LOADING -> {
                                Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}