package com.joaovitormo.stockhub.activity.manageProduct

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.*
import com.joaovitormo.stockhub.R
import com.joaovitormo.stockhub.activity.homePage.HomePageActivity
import com.joaovitormo.stockhub.activity.listProducts.ListProductsActivity
import com.joaovitormo.stockhub.activity.login.LoginActivity
import com.joaovitormo.stockhub.databinding.FragmentManageProductBinding
import com.joaovitormo.stockhub.model.Product
import kotlinx.coroutines.NonDisposableHandle.parent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ManageProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageProductFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentManageProductBinding

    private val db = FirebaseFirestore.getInstance()

    val spinnerListCategoryItens: List<String> = listOf(
        "Madeira", "Tintas", "Cimento", "Ferragens",
        "Telhas","Ferramentas Elétricas","Encanamento",
        "Iluminação", "Revestimentos","Isolamento",
        "Vidros","Isolamento Térmico","Máquinas de Construção",
        "Sistemas de Segurança", "Portas e Janelas"
    )

    val spinnerListBrandItens: List<String> = listOf(
        "Eucatex", "Duratex", "Sherwin-Williams", "Behr",
        "Cimpor", "Votorantim","Tramontina", "Stanley",
        "Eternit", "Brasilit","Bosch", "Makita",
        "Tigre", "Amanco","Philips", "Osram",
        "Portobello", "Eliane","Isover", "Rockwool",
        "Saint-Gobain", "Cebrace","Caterpillar", "Bobcat",
        "Hikvision", "Honeywell","Pado", "Sasazaki"
    )

    val spinnerListStockPositionItens: List<String> = listOf(
        "A001", "A002", "A003", "A004", "A005"
    )


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


        productID?.let { findProduct(it) }


/*
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
                            spinnerList.add(dc.document.getString("cName").toString())
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            })
*/

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageProductBinding.inflate(inflater,container,false)
        //return inflater.inflate(R.layout.fragment_manage_product, container, false)


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


            productID?.let { it1 -> updateProduct(it1, cName, cCategory, cBrand, cStock, nAmount) }
            //getActivity()?.onBackPressed()
            //dismiss()
        }

        val spinnerCategory = binding.spinnerCategory
        val spinnerBrand = binding.spinnerBrand
        val spinnerStock = binding.spinnerStockPosition



        //spinner?.adapter =ArrayAdapter.createFromResource(getActivity(), spinnerList, android.R.layout.simple_spinner_item)

        spinnerCategory?.adapter = ArrayAdapter(activity?.applicationContext!!, android.R.layout.simple_spinner_dropdown_item, spinnerListCategoryItens)
        spinnerBrand?.adapter = ArrayAdapter(activity?.applicationContext!!, android.R.layout.simple_spinner_dropdown_item, spinnerListBrandItens)
        spinnerStock?.adapter = ArrayAdapter(activity?.applicationContext!!, android.R.layout.simple_spinner_dropdown_item, spinnerListStockPositionItens)


        return binding.root
    }

    fun updateProduct(productID: String, cName: String, cCategory: String, cBrand: String, cStockPosition: String, nAmount: Int){
        /*
        db.collection("products").document("rYROCQza8NxtWgfpUpc2")
            .update("cName", cName,
                "cCategory", "cCategory",
                "cBrand", "cBrand",
                "cStockPosition", "cStockPosition",
                "nAmount", nAmount
            ).addOnCompleteListener {
                Log.d("db_update", "Sucesso ao atualizar os dados do produto!")

                Toast.makeText(
                    activity?.applicationContext!!,
                    "Sucesso ao atualizar os dados do produto!",
                    Toast.LENGTH_SHORT
                ).show()
            }

         */

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

    companion object {
        const val PRODUCT_ID = "PRODUCT_ID"


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManageProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}