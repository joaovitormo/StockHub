package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitormo.stockhub.databinding.ProductItemBinding
import com.joaovitormo.stockhub.databinding.ProductReportItemBinding
import com.joaovitormo.stockhub.model.Product

class AdapterReport(private val context: Context, private val listProducts: List<Product>):
RecyclerView.Adapter<AdapterReport.ProductViewHolder>()
{
    private var productsListToSearch = listProducts.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemList = ProductReportItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(itemList)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.txtProductName.text = productsListToSearch[position].cName
        holder.txtProductAmount.text = productsListToSearch[position].nAmount.toString()
        holder.txtProductID.text = productsListToSearch[position].id
        holder.txtBrand.text = productsListToSearch[position].cBrand
        holder.txtCategory.text = productsListToSearch[position].cCategory
        holder.txtPosition.text = productsListToSearch[position].cStockPosition


        //val item = productsListToSearch.get(holder.absoluteAdapterPosition)
    }

    override fun getItemCount() = productsListToSearch.size

    inner class  ProductViewHolder(binding: ProductReportItemBinding): RecyclerView.ViewHolder(binding.root){
        val txtProductName = binding.txtProductName
        val txtProductAmount = binding.txtAmountValue
        val txtProductID = binding.txtProductID
        val txtBrand = binding.txtBrandValue
        val txtCategory = binding.txtCategoryValue
        val txtPosition= binding.txtPositionValue

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