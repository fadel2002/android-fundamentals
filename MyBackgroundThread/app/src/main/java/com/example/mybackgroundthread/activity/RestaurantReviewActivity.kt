package com.example.mybackgroundthread.activity

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mybackgroundthread.*
import com.example.mybackgroundthread.adapter.ReviewAdapter
import com.example.mybackgroundthread.api.ApiConfig
import com.example.mybackgroundthread.databinding.ActivityRestaurantReviewBinding
import com.example.mybackgroundthread.model.MyLiveDataModel
import com.example.mybackgroundthread.model.RestaurantReviewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantReviewBinding
//    private val viewModel: RestaurantReviewModel by viewModels()
    private val viewModel by viewModels<RestaurantReviewModel>()

    companion object {
        private const val TAG = "RestaurantReviewActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        viewModel.restaurant.observe(this, { restaurant ->
            setRestaurantData(restaurant)
        })

        viewModel.listReview.observe(this, { consumerReviews ->
            setReviewData(consumerReviews)
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewModel.snackbarText.observe(this, {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        binding.btnSend.setOnClickListener { view ->
            viewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this@RestaurantReviewActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

    private fun setReviewData(consumerReviews: List<CustomerReviewsItem>) {
        val listReview = ArrayList<String>()
        for (review in consumerReviews) {
            listReview.add(
                """
                ${review.review}
                - ${review.name}
                """.trimIndent()
            )
        }
        val adapter = ReviewAdapter(listReview)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}