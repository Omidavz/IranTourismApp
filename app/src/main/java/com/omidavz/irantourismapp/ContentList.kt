package com.omidavz.irantourismapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.omidavz.irantourismapp.databinding.ActivityContentListBinding

class ContentList : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener {
    lateinit var binding: ActivityContentListBinding

    // Firebase References
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var user: FirebaseUser
    var db = FirebaseFirestore.getInstance()
    lateinit var storageReference: StorageReference
    var collectionReference: CollectionReference = db.collection("Content")

    lateinit var contentList: MutableList<Content>
    lateinit var adapter: ContentAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content_list)


        // Firebase Auth
        firebaseAuth = Firebase.auth
        user = firebaseAuth.currentUser!!

        // RecyclerView & Swipe Refresh layout
        binding.apply {

            // RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@ContentList)

            // Swipe Refresh Layout
            swipy.isRefreshing = true
            swipy.setColorSchemeResources(R.color.orange,R.color.blue,R.color.green)
            swipy.setOnRefreshListener(this@ContentList)



        }



        // Posts arraylist
        contentList = arrayListOf<Content>()

        // Load Contents
        loadContents()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_add -> {
                val intent = Intent(this, AddContentActivity::class.java)
                startActivity(intent)
            }

            R.id.action_signout -> {
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadContents(){
        collectionReference
            .orderBy("timeAdded",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {

                    for (document in it) {
                        var content = Content(
                            document.data["title"].toString(),
                            document.data["description"].toString(),
                            document.data["imageUrl"].toString(),
                            document.data["userId"].toString(),
                            document.data["timeAdded"] as Timestamp,
                            document.data["username"].toString()
                        )
                        contentList.add(content)


                    }




                    // RecyclerView
                    adapter = ContentAdapter(this, contentList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()




                }else{
                    binding.listNoPosts.visibility = View.VISIBLE
                }
                binding.swipy.isRefreshing = false
            }.addOnFailureListener(){
                Toast.makeText(this,"Opps! Something went wrong",Toast.LENGTH_SHORT).show()

            }

    }

    override fun onRefresh() {
        contentList.clear()
        loadContents()
    }
}


