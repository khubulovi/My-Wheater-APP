package com.example.myapplication.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.app.AppState
import com.example.myapplication.databinding.FragmentHistoryBinding
import com.example.myapplication.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by lazy { ViewModelProvider(this)[HistoryViewModel::class.java] }
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Sucsess -> {
                binding.historyFragmentRecyclerview.visibility = View.VISIBLE
                binding.loadingView.visibility = View.GONE
                adapter.setData(appState.wheateherData)
            }

            is AppState.Loading -> {
                binding.historyFragmentRecyclerview.visibility = View.GONE
                binding.loadingView.visibility = View.VISIBLE
            }

            is AppState.Eror -> {
                binding.historyFragmentRecyclerview.visibility = View.VISIBLE
                binding.loadingView.visibility = View.GONE
                viewModel.getAllHistory()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}

