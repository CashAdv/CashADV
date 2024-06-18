package app.cashadvisor.analytics.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import app.cashadvisor.analytics.presentation.model.CategorySummary

class CategorySummaryDiffUtilCallback : DiffUtil.ItemCallback<CategorySummary>() {
    override fun areItemsTheSame(oldItem: CategorySummary, newItem: CategorySummary): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CategorySummary, newItem: CategorySummary): Boolean =
        oldItem == newItem
}