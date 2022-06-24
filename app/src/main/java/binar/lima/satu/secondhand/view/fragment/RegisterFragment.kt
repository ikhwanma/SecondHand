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
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentRegisterBinding
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

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_register -> {
                binding.apply {
                    val name = etName.text.toString()
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    if (email != "" && password != "" && name != ""){
                        val registerBody = RegisterBody("temp", email, name, "temp", password, 0, "temp")
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
                    Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
                    Toast.makeText(requireContext(), "Register berhasil", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    if(it.message!!.contains("400")){
                        Toast.makeText(requireContext(), "Email sudah digunakan", Toast.LENGTH_SHORT).show()
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