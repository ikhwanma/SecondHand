package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentLoginBinding
import binar.lima.satu.secondhand.databinding.FragmentRegisterBinding
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class RegisterFragment : Fragment(), View.OnClickListener {

    private var _bindingr: FragmentRegisterBinding? = null
    private val binding get() = _bindingr!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingr = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "token : $it", Toast.LENGTH_SHORT).show()

          /*  if (it != null){
                apiViewModel.getRegiterUser(it).observe(viewLifecycleOwner){ user ->
                    when(user.status){
                        Status.SUCCESS -> {
                            Toast.makeText(requireContext(), user.data.toString(), Toast.LENGTH_SHORT).show()
                        }
                        Status.ERROR -> {
                            Toast.makeText(requireContext(), "Get User Gagal", Toast.LENGTH_SHORT).show()
                        }
                        Status.LOADING -> {
                            Toast.makeText(requireContext(), "Loading Get User", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }*/
        }

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_register -> {
                binding.apply {
                    val username = etUsername.text.toString()
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    if (email != "" && password != ""){
                        val registerBody = RegisterBody(email, password, username)
                        register(registerBody)
                    }
                }
            }
        }
    }

    private fun register(registerBody: RegisterBody) {
        apiViewModel.registerUser(registerBody).observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    userViewModel.setToken(it.data!!.accessToken)
                    Toast.makeText(requireContext(), "Register Berhasil", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    if(it.message!!.contains("401")){
                        Toast.makeText(requireContext(), "Lengkapi data anda", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Lengkapi lagi", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}