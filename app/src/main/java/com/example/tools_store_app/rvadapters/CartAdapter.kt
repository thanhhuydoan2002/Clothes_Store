package com.example.tools_store_app.rvadapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tools_store_app.CartFragment
import com.example.tools_store_app.Models.CartModel
import com.example.tools_store_app.SwipeToDelete
import com.example.tools_store_app.databinding.CartProductItemBinding

class CartAdapter(
    private val context: Context,
    private val list:ArrayList<CartModel>,
    private val onLongClickRemove: OnLongClickRemove,
):RecyclerView.Adapter<CartAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: CartProductItemBinding):RecyclerView.ViewHolder(binding.root){

        private val onSwipeDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                list.removeAt(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CartProductItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivCartProduct)


        holder.binding.tvCartProductName.text = currentItem.name
        holder.binding.tvCartProductPrice.text = "${currentItem.price} VND"
        holder.binding.tvCartItemCount.text = currentItem.quantity.toString()
        holder.binding.tvCartProductSize.text = currentItem.size

        var count = holder.binding.tvCartItemCount.text.toString().toInt()

        holder.binding.btnCartItemAdd.setOnClickListener {
            if(count < 5){
                count++
                val newPrice = (currentItem.price)!!.toInt() * count

                // TODO: Update Quantity in Database also
                holder.binding.tvCartItemCount.text = count.toString()
                holder.binding.tvCartProductPrice.text = "${newPrice} VND"


            }

        }

        holder.binding.btnCartItemMinus.setOnClickListener {
            if(count > 1){
                count--
                val newPrice = (currentItem.price)!!.toInt() * count

                // TODO: Update Quantity in Database also
                holder.binding.tvCartItemCount.text = count.toString()
                holder.binding.tvCartProductPrice.text = "${newPrice} VND"

            }
        }

        holder.itemView.setOnLongClickListener {
            onLongClickRemove.onLongRemove(currentItem , position)
            true

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnLongClickRemove{
        fun onLongRemove(item:CartModel , position: Int)
    }




}