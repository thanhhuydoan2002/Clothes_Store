package com.example.tools_store_app

import com.example.tools_store_app.Extensions.toast
import com.example.tools_store_app.Models.CartModel

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tools_store_app.rvadapters.CartAdapter
import com.example.tools_store_app.databinding.CartFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CartFragment : Fragment(R.layout.cart_fragment), CartAdapter.OnLongClickRemove {

    private lateinit var binding: CartFragmentBinding
    private lateinit var cartList: ArrayList<CartModel>
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CartAdapter
    private var subTotalPrice = 0
    private var totalPrice = 0

    private var orderDatabaseReference = Firebase.firestore.collection("orders")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = CartFragmentBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.cartActualToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }


        val layoutManager = LinearLayoutManager(context)


        cartList = ArrayList()

        retrieveCartItems()



        adapter = CartAdapter(requireContext(),cartList ,this)
        binding.rvCartItems.adapter = adapter
        binding.rvCartItems.layoutManager = layoutManager






        binding.btnCartCheckout.setOnClickListener {

            requireActivity().toast("Whooooa!! Giá trị đơn hàng của bạn là ${totalPrice}\n và đơn hàng sẽ giao đến bạn trong 7 ngày tiếp theo")
            cartList.clear()
            binding.tvLastSubTotalprice.text ="0"
            binding.tvLastTotalPrice.text ="Cần ít nhất 1 sản phẩm"
            binding.tvLastTotalPrice.setTextColor(Color.RED)
            // TODO: remove the data of the Products from the fireStore after checkout or insert a boolean isDelivered
            adapter.notifyDataSetChanged()
        }


    }




    @SuppressLint("SuspiciousIndentation")
    private fun retrieveCartItems() {

        orderDatabaseReference
            .whereEqualTo("uid",auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    val cartProduct = item.toObject<CartModel>()


                    cartList.add(cartProduct)
                    subTotalPrice += cartProduct.price!!.toInt()
                    totalPrice += cartProduct.price!!.toInt()
                    binding.tvLastSubTotalprice.text = subTotalPrice.toString()
                    binding.tvLastTotalPrice.text = totalPrice.toString()
                    binding.tvLastSubTotalItems.text = "Giá tiền của ${cartList.size} sản phẩm"
                    adapter.notifyDataSetChanged()


                }

            }
            .addOnFailureListener{
                requireActivity().toast(it.localizedMessage!!)
            }


    }

    override fun onLongRemove(item: CartModel , position:Int) {


        orderDatabaseReference
            .whereEqualTo("uid",item.uid)
            .whereEqualTo("pid",item.pid)
            .whereEqualTo("size",item.size)
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (item in querySnapshot){
                    orderDatabaseReference.document(item.id).delete()
                    cartList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    requireActivity().toast("Xóa thành công!!!")
                }

            }
            .addOnFailureListener {
                requireActivity().toast("Xóa thất bại, vui lòng thử lại sau")
            }



    }




}