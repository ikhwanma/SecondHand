package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentRegisterBinding
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import java.util.regex.Pattern


class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var viewPass: Boolean = false
    private var cekData: Boolean = false

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkEditText()

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.btnRegister.setOnClickListener(this)
        binding.btnBack.setOnClickListener{
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.btnViewPass.setOnClickListener(this)
    }

    private fun checkEditText() {

        binding.apply {
            etName.addTextChangedListener(object  : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.length < 3){
                        cekData = false
                        binding.tvErrName.visibility = View.VISIBLE
                    }else{
                        cekData = true
                        binding.tvErrName.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            etEmail.addTextChangedListener(object  : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!isValidEmail(p0.toString())){
                        cekData = false
                        binding.tvErrEmail.visibility = View.VISIBLE
                    }else{
                        cekData = true
                        binding.tvErrEmail.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            etPassword.addTextChangedListener(object  : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.length < 6){
                        cekData = false
                        binding.tvErrPassword.visibility = View.VISIBLE
                    }else{
                        cekData = true
                        binding.tvErrPassword.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_register -> {
                binding.apply {
                    val name = etName.text.toString()
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    if (email != "" && password != "" && name != ""){
                        val registerBody = RegisterBody("", email, name, "", password, 0, "")
                        if (cekData){
                            register(registerBody)
                        }
                    }else{
                        Toast.makeText(requireContext(), "Isi semua form", Toast.LENGTH_SHORT).show()
                        if (email == ""){
                            binding.tvErrEmail.visibility = View.VISIBLE
                        }
                        if (password == ""){
                            binding.tvErrPassword.visibility = View.VISIBLE
                        }
                        if (name == ""){
                            binding.tvErrName.visibility = View.VISIBLE
                        }
                    }
                }
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

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }
}