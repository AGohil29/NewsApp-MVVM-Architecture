package com.arun.newsapp.ui.sources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arun.newsapp.data.model.SourceData
import com.arun.newsapp.databinding.CountryItemLayoutBinding
import com.arun.newsapp.ui.newslist.NewsListActivity

class NewsSourcesAdapter(
    private val sourcesList: ArrayList<SourceData>
) : RecyclerView.Adapter<NewsSourcesAdapter.NewsSourcesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsSourcesViewHolder {
        return NewsSourcesViewHolder(
            CountryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: NewsSourcesViewHolder,
        position: Int
    ) {
        holder.bind(sourcesList[position])
    }

    override fun getItemCount() = sourcesList.size

    class NewsSourcesViewHolder(private val binding: CountryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sourceData: SourceData) {
            binding.countryName.text = sourceData.name

            itemView.setOnClickListener {
                NewsListActivity.start(it.context, NewsListActivity.NEWS_BY_SOURCE, sourceData.id)
            }
        }
    }

    fun addData(list: List<SourceData>) {
        sourcesList.addAll(list)
    }
}