package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.data.DataHolder
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


        //val item = productsListToSearch.get(holder.absoluteAdapterPosition)
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

        override fun onClick(p0: View?) {
            //listener.onItemClick()
            //Log.d("ITEMCLICK", adapterPosition.toString())
            val position = adapterPosition
            //val position2 = getBindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {

                listener.onItemClick(position)
            }
        }


    }

    interface RecyclerViewEvent{
        fun onItemClick(position: Int)
    }

    fun setDataChanged(query: String): MutableList<Product> {

        productsListToSearch.clear()

        productsListToSearch.addAll(listProducts.filter { it.cName.contains(query,true) })

        notifyDataSetChanged()

        return productsListToSearch
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