//package com.abc.adminmyshop
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.navigation.Navigation
//import androidx.navigation.ui.NavigationUI
//import com.abc.adminmyshop.databinding.ActivityAdminMainBinding
//
//class AdminMainActivity : AppCompatActivity() {
//    private lateinit var binding : ActivityAdminMainBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAdminMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_admin_main)
//        setContentView(binding.root)
//
//        NavigationUI.setupWithNavController(binding.bottomMenu, Navigation.findNavController(this, R.id.fragmentContainerView1))
//    }
//}
package com.abc.adminmyshop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.abc.adminmyshop.R
import com.abc.adminmyshop.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) as NavHostFragment
        navController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.bottomMenu, navController)
    }
}


//package com.abc.adminmyshop
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.navigation.Navigation
//import androidx.navigation.ui.NavigationUI
//import com.abc.adminmyshop.databinding.ActivityAdminMainBinding
//
//class AdminMainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityAdminMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Initialize view binding
//        binding = ActivityAdminMainBinding.inflate(layoutInflater)
//        setContentView(binding.root) // Set the content view to the binding's root
//
//        // Set up Bottom Navigation with NavController
//        val navController = Navigation.findNavController(this, R.id.fragmentContainerView1)
//        NavigationUI.setupWithNavController(binding.bottomMenu, navController)
//    }
//}
