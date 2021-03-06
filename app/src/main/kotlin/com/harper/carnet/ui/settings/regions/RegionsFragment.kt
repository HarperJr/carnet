package com.harper.carnet.ui.settings.regions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.harper.carnet.R
import com.harper.carnet.domain.model.Region
import com.harper.carnet.ext.observe
import com.harper.carnet.ui.settings.regions.adapter.RegionsAdapter
import com.harper.carnet.ui.settings.regions.service.RegionLoadProgress
import com.harper.carnet.ui.settings.regions.service.RegionLoadingService
import kotlinx.android.synthetic.main.fragment_regions.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

class RegionsFragment : Fragment(R.layout.fragment_regions) {
    private val viewModel: RegionsViewModel by currentScope.viewModel(this)
    private val adapter: RegionsAdapter = RegionsAdapter({ requireContext() }) { onRegionItemClicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            regionsLiveDate.observe(this@RegionsFragment, ::setRegions)
            progressLiveData.observe(this@RegionsFragment) { adapter.setRegionLoadProgress(it.region, it.value) }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_regions, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            Navigation.findNavController(
                requireActivity(),
                R.id.nestedNavHostFragment
            ).popBackStack()
        }

        recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = this@RegionsFragment.adapter
        }
    }

    override fun onStart() {
        super.onStart()
        bindRegionLoadingService()
    }

    override fun onStop() {
        unbindRegionLoadingService()
        super.onStop()
    }

    private fun setRegions(regions: List<Region>) {
        adapter.submitList(regions)
    }

    private fun onRegionItemClicked(region: Region) {
        startRegionLoadingService(region)
    }

    private fun startRegionLoadingService(region: Region) {
        requireActivity().startService(Intent(requireContext(), RegionLoadingService::class.java).apply {
            putExtra(
                RegionLoadingService.REGION_EXTRA,
                region
            )
        })
        bindRegionLoadingService()
    }

    private fun bindRegionLoadingService() {
        requireActivity().bindService(
            Intent(requireContext(), RegionLoadingService::class.java),
            viewModel.getServiceConnection(),
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindRegionLoadingService() {
        requireActivity().unbindService(viewModel.getServiceConnection())
    }
}