package com.omidavz.irantourismapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.omidavz.irantourismapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        auth = Firebase.auth


        binding.accSignUpButton.setOnClickListener(){
            createUser()

        }
    }
    private fun createUser() {

        val email = binding.emailCreate.text.toString()
        val password = binding.passwordCreate.text.toString()

        
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGY", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this , ContentList::class.java)
                    startActivity(intent)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGY", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }.addOnFailureListener(){
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {


    }
}

