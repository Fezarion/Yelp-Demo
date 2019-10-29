package com.stevenlee.yelpdemo.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.stevenlee.yelpdemo.R
import com.stevenlee.yelpdemo.network.models.YelpSearch
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Launcher Activity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var yelpViewModel: YelpViewModel
    private lateinit var yelpRecyclerAdapter: YelpRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init recycler adapter
        yelpRecyclerAdapter = YelpRecyclerAdapter()

        // Init recycler view
        main_recycler_view.adapter = yelpRecyclerAdapter
        main_recycler_view.layoutManager = LinearLayoutManager(this)

        // Init view model
        yelpViewModel = ViewModelProviders.of(this).get(YelpViewModel::class.java)
        yelpViewModel.getYelpSearchLiveData().observe(this,
            Observer<YelpSearch> {
                main_recycler_view.scrollToPosition(0)
                yelpRecyclerAdapter.setYelpSearch(it, main_search.query.toString())
            })

        // Init search view
        main_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                main_search.clearFocus()
                if (!query.isNullOrBlank()) {
                    yelpViewModel.getYelpSearch(query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Note: Can implement whenever the text change, call the API with a debouncer.
                return false
            }
        })
        main_search.setQuery("coffee", true)
    }
}
