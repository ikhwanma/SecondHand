package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentUbahPasswordBinding
import binar.lima.satu.secondhand.model.auth.update.UpdatePasswordBody
import binar.lima.satu.secondhand.view.dialogfragment.Dialog
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class UbahPasswordFragment : Fragment() {

    private var _binding: FragmentUbahPasswordBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var viewPass = false
    private var viewNewPass = false
    private var viewConfPass = false

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUbahPasswordBinding.inflate(inflater, container, false)
        dialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnViewPass.setOnClickListener {
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

            btnViewNewPass.setOnClickListener {
                if (!viewNewPass) {
                    binding.apply {
                        btnViewNewPass.setImageResource(R.drawable.ic_green_eye_24)
                        etNewPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }
                    viewNewPass = true
                } else {
                    binding.apply {
                        btnViewNewPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        etNewPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    }
                    viewNewPass = false
                }
            }

            btnViewConfPass.setOnClickListener {
                if (!viewConfPass) {
                    binding.apply {
                        btnViewConfPass.setImageResource(R.drawable.ic_green_eye_24)
                        etConfPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }
                    viewConfPass = true
                } else {
                    binding.apply {
                        btnViewConfPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        etConfPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    }
                    viewConfPass = false
                }
            }

            btnEdit.setOnClickListener {
                val pass = etPassword.text.toString()
                val newPass = etNewPassword.text.toString()
                val confPass = etConfPassword.text.toString()

                if (newPass == confPass && newPass.length >= 6){
                    userViewModel.getToken().observe(viewLifecycleOwner){ token ->
                        apiViewModel.updatePassword(token, UpdatePasswordBody(pass, newPass, confPass)).observe(viewLifecycleOwner){
                            when(it.status){
                                SUCCESS -> {
                                    dialog.dismissDialog()
                                    Snackbar.make(requireView(), "Password diubah", Snackbar.LENGTH_SHORT).show()
                                    Navigation.findNavController(requireView()).navigate(R.id.action_ubahPasswordFragment_to_profileFragment)
                                }
                                ERROR -> {
                                    dialog.dismissDialog()
                                    if (it.message!!.contains("400")) Toast.makeText(
                                        requireContext(),
                                        "Password lama tidak sesuai",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LOADING -> {
                                    dialog.startDialog()
                                }
                            }
                        }
                    }
                }else if (newPass.length < 6){
                    Toast.makeText(requireContext(), "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Password tidak sama", Toast.LENGTH_SHORT).show()
                }
            }

            btnBack.setOnClickListener {
                it.findNavController().navigate(R.id.action_ubahPasswordFragment_to_profileFragment)
            }
        }

    }

}