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
            Log.d(APP_LOGTAG, "Hours: $it")
            hoursAdapter.updateHours(it)
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveHours()
    }
}