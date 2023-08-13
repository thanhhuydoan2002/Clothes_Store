package com.example.tools_store_app

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.Models.LikeModel
import com.example.tools_store_app.Models.TeeDisplayModel
import com.example.tools_store_app.databinding.MainFragmentBinding
import com.example.tools_store_app.rvadapters.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment(R.layout.main_fragment),
    CategoryOnClickInterface,
    ProductOnClickInterface, LikeOnClickInterface {


    private lateinit var binding: MainFragmentBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var productList: ArrayList<TeeDisplayModel>
    private lateinit var categoryList: ArrayList<String>
    private lateinit var productsAdapter: TeeDisplayAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private lateinit var auth: FirebaseAuth
    private var likeDBRef = Firebase.firestore.collection("LikedProducts")



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = MainFragmentBinding.bind(view)
        categoryList = ArrayList()
        productList = ArrayList()
        databaseReference = FirebaseDatabase.getInstance("https://tools-store-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("products")
        auth = FirebaseAuth.getInstance()


        // region implements category Recycler view

        categoryList.add("Thịnh hành")
        binding.rvMainCategories.setHasFixedSize(true)
        val categoryLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvMainCategories.layoutManager = categoryLayoutManager
        categoryAdapter = MainCategoryAdapter(categoryList, this)
        binding.rvMainCategories.adapter = categoryAdapter
        setCategoryList()

        // endregion implements category Recycler view


        // region implements products Recycler view

        val productLayoutManager = GridLayoutManager(context, 2)
        productsAdapter = TeeDisplayAdapter(requireContext(), productList, this,this)
        binding.rvMainProductsList.layoutManager = productLayoutManager
        binding.rvMainProductsList.adapter = productsAdapter
        setProductsData()
        // endregion implements products Recycler view


        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_self)
                    true
                }
                R.id.likeFragment -> {
//                    requireActivity().toast("Like Page coming Soon")
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_likeFragment)
                    true
                }
                R.id.cartFragment -> {

                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_cartFragment)

                    true
                }
                R.id.profileFragment -> {

                    auth.signOut()
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_loginFragment)
                    true
                }
                else -> false

            }

        }


    }

    private fun setCategoryList() {

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                categoryList.clear()
                categoryList.add("Thịnh hành")

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(TeeDisplayModel::class.java)

                        categoryList.add(products!!.brand!!)

                    }

                    categoryAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }


        databaseReference.addValueEventListener(valueEvent)




    }


    private fun setProductsData() {

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(TeeDisplayModel::class.java)
                        productList.add(products!!)
                    }

                    productsAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }

        databaseReference.addValueEventListener(valueEvent)

    }

    override fun onClickCategory(button: Button) {
        binding.tvMainCategories.text = button.text

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(TeeDisplayModel::class.java)

                        if (products!!.brand == button.text) {
                            productList.add(products)
                        }

                        if (button.text == "Thịnh hành") {
                            productList.add(products)
                        }

                    }

                    productsAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }

        databaseReference.addValueEventListener(valueEvent)


    }


    override fun onClickProduct(item: TeeDisplayModel) {

        val direction = MainFragmentDirections
            .actionMainFragmentToDetailsFragment(
                item.id!!
            )

        Navigation.findNavController(requireView())
            .navigate(direction)


    }

    override fun onClickLike(item: TeeDisplayModel) {

        likeDBRef
            .add(LikeModel(item.id , auth.currentUser!!.uid , item.brand , item.description , item.imageUrl , item.name ,item.price))
            .addOnSuccessListener {
                requireActivity().toast("Đã thêm vào mục yêu thích")
            }
            .addOnFailureListener {
                requireActivity().toast("Thêm vào mục yêu thích thất bại")
            }

    }



}




