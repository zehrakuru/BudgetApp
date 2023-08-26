package com.example.budgetapp.ui.income

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.budgetapp.R
import com.example.budgetapp.common.viewBinding
import com.example.budgetapp.data.model.IncomeExpense
import com.example.budgetapp.databinding.FragmentExpenseBinding
import com.example.budgetapp.databinding.FragmentIncomeBinding
import com.example.budgetapp.ui.summary.SummaryAdapter
import com.example.budgetapp.ui.summary.SummaryFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IncomeFragment : Fragment() {

    private lateinit var binding: FragmentIncomeBinding
    private val incomeAdapter by lazy { SummaryAdapter(::onSummaryClick, ::onDeleteClick) }
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        with(binding) {
            recyclerView.adapter = incomeAdapter
        }
        listenIncomeBudget()
    }

    @SuppressLint("SetTextI18n")
    private fun listenIncomeBudget() {
        db.collection("budget").whereEqualTo("incomeExpenseType", true).addSnapshotListener{ snapshot, e ->
            val templist = arrayListOf<IncomeExpense>()
            var totalBudget: Double = 0.0

            snapshot?.forEach { document ->
                templist.add(
                    IncomeExpense(
                        document.id,
                        document.get("title") as String,
                        (document.get("price") as Number).toDouble(),
                        document.get("desc") as String,
                        document.get("incomeExpenseType") as Boolean?
                    )
                )
                totalBudget += document.get("price") as Double

                with(binding) {
                    tvTotalBudget.text = "+${totalBudget} ₺"
                    tvTotalBudget.setTextColor(Color.rgb(50,205,50))
                }
            }
            incomeAdapter.submitList(templist)
        }
    }
    private fun deleteBudget(docId: String) {
        db.collection("budget").document(docId)
            .delete()
            .addOnSuccessListener {
                //
            }
            .addOnFailureListener {
                //
            }
    }
    private fun onSummaryClick(incomeExpense: IncomeExpense) {
        val action = IncomeFragmentDirections.incomeToAddEdit().setIncomeExpense(incomeExpense)
        findNavController().navigate(action)
    }

    private fun onDeleteClick(docId: String) {
        deleteBudget(docId)
    }
}