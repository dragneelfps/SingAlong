package com.nooblabs.singalong

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity: AbsMainActivity(){

    companion object {
        val TAG = javaClass.simpleName
    }
    lateinit var mNavItemSelectedListener : NavigationView.OnNavigationItemSelectedListener
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if(requestStoragePermission()){
            init()
        }
    }

    fun setupToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp)
    }

    fun setupNavView(){
        mNavItemSelectedListener = NavigationView.OnNavigationItemSelectedListener{ item ->
            item.isChecked = true
            drawerLayout.closeDrawers()

            Log.d(TAG,"HERE")


            when(item.itemId){
                R.id.nav_songs -> {
                    replaceFragment(SongsFragment())
                }
                else -> {
                    replaceFragment(Fragment())
                }
            }

            true
        }
        nav_view.setNavigationItemSelectedListener(mNavItemSelectedListener)
        nav_view.getHeaderView(0).nav_username.text = "Sourabh"

        mDrawerToggle = object : ActionBarDrawerToggle(this,drawerLayout, R.string.close_drawer,
                R.string.open_drawer){

        }

        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
    }

    fun replaceFragment(fragment: Fragment){
        val backStateName = fragment::class.java.simpleName
        Log.d(TAG,"BackStateName = $backStateName")
        val fgPopped = supportFragmentManager.popBackStackImmediate(backStateName, 0)
        if(!fgPopped){
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.main_frame_layout, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    fun init(){
        setupToolbar()
        setupNavView()
        loadSongs()
    }

    fun loadSongs(){
        replaceFragment(SongsFragment())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_READ_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG,"Storage read permission were granted by user.")
                    init()
                }
            }
        }
    }





}