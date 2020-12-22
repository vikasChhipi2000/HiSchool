package com.example.hischool

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : AppCompatActivity() {

    val publicFiles :ArrayList<File> = ArrayList()
    lateinit var publicArrayAdapter : DataRecyclerAdapter
    val privateFiles : ArrayList<File> = ArrayList()
    lateinit var privateArrayAdapter : DataRecyclerAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logoutItem){
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext,SignUp::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        publicArrayAdapter = DataRecyclerAdapter(publicFiles,this)
        publicRecyclerView.layoutManager = LinearLayoutManager(this)
        publicRecyclerView.adapter = publicArrayAdapter

        privateArrayAdapter = DataRecyclerAdapter(privateFiles,this)
        privateRecyclerView.layoutManager = LinearLayoutManager(this)
        privateRecyclerView.adapter = privateArrayAdapter

        FirebaseDatabase.getInstance().reference
            .child("public")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    publicFiles.add(File(snapshot.child("name").value.toString(),
                        snapshot.child("type").value.toString(),
                        snapshot.child("url").toString(),
                        snapshot.child("from").toString()))
                    publicArrayAdapter.notifyDataSetChanged()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })

        FirebaseDatabase.getInstance().reference
            .child(FirebaseAuth.getInstance().currentUser?.uid!!).child("private")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    privateFiles.add(File(snapshot.child("name").value.toString(),
                        snapshot.child("type").value.toString(),
                        snapshot.child("url").toString(),
                        snapshot.child("from").toString()))
                    privateArrayAdapter.notifyDataSetChanged()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })


    }

    fun addItem(view: View){
        val intent = Intent(applicationContext, AddItem::class.java)
        startActivity(intent)
    }
}