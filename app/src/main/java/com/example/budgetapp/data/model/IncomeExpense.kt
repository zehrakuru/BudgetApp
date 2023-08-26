package com.example.budgetapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncomeExpense(
    val docId: String?,
    val title: String,
    val price: Double,
    val desc: String,
    val incomeExpenseType: Boolean?
) : Parcelable
