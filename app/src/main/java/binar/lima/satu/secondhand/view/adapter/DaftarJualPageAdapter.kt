package binar.lima.satu.secondhand.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import binar.lima.satu.secondhand.view.viewpageritem.DiminatiTabFragment
import binar.lima.satu.secondhand.view.viewpageritem.HistoryTabFragment
import binar.lima.satu.secondhand.view.viewpageritem.ProductTabFragment
import binar.lima.satu.secondhand.view.viewpageritem.TerjualTabFragment


class DaftarJualPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ProductTabFragment()
            }
            1 -> {
                DiminatiTabFragment()
            }
            2 -> {
                TerjualTabFragment()
            }
            3 -> {
                HistoryTabFragment()
            }
            else -> {
                ProductTabFragment()
            }
        }
    }

}