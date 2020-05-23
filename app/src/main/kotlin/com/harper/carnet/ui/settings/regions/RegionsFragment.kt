package com.harper.carnet.ui.settings.regions

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
import kotlinx.android.synthetic.main.fragment_regions.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel
import timber.log.Timber

class RegionsFragment : Fragment(R.layout.fragment_regions) {
    private val viewModel: RegionsViewModel by currentScope.viewModel(this)
    private val adapter: RegionsAdapter = RegionsAdapter({ requireContext() }) { onRegionItemClicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            regionsLiveDate.observe(this@RegionsFragment, ::setRegions)
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

    private fun setRegions(regions: List<Region>) {
        adapter.submitList(regions)
    }

    private fun onRegionItemClicked(region: Region) {
        Timber.d("Region is clicked, region=$region")
    }
}