package com.shield.newsx

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.shield.newsx.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var author = intent.getStringExtra("author") ?: "Unknown"

        val time = intent.getStringExtra("time") ?: "--"
        val content = intent.getStringExtra("content") ?: "No Content Found"
        val des = intent.getStringExtra("des") ?: ""
        val imgUrl = intent.getStringExtra("imageurl")
        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title") ?: ""

        if(author?.length?.toInt()!! > 20) author = author.substring(0,20) + "...."
        binding.newsTime.text = time
        binding.newsAuth.text = author
        binding.newsFakeContent.text = title
        binding.newsRealContent.text = content
        binding.newsDes.text = des
        Glide.with(this).load(
            imgUrl
        ).thumbnail(Glide.with(this).load(R.drawable.baseline_download_24))
            .into(binding.newImage)

        binding.newsDes.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

    }


}