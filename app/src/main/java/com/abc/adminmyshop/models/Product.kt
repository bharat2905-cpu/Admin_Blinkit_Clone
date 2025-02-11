package com.abc.adminmyshop.models

import java.util.UUID

data class Product(
    val productRandomId : String? = null,
    var productTitle : String ? = null,
    var productQuantity : Int ? = null,
    var productUnit : String ? = null,
    var productPrice : Int ? = null,
    var productStock : Int ? = null,
    var productCategory :String ? = null,
    var productType : String ? = null,
    val itemCount : Int ? = null,
    val adminUid : String ? = null,
    var productImageUris : ArrayList<String?> ? = null,
)
