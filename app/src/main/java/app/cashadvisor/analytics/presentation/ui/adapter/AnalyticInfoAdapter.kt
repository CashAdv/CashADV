package app.cashadvisor.analytics.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.cashadvisor.analytics.presentation.formatAmount
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.analytics.presentation.model.SubcategorySummary
import app.cashadvisor.categories.presentation.ui.CategoriesIcon
import app.cashadvisor.databinding.ItemAnalyticsFactBinding
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide

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
            tvCategoryCurrency.text = getCategoryCurrencyText(categorySummary.analyticType)
            tvCategoryCurrency.setTextColor(getColor(itemView.context, getCategoryCurrencyTextColor(categorySummary.analyticType)))
            val imageDrawableRes = CategoriesIcon.getCategoriesImageResIdFromId(categorySummary.id.toInt())
            imageDrawableRes?.let {
                Glide.with(itemView)
                    .load(ContextCompat.getDrawable(
                        itemView.context,
                        imageDrawableRes
                    ))
                    .placeholder(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.placeholder_profile_picture
                        )
                    )
                    .into(ivCategory)
            }
            llCategoryDetails.removeAllViews()
            categorySummary.subcategoryList?.let {
                fillSubcategories(it, llCategoryDetails)
            }
        }

        private fun fillSubcategories(list: List<SubcategorySummary>, parent: ViewGroup) {
            list.forEach { subcategorySummary ->
                val layout = LayoutInflater.from(parent.context)
                    .inflate(app.cashadvisor.R.layout.item_analytics_fact_row, parent, false)
                layout.findViewById<TextView>(app.cashadvisor.R.id.tvCategoryItemName).text =
                    subcategorySummary.name
                layout.findViewById<TextView>(app.cashadvisor.R.id.tvCategoryItemAmount).text =
                    subcategorySummary.amount.formatAmount()
                val lLayout = layout.findViewById<LinearLayout>(app.cashadvisor.R.id.llItemAnalyticsFactRow)
                parent.addView(lLayout)
            }
        }

        private fun getCategoryCurrencyText(type: AnalyticType): String {
            return when (type) {
                AnalyticType.INCOME -> getString(itemView.context, app.cashadvisor.uikit.R.string.mp_plus_currency_symbol)
                AnalyticType.EXPENSE -> getString(itemView.context,app.cashadvisor.uikit.R.string.mp_minus_currency_symbol)
                else -> getString(itemView.context,app.cashadvisor.uikit.R.string.mp_currency_symbol)
            }
        }

        @ColorRes
        private fun getCategoryCurrencyTextColor(type: AnalyticType): Int {
            return when (type) {
                AnalyticType.INCOME -> R.color.m1
                AnalyticType.EXPENSE -> R.color.m2
                else -> R.color.m3
            }
        }
    }
}