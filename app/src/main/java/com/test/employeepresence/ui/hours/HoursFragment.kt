package com.test.employeepresence.ui.hours

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.employeepresence.databinding.FragmentHoursBinding
import com.test.employeepresence.hours.domain.DayRecord
import com.test.employeepresence.ui.settings.SettingsViewModel
import com.test.employeepresence.utils.APP_LOGTAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HoursFragment : Fragment() {

    private var _binding: FragmentHoursBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var hoursAdapter: HoursAdapter
    private val viewModel: HoursViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHoursBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hoursAdapter = HoursAdapter()

        binding.recyclerViewHours.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hoursAdapter
            setHasFixedSize(true)
        }

        viewModel.hours.observe(viewLifecycleOwner) {
            updateUI(it)
        }

    }

    private fun updateUI(records: List<DayRecord>?) {
        if (records.isNullOrEmpty()) {
            binding.noHours.visibility = View.VISIBLE
            binding.recyclerViewHours.visibility = View.GONE
        } else {
            binding.noHours.visibility = View.GONE
            binding.recyclerViewHours.visibility = View.VISIBLE
            hoursAdapter.updateHours(records)
        }

    }
    override fun onStart() {
        super.onStart()
        viewModel.retrieveHours()
    }
}