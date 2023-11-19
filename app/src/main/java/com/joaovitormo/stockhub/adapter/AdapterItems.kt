package com.joaovitormo.stockhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitormo.stockhub.databinding.ListItemBinding
import com.joaovitormo.stockhub.model.Brand


class AdapterItems(private val context: Context, private val listItems: List<Brand>, private val listener: AdapterItems.RecyclerViewEvent):
    RecyclerView.Adapter<AdapterItems.ItemViewHolder>() {
    private var itemsListToSearch = listItems.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //val itemList = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val itemList = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemViewHolder(itemList)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.txtItemName.text = itemsListToSearch[position].cName
        holder.txtItemID.text = itemsListToSearch[position].id


        //val item = productsListToSearch.get(holder.absoluteAdapterPosition)
    }

    override fun getItemCount() = itemsListToSearch.size

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

        itemsListToSearch.clear()

        itemsListToSearch.addAll(listItems.filter { it.cName.contains(query,true) })

        notifyDataSetChanged()

        return itemsListToSearch
    }


    fun search(query: String): Boolean {

        itemsListToSearch.clear()

        itemsListToSearch.addAll(listItems.filter { it.cName.contains(query,true) })

        notifyDataSetChanged()

        return itemsListToSearch.isEmpty()
    }


    fun clearSearch(){
        itemsListToSearch = listItems.toMutableList()
        notifyDataSetChanged()
    }
}