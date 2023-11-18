package com.joaovitormo.stockhub.activity.listProducts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.activity.homePage.HomePageActivity
import com.joaovitormo.stockhub.activity.manageProduct.ManageProductFragment
import com.joaovitormo.stockhub.adapter.AdapterProducts
import com.joaovitormo.stockhub.databinding.ActivityListProductsBinding
import com.joaovitormo.stockhub.model.Product

class ListProductsActivity : AppCompatActivity(), AdapterProducts.RecyclerViewEvent, ManageProductFragment.ManageProductDialogListener {

    private lateinit var binding: ActivityListProductsBinding
    private lateinit var adapterProducts: AdapterProducts
    private var listProducts: MutableList<Product> = mutableListOf()
    private var productsListToSearch: MutableList<Product> = mutableListOf()
    lateinit var listener: ManageProductFragment.ManageProductDialogListener

    //db
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this

        binding = ActivityListProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchView()
        val recyclerViewProducts = binding.recyclerViewProducts
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.setHasFixedSize(true)
        adapterProducts = AdapterProducts(this, listProducts, this)
        recyclerViewProducts.adapter = adapterProducts
        products()
        customActionBar()



        binding.btnNewProduct.setOnClickListener{
            val dialog = ManageProductFragment(listener)
            dialog.show(supportFragmentManager, dialog.tag)
        }

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
            .addSnapshotListener(object  : EventListener<QuerySnapshot>{
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
        actionbar!!.title = "Lista de Produtos"
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

    override fun onItemClick(position: Int) {
        val product = listProducts[position].id

        val args = Bundle()
        args.putString(ManageProductFragment.PRODUCT_ID, product.toString())

        adapterProducts.notifyDataSetChanged()

        //Log.d("IDPRODUCT", args.toString())

        val dialog = ManageProductFragment(listener)
        dialog.show(supportFragmentManager, dialog.tag)
        dialog.setArguments(args)
        //dialog.cancelDialog()
        //dialog.setCancelable(false)
    }

    override fun editProduct() {
        adapterProducts.notifyDataSetChanged()
        val intent = Intent(this, ListProductsActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}






