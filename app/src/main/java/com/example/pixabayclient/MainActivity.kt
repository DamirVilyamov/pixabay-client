package com.example.pixabayclient

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE

import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pixabayclient.retrofit.Hit
import com.example.pixabayclient.retrofit.PixabayApiService
import com.example.pixabayclient.retrofit.Post
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_grid_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val NUM_COLUMNS: Int = 2
    lateinit var searchBar: androidx.appcompat.widget.SearchView

    private val KEY: String = "17058701-b7d71434149dd47c53775f272"
    private val BASE_URL: String = "https://pixabay.com/"
    private val imageType: String = "photo"
    private lateinit var searchQuery: String
    var names: ArrayList<String> = ArrayList()
    var imageUrls: ArrayList<String> = ArrayList()
    lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchBar = search_bar
        errorText = errorTextView

        /*searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
        androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            searchQuery = query.toString()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            TODO("Not yet implemented")
        }

    })*/

        searchQuery = "kittens"
        var retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        var apiService: PixabayApiService = retrofit.create(PixabayApiService::class.java)
        var call: Call<Post?> = apiService.getPosts(KEY, searchQuery, imageType)


        call.enqueue(object : Callback<Post?> {
            override fun onFailure(call: Call<Post?>, t: Throwable) {
                errorText.visibility = VISIBLE
                errorText.text = t.message
            }


            override fun onResponse(
                call: Call<Post?>,
                response: Response<Post?>
            ) {
                if (!response.isSuccessful) {
                    errorText.visibility = VISIBLE
                    errorText.text = "code" + response.code()
                    return
                }
                val post: Post? = response.body()
                var hitList: List<Hit>? = post?.hits
                if (hitList != null) {
                    for (hit in hitList) {
                        names.add(hit.user.toString())
                        imageUrls.add(hit.previewURL.toString())
                    }
                }
            }

        })
        initRecyclerView()

    }
    private fun initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview")
        val recyclerView: RecyclerView = findViewById(R.id.search_recycler_view)
        val staggeredRecyclerViewAdapter: StaggeredRecyclerViewAdapter =
            StaggeredRecyclerViewAdapter(this, names, imageUrls)
        val staggeredGridLayoutManager: StaggeredGridLayoutManager =
            StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayout.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = staggeredRecyclerViewAdapter
    }

}



