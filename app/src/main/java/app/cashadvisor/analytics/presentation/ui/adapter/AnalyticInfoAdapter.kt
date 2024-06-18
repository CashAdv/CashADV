package app.cashadvisor.analytics.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
import app.cashadvisor.databinding.ItemAnalyticsFactRowBinding
import app.cashadvisor.databinding.ItemAnalyticsPlanBinding
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide

class AnalyticInfoAdapter : ListAdapter<CategorySummary, RecyclerView.ViewHolder>(
    CategorySummaryDiffUtilCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return when(viewType) {
            PLAN_VIEW_TYPE -> {
                val binding =
                    ItemAnalyticsPlanBinding.inflate(layout, parent, false)
                SimpleCategorySummaryViewHolder(binding)
            }
            FACT_VIEW_TYPE -> {
                val binding =
                    ItemAnalyticsFactBinding.inflate(layout, parent, false)
                CategorySummaryViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemAnalyticsFactBinding.inflate(layout, parent, false)
                CategorySummaryViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is CategorySummaryViewHolder -> holder.bind(getItem(position))
            is SimpleCategorySummaryViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.planned) PLAN_VIEW_TYPE else FACT_VIEW_TYPE
    }

    inner class SimpleCategorySummaryViewHolder(
        private val binding: ItemAnalyticsPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categorySummary: CategorySummary) = with(binding) {
            tvCategoryName.text = categorySummary.name
            tvCategoryAmount.text = categorySummary.amount.formatAmount()
            tvCategoryCurrency.text = getCategoryCurrencyText(categorySummary.analyticType)
            val imageDrawableRes = CategoriesIcon.getCategoriesImageResIdFromId(categorySummary.id.toInt())
            if (imageDrawableRes != null) {
                Glide.with(itemView)
                    .load(ContextCompat.getDrawable(
                        itemView.context,
                        imageDrawableRes
                    ))
                    .into(ivCategory)
            } else {
                ivCategory.setImageDrawable(null)
            }
            piAnalyticProgress.progress = 0
            categorySummary.completePercent?.let { percent ->
                piAnalyticProgress.progress = percent.toInt()
            }
            tvCategoryCurrency.setTextColor(
                getColor(
                    itemView.context,
                    getCategoryCurrencyTextColor(piAnalyticProgress.progress)
                )
            )
        }

        private fun getCategoryCurrencyText(type: AnalyticType): String {
            return when (type) {
                AnalyticType.INCOME -> getString(itemView.context, R.string.mp_plus_currency_symbol)
                AnalyticType.EXPENSE -> getString(itemView.context,R.string.mp_minus_currency_symbol)
                else -> getString(itemView.context,R.string.mp_currency_symbol)
            }
        }

        @ColorRes
        private fun getCategoryCurrencyTextColor(percent: Int): Int {
            return if (percent == 100) {
                R.color.m5
            } else {
                R.color.subcolour2
            }
        }
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
            if (imageDrawableRes != null) {
                Glide.with(itemView)
                    .load(ContextCompat.getDrawable(
                        itemView.context,
                        imageDrawableRes
                    ))
                    .into(ivCategory)
            } else {
                ivCategory.setImageDrawable(null)
            }
            llCategoryDetails.removeAllViews()
            categorySummary.subcategoryList?.let {
                fillSubcategories(it, llCategoryDetails)
            }
        }

        private fun fillSubcategories(list: List<SubcategorySummary>, parent: ViewGroup) {
            list.forEach { subcategorySummary ->
                val layout = LayoutInflater.from(parent.context)
                val binding = ItemAnalyticsFactRowBinding.inflate(layout, parent, false)
                binding.tvCategoryItemName.text = subcategorySummary.name
                binding.tvCategoryItemAmount.text = subcategorySummary.amount.formatAmount()
                parent.addView(binding.root)
            }
        }

        private fun getCategoryCurrencyText(type: AnalyticType): String {
            return when (type) {
                AnalyticType.INCOME -> getString(itemView.context, R.string.mp_plus_currency_symbol)
                AnalyticType.EXPENSE -> getString(itemView.context,R.string.mp_minus_currency_symbol)
                else -> getString(itemView.context,R.string.mp_currency_symbol)
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

    companion object {
        private const val PLAN_VIEW_TYPE = 101
        private const val FACT_VIEW_TYPE = 102
    }
}