package com.example.mybackgroundthread.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.mybackgroundthread.R
import com.example.mybackgroundthread.databinding.ActivityQuoteBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class QuoteActivity : AppCompatActivity(), OnClickListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private lateinit var binding: ActivityQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRandomQuote()

        binding.btnAllQuotes.setOnClickListener(this)
    }

    private fun getRandomQuote() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://quote-api.dicoding.dev/random"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val quote = responseObject.getString("en")
                    val author = responseObject.getString("author")
                    binding.tvQuote.text = quote
                    binding.tvAuthor.text = author
                } catch (e: Exception) {
                    Toast.makeText(this@QuoteActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@QuoteActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAllQuotes -> {
                startActivity(Intent(this@QuoteActivity, ListQuotesActivity::class.java))
            }
        }
    }
}