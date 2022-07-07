package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentNotificationBinding
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import binar.lima.satu.secondhand.model.product.GetDetailProductResponse
import binar.lima.satu.secondhand.view.adapter.NotificationAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner) {
            if (it != "") {
                setToken(it)
                apiViewModel.getNotification(it).observe(viewLifecycleOwner) { data ->
                    when (data.status) {
                        SUCCESS -> {
                            if (data.data!!.isNotEmpty()) {
                                val notif = data.data

                                setList(notif)
                                binding.progressCircular.visibility = View.GONE
                            }
                        }
                        ERROR -> {

                        }
                        LOADING -> {

                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_notificationFragment_to_loginFragment)
            }
        }

    }

    private fun setToken(token: String) {
        this.token = token
    }

    private fun setList(data: List<GetNotificationResponseItem>) {
        binding.apply {
            val adapter = NotificationAdapter {
                Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
                /*apiViewModel.patchNotification(token, it.id).observe(viewLifecycleOwner) { notif ->
                    when (notif.status) {
                        SUCCESS -> {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.notificationFragment)
                        }
                        ERROR -> {
                            Toast.makeText(requireContext(), notif.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        LOADING -> {

                        }
                    }
                }*/
            }
            adapter.submitData(data)
            rvNotification.adapter = adapter
            rvNotification.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}