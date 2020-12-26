package com.example.hischool

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_content_view.view.*


class DataRecyclerAdapter(private val files: ArrayList<File>, private val context: Context):
    RecyclerView.Adapter<DataRecyclerAdapter.DataViewHolder>(){
    class DataViewHolder(val view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_content_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.view.titleTextView4.text = files[position].name
        holder.view.typeTextView5.text = files[position].type
        holder.view.shareBtn.setOnClickListener {
            Toast.makeText(context,"this functionally is not add at the moment but download works",Toast.LENGTH_SHORT).show()
        }
        holder.view.downloadBtn.setOnClickListener {
            val intent = Intent(context,Browser::class.java)
            intent.putExtra("url",files[position].url)
            intent.putExtra("type",files[position].type)
            startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int = files.size


}