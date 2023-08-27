package com.example.tools_store_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.Models.UserModel
import com.example.tools_store_app.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment: Fragment(R.layout.profile_fragment) {

    private lateinit var binding: ProfileFragmentBinding
    private lateinit var auth: FirebaseAuth

    private var userDBRef = Firebase.firestore.collection("users")




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ProfileFragmentBinding.bind(view)
        auth = FirebaseAuth.getInstance()


        binding.profileActualToolbar.setNavigationOnClickListener {
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



        val currentUser = auth.currentUser

        var uid: String? = null
        var uname: String? = null
        var email: String? = null
        var createdDate: String? = null

        currentUser?.let { user ->
            uid = user.uid

            val userRef = userDBRef.document(uid ?: "")

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userModel = documentSnapshot.toObject(UserModel::class.java)
                        userModel?.let {
                            uname = userModel.uname
                            email = userModel.email
                            createdDate = userModel.createdDate
                            binding.tvName.text= uname
                            binding.tvEmailInfo.text = email
                            binding.tvCreatedDate.text = createdDate
                        }
                    } else {
                        // Không tìm thấy dữ liệu người dùng
                    }
                }
                .addOnFailureListener { exception ->
                    // Xử lý lỗi khi truy vấn dữ liệu
                }
        }

    }



}