package com.example.tools_store_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = LoginFragmentBinding.bind(view)
        auth = FirebaseAuth.getInstance()



        if (auth.currentUser != null) {
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                .navigate(R.id.action_loginFragment_to_mainFragment)
        }

        binding.btnLogin.setOnClickListener {

            if (
                binding.etEmailLogin.text.isNotEmpty() &&
                binding.etPasswordLogin.text.isNotEmpty()
            ) {


                signinUser(binding.etEmailLogin.text.toString(),
                    binding.etPasswordLogin.text.toString())


            } else {
                requireActivity().toast("Email và mật khẩu không được bỏ trống")
            }
        }


        binding.tvNavigateToRegister.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

    private fun signinUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    requireActivity().toast("Đăng nhập thành công")

                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_loginFragment_to_mainFragment)
                } else {
                    requireActivity().toast(task.exception!!.localizedMessage!!)

                }


            }

    }


}