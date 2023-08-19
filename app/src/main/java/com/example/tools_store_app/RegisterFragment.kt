package com.example.tools_store_app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var auth : FirebaseAuth



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = RegisterFragmentBinding.bind(view)
        auth = FirebaseAuth.getInstance()



        binding.btnRegister.setOnClickListener {

            if (
                binding.etEmailRegister.text.isNotEmpty() &&
                binding.etNameRegister.text.isNotEmpty() &&
                binding.etPasswordRegister.text.isNotEmpty()
            ) {

                createUser(binding.etEmailRegister.text.toString(),binding.etPasswordRegister.text.toString())


            }
        }
        binding.tvNavigateToLogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }


    }

    private fun createUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){

                    requireActivity().toast("Đăng ký tài khoản thành công")

                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_registerFragment_to_mainFragment)

                }
                else{
                    requireActivity().toast(task.exception!!.localizedMessage)
                }

            }

    }


}