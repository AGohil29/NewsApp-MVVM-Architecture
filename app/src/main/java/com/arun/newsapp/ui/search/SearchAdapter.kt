package com.arun.newsapp.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.databinding.SearchItemLayoutBinding
import com.bumptech.glide.Glide

class SearchAdapter(private val articleList: ArrayList<Article>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(articleList[position])

    override fun getItemCount() = articleList.size

    fun updateData(list: List<Article>) {
        Log.d(
            "SearchAdapter",
            "updateData called with ${list.size} items, current list size: ${articleList.size}"
        )
        val diffCallback = ArticleDiffCallback(articleList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        articleList.clear()
        articleList.addAll(list)
        Log.d("SearchAdapter", "After update, adapter size: ${articleList.size}")
        diffResult.dispatchUpdatesTo(this)
    }

    private class ArticleDiffCallback(
        private val oldList: List<Article>,
        private val newList: List<Article>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].url == newList[newItemPosition].url

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    class SearchViewHolder(private val binding: SearchItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            Log.d("SearchViewHolder", "Binding article: ${article.title}")
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
}
