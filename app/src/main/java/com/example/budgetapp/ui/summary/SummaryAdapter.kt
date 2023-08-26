package com.example.budgetapp.ui.summary

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.data.model.IncomeExpense
import com.example.budgetapp.databinding.ItemBudgetCardBinding
import kotlin.reflect.KFunction1

class SummaryAdapter(
    private val onNoteClick: (IncomeExpense) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<IncomeExpense, SummaryAdapter.SummaryViewHolder>(SummaryDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder =
        SummaryViewHolder(
            ItemBudgetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onNoteClick,
            onDeleteClick
        )

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) = holder.bind(getItem(position))


    class SummaryViewHolder(
        private val binding: ItemBudgetCardBinding,
        private val onNoteClick: (IncomeExpense) -> Unit,
        private val onDeleteClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(incomeExpense: IncomeExpense) = with(binding) {

            val type = incomeExpense.incomeExpenseType
            tvTitle.text = incomeExpense.title
            tvDesc.text = incomeExpense.desc

            if (type == true) {
                price.text = "+${incomeExpense.price} ₺"
                price.setTextColor(Color.rgb(50,205,50))
            } else {
                price.text = "-${incomeExpense.price} ₺"
                price.setTextColor(Color.RED)
            }

            root.setOnClickListener {
                onNoteClick(incomeExpense)
            }
            btnDelete.setOnClickListener {
                incomeExpense.docId?.let(onDeleteClick)
            }
        }
    }

    class SummaryDiffCallBack : DiffUtil.ItemCallback<IncomeExpense>() {
        override fun areItemsTheSame(oldItem: IncomeExpense, newItem: IncomeExpense): Boolean {
            return oldItem.docId == newItem.docId
        }

        override fun areContentsTheSame(oldItem: IncomeExpense, newItem: IncomeExpense): Boolean {
            return oldItem == newItem
        }

    }
}