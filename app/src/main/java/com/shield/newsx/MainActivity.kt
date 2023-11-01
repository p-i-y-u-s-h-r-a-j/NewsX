package com.shield.newsx

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.shield.newsx.databinding.ActivityMainBinding

import androidx.lifecycle.lifecycleScope
import com.shield.newsx.adapters.CategoryAdapter
import com.shield.newsx.adapters.NewsAdapter
import com.shield.newsx.api.ApiCategoryInterface
import com.shield.newsx.api.ApiTopHeadlineInterface
import com.shield.newsx.api.ApiUtility
import com.shield.newsx.models.ArticlesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NewsAdapter.onClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fecthTopHeadLinesfast()
        val listCategory: List<String> = listOf("Top HeadLines","Business","Entertainment","Health","Science","Sports","Technology")
        checkInternetConnection()
        fetchTopHeadlines(listCategory)

        binding.searchBtn.setOnClickListener {
            if(binding.searchEd.text.isEmpty()){
                Toast.makeText(this, "Please Enter Some Data", Toast.LENGTH_SHORT).show()
            }else{
                val inputTxt = binding.searchEd.text.toString()
                lifecycleScope.launch(Dispatchers.IO){
                    val res = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData(inputTxt,"d6085022f73741f488d6f4d665bb37c1")
                    withContext(Dispatchers.Main){
                        if(res.body()?.articles?.isEmpty() == true){
                            Toast.makeText(this@MainActivity, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@MainActivity, "Search Completed..", Toast.LENGTH_SHORT).show()
                            binding.topHeadlines.adapter = NewsAdapter(this@MainActivity, res.body()?.articles)
                        }
                    }
                }
            }
        }
    }

    private fun fecthTopHeadLinesfast() {
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtility.getInstance().create(ApiTopHeadlineInterface::class.java).getTopHeadline()
            withContext(Dispatchers.Main){
                if(res.body()?.articles?.isEmpty() == true){
                    Toast.makeText(this@MainActivity, "No Data Found", Toast.LENGTH_SHORT).show()
                }
                else{
                    val customNewsAdapter = NewsAdapter(this@MainActivity,res.body()?.articles )
                    binding.topHeadlines.adapter = customNewsAdapter
                    customNewsAdapter.setOnItemClickListener(this@MainActivity)
                }

            }
        }
    }

    private fun fetchTopHeadlines(listCategory: List<String>) {
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtility.getInstance().create(ApiTopHeadlineInterface::class.java).getTopHeadline()

            val resB = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData("Business","d6085022f73741f488d6f4d665bb37c1")
            val resE = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData("Entertainment","d6085022f73741f488d6f4d665bb37c1")
            val resH = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData("Health", "d6085022f73741f488d6f4d665bb37c1")
            val resSc = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData("Science", "d6085022f73741f488d6f4d665bb37c1")
            val resSp = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData("Sports", "d6085022f73741f488d6f4d665bb37c1")
            val resTech = ApiUtility.getInstance().create(ApiCategoryInterface::class.java).getCategoryData("Technology", "d6085022f73741f488d6f4d665bb37c1")
            withContext(Dispatchers.Main){
                val tpH = res.body()?.articles
                val business = resB.body()?.articles
                val enter = resE.body()?.articles
                val health = resH.body()?.articles
                val science = resSc.body()?.articles
                val sport = resSp.body()?.articles
                val tech = resTech.body()?.articles
                val datalist: List<List<ArticlesItem>?> = listOf(tpH,business,enter,health,science,sport,tech)

                val customNewsAdapter = NewsAdapter(this@MainActivity,res.body()?.articles )
                binding.topHeadlines.adapter = customNewsAdapter
                customNewsAdapter.setOnItemClickListener(this@MainActivity)
                binding.categoryRv.adapter = CategoryAdapter(this@MainActivity, listCategory, binding.topHeadlines, datalist)
            }

        }
    }


    private fun checkInternetConnection(){
        if(!isOnline(this)){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("INTERNET CONNECTION NOT FOUND")
            alert.setMessage("Please Check Your internet Connectivity")
            alert.setCancelable(false)
            alert.setPositiveButton("Refresh",
                DialogInterface.OnClickListener { dialog, i ->
                    if(!isOnline(this)){
                        Toast.makeText(this,"Please Check Your Internet Connectivity", Toast.LENGTH_SHORT).show()
                    }
                    checkInternetConnection()
                })
            alert.create()
            alert.show()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    override fun onItemClick(position: Int, adapterData: List<String?>) {
        var intentImageUrl = adapterData[0]
        var intentTime = adapterData[1]
        var intentAuthor = adapterData[2]
        var intentTitle = adapterData[3]
        var intentContent = adapterData[4]
        var intentDes = adapterData[5]
        var intentUrl = adapterData[6]
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("author",intentAuthor)
        intent.putExtra("title", intentTitle)
        intent.putExtra("imageurl", intentImageUrl)
        intent.putExtra("time", intentTime)
        intent.putExtra("content", intentContent)
        intent.putExtra("des",intentDes)
        intent.putExtra("url", intentUrl)
        startActivity(intent)
    }
}
