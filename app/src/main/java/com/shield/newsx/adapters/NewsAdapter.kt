package com.shield.newsx.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shield.newsx.DetailActivity
import com.shield.newsx.R
import com.shield.newsx.databinding.NewsListItemBinding
import com.shield.newsx.models.ArticlesItem
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class NewsAdapter(var context: Context, var list: List<ArticlesItem>?):
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    inner class NewsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = NewsListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        if(list?.isNotEmpty() == true){
            if(list!!.size>20) return 20
        }
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = list?.get(position)
        val finalBinding = holder.binding

        if (item != null) {
            finalBinding.des.text = item.description
            val imageUrl = item.urlToImage
            val agoTime = item.publishedAt
            finalBinding.time.text = convertToTimeAgo(agoTime)
            finalBinding.views.text = item.author
            Glide.with(context).load(
                imageUrl
            ).thumbnail(Glide.with(context).load(R.drawable.baseline_download_24))
                .into(holder.binding.setImg)



            holder.itemView.setOnClickListener {


                try {
                    var intentImageUrl = imageUrl
                    var intentTime = convertToTimeAgo(agoTime)
                    var intentAuthor = item.author
                    var intentTitle = item.title
                    var intentContent = item.content
                    var intentDes = item.description
                    var intentUrl = item.url
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("author",intentAuthor)
                    intent.putExtra("title", intentTitle)
                    intent.putExtra("imageurl", intentImageUrl)
                    intent.putExtra("time", intentTime)
                    intent.putExtra("content", intentContent)
                    intent.putExtra("des",intentDes)
                    intent.putExtra("url", intentUrl)
                    context.startActivity(intent)
                }
                catch (ex: Exception){
                    Toast.makeText(context, "Some Error Occured!!", Toast.LENGTH_SHORT).show()
                }

            }
        }






    }

    @SuppressLint("SimpleDateFormat")
    fun convertToTimeAgo(dateTimeString: String?): String {
        if(dateTimeString.isNullOrBlank()) return "N/A"
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(dateTimeString)
            val currentTime = Date()

            if (date != null) {
                val diffMillis = currentTime.time - date.time
                val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
                return "$hours hours ago"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "N/A" // Return "Not Available" for invalid input
    }
}