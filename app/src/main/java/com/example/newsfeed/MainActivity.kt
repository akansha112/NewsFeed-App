package com.example.newsfeed

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var myAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView);
        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        myAdapter=NewsListAdapter(this)
        recyclerView.adapter=myAdapter
    }


    fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=474b16e7da9043a481422b4de63146cc"
        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                Log.e("sdsadas","$it")
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until  newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                   // Toast.makeText(this,"Entered",Toast.LENGTH_LONG).show()
                    newsArray.add(news)
                }
                myAdapter.updateNews(newsArray)
            },
            Response.ErrorListener { error ->

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        queue.add(getRequest)
    }

  override  fun onItemClicked(item:News){
        val builder=CustomTabsIntent.Builder()
        val customTabsIntent=builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
    }
/*
private fun fetchData(){
        val url="https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=474b16e7da9043a481422b4de63146cc"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
          Response.Listener  {
                val newsJsonArray=it.getJSONArray("articles")
                val newsArray =ArrayList<News>()
                for (i in 0 until newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                myAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {  }

        ) {
            @Throws(AuthFailureError::class)
            fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        MySingleton.getInstance(this).addToRequest(jsonObjectRequest)
    }

*/