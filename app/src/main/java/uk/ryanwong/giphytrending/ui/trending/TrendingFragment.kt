package uk.ryanwong.giphytrending.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import uk.ryanwong.giphytrending.databinding.FragmentTrendingBinding
import uk.ryanwong.giphytrending.di.DaggerAppComponent
import uk.ryanwong.giphytrending.ui.TrendingAdapter
import uk.ryanwong.giphytrending.ui.setupRefreshLayout
import javax.inject.Inject

class TrendingFragment : Fragment() {

    @Inject
    lateinit var trendingAdapter: TrendingAdapter

    private val viewModel: TrendingViewModel by viewModels()

    lateinit var binding: FragmentTrendingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        DaggerAppComponent.create().inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The purpose of LifecycleObserver is to eliminate writing the boilerplate code
        // to load and cleanup resources in onCreate() and onDestroy()
        binding.lifecycleOwner = viewLifecycleOwner
        setUpRecyclerView()
        binding.fetchProgress.setupRefreshLayout { viewModel.refreshList() }
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(false)
            itemAnimator = DefaultItemAnimator()
            adapter = trendingAdapter
        }

        trendingAdapter.apply {
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
        observeInProgress()
        observeIsError()
        observeGiphyList()
    }

    private fun observeInProgress() {
        viewModel.repository.isInProgress.observe(this, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    binding.emptyText.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.fetchProgress.isRefreshing = true
                } else {
                    binding.fetchProgress.isRefreshing = false
                }
            }
        })
    }

    private fun observeIsError() {
        viewModel.repository.isError.observe(this, Observer { isError ->
            isError?.let {
                if (it.isNotEmpty()) {
                    disableViewsOnError(it)
                } else {
                    binding.emptyText.visibility = View.GONE
                    binding.fetchProgress.isRefreshing = true
                }
            } ?: run {
                binding.emptyText.visibility = View.GONE
                binding.fetchProgress.isRefreshing = true
            }
        })
    }

    private fun disableViewsOnError(errorMsg: String) {
        binding.recyclerView.visibility = View.GONE
        binding.emptyText.apply {
            visibility = View.VISIBLE
            text = errorMsg
        }
        viewModel.saveListState(binding.recyclerView.layoutManager?.onSaveInstanceState())
        trendingAdapter.submitList(emptyList())
        binding.fetchProgress.isRefreshing = false
    }

    private fun observeGiphyList() {
        viewModel.repository.trendingList.observe(this, Observer { giphies ->
            giphies.let {
                if (it != null && it.isNotEmpty()) {
                    binding.fetchProgress.isRefreshing = true
                    binding.recyclerView.visibility = View.VISIBLE
                    viewModel.saveListState(binding.recyclerView.layoutManager?.onSaveInstanceState())
                    trendingAdapter.submitList(it)
                    binding.emptyText.visibility = View.GONE
                    binding.fetchProgress.isRefreshing = false
                } else {
                    disableViewsOnError("Empty List")
                }
            }
        })
    }
}