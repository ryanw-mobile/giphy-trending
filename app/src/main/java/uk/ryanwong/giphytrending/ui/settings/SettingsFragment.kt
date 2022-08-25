package uk.ryanwong.giphytrending.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.giphytrending.R
import uk.ryanwong.giphytrending.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding
    private var errorDialog: AlertDialog? = null
    private var uiState: SettingsUIState = SettingsUIState.Ready

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(false)
        observeStateFlow()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.seekbarApimax.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                // Only text label updated, but not committed to user preferences yet
                binding.seekbarTextlabel.text =
                    settingsViewModel.translateMaxApiEntries(seek.progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {}

            override fun onStopTrackingTouch(seek: SeekBar) {
                Timber.v("final seekbar progress = $seek.progress")
                settingsViewModel.setApiMax(seek.progress)
            }
        })
    }


    override fun onResume() {
        super.onResume()
        settingsViewModel.getApiMax()
    }

    private fun observeStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.settingsUIState.collect { settingsUIState ->
                    uiState = settingsUIState
                    if (settingsUIState is SettingsUIState.Error) {
                        showErrorDialog(settingsUIState.errMsg)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.apiMaxEntriesProgress.collect { progress ->
                    Timber.v("Update progress bar value from live data: $progress")
                    binding.seekbarApimax.progress = progress
                    binding.seekbarTextlabel.text =
                        settingsViewModel.translateMaxApiEntries(progress)
                }
            }
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
                            settingsViewModel.notifyErrorMessageDisplayed()
                        }
                    }.create()
                errorDialog?.show()
            }
        }
    }
}