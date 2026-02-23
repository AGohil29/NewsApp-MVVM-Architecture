package com.arun.newsapp.ui.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.databinding.CountryItemLayoutBinding

class CountriesAdapter(private val countriesList: ArrayList<Country>): RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountriesViewHolder {
        return CountriesViewHolder(
            CountryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CountriesViewHolder,
        position: Int
    ) {
        holder.bind(countriesList[position])
    }

    override fun getItemCount() = countriesList.size

    class CountriesViewHolder(private val binding: CountryItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            binding.countryName.text = country.name
        }
    }

    fun addData(list: List<Country>) {
        countriesList.addAll(list)
    }
}