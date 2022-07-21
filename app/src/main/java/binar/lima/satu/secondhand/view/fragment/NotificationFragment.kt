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
import binar.lima.satu.secondhand.data.utils.OnlineChecker
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentNotificationBinding
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import binar.lima.satu.secondhand.view.activity.MainActivity
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
            if (it != "") {
                setToken(it)
                apiViewModel.getNotification(it).observe(viewLifecycleOwner) { data ->
                    when (data.status) {
                        SUCCESS -> {
                            binding.progressCircular.visibility = View.GONE
                            if (data.data!!.isNotEmpty()) {
                                val notif = data.data

                                val x = notif.size - 1

                                val listNotif = mutableListOf<GetNotificationResponseItem>()

                                for(i in x downTo 0){
                                    if (!notif[i].read) listNotif.add(notif[i])
                                }
                                for(i in x downTo 0){
                                    if (notif[i].read) listNotif.add(notif[i])
                                }
                                setList(listNotif)
                                binding.progressCircular.visibility = View.GONE
                            }else{
                                binding.llStatus.visibility = View.VISIBLE
                            }
                        }
                        ERROR -> {
                            binding.progressCircular.visibility = View.GONE
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
                apiViewModel.patchNotification(token, it.id).observe(viewLifecycleOwner) { notif ->
                    when (notif.status) {
                        SUCCESS -> {
                            val status = it.status
                            if (status == "success"){
                                val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.productId)
                                Navigation.findNavController(requireView()).navigate(R.id.action_notificationFragment_to_detailFragment, mBundle)
                            }
                            if (status == "create"){
                                val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.productId)
                                Navigation.findNavController(requireView()).navigate(R.id.action_notificationFragment_to_detailFragment, mBundle)
                            }
                            if (status == "declined"){
                                val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.productId)
                                Navigation.findNavController(requireView()).navigate(R.id.action_notificationFragment_to_detailFragment, mBundle)
                            }
                            if (status == "accepted"){
                                val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.productId)
                                Navigation.findNavController(requireView()).navigate(R.id.action_notificationFragment_to_detailFragment, mBundle)
                            }
                            if (status == "tolak"){
                                val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.productId)
                                Navigation.findNavController(requireView()).navigate(R.id.action_notificationFragment_to_detailFragment, mBundle)
                            }
                            if (status == "bid"){
                                val mBundle = bundleOf(InfoPenawarFragment.EXTRA_ORDER_ID to it.orderId)
                                Navigation.findNavController(requireView()).navigate(R.id.action_notificationFragment_to_infoPenawarFragment2, mBundle)
                            }
                            (activity as MainActivity).getBadge()
                        }
                        ERROR -> {

                        }
                        LOADING -> {

                        }
                    }
                }
            }
            adapter.submitData(data)
            rvNotification.adapter = adapter
            rvNotification.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}