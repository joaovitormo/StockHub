package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitormo.stockhub.databinding.ListItemBinding
import com.joaovitormo.stockhub.model.Brand


class AdapterBrands(private val context: Context, private val listBrands: List<Brand>, private val listener: AdapterBrands.RecyclerViewEvent):
    RecyclerView.Adapter<AdapterBrands.ItemViewHolder>() {
    private var brandsListToSearch = listBrands.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //val itemList = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val itemList = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemViewHolder(itemList)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.txtItemName.text = brandsListToSearch[position].cName
        holder.txtItemID.text = brandsListToSearch[position].id


        //val item = productsListToSearch.get(holder.absoluteAdapterPosition)
    }

    override fun getItemCount() = brandsListToSearch.size

    inner class  ItemViewHolder(binding: ListItemBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val txtItemName = binding.txtItemName
        val txtItemID = binding.txtItemID

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

    fun setDataChanged(query: String): MutableList<Brand> {

        brandsListToSearch.clear()

        brandsListToSearch.addAll(listBrands.filter { it.cName.contains(query,true) })

        notifyDataSetChanged()

        return brandsListToSearch
    }


    fun search(query: String): Boolean {

        brandsListToSearch.clear()

        brandsListToSearch.addAll(listBrands.filter { it.cName.contains(query,true) })

        notifyDataSetChanged()

        return brandsListToSearch.isEmpty()
    }


    fun clearSearch(){
        brandsListToSearch = listBrands.toMutableList()
        notifyDataSetChanged()
    }
}