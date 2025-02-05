package com.abc.adminmyshop.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.abc.adminmyshop.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class AdminViewModel< Product : Any> : ViewModel() {

    private val _isImagesUploaded = MutableStateFlow(false)
    val isImagesUploaded: StateFlow<Boolean> = _isImagesUploaded

    private val _downloadedUrls = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    val downloadedUrls: StateFlow<ArrayList<String?>> = _downloadedUrls

    private val _isProductSaved = MutableStateFlow(false)
    var isProductSaved : StateFlow<Boolean> = _isProductSaved

    fun saveImageInDB(imageUri: ArrayList<Uri>) {
        val downloadUrls = ArrayList<String?>()

        imageUri.forEach { uri ->
            val imageRef = FirebaseStorage.getInstance().reference
                .child(Utils.getCurrentUserId()) // Ensure this returns a valid userId
                .child("images")
                .child(UUID.randomUUID().toString())
            imageRef.putFile(uri).continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                val url = task.result
                downloadUrls.add(url.toString())

                if (downloadUrls.size == imageUri.size) {
                    _isImagesUploaded.value = true
                    _downloadedUrls.value = downloadUrls
                }
            }
        }

    }

    fun saveProduct(product: com.abc.adminmyshop.models.Product) {
        val productRandomId = product.productRandomId

        FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts/$productRandomId").setValue(product)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/$productRandomId").setValue(product)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("Admins").child("ProductType/$productRandomId").setValue(product)
                            .addOnSuccessListener {
                                _isProductSaved.value = true
                            }
                    }
            }
    }

    fun fetchAllTheProducts(category: String): Flow<List<com.abc.adminmyshop.models.Product>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                val products = ArrayList<Product>()
                val products = mutableListOf<com.abc.adminmyshop.models.Product>()
                for (product in snapshot.children){
//                    val prod : Product? = product.getValue(Product::class.java)
                    val prod = product.getValue(com.abc.adminmyshop.models.Product::class.java)
                    if (category == "All" || prod?.productCategory == category) {
                        products.add(prod!!)
                    }
                }
                trySend(products).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
//                close(error.toException())
            }
        }

        db.addValueEventListener(eventListener)

        awaitClose{db.removeEventListener(eventListener)}
    }

}