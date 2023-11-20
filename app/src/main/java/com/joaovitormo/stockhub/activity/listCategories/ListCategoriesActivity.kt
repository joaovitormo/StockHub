package com.joaovitormo.stockhub.activity.listCategories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.R
import com.joaovitormo.stockhub.activity.homePage.HomePageActivity
import com.joaovitormo.stockhub.activity.listBrands.ListBrandsActivity
import com.joaovitormo.stockhub.activity.manageBrand.ManageBrandFragment
import com.joaovitormo.stockhub.activity.manageBrand.ManageCategoryFragment
import com.joaovitormo.stockhub.adapter.AdapterItems
import com.joaovitormo.stockhub.databinding.ActivityListBrandsBinding
import com.joaovitormo.stockhub.databinding.ActivityListCategoriesBinding
import com.joaovitormo.stockhub.model.Brand

class ListCategoriesActivity : AppCompatActivity(), AdapterItems.RecyclerViewEvent, ManageCategoryFragment.ManageCategoryDialogListener  {
    private lateinit var binding: ActivityListCategoriesBinding
    private lateinit var adapterItems: AdapterItems
    private var listItems: MutableList<Brand> = mutableListOf()
    lateinit var listener: ManageCategoryFragment.ManageCategoryDialogListener

    //db
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this

        binding = ActivityListCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchView()
        val recyclerViewItems = binding.recyclerViewItems
        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        recyclerViewItems.setHasFixedSize(true)
        adapterItems = AdapterItems(this, listItems, this)
        recyclerViewItems.adapter = adapterItems
        Items()
        customActionBar()




        binding.btnNewItem.setOnClickListener{
            val dialog = ManageCategoryFragment(listener)
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
                if (adapterItems.search(query)) {
                    binding.textInfo.text= "Nenhum resultado encontrado."
                } else {
                    binding.textInfo.text =""
                    listItems = adapterItems.setDataChanged(query)
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

        db.collection("categories").orderBy("cName")
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
                    adapterItems.notifyDataSetChanged()
                    adapterItems.clearSearch()
                    binding.textInfo.text=""

                }
            })
    }


    private fun customActionBar() {
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Lista de Categorias"
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
        args.putString(ManageCategoryFragment.ITEM_ID, product.toString())

        adapterItems.notifyDataSetChanged()

        //Log.d("IDPRODUCT", args.toString())

        val dialog = ManageCategoryFragment(listener)
        dialog.show(supportFragmentManager, dialog.tag)
        dialog.setArguments(args)
        //dialog.cancelDialog()
        //dialog.setCancelable(false)
    }

    override fun editItem() {
        adapterItems.notifyDataSetChanged()
        val intent = Intent(this, ListCategoriesActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}