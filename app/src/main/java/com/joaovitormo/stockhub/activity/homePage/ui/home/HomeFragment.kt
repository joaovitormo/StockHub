package com.joaovitormo.stockhub.activity.homePage.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.joaovitormo.stockhub.activity.homePage.HomePageActivity
import com.joaovitormo.stockhub.activity.listProducts.ListProductsActivity
import com.joaovitormo.stockhub.activity.login.LoginActivity
import com.joaovitormo.stockhub.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        val btLogout: Button = binding.btLogout
        btLogout.setOnClickListener {
            auth.signOut()
            backToLogin()
        }
        */


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

    fun backToLogin() {

        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun listProducts() {
        val intent = Intent(activity, ListProductsActivity::class.java)
        startActivity(intent)
    }



}