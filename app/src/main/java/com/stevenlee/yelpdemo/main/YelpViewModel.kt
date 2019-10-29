package com.stevenlee.yelpdemo.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.stevenlee.yelpdemo.network.models.YelpSearch

class YelpViewModel(application: Application) : AndroidViewModel(application) {

    // Network Calls Repository
    private var repository = YelpRepository(application)
    // LiveData to store the results
    private var yelpSearchLiveData = repository.getLiveData()

    /**
     * @return LiveData of YelpSearch
     */
    fun getYelpSearchLiveData(): LiveData<YelpSearch> {
        return yelpSearchLiveData
    }

    /**
     * @param searchTerm the search term to fetch
     */
    fun getYelpSearch(searchTerm: String) {
        repository.getYelpSearch(searchTerm)
    }

}