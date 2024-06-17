package app.cashadvisor.analytics.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.cashadvisor.analytics.presentation.formatAmount
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.databinding.ItemAnalyticsFactBinding

class AnalyticInfoAdapter : ListAdapter<CategorySummary, AnalyticInfoAdapter.CategorySummaryViewHolder>(
    CategorySummaryDiffUtilCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnalyticInfoAdapter.CategorySummaryViewHolder {
        val binding =
            ItemAnalyticsFactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategorySummaryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AnalyticInfoAdapter.CategorySummaryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class CategorySummaryViewHolder(
        private val binding: ItemAnalyticsFactBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categorySummary: CategorySummary) = with(binding) {
            tvCategoryName.text = categorySummary.name
            tvCategoryAmount.text = categorySummary.amount.formatAmount()
        }
    }
}