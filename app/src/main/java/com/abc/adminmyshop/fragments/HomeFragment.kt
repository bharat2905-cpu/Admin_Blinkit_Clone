package com.abc.adminmyshop.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.abc.adminmyshop.Constants
import com.abc.adminmyshop.R
import com.abc.adminmyshop.adapter.AdapterProduct
import com.abc.adminmyshop.adapter.CategoriesAdapter
import com.abc.adminmyshop.databinding.FragmentHomeBinding
import com.abc.adminmyshop.models.Categories
import com.abc.adminmyshop.models.Product
import com.abc.adminmyshop.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

//    val viewModel: AdminViewModel by viewModels()
    private val viewModel: AdminViewModel<Product> by viewModels()

    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()

        setCategories()
        getAlltheProducts("All")
        return binding.root
    }

    private fun getAlltheProducts(category: String) {
        lifecycleScope.launch {
            viewModel.fetchAllTheProducts(category).collect{
                val adapterProduct = AdapterProduct()
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
            }
        }
    }

    private fun setCategories() {
        val categoryList = ArrayList<Categories>()
        for(i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(Categories(Constants.allProductsCategory[i], Constants.allProductsCategoryIcon[i]))
        }

        binding.rvCategories.adapter = CategoriesAdapter(categoryList, ::onCategoryClicked)
    }

    fun onCategoryClicked(categories: Categories){
        getAlltheProducts(categories.category)
    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}