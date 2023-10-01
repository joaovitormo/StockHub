package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitormo.stockhub.databinding.ProductItemBinding
import com.joaovitormo.stockhub.model.Product

class AdapterProducts(private val context: Context, private val listProducts: MutableList<Product>):
    RecyclerView.Adapter<AdapterProducts.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemList = ProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.txtProductName.text = listProducts[position].name
    }

    override fun getItemCount() = listProducts.size
    inner class  ProductViewHolder(binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root) {
        val txtProductName = binding.txtProductName

    }

}