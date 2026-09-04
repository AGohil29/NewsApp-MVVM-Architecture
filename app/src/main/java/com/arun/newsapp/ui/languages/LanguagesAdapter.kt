package com.arun.newsapp.ui.languages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arun.newsapp.data.model.Language
import com.arun.newsapp.databinding.LanguageItemLayoutBinding

class LanguagesAdapter(private val languagesList: ArrayList<Language>): RecyclerView.Adapter<LanguagesAdapter.LanguagesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LanguagesViewHolder {
        return LanguagesViewHolder(
            LanguageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: LanguagesViewHolder,
        position: Int
    ) {
        holder.bind(languagesList[position])
    }

    override fun getItemCount() = languagesList.size

    class LanguagesViewHolder(private val binding: LanguageItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(language: Language) {
            binding.languageName.text = language.name
        }
    }

    fun addData(list: List<Language>) {
        languagesList.addAll(list)
    }
}
