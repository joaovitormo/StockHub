package com.joaovitormo.stockhub.activity.listBrands

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.R
import com.joaovitormo.stockhub.activity.homePage.HomePageActivity
import com.joaovitormo.stockhub.activity.listProducts.ListProductsActivity
import com.joaovitormo.stockhub.activity.manageBrand.ManageBrandFragment
import com.joaovitormo.stockhub.activity.manageProduct.ManageProductFragment
import com.joaovitormo.stockhub.adapter.AdapterBrands
import com.joaovitormo.stockhub.adapter.AdapterProducts
import com.joaovitormo.stockhub.databinding.ActivityListBrandsBinding
import com.joaovitormo.stockhub.databinding.ActivityListProductsBinding
import com.joaovitormo.stockhub.model.Brand
import com.joaovitormo.stockhub.model.Product

class ListBrandsActivity : AppCompatActivity(), AdapterBrands.RecyclerViewEvent, ManageBrandFragment.ManageBrandDialogListener  {
    private lateinit var binding: ActivityListBrandsBinding
    private lateinit var adapterBrands: AdapterBrands
    private var listItems: MutableList<Brand> = mutableListOf()
    lateinit var listener: ManageBrandFragment.ManageBrandDialogListener

    //db
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this

        binding = ActivityListBrandsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchView()
        val recyclerViewBrands = binding.recyclerViewBrands
        recyclerViewBrands.layoutManager = LinearLayoutManager(this)
        recyclerViewBrands.setHasFixedSize(true)
        adapterBrands = AdapterBrands(this, listItems, this)
        recyclerViewBrands.adapter = adapterBrands
        Items()
        customActionBar()




        binding.btnNewProduct.setOnClickListener{
            val dialog = ManageBrandFragment(listener)
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
                if (adapterBrands.search(query)) {
                    binding.textInfo.text= "Nenhum resultado encontrado."
                } else {
                    binding.textInfo.text =""
                    listItems = adapterBrands.setDataChanged(query)
                }
                return true

            }

            fun onQueryTextCleared(): Boolean {
                Log.d("SimpleSearchView", "Text cleared")
                return true
            }


        })
    }

    private fun Items(){

        db.collection("brands").orderBy("cName")
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
                            listItems.add(dc.document.toObject(Brand::class.java))
                        }
                    }
                    adapterBrands.notifyDataSetChanged()
                    adapterBrands.clearSearch()
                    binding.textInfo.text=""

                }
            })
    }


    private fun customActionBar() {
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Lista de Fornecedores"
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
        val product = listItems[position].id

        val args = Bundle()
        args.putString(ManageBrandFragment.ITEM_ID, product.toString())

        adapterBrands.notifyDataSetChanged()

        //Log.d("IDPRODUCT", args.toString())

        val dialog = ManageBrandFragment(listener)
        dialog.show(supportFragmentManager, dialog.tag)
        dialog.setArguments(args)
        //dialog.cancelDialog()
        //dialog.setCancelable(false)
    }

    override fun editBrand() {
        adapterBrands.notifyDataSetChanged()
        val intent = Intent(this, ListBrandsActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}