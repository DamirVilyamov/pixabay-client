package com.example.pixabayclient


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class StaggeredRecyclerViewAdapter(
    var context: Context,
    var names: ArrayList<String>,
    var mImageUrls: ArrayList<String>
) : RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder>() {
    private val TAG = "StaggeredRecyclerViewAd"

    class ViewHolder(var itemView: View, val image: ImageView, val name: TextView) :
        RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_grid_item, parent, false)
        return ViewHolder(
            view,
            view.findViewById(R.id.imageWidget),
            view.findViewById(R.id.authorName)
        )
    }
    override fun getItemCount(): Int {
        return mImageUrls.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")
        val requestOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
        Glide.with(context).load(mImageUrls[position]).apply(requestOptions).into(holder.image)
        holder.name.text = names[position]
        holder.image.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "onClick: clicked on" + names[position])
                Toast.makeText(context, names[position], Toast.LENGTH_SHORT).show()
            }
        })
    }
}