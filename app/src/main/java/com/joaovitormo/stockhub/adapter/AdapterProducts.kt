package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitormo.stockhub.databinding.ProductItemBinding
import com.joaovitormo.stockhub.model.Product

class AdapterProducts(private val context: Context, private val listProducts: List<Product>, private val listener: RecyclerViewEvent):
    RecyclerView.Adapter<AdapterProducts.ProductViewHolder>() {

    private var productsListToSearch = listProducts.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemList = ProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.txtProductName.text = productsListToSearch[position].cName
        holder.txtProductAmount.text = productsListToSearch[position].nAmount.toString()
        holder.txtProductID.text = productsListToSearch[position].id


    }

    override fun getItemCount() = productsListToSearch.size

    inner class  ProductViewHolder(binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val txtProductName = binding.txtProductName
        val txtProductAmount = binding.txtAmountValue
        val txtProductID = binding.txtProductID

        init {
            binding.root.setOnClickListener(this)
        }


/*
        fun getDataAtPosition(position: Int): Product? {
            return if (productsListToSearch != null && getItemCount() > position) productsListToSearch.get(position) else null
        }
 */
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

    fun search(query: String): Boolean {

        productsListToSearch.clear()

        productsListToSearch.addAll(listProducts.filter { it.cName.contains(query,true) })

        notifyDataSetChanged()

        return productsListToSearch.isEmpty()
    }

    fun clearSearch(){
        productsListToSearch = listProducts.toMutableList()
        notifyDataSetChanged()
    }
}