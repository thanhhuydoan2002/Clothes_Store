package com.example.tools_store_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment: Fragment(R.layout.profile_fragment) {

    private lateinit var binding: ProfileFragmentBinding
    private lateinit var auth: FirebaseAuth



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ProfileFragmentBinding.bind(view)
        auth = FirebaseAuth.getInstance()


        binding.likeActualToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }


        binding.btnCheckOrder.setOnClickListener{
//            testing
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                .navigate(R.id.action_profileFragment_to_mainFragment)
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                .navigate(R.id.action_profileFragment_to_loginFragment)

            requireActivity().toast("Đăng xuất thành công")
        }

    }


}