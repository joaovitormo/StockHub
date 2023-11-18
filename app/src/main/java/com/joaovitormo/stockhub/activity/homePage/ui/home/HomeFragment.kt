package com.joaovitormo.stockhub.activity.homePage.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitormo.stockhub.activity.listProducts.ListProductsActivity
import com.joaovitormo.stockhub.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val txtCountItens = binding.txtCountProducts
        val txtSumItens = binding.txtSumProducts

        getCountProducts()

        getSumProducts()


        val btListProducts: Button = binding.btProductList
        btListProducts.setOnClickListener {
            listProducts()
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun listProducts() {
        val intent = Intent(activity, ListProductsActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    fun getCountProducts() {
        var totalCountItens : String = "0"
        var count = 0
        db.collection("products")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        count++
                    }
                } else {
                    Log.d("error", "Error getting documents: ", task.exception)
                }
                binding.txtCountProducts.setText(count.toString())
            }
    }

    fun getSumProducts() {
        var total: Long = 0
        var count : Long = 0
        db.collection("products")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        if (document.getLong("nAmount") != null) {
                            //count++
                            //count =
                            //total + count
                            count = document.getLong("nAmount")!!
                            total += count
                            //Log.d("count",  document.getLong("nAmount").toString())
                            }
                    }
                } else {
                    Log.d("error", "Error getting documents: ", task.exception)
                }
                Log.d("count",  total.toString())
                binding.txtSumProducts.setText(total.toString())
            }
    }

}