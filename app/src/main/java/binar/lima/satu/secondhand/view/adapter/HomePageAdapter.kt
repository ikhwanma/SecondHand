package binar.lima.satu.secondhand.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.view.viewpageritem.HomeProductFragment

class HomePageAdapter(fragment: Fragment, private val listCategory : List<GetSellerCategoryResponseItem>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun createFragment(position: Int): Fragment {
        for (pos in 0..listCategory.size){
            return when(position){
                pos -> {
                    HomeProductFragment(listCategory[pos].id)
                }
                else -> {
                    HomeProductFragment(0)
                }
            }
        }
        return HomeProductFragment(0)
    }

}