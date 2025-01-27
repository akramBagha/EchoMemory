package com.bagha.echomemory

import android.os.Bundle
import android.os.DeadObjectException
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import com.bagha.echomemory.B_Function.B_SharedPreferences
import com.bagha.echomemory.DB_Room.DB.Config_DB
import com.bagha.echomemory.DB_Room.DB.DB_DAO
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table
import com.bagha.echomemory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //****DB
    companion object{
        lateinit var DB : DB_DAO
        var memoryInfoList :List<MemoInfo_Table> = ArrayList<MemoInfo_Table>()
        var memoryTitleId = -1
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain.toolbar)


        try {

            //****SharePerferance
            B_SharedPreferences(this)

            //****DB
            DB = Config_DB().getDB(this)
            memoryInfoList = DB.getAllMemoList()


            onClickBtn()

            val navHostFragment =
                (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
            val navController = navHostFragment.navController

            binding.navView?.let {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow
                    ),
                    binding.drawerLayout
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                it.setupWithNavController(navController)
            }

            binding.appBarMain.contentMain.bottomNavView?.let {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                it.setupWithNavController(navController)
            }


        }
        catch (e : IllegalStateException){
            e.printStackTrace()
        }
        catch (e : DeadObjectException){
            e.printStackTrace()
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }

    }





    private fun onClickBtn() {
       /* binding.appBarMain.fab?.setOnClickListener { view ->
            //GlobalFunction().ShowSnakBar(view , "Replace with your own action" )
            if(binding.appBarMain.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility == View.GONE){
                binding.appBarMain.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility = View.VISIBLE
            }else{

                binding.appBarMain.addNewMemoryTitle!!.CardViewAddNewMemo!!.visibility = View.GONE
            }
            resetAddNewMemoir()
        }*/


    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        val navView: NavigationView? = findViewById(R.id.nav_view)
        /*if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            menuInflater.inflate(R.menu.overflow, menu)
        }*/
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            /*R.id.nav_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_settings)
            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }






    //______________ Gallery_Action
    /*val onActivityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (imageOption!!.onActivityResult_2(
                    result.data,
                    binding.appBarMain.addNewMemoryTitle!!.AppCompatImageViewCaverMemoir
                )
            ) {
            }
        }
        else {
           // binding.ProgressBarAttachImage.setVisibility(View.GONE)
        }
    }*/


}//end class