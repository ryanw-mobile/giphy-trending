package uk.ryanwong.giphytrending.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import uk.ryanwong.giphytrending.GiphyApplication
import uk.ryanwong.giphytrending.databinding.FragmentSettingsBinding
import uk.ryanwong.giphytrending.ui.trending.TrendingViewModel
import uk.ryanwong.giphytrending.ui.trending.TrendingViewModelFactory
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        GiphyApplication.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.seekbarApimax.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                // Only text label updated, but not committed to user preferences yet
                binding.seekbarTextlabel.text = viewModel.translateMaxApiEntries(seek.progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {}

            override fun onStopTrackingTouch(seek: SeekBar) {
                Timber.v("final seekbar progress = $seek.progress")
                viewModel.setApiMax(seek.progress)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.apiMaxEntriesProgress.observe(viewLifecycleOwner, { progress ->
            Timber.v("Update progress bar value from live data: $progress")
            binding.seekbarApimax.progress = progress
            binding.seekbarTextlabel.text = viewModel.translateMaxApiEntries(progress)
        })
    }
}