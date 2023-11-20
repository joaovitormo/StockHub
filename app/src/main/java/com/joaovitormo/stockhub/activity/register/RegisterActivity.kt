package com.joaovitormo.stockhub.activity.register

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitormo.stockhub.R
import com.joaovitormo.stockhub.activity.login.LoginActivity
import com.joaovitormo.stockhub.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Remove ActionBar
        supportActionBar?.hide()

        binding.btCadastrar.setOnClickListener {
            //Close keyboard after click
            binding.editEmail.onEditorAction(EditorInfo.IME_ACTION_DONE)
            binding.editSenha.onEditorAction(EditorInfo.IME_ACTION_DONE)

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            val nome = binding.editName.text.toString()

            if(email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else{
                auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener { cadastro ->
                    if (cadastro.isSuccessful) {
                        val snackbar = Snackbar.make(it, "Sucesso ao cadastrar usuário!", Snackbar.LENGTH_SHORT)
                        //snackbar.setBackgroundTint(Color.GREEN)
                        snackbar.show()
                        saveUser(nome, email)
                        binding.editEmail.setText("")
                        binding.editSenha.setText("")

                        navToLogin()

                    }
                }.addOnFailureListener { exception ->
                    val mensagemErro = when(exception){
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Digite um e-mail válido!"
                        is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada!"
                        is FirebaseNetworkException -> "Sem conexão com a internet!"
                        else -> "Erro ao cadastrar usuário!"

                    }
                    val snackbar = Snackbar.make(it, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }
            }

        }


    }

    private fun navToLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)


    }
    fun saveUser(cName: String, cEmail: String) {
        val documentPath: String = java.time.Instant.now().toString().replace(":", "").replace(".","").replace("-","")
        Log.d("db_save", documentPath)
        db.collection("users").document(documentPath)
            .set(mapOf("cName" to cName,
                "cEmail" to cEmail,
                "id" to documentPath

            )).addOnCompleteListener {
                Log.d("db_save", "Sucesso ao criar novo usuário!")
            }
    }
}