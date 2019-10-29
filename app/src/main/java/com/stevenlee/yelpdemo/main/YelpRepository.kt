package com.stevenlee.yelpdemo.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stevenlee.yelpdemo.R
import com.stevenlee.yelpdemo.network.DaggerRepositoryInjector
import com.stevenlee.yelpdemo.network.RepositoryInjector
import com.stevenlee.yelpdemo.network.RetrofitModule
import com.stevenlee.yelpdemo.network.RetrofitService
import com.stevenlee.yelpdemo.network.models.YelpSearch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class YelpRepository(private val application: Application) {

    /**
     * Dagger2 DI for Retrofit
     */
    init {
        val injector: RepositoryInjector =
            DaggerRepositoryInjector.builder().retrofitModule(RetrofitModule).build()
        injector.inject(this)
    }

    @Inject
    lateinit var retrofitService: RetrofitService

    // LiveData to store the results
    private var yelpSearch = MutableLiveData<YelpSearch>()

    /**
     * @return LiveData of YelpSearch
     */
    fun getLiveData(): LiveData<YelpSearch> {
        return yelpSearch
    }

    /**
     * @param term the search term
     * Latitude and Longitude set to Brisbane City Hall
     */
    fun getYelpSearch(term: String) {
        retrofitService.getYelpSearch(
            "Bearer ${application.getString(R.string.yelp_api_key)}",
            term,
            -27.468944,
            153.023407
        ).enqueue(object : Callback<YelpSearch> {
            override fun onFailure(call: Call<YelpSearch>, t: Throwable) {
                onFailureCallback(t.message)
            }

            override fun onResponse(call: Call<YelpSearch>, response: Response<YelpSearch>) {
                if (response.isSuccessful) {
                    yelpSearch.value = response.body()
                } else {
                    if (response.errorBody() != null) {
                        val error =
                            JSONObject(response.errorBody()!!.string()).getJSONObject("error")
                                .getString("description")
                        onFailureCallback(error)
                    } else {
                        onFailureCallback(response.code().toString())
                    }
                }
            }
        })
    }

    /**
     * Displays a toast to the user on any failures.
     */
    private fun onFailureCallback(errorMessage: String?) {
        Toast.makeText(
            application.applicationContext,
            "Network error.\n$errorMessage",
            Toast.LENGTH_LONG
        ).show()
        Timber.e(errorMessage)
    }
}