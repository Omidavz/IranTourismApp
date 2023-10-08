package com.omidavz.irantourismapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omidavz.irantourismapp.databinding.ActivityAddContentBinding
import com.omidavz.irantourismapp.databinding.ActivityContentListBinding
import kotlinx.coroutines.delay
import java.util.Date
import java.util.HashMap

class AddContentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddContentBinding

    // Credentials
    var currentUserId : String = ""
    var currentUserName : String = ""

    // Firebase
    lateinit var auth : FirebaseAuth ;
    lateinit var user: FirebaseUser

    // Firebase Firestore
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var storageReference: StorageReference

    var collectionReference : CollectionReference = db.collection("Content")
    lateinit var imageUri : Uri

    private var allowPost = 1 ; // if  1 = allow , 2 = denied


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_content)

        imageUri = Uri.EMPTY

        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth

        binding.apply {
            allowPost = 1

            postProgressBar.visibility = View.INVISIBLE

            if (ContentUser.instance != null){
                currentUserId =auth.currentUser?.uid.toString()
                currentUserName = auth.currentUser?.displayName.toString()

                postUsernameTextview.text = currentUserName

            }

            // Getting image from gallery
            postCameraButton.setOnClickListener(){
                var i : Intent = Intent(Intent.ACTION_GET_CONTENT)
                i.type = "image/*"
                startActivityForResult(i,1)
            }

            postSaveContentButton.setOnClickListener(){
                if (imageUri == Uri.EMPTY){
                    Toast.makeText(this@AddContentActivity,"Please Upload an Image", Toast.LENGTH_SHORT).show()
                }
                if (allowPost == 1){
                    Toast.makeText(this@AddContentActivity,"Your Post Will Publish in Few Second", Toast.LENGTH_SHORT).show()
                    saveContent()
                }else{
                    Toast.makeText(this@AddContentActivity,"Please wait", Toast.LENGTH_SHORT).show()
                }
            }



        }


    }

    private fun saveContent() {
        var title : String = binding.postTitleEt.text.toString().trim()
        var description : String = binding.postDescriptionEt.text.toString().trim()

        allowPost = 2

        binding.postProgressBar.visibility = View.VISIBLE

        if ((!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null )){

            val filePath : StorageReference = storageReference.child("content_images")
                .child("my_image_" + Timestamp.now().seconds)

            // Uploading the images

            filePath.putFile(imageUri)
                .addOnSuccessListener {
                    filePath.downloadUrl.addOnSuccessListener {
                        var imageUri : String = it.toString()
                        var timestamp = Timestamp(Date())


                        // Creating he object of Content
                        var content = Content(
                            title,
                            description,
                            imageUri,
                            currentUserId,
                            timestamp,
                            currentUserName
                        )

                        // Adding the new Content
                        collectionReference.add(content)
                            .addOnSuccessListener {
                                allowPost=1
                                binding.postProgressBar.visibility = View.INVISIBLE
                                var i = Intent(this , ContentList::class.java)
                                startActivity(i)
                                finish()
                            }

                    }

                }.addOnFailureListener(){
                    binding.postProgressBar.visibility = View.INVISIBLE
                    allowPost  = 1

                }
        }else{
            binding.postProgressBar.visibility = View.INVISIBLE
            Toast.makeText(this,"Please Enter Name and Description ",Toast.LENGTH_SHORT).show()



        }






    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            if (data != null ){
                imageUri = data.data!! // getting the actioal image path
                binding.postImageView.setImageURI(imageUri) // showing the image
            }

        }


    }

    override fun onStart() {
        super.onStart()
        user =auth.currentUser!!
    }

    override fun onStop() {
        super.onStop()
        if(user!= null){

        }
    }
}