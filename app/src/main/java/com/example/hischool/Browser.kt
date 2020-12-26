package com.example.hischool

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_browser.*


class Browser : AppCompatActivity() {
    private lateinit var uri: Uri
    private var data:String? = null
    private var type : String? = null;
    val list: ArrayList<Long> = ArrayList()
    private lateinit var downloadManager: DownloadManager
    private val REQUEST_CODE = 1


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        data = intent.getStringExtra("url")
        val url = data!!.subSequence(33, data!!.length - 2).toString().trim()
        uri = Uri.parse(url)
        type = intent.getStringExtra("type")
        if(type != null){
            type = type!!.subSequence(0, type!!.indexOf("/")).toString().trim()
            Toast.makeText(this, type, Toast.LENGTH_SHORT).show()
            if(type == "image" || type == "video"){
                openBtn.visibility = View.VISIBLE
            }else{
                openBtn.visibility = View.INVISIBLE
            }
        }

        registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_CODE
        )
    }

    fun openClick(view : View){
        if(type == "image"){
            val intent = Intent(applicationContext,BrowserImageView::class.java)
            intent.putExtra("url",uri.toString())
            startActivity(intent)
        }else if(type == "video"){
            val intent = Intent(applicationContext,BrowserVideoView::class.java)
            intent.putExtra("url",uri.toString())
            startActivity(intent)
        }
    }

    fun buttonClick(view : View) {
        downloadData()
    }

    private fun downloadData() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val filename: String = URLUtil.guessFileName(data, null, MimeTypeMap.getFileExtensionFromUrl(data))
        val request = DownloadManager.Request(uri)
        request.setTitle(filename)
        request.setDescription("hiSchool file download")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
       Toast.makeText(this,"xxxx",Toast.LENGTH_LONG).show()
        val refid = downloadManager.enqueue(request)
        list.add(refid)
        downloadStatusTextView.text = "file is downloading"
    }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {

            // get the refid from the download manager
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            // remove it from our list
            list.remove(referenceId)

            // if list is empty means all downloads completed
            if (list.isEmpty()) {
                // show a notification
                Log.e("INSIDE", "" + referenceId)
                val mBuilder = NotificationCompat.Builder(this@Browser)
                    .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                    .setContentTitle("GadgetSaint")
                    .setContentText("All Download completed")
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(455, mBuilder.build())
            }
        }
    }
}
