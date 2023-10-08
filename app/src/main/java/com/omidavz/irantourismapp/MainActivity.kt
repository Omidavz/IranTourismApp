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
import com.omidavz.irantourismapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    // Firebase Auth
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        // Create acc Btn
        binding.createAcctBTN.setOnClickListener(){
            val intent = Intent(this@MainActivity , SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.emailSignInButton.setOnClickListener(){
            loginWithEmailAndPassword(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        }





        // Auth ref
        auth = Firebase.auth

    }

    private fun loginWithEmailAndPassword(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){

                    // Sign in success
                    var content : ContentUser = ContentUser.instance!!
                    content.userId = auth.currentUser?.uid
                    content.username = auth.currentUser?.displayName

                    goToContentList()
                }else{
                    Toast.makeText(this,"Authentication Failed",Toast.LENGTH_LONG).show()
                }

            }

    }

    private fun goToContentList() {
        val intent = Intent(this , ContentList::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null ){
            var intent = Intent(this , ContentList::class.java)
            startActivity(intent)
        }

    }


}