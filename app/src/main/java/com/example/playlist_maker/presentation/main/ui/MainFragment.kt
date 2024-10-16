package com.example.playlist_maker.presentation.main.ui

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentMainBinding
import com.example.playlist_maker.presentation.common.BaseFragment

class MainFragment: BaseFragment<FragmentMainBinding>() {
    override fun createViewBinding(): FragmentMainBinding {
        return FragmentMainBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        val navController = viewBinding.bottomNavHost.getFragment<NavHostFragment>().navController
        viewBinding.mainBottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            with(viewBinding) {
                if (destination.id == R.id.playerFragment) {
                    mainBottomNavigationView.isGone = true
                    mainBNVDivider.isGone = true
                } else {
                    mainBottomNavigationView.isVisible = true
                    mainBNVDivider.isVisible = true
                }
            }
        }
    }

    override fun observeData() {}
}