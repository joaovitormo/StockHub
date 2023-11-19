package com.joaovitormo.stockhub.activity.manageBrand

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
//import com.joaovitormo.stockhub.activity.manageBrand.ARG_PARAM1
//import com.joaovitormo.stockhub.activity.manageBrand.ARG_PARAM2
import com.joaovitormo.stockhub.activity.manageProduct.ManageProductFragment
import com.joaovitormo.stockhub.databinding.FragmentManageBrandBinding
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
class ManageBrandFragment(listener: ManageBrandDialogListener) : BottomSheetDialogFragment() {

    private var mManageBrandFragment : ManageBrandDialogListener?=null
    init {
        this.mManageBrandFragment = listener
    }


    private lateinit var binding: FragmentManageBrandBinding

    private val db = FirebaseFirestore.getInstance()


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var itemID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            itemID = it.getString(ITEM_ID, null)
        }

        Log.d("IDPRODUCT", itemID.toString())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageBrandBinding.inflate(inflater,container,false)
        //return inflater.inflate(R.layout.fragment_manage_product, container, false)

        binding.deleteButton.setVisibility(View.INVISIBLE)
        itemID?.let { findProduct(it) }



        binding.saveButton.setOnClickListener {
            val cName = binding.name.text.toString()



            if(itemID == null){
                Log.d("db_save", "save")
                saveNewProduct(cName)
            }

            itemID?.let { it1 -> updateProduct(it1, cName) }


            mManageBrandFragment?.editBrand()

        }

        binding.deleteButton.setOnClickListener {

            itemID?.let { it1 -> deleteItem(it1) }

            mManageBrandFragment?.editBrand()

        }
        return binding.root
    }

    fun updateProduct(productID: String, cName: String){

        db.collection("brands").document(productID)
            .update(mapOf("cName" to cName
            )).addOnCompleteListener {
                Log.d("db_update", "Sucesso ao atualizar os dados do produto!")

//                Toast.makeText(
//                    activity?.applicationContext!!,
//                    "Sucesso ao atualizar os dados do produto!",
//                    Toast.LENGTH_SHORT
//                ).show()
                dismiss()

            }
    }

    fun saveNewProduct(cName: String){

        val documentPath: String = java.time.Instant.now().toString().replace(":", "").replace(".","").replace("-","")
        Log.d("db_save", documentPath)
        db.collection("brands").document(documentPath)
            .set(mapOf("cName" to cName,
                "id" to documentPath

            )).addOnCompleteListener {
                Log.d("db_save", "Sucesso ao criar novo fornecedor!")
                Toast.makeText(
                    activity?.applicationContext!!,
                    "Sucesso ao criar novo fornecedor!",
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }
    }
    fun deleteItem(productID: String){
        db.collection("brands").document(productID)
            .delete().addOnCompleteListener {
                Log.d("db_delete", "Sucesso ao deletar os dados do usuário!")
                Toast.makeText(
                    activity?.applicationContext!!,
                    "Sucesso ao deletar fornecedor!",
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }
    }

    fun findProduct(idProduct: String){
        binding.deleteButton.setVisibility(View.VISIBLE)
        db.collection("brands").document(idProduct)
            .addSnapshotListener { documento, error ->
                if (documento != null) {
                    binding.name.setText(documento.getString("cName"))
                }
            }
    }



    companion object {
        const val ITEM_ID = "ITEM_ID"


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

    interface ManageBrandDialogListener {
        fun editBrand()
    }

}