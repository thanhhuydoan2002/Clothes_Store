package com.example.tools_store_app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.Models.UserModel
import com.example.tools_store_app.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var auth : FirebaseAuth
    private var userDBRef = Firebase.firestore.collection("users")



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

                val userModel = UserModel(
                    uname = binding.etNameRegister.text.toString(),
                    email = binding.etEmailRegister.text.toString(),
                )

                createUser(binding.etEmailRegister.text.toString(),binding.etPasswordRegister.text.toString(),userModel)


            }
        }


        binding.tvNavigateToLogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }


    }

    private fun createUser(email: String, password: String, userModel: UserModel) {
        val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val currentDate = sdf.format(Date()) // Get current time and format it

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    val uid = task.result?.user?.uid

                    if (uid != null) {

                        userModel.uid = uid
                        userModel.createdDate = currentDate // Assign creation time

                        saveUserData(userModel)

                        requireActivity().toast("Đăng ký tài khoản thành công")

                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_registerFragment_to_mainFragment)

                    } else {
                        requireActivity().toast("Không thể lấy UID của người dùng")
                    }
                }
                else{
                    requireActivity().toast(task.exception!!.localizedMessage)
                }

            }

    }

    private fun saveUserData(userModel: UserModel) {
        val uid = userModel.uid

        if (uid != null) {
            userDBRef
                .document(uid)
                .set(userModel)
                .addOnSuccessListener {
                    // Success
                }
                .addOnFailureListener {
                    // Fail
                    Toast.makeText(requireContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Không có UID để lưu thông tin", Toast.LENGTH_SHORT).show()
        }
    }


}