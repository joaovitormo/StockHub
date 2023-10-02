package com.joaovitormo.stockhub.activity.listProducts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaovitormo.stockhub.R
import com.joaovitormo.stockhub.adapter.AdapterProducts
import com.joaovitormo.stockhub.databinding.ActivityListProductsBinding
import com.joaovitormo.stockhub.databinding.ProductItemBinding
import com.joaovitormo.stockhub.model.Product

class ListProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListProductsBinding
    private lateinit var adapterProducts: AdapterProducts
    private val listProducts: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customActionBar()

        val recyclerViewProducts = binding.recyclerViewProducts
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.setHasFixedSize(true)
        adapterProducts = AdapterProducts(this, listProducts)
        recyclerViewProducts.adapter = adapterProducts
        products()

    }

    private fun products(){
        val product1 = Product("Cimento")
        listProducts.add(product1)

        val product2 = Product("Cimento2")
        listProducts.add(product2)

        val product3 = Product("Cimento")
        listProducts.add(product1)

        val product4 = Product("Cimento2")
        listProducts.add(product4)
        val product5 = Product("Cimento")
        listProducts.add(product5)

        val product6 = Product("Cimento2")
        listProducts.add(product6)
        val product7 = Product("Cimento")
        listProducts.add(product7)

        val product8 = Product("Cimento2")
        listProducts.add(product8)
        val product9 = Product("Cimento")
        listProducts.add(product9)

        val product10 = Product("Cimento2")
        listProducts.add(product10)
        val product11 = Product("Cimento")
        listProducts.add(product11)

        val product12 = Product("Cimento2")
        listProducts.add(product12)

    }


    private fun customActionBar() {
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Lista de Produtos"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
