package com.example.tools_store_app.rvadapters

import com.example.tools_store_app.Models.TeeDisplayModel
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tools_store_app.databinding.MainDisplayItemBinding

class TeeDisplayAdapter(
    private val context:Context,
    private val list: List<TeeDisplayModel>,
    private val productClickInterface: ProductOnClickInterface,
    private val likeClickInterface: LikeOnClickInterface,

    ) : RecyclerView.Adapter<TeeDisplayAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: MainDisplayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MainDisplayItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.tvNameTeeDisplayItem.text = "${currentItem.brand} ${currentItem.name}"
        holder.binding.tvPriceTeeDisplayItem.text = "${currentItem.price} VND"


        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivTeeDisplayItem)


        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(list[position])
        }

        holder.binding.btnLike.setOnClickListener {
            if(holder.binding.btnLike.isChecked){
                holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.RED)
                likeClickInterface.onClickLike(currentItem)
            }
            else{
                holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}

interface ProductOnClickInterface {
    fun onClickProduct(item: TeeDisplayModel)
}

interface LikeOnClickInterface{
    fun onClickLike(item :TeeDisplayModel)
}