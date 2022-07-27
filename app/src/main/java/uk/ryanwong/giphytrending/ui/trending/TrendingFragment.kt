package uk.ryanwong.giphytrending.ui.trending

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uk.ryanwong.giphytrending.R
import uk.ryanwong.giphytrending.databinding.FragmentTrendingBinding
import uk.ryanwong.giphytrending.ui.GiphyImageItemAdapter
import uk.ryanwong.giphytrending.ui.TrendingUIState
import uk.ryanwong.giphytrending.ui.animateDown
import uk.ryanwong.giphytrending.ui.animateUp
import uk.ryanwong.giphytrending.ui.setupRecyclerView
import javax.inject.Inject

@AndroidEntryPoint
class TrendingFragment : Fragment() {

    private val trendingViewModel: TrendingViewModel by viewModels()
    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!
    private var errorDialog: AlertDialog? = null
    private var uiState: TrendingUIState = TrendingUIState.Ready

    @Inject
    lateinit var giphyImageItemAdapter: GiphyImageItemAdapter

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            trendingViewModel.listState?.let {
                // layoutManager.scrollToPositionWithOffset(position, 0)
                binding.recyclerView.layoutManager?.onRestoreInstanceState(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeStateFlow()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The purpose of LifecycleObserver is to eliminate writing the boilerplate code
        // to load and cleanup resources in onCreate() and onDestroy()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.setupRecyclerView().apply {
            adapter = giphyImageItemAdapter
        }
        setupItemAdapter()
    }

    override fun onStart() {
        super.onStart()
        trendingViewModel.refresh()
    }

    override fun onDestroyView() {
        giphyImageItemAdapter.apply {
            unregisterAdapterDataObserver(adapterDataObserver)
        }
        binding.recyclerView.apply {
            adapter = null
        }
        _binding = null
        super.onDestroyView()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_trendingfragment, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                trendingViewModel.refresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupItemAdapter() {
        giphyImageItemAdapter.apply {
            registerAdapterDataObserver(adapterDataObserver)
        }
    }

    private fun observeStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                trendingViewModel.trendingUIState.collect { trendingUIState ->
                    uiState = trendingUIState
                    when {
                        trendingUIState is TrendingUIState.Loading && binding.loadingBar.visibility == View.GONE -> {
                            binding.loadingBar.apply {
                                visibility = View.VISIBLE
                                animateDown()
                            }
                        }
                        trendingUIState !is TrendingUIState.Loading
                                && binding.loadingBar.visibility == View.VISIBLE -> {
                            binding.loadingBar.animateUp()
                        }
                        trendingUIState is TrendingUIState.Error -> {
                            showErrorDialog(trendingUIState.errMsg)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                trendingViewModel.trendingList.collect { trendingList ->
                    trendingViewModel.saveListState(binding.recyclerView.layoutManager?.onSaveInstanceState())
                    giphyImageItemAdapter.submitList(trendingList)
                    toggleNoDataScreen(showNoData = (uiState is TrendingUIState.Ready && trendingList.isEmpty()))
                }
            }
        }
    }

    private fun toggleNoDataScreen(showNoData: Boolean) {
        if (showNoData) {
            binding.recyclerView.visibility = View.GONE
            binding.textviewNodata.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textviewNodata.visibility = View.GONE
        }
    }

    private fun showErrorDialog(errorMessage: String?) {
        errorMessage?.let {
            if (errorMessage.isNotBlank()) {
                // make sure we only show one latest dialog to users for better UX:
                errorDialog?.dismiss()

                // Show an error dialog
                errorDialog =
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(getString(R.string.something_went_wrong))
                        setMessage(errorMessage)
                        setPositiveButton(getString(R.string.ok)) { _, _ ->
                            trendingViewModel.notifyErrorMessageDisplayed()
                        }
                    }.create()
                errorDialog?.show()
            }
        }
    }
}