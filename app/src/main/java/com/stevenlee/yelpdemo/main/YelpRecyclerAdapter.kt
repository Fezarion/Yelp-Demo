package com.stevenlee.yelpdemo.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stevenlee.yelpdemo.R
import com.stevenlee.yelpdemo.network.models.Businesses
import com.stevenlee.yelpdemo.network.models.YelpSearch

/**
 * RecyclerAdapter to provide the views for businesses
 */
class YelpRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var businesses: List<Businesses> = listOf()
    private var searchTerm = ""

    /**
     * @see getItemViewType for viewType description
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_empty,
                    parent,
                    false
                )
            )
        } else {
            YelpViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_yelp,
                    parent,
                    false
                )
            )

        }
    }

    /**
     * @see getItemViewType for viewType description
     */
    override fun getItemCount(): Int {
        if (businesses.isEmpty()) {
            return if (searchTerm.isBlank()) { // No searches has been done yet.
                0
            } else {
                1
            }
        }
        return businesses.size
    }

    /**
     * @see getItemViewType for viewType description
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as EmptyViewHolder).bindInfo(searchTerm)
        } else {
            (holder as YelpViewHolder).bindInfo(businesses[position])
        }

    }

    /**
     * View Type 0: Empty view, when there's no result then display a message.
     * View Type 1: Businesses view, when there's a result then display the businesses.
     */
    override fun getItemViewType(position: Int): Int {
        return if (businesses.isEmpty()) {
            0
        } else {
            1
        }
    }

    /**
     * Updates the data and notify the adapter.
     * @param yelpSearch the result of the search
     * @param searchTerm the term searched
     */
    fun setYelpSearch(yelpSearch: YelpSearch?, searchTerm: String) {
        this.businesses = yelpSearch?.businesses ?: listOf()
        this.searchTerm = searchTerm
        notifyDataSetChanged()
    }

    /**
     * ViewHolder to display a message with the search term when yelpSearch is empty.
     */
    class EmptyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val emptyTextView: TextView = view.findViewById(R.id.empty_message)

        fun bindInfo(searchTerm: String) {
            emptyTextView.text = view.context.getString(R.string.empty_message, searchTerm)
        }
    }

    /**
     * ViewHolder to display the business including image, name, address and review.
     */
    class YelpViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.business_image)
        private val nameTextView: TextView = view.findViewById(R.id.business_name)
        private val addressTextView: TextView = view.findViewById(R.id.business_address)
        private val reviewTextView: TextView = view.findViewById(R.id.business_review)

        fun bindInfo(business: Businesses) {
            if (business.image_url.isNotBlank()) {
                Picasso.get().load(business.image_url).fit().into(imageView)
            }
            nameTextView.text = business.name
            addressTextView.text = business.location.address1
            reviewTextView.text =
                if (business.review_count == 0 || business.review_count == 1)
                    "${business.review_count} review"
                else
                    "${business.review_count} reviews"
        }
    }
}