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
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentLoginBinding
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner){
            if (it != ""){
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_login -> {
                binding.apply {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    if (email != "" && password != ""){
                        val loginBody = LoginBody(email, password)
                        login(loginBody)
                    }
                }
            }
            R.id.tv_register -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun login(loginBody: LoginBody) {
        apiViewModel.loginUser(loginBody).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    userViewModel.setToken(it.data!!.accessToken)
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                    Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                }
                ERROR -> {
                    if(it.message!!.contains("401")){
                        Toast.makeText(requireContext(), "Email atau password salah", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                    }
                }
                LOADING -> {
                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}