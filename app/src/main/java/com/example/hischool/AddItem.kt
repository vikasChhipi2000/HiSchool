package com.example.hischool

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_item.*
import java.io.File

class AddItem : AppCompatActivity() {

    private lateinit var filePath : Uri
    private var fileselected = false
    private var public = true
    private var uploadUrl : Uri? = null
    private val map: HashMap<String, String> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
    }

    fun shareMode(view: View){
        val index = shareModeRadio.checkedRadioButtonId
        public = index != R.id.privateRadioButton
    }

    fun chooseFile(view: View){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
            filePath = data.data!!
            filePathTextView.text = "file is selected"
            fileselected = true
        }else{
            fileselected = false
            filePathTextView.text = "file is not selected"
        }
    }

    fun uploadFile(view: View){
        if(titleEditText.text.toString().isEmpty()){
            titleEditText.error = "enter the name or topic"
            titleEditText.requestFocus()
            return
        }
        if(fileselected){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()
            val storageRef = FirebaseStorage.getInstance().reference
            val riversRef = storageRef.child("usersFiles/${File(filePath.path!!).name}")
            val uploadTask = riversRef.putFile(filePath)

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                map["type"] = taskSnapshot.metadata?.contentType.toString()
                map["name"] = titleEditText.text.toString()
                map["from"] = FirebaseAuth.getInstance().currentUser!!.uid
                pd.dismiss()
                Toast.makeText(this, "file is uploaded", Toast.LENGTH_LONG).show()
                finish()
            }.addOnProgressListener {
                var progress = (100 * (it.bytesTransferred.toDouble() / it.totalByteCount.toDouble())).toInt()
                pd.setMessage("uploaded ${progress}%")
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                riversRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadUrl = task.result
                    map["url"] = uploadUrl.toString()
                    val ref =  FirebaseDatabase.getInstance().reference
                    if(public){
                        ref.child("public").push().setValue(map)
                    }else{
                        ref.child(FirebaseAuth.getInstance().currentUser?.uid!!).child("private").push().setValue(map)
                    }
                } else {
                    Toast.makeText(this, "upload is failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}