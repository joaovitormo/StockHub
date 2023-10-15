package com.joaovitormo.stockhub.activity.listProducts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.activity.manageProduct.ManageProductFragment
import com.joaovitormo.stockhub.adapter.AdapterProducts
import com.joaovitormo.stockhub.databinding.ActivityListProductsBinding
import com.joaovitormo.stockhub.model.Product

class ListProductsActivity : AppCompatActivity(), AdapterProducts.RecyclerViewEvent {

    private lateinit var binding: ActivityListProductsBinding
    private lateinit var adapterProducts: AdapterProducts
    private val listProducts: MutableList<Product> = mutableListOf()

    //db
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customActionBar()

        val recyclerViewProducts = binding.recyclerViewProducts
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.setHasFixedSize(true)
        adapterProducts = AdapterProducts(this, listProducts, this)
        recyclerViewProducts.adapter = adapterProducts
        products()


    }



    private fun products(){
        db.collection("products").document("teste")
            .addSnapshotListener { documento, error ->
                if (documento != null) {
                    Log.d("db", documento.toString())
                }
            }

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
                            //Log.e("Firestore", dc.document.id)
                            listProducts.add(dc.document.toObject(Product::class.java))
                        }
                    }
                    adapterProducts.notifyDataSetChanged()

                }
            })




        /*
        val product1 = Product("Cimento")
        listProducts.add(product1)
        val product2 = Product("Cimento2")
        listProducts.add(product2)
        val product3 = Product("Cimento3")
        listProducts.add(product1)
        val product4 = Product("Cimento4")
        listProducts.add(product4)
        val product5 = Product("Cimento5")
        listProducts.add(product5)
        val product6 = Product("Cimento6")
        listProducts.add(product6)
        val product7 = Product("Cimento7")
        listProducts.add(product7)
        val product8 = Product("Cimento8")
        listProducts.add(product8)
        val product9 = Product("Cimento9")
        listProducts.add(product9)
        val product10 = Product("Cimento10")
        listProducts.add(product10)
        val product11 = Product("Cimento11")
        listProducts.add(product11)
        val product12 = Product("Cimento12")
        listProducts.add(product12)
        */

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

    override fun onItemClick(position: Int) {
        val product = listProducts[position].id

        val args = Bundle()
        args.putString(ManageProductFragment.PRODUCT_ID, product.toString())


        Log.d("IDPRODUCT", args.toString())




        val dialog = ManageProductFragment()
        dialog.show(supportFragmentManager, dialog.tag)
        dialog.setArguments(args)
        //dialog.onCreate(args)
    }
}
