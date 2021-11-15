package uk.ryanwong.giphytrending.ui.trending

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import uk.ryanwong.giphytrending.GiphyApplication
import uk.ryanwong.giphytrending.R
import uk.ryanwong.giphytrending.databinding.FragmentTrendingBinding
import uk.ryanwong.giphytrending.ui.GiphyImageItemAdapter
import uk.ryanwong.giphytrending.ui.animateDown
import uk.ryanwong.giphytrending.ui.animateUp
import uk.ryanwong.giphytrending.ui.setupRecyclerView
import javax.inject.Inject

class TrendingFragment : Fragment() {

    @Inject
    lateinit var giphyImageItemAdapter: GiphyImageItemAdapter

    @Inject
    lateinit var viewModelFactory: TrendingViewModelFactory
    private lateinit var viewModel: TrendingViewModel
    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!
    private var errorDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        GiphyApplication.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[TrendingViewModel::class.java]
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
        observeLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_trendingfragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupItemAdapter() {
        giphyImageItemAdapter.apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    viewModel.listState?.let {
                        // layoutManager.scrollToPositionWithOffset(position, 0)
                        binding.recyclerView.layoutManager?.onRestoreInstanceState(it)
                    }
                }
            })
        }
    }

    private fun observeLiveData() {
        viewModel.showLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading && binding.loadingBar.visibility == View.GONE) {
                binding.loadingBar.apply {
                    visibility = View.VISIBLE
                    animateDown()
                }
            } else if (!isLoading && binding.loadingBar.visibility == View.VISIBLE) {
                binding.loadingBar.animateUp()
            }
        })

        viewModel.trendingList.observe(viewLifecycleOwner, { trendingList ->
            trendingList.let {
                viewModel.saveListState(binding.recyclerView.layoutManager?.onSaveInstanceState())
                giphyImageItemAdapter.submitList(it)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            showErrorDialog(errorMessage)
        })


        viewModel.showNoData.observe(viewLifecycleOwner, { showNoData ->
            if (showNoData) {
                binding.recyclerView.visibility = View.GONE
                binding.textviewNodata.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.textviewNodata.visibility = View.GONE
            }
        })
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
                            // do nothing
                        }
                    }.create()
                errorDialog?.show()
            }
        }
    }
}