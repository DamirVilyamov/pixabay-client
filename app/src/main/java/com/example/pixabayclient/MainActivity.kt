package com.example.pixabayclient

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
    private val NUM_COLUMNS: Int = 2;
    lateinit var searchBar: androidx.appcompat.widget.SearchView

    private val KEY = "17058701-b7d71434149dd47c53775f272"
    private val BASE_URL = "https://pixabay.com/api/"
    private val imageType = "photo"
    lateinit var searchQuery: String
    var names: ArrayList<String> = ArrayList()
    var imageUrls: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchBar = search_bar
        searchBar.setOnSearchClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                searchQuery = searchBar.query.toString()
                var retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                var apiService: PixabayApiService = retrofit.create(PixabayApiService::class.java)
                var call: Call<List<Post?>?>? = apiService.getPosts(KEY,searchQuery,imageType)


                call!!.enqueue(object : Callback<List<Post?>?> {
                    override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                        authorName.text = t.message
                    }


                    override fun onResponse(
                        call: Call<List<Post?>?>,
                        response: Response<List<Post?>?>
                    ) {
                        if (!response.isSuccessful) {
                            authorName.text = "code" + response.code()
                            return
                        }
                        val posts: List<Post>? = response.body() as List<Post>?
                        if (posts != null) {
                            for (post:Post in posts){
                                names.add(post.user)
                                imageUrls.add(post.imageURL)
                            }
                        }
                    }

                })
                initRecyclerView()
            }
        })

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
        val staggeredGridLayoutManager:StaggeredGridLayoutManager = StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayout.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = staggeredRecyclerViewAdapter
    }

}



