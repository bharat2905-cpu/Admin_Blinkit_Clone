package com.abc.adminmyshop.models

import java.util.UUID

data class Product(
    val productRandomId : String? = null,
    val productTitle : String ? = null,
    val productQuantity : Int ? = null,
    val productUnit : String ? = null,
    val productPrice : Int ? = null,
    val productStock : Int ? = null,
    val productCategory :String ? = null,
    val productType : String ? = null,
    val itemCount : Int ? = null,
    val adminUid : String ? = null,
    var productImageUris : ArrayList<String?> ? = null,
)
