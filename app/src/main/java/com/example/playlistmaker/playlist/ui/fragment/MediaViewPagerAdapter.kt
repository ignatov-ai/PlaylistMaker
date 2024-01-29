package com.example.playlistmaker.playlist.ui.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.favourites.ui.fragments.FavouriteFragment

class MediaViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouriteFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}