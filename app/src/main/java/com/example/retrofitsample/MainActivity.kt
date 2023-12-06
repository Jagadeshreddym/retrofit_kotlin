package com.example.retrofitsample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.retrofitsample.ui.theme.RetrofitSampleTheme
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    Column {
                        Button(onClick = {

                            retrofitGetMethod()
                        }) {
                            Text(text = "GetMethod")
                        }

                        Button(onClick = {
                            postData("testing", "test")
                        }) {
                            Text(text = "PostMethod")
                        }
                    }




                }
            }
        }
    }


    fun retrofitGetMethod()
    {
        val quotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)
        Log.i("retrofit","Step 1")
        // launching a new coroutine
        GlobalScope.launch {
            val result = quotesApi.getQuotes()
            if (result != null)
            // Checking the results
                Log.i("retrofit: ", result.body().toString())
        }
    }




    private fun postData(name: String, job: String) {

        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()

        // on below line we are creating a retrofit
        // builder and passing our base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            // at last we are building our retrofit builder.
            .build()
        // below line is to create an instance for our retrofit api class.
        val retrofitAPI = retrofit.create(QuotesApi::class.java)

        // passing data from our text fields to our modal class.
        val dataModal: DataModal = DataModal(name, job)

        // calling a method to create a post and passing our modal class.
        val call: Call<DataModal?>? = retrofitAPI.postData(dataModal)

        // on below line we are executing our method.
        call!!.enqueue(object : Callback<DataModal?> {
            override fun onResponse(call: Call<DataModal?>?, response: Response<DataModal?>) {
                // this method is called when we get response from our api.
                Toast.makeText(this@MainActivity, "Data added to API", Toast.LENGTH_SHORT).show()



                // we are getting response from our body
                // and passing it to our modal class.
                val response: DataModal? = response.body()

                Log.i("test", "response --->"+response.toString())
                // on below line we are getting our data from modal class
                // and adding it to our string.
                val responseString =
                    "Response Code : " + "201" + "\n" + "Name : " + response!!.name + "Job : " + response!!.job

                // below line we are setting our
                // string to our text view.
                Log.i("test", "response --->"+responseString)

            }

            override fun onFailure(call: Call<DataModal?>?, t: Throwable) {
                // setting text to our text view when
                // we get error response from API.

            }
        })
    }
}

data class QuoteList(
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<Result>,
    val totalCount: Int,
    val totalPages: Int
)

data class Result(
    val _id: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int,
    val tags: List<String>
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitSampleTheme {
        Greeting("Android")
    }
}
