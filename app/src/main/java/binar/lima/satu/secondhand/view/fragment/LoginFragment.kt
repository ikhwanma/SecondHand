package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentLoginBinding
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var viewPass: Boolean = false

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

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.btnLogin.setOnClickListener(this)
        binding.btnBack.setOnClickListener{
            it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        binding.tvRegister.setOnClickListener(this)
        binding.btnViewPass.setOnClickListener(this)
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
            R.id.btn_view_pass -> {
                if (!viewPass) {
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_green_eye_24)
                        etPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }
                    viewPass = true
                } else {
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        etPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    }
                    viewPass = false
                }
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