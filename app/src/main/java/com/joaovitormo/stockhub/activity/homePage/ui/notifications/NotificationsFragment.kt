package com.joaovitormo.stockhub.activity.homePage.ui.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitormo.stockhub.activity.login.LoginActivity
import com.joaovitormo.stockhub.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

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
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val txtHelloName = binding.txtHelloName

        val btLogout: Button = binding.btLogout

        txtHelloName.setText("Olá, ")

        Log.d("user", auth.currentUser?.email.toString())
        Log.d("user", auth.currentUser?.displayName.toString())
        getUserName()

        btLogout.setOnClickListener {

            auth.signOut()
            backToLogin()
        }

        return root
    }

    fun backToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    fun getUserName() {
        var cNameUser = ""
        db.collection("users")
            .whereEqualTo("cEmail", auth.currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    cNameUser= document.get("cName") as String
                    Log.d("user", "${document.id} => ${document.data}")
                    binding.txtHelloName.setText("Olá, $cNameUser")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

    }
}