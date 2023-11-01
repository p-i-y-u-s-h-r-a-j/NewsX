package com.shield.newsx.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shield.newsx.DetailActivity
import com.shield.newsx.MainActivity
import com.shield.newsx.R
import com.shield.newsx.databinding.CategoryItemListBinding
import com.shield.newsx.models.ArticlesItem


class CategoryAdapter(
    var context: Context,
    var list: List<String>,
    private var mainRecyclerView: RecyclerView,
    private var datalist: List<List<ArticlesItem>?>
):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), NewsAdapter.onClickListener{
    private val selectedItems = mutableSetOf<Int>()
    inner class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = CategoryItemListBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_list, parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val finalBinding = holder.binding
        val name = list[position]
        finalBinding.categoryName.text = name

        if (selectedItems.contains(position)) {
            finalBinding.categoryName.setTypeface(null, Typeface.BOLD)
            finalBinding.categoryName.setTextColor(Color.BLACK)
            finalBinding.categoryName.textSize = 25f
        } else {
            finalBinding.categoryName.setTypeface(null, Typeface.NORMAL)
            finalBinding.categoryName.setTextColor(Color.GRAY)
            finalBinding.categoryName.textSize = 23f
        }
        holder.itemView.setOnClickListener {
            val customNewsAdapter = NewsAdapter(context,datalist[position])
            mainRecyclerView.adapter = customNewsAdapter
            customNewsAdapter.setOnItemClickListener(this)

            selectedItems.clear()
            selectedItems.add(position)
            notifyDataSetChanged()
        }

    }

    override fun onItemClick(position: Int, adapterData: List<String?>) {
        var intentImageUrl = adapterData[0]
        var intentTime = adapterData[1]
        var intentAuthor = adapterData[2]
        var intentTitle = adapterData[3]
        var intentContent = adapterData[4]
        var intentDes = adapterData[5]
        var intentUrl = adapterData[6]
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


}
