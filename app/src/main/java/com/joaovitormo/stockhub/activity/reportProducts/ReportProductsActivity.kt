package com.joaovitormo.stockhub.activity.reportProducts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.activity.homePage.HomePageActivity
import com.joaovitormo.stockhub.activity.manageProduct.ManageProductFragment
import com.joaovitormo.stockhub.adapter.AdapterReport
import com.joaovitormo.stockhub.databinding.ActivityReportProductsBinding
import com.joaovitormo.stockhub.model.Product


class ReportProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportProductsBinding
    private lateinit var adapterProducts: AdapterReport
    private var listProducts: MutableList<Product> = mutableListOf()
    private var productsListToSearch: MutableList<Product> = mutableListOf()

    //db
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityReportProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchView()
        val recyclerViewProducts = binding.recyclerViewProducts
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.setHasFixedSize(true)
        adapterProducts = AdapterReport(this, listProducts)
        recyclerViewProducts.adapter = adapterProducts
        products()
        customActionBar()

    }

    private fun initSearchView() {

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                Log.d("SimpleSearchView", "Text changed:$query")
                if (adapterProducts.search(query)) {
                    binding.textInfo.text= "Nenhum resultado encontrado."
                } else {
                    binding.textInfo.text =""
                    listProducts = adapterProducts.setDataChanged(query)
                }
                return true

            }

            fun onQueryTextCleared(): Boolean {
                Log.d("SimpleSearchView", "Text cleared")
                return true
            }


        })
    }

    private fun products(){

        db.collection("products").orderBy("cName")
            .addSnapshotListener(object  : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            //Log.e("SimpleSearchView", dc.document.id)
                            listProducts.add(dc.document.toObject(Product::class.java))
                        }
                    }
                    adapterProducts.notifyDataSetChanged()
                    adapterProducts.clearSearch()
                    binding.textInfo.text=""

                }
            })
    }


    private fun customActionBar() {
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Relatório"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
        this.finish()
        onBackPressed()
        return true
    }


}