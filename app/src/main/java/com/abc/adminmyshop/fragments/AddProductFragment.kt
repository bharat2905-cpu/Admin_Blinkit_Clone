package com.abc.adminmyshop.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.abc.adminmyshop.Constants
import com.abc.adminmyshop.R
import com.abc.adminmyshop.Utils
import com.abc.adminmyshop.activity.AdminMainActivity
import com.abc.adminmyshop.adapter.AdapterSelectedImage
import com.abc.adminmyshop.databinding.FragmentAddProductBinding
import com.abc.adminmyshop.models.Product
import com.abc.adminmyshop.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

    private val viewModel : AdminViewModel<Any> by viewModels()
    private lateinit var binding : FragmentAddProductBinding
    private val imageUris : ArrayList<Uri> = arrayListOf()

    val selectedImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){listOfUri ->
        val fiveImages = listOfUri.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)

        binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)

        setStatusBarColor()
        setAutoCompleteTextViews()
        onImageSelectClicked()
        onAddButtonClicked()

        return binding.root
    }

    private fun onAddButtonClicked() {
        binding.btnAddProduct.setOnClickListener {
            Utils.showDialog(requireContext(), "Uploading images...")

            val productTitle = binding.etProductTitle.text.toString().trim()
            val productQuantity = binding.etProductQuantity.text.toString().trim()
            val productUnit = binding.etProductUnit.text.toString().trim()
            val productPrice = binding.etProductPrice.text.toString().trim()
            val productStock = binding.etProductStock.text.toString().trim()
            val productCategory = binding.etProductCategory.text.toString().trim()
            val productType = binding.etProductType.text.toString().trim()

            if (productTitle.isEmpty() || productQuantity.isEmpty() || productUnit.isEmpty() || productPrice.isEmpty() ||
                productStock.isEmpty() || productCategory.isEmpty() || productType.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Empty fields are not allowed")
                }
                return@setOnClickListener
            }

            else if (productPrice.toIntOrNull() == null || productQuantity.toIntOrNull() == null || productStock.toIntOrNull() == null) {
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please enter valid numbers for price, quantity, and stock")
                }
                return@setOnClickListener
            }

            else if (imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please upload some images")
                }
                return@setOnClickListener
            }

            else{
                val product = Product(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productRandomId = Utils.getRandomId()
                )
                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product) {
        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUploaded.collect{
                if (it){
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(), "image saved")
                    }
                    getUrls(product)
                }
            }
        }
    }

    private fun getUrls(product: Product) {
        Utils.showDialog(requireContext(), "Publishing Product...")

        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{
                val urls = it
                product.productImageUris = urls
                saveProduct(product)
            }
        }
    }

    private suspend fun saveProduct(product: Product) {
        viewModel.saveProduct(product)
        viewModel.isProductSaved.collect{
            if (it){
                Utils.hideDialog()
                startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                Utils.showToast(requireContext(), "Your product is live")
            }
        }
    }


    private fun onImageSelectClicked() {
        binding.btnSelectImage.setOnClickListener{
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextViews(){
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
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