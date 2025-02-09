package com.example.socialmediaapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.socialmediaapp.api.UserApi
import com.example.socialmediaapp.databinding.ActivityMainBinding
import com.example.socialmediaapp.model.FcmTokenRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_account, R.id.nav_add,R.id.nav_chat,R.id.nav_friends,R.id.nav_home,
                R.id.nav_logout,R.id.nav_post,R.id.nav_profile,R.id.nav_search,R.id.nav_user
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        getNewToken()
    }

    private fun getNewToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if (!it.isSuccessful){
                Log.w("FCM","Failed to fetched FCM Token")
            }
            val token=it.result
            Log.d("FCM","The token is $token")
            sendTokenToServer(token)
        }
    }

    private fun sendTokenToServer(token: String?) {
        lifecycleScope.launch {
            try {
                var userId=FirebaseAuth.getInstance()
                var uid= userId.currentUser?.uid
                var fcmTokenRequest=FcmTokenRequest("user1", token!!)
                var response=UserApi.apiService.register_token(fcmTokenRequest)
                if (response.isSuccessful){
                    Log.d("FCM","Token has registered")
                }
                else{
                    Log.d("FCM","Token failed to registered")
                }
            }
            catch (e:Exception){
                Log.w("FCM","${e.message}")
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}