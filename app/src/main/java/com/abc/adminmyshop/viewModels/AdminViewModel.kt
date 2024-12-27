package com.abc.adminmyshop.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase

class AdminViewModel : ViewModel() {


    fun saveImageInDB(imageUri : ArrayList<Uri>){
        val downloadUrls = ArrayList<String>()

        imageUri.forEach { uri ->
//            val imageRef = FirebaseStorage
        }
    }
}