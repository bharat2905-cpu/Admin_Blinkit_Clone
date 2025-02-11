package com.abc.adminmyshop

import android.widget.Filter
import com.abc.adminmyshop.adapter.AdapterProduct
import com.abc.adminmyshop.models.Product
import java.util.Locale

class FilteringProducts(
    val adapter : AdapterProduct,
    val filter : ArrayList<Product>
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()

        if (!constraint.isNullOrEmpty()){
            val filteredList = ArrayList<Product>()
            val query = constraint.toString().uppercase(Locale.getDefault()).split(" ")

            for (products in filter){
                if (query.any {
                                products.productTitle?.uppercase(Locale.getDefault())?.contains(it) == true ||
                                products.productCategory?.uppercase(Locale.getDefault())?.contains(it) == true ||
                                products.productPrice?.toString()?.uppercase(Locale.getDefault())?.contains(it) == true ||
                                products.productType?.uppercase(Locale.getDefault())?.contains(it) == true
                    }){
                    filteredList.add(products)
                }
            }
            result.values = filteredList
            result.count = filteredList.size
        }
        else{
            result.values = filter
            result.count = filter.size
        }

        return result
    }

    override fun publishResults(p0: CharSequence?, result: FilterResults?) {
        adapter.differ.submitList(result?.values as ArrayList<Product>)
    }
}