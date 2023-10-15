package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.joaovitormo.stockhub.databinding.ProductItemBinding
import com.joaovitormo.stockhub.model.Product

class AdapterProducts(private val context: Context, private val listProducts: MutableList<Product>, private val listener: RecyclerViewEvent):
    RecyclerView.Adapter<AdapterProducts.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemList = ProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.txtProductName.text = listProducts[position].cName

    }

    override fun getItemCount() = listProducts.size
    inner class  ProductViewHolder(binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val txtProductName = binding.txtProductName

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface RecyclerViewEvent{
        fun onItemClick(position: Int)
    }

}