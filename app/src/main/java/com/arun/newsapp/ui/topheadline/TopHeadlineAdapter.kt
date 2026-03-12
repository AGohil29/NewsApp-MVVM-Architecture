package com.arun.newsapp.ui.topheadline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.databinding.TopHeadlineItemLayoutBinding
import com.bumptech.glide.Glide

class TopHeadlineAdapter(
    private val articleList: ArrayList<Article>
): RecyclerView.Adapter<TopHeadlineAdapter.DataViewHolder>() {

    class DataViewHolder(private val binding: TopHeadlineItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.textViewTitle.text = article.title
            binding.textViewDescription.text = article.description
            binding.textViewSource.text = article.source.name

            Glide.with(binding.imageViewBanner.context)
                .load(article.imageUrl)
                .into(binding.imageViewBanner)

            itemView.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(it.context, article.url.toUri())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = DataViewHolder(
        TopHeadlineItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: DataViewHolder,
        position: Int
    ) = holder.bind(articleList[position])

    override fun getItemCount() = articleList.size

    fun addData(list: List<Article>) {
        articleList.addAll(list)
    }

    fun updateData(list: List<Article>) {
        articleList.clear()
        articleList.addAll(list)
    }
}