package com.example.budgetapp.ui.summary

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgetapp.R
import com.example.budgetapp.common.viewBinding
import com.example.budgetapp.data.model.IncomeExpense
import com.example.budgetapp.databinding.FragmentSummaryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SummaryFragment : Fragment() {

    private lateinit var binding: FragmentSummaryBinding
    private val summaryAdapter by lazy {SummaryAdapter(::onSummaryClick, ::onDeleteClick)}
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val args by navArgs<SummaryFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        with(binding) {
            recyclerView.adapter = summaryAdapter

            ivSignOut.setOnClickListener {
                auth.signOut()
                findNavController().navigate(R.id.summaryToSignUp)
            }
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.summaryToAddEditFragment)
            }
            tvUsername.text = args.nameId
        }
        listenBudget()
    }

    @SuppressLint("SetTextI18n")
    private fun listenBudget() {
        db.collection("budget").addSnapshotListener{ snapshot, e ->
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
                if(document.get("incomeExpenseType") as Boolean) {
                    totalBudget += document.get("price") as Double
                } else {
                    totalBudget -= document.get("price") as Double
                }
                with(binding) {
                    if (totalBudget > 0) {
                        tvTotalBudget.text = "+${totalBudget} ₺"
                        tvTotalBudget.setTextColor(Color.rgb(50,205,50))
                    } else {
                        tvTotalBudget.text = "${totalBudget} ₺"
                        tvTotalBudget.setTextColor(Color.RED)
                    }
                }
            }
            summaryAdapter.submitList(templist)
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
        val action = SummaryFragmentDirections.summaryToAddEditFragment().setIncomeExpense(incomeExpense)
        findNavController().navigate(action)
    }

    private fun onDeleteClick(docId: String) {
        deleteBudget(docId)
    }
}