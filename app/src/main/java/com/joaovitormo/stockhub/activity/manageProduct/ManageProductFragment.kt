package com.joaovitormo.stockhub.activity.manageProduct

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.databinding.FragmentManageProductBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ManageProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageProductFragment(listener: ManageProductDialogListener) : BottomSheetDialogFragment() {

    private var mManageProductFragment : ManageProductDialogListener?=null
    init {
        this.mManageProductFragment = listener
    }


    private lateinit var binding: FragmentManageProductBinding

    private val db = FirebaseFirestore.getInstance()

    var spinnerListCategoryItens: MutableList<String> = ArrayList()

    val spinnerListStockPositionItens: MutableList<String> = ArrayList()

    var spinnerListBrandItens: MutableList<String> = ArrayList()



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var productID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            productID = it.getString(PRODUCT_ID, null)
        }

        Log.d("IDPRODUCT", productID.toString())


        //productID?.let { findProduct(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageProductBinding.inflate(inflater,container,false)
        //return inflater.inflate(R.layout.fragment_manage_product, container, false)

        getBrands()
        getCategories()
        getStockPositions()
        productID?.let { findProduct(it) }



        binding.saveButton.setOnClickListener {
            val cName = binding.name.text.toString()
            val cCategory = binding.spinnerCategory.selectedItem.toString()
            val cBrand = binding.spinnerBrand.selectedItem.toString()
            val cStock = binding.spinnerStockPosition.selectedItem.toString()
            val nAmount = binding.editAmount.text.toString().toInt()

            Log.d("VALUES", nAmount.toString())
            Log.d("VALUES", cStock.toString())
            Log.d("VALUES", cCategory.toString())
            Log.d("VALUES", cBrand.toString())


            if(productID == null){
                Log.d("db_save", "save")
                saveNewProduct(cName, cCategory, cBrand, cStock, nAmount)
            }

            productID?.let { it1 -> updateProduct(it1, cName, cCategory, cBrand, cStock, nAmount) }
            //getActivity()?.onBackPressed()
            //dismiss()

            mManageProductFragment?.editProduct()

        }

        val spinnerBrand: Spinner = binding.spinnerBrand


        /*val adapterBrand: ArrayAdapter<String> = ArrayAdapter<String>(
            activity?.applicationContext!!,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerListBrandItens
        )
        spinnerBrand.setAdapter(adapterBrand)
        //adapterBrand.add("TESTE ")
        adapterBrand.toString()*/

        val spinnerCategory: Spinner = binding.spinnerCategory


        val spinnerStock: Spinner = binding.spinnerStockPosition




        //spinnerCategory?.adapter = ArrayAdapter(activity?.applicationContext!!, android.R.layout.simple_spinner_dropdown_item, getBrands2())
        //spinnerBrand?.adapter = ArrayAdapter(activity?.applicationContext!!, android.R.layout.simple_spinner_dropdown_item, spinnerListBrandItens)
        //spinnerStock?.adapter = ArrayAdapter(activity?.applicationContext!!, android.R.layout.simple_spinner_dropdown_item, spinnerListStockPositionItens)


        return binding.root
    }

    fun updateProduct(productID: String, cName: String, cCategory: String, cBrand: String, cStockPosition: String, nAmount: Int){

        db.collection("products").document(productID)
            .update(mapOf("cName" to cName,
                "cCategory" to cCategory,
                "cBrand" to cBrand,
                "cStockPosition" to cStockPosition,
                "nAmount" to nAmount
                )).addOnCompleteListener {
                Log.d("db_update", "Sucesso ao atualizar os dados do produto!")

                Toast.makeText(
                    activity?.applicationContext!!,
                    "Sucesso ao atualizar os dados do produto!",
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()

            }
    }

    fun saveNewProduct(cName: String, cCategory: String, cBrand: String, cStockPosition: String, nAmount: Int){

        val documentPath: String = java.time.Instant.now().toString().replace(":", "").replace(".","").replace("-","")
        Log.d("db_save", documentPath)
        db.collection("products").document(documentPath)
            .set(mapOf("cName" to cName,
                "cCategory" to cCategory,
                "cBrand" to cBrand,
                "cStockPosition" to cStockPosition,
                "nAmount" to nAmount,
                "id" to documentPath

            )).addOnCompleteListener {
                Log.d("db_save", "Sucesso ao criar novo produto!")

                Toast.makeText(
                    activity?.applicationContext!!,
                    "Sucesso ao criar novo produto!",
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }
    }

    fun findProduct(idProduct: String){
        db.collection("products").document(idProduct)
            .addSnapshotListener { documento, error ->
                if (documento != null) {

                    var categorySelected = 0
                    fun getCategory() {
                        val category = documento.getString("cCategory")

                        var i=0
                        for ( item in spinnerListCategoryItens)
                        {

                            if(spinnerListCategoryItens.get(i).equals(category)){
                                categorySelected = i
                                break
                            } else{
                                categorySelected = 0;
                            }
                            i++
                        }
                    }
                    var brandSelected = 0
                    fun getBrand() {
                        val brand = documento.getString("cBrand")

                        var i=0
                        for ( item in spinnerListBrandItens)
                        {
                            if(spinnerListBrandItens.get(i).equals(brand)){
                                brandSelected = i
                                break
                            } else{
                                brandSelected = 0;
                            }
                            i++
                        }
                    }
                    var stockPositionSelected = 0
                    fun getStockPosition() {
                        val stock = documento.getString("cStockPosition")
                        var i=0
                        for ( item in spinnerListStockPositionItens)
                        {
                            if(spinnerListStockPositionItens.get(i).equals(stock.toString())){
                                stockPositionSelected = i
                                break
                            } else{
                                stockPositionSelected = 0;
                            }
                            i++
                        }
                    }

                    getCategory()
                    getBrand()
                    getStockPosition()

                    binding.name.setText(documento.getString("cName"))
                    val nAmount = documento.getLong("nAmount")
                    binding.editAmount.setText(nAmount.toString())
                    binding.spinnerCategory.setSelection(categorySelected)
                    binding.spinnerBrand.setSelection(brandSelected)
                    binding.spinnerStockPosition.setSelection(stockPositionSelected)

                }
            }
    }

    private fun getBrands() {
        val spinnerBrand: Spinner = binding.spinnerBrand

        val adapterBrand: ArrayAdapter<String> = ArrayAdapter<String>(
            activity?.applicationContext!!,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerListBrandItens
        )
        spinnerBrand.setAdapter(adapterBrand)

        adapterBrand.toString()
        db.collection("brands").orderBy("cName")
            .addSnapshotListener(object  : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            Log.e("brands", dc.document.id)
                            //adapterBrand.add("TESTE ")
                            spinnerListBrandItens.add(dc.document.getString("cName").toString())
                            adapterBrand.notifyDataSetChanged()
                            Log.e("brandsSubjects", spinnerListBrandItens.toString())
                        }
                    }
                }
            })
    }

    private fun getCategories() {
        val spinnerCategory: Spinner = binding.spinnerCategory

        val adapterCategory: ArrayAdapter<String> = ArrayAdapter<String>(
            activity?.applicationContext!!,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerListCategoryItens
        )
        spinnerCategory.setAdapter(adapterCategory)
        db.collection("categories").orderBy("cName")
            .addSnapshotListener(object  : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            Log.e("categories", dc.document.id)
                            spinnerListCategoryItens.add(dc.document.getString("cName").toString())
                            adapterCategory.notifyDataSetChanged()
                        }
                    }
                }
            })
    }
    private fun getStockPositions() {
        val spinnerStock: Spinner = binding.spinnerStockPosition

        val adapterStock: ArrayAdapter<String> = ArrayAdapter<String>(
            activity?.applicationContext!!,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerListStockPositionItens
        )
        spinnerStock.setAdapter(adapterStock)

        db.collection("stockPositions").orderBy("cName")
            .addSnapshotListener(object  : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            Log.e("stockPositions", dc.document.id)
                            spinnerListStockPositionItens.add(dc.document.getString("cName").toString())
                            adapterStock.notifyDataSetChanged()
                        }
                    }
                }
            })
    }


    companion object {
        const val PRODUCT_ID = "PRODUCT_ID"


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManageProductFragment.

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
         */
    }

    interface ManageProductDialogListener {
        fun editProduct()
    }

}