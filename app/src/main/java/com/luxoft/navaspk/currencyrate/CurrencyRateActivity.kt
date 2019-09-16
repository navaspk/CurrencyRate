package com.luxoft.navaspk.currencyrate

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_currency_rate_main.*

class CurrencyRateActivity : AppCompatActivity(), View.OnClickListener {

    private var mDrawerLayout: DrawerLayout? = null
    private var mActionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var mNavigationView: NavigationView? = null
    private var mCoordinatorLayout: CoordinatorLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_rate_main)
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout)

        configureNavigationDrawer()
        configureToolbar()
        initFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.collps_menu, menu)
        return true
    }

    fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    private fun configureToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar_container) as Toolbar
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(false)
        actionbar.setHomeButtonEnabled(true)

        mActionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout,
            R.string.app_name, R.string.app_name)
        if (mActionBarDrawerToggle != null)
            mActionBarDrawerToggle!!.syncState()

        val tooBarSettings = findViewById<View>(R.id.toolbar_settings_imageview) as ImageView
        tooBarSettings.setOnClickListener(this)
    }

    private fun configureNavigationDrawer() {
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mNavigationView = findViewById<View>(R.id.navigation_drawer) as NavigationView
    }

    private fun initFragment() {
        val frag = CurrencyRateFragment()
        if (frag != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.framelayout_container, frag)
            transaction.commitAllowingStateLoss()
            mDrawerLayout?.closeDrawers()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            // Android home
            android.R.id.home -> {
                mDrawerLayout?.openDrawer(GravityCompat.START)
                return true
            }
        }// manage other entries
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toolbar_settings_imageview -> openSettingsDrawer()
        }
    }

    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(mNavigationView!!)) {
            closeSettingsDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun closeSettingsDrawer() {
        mDrawerLayout?.closeDrawer(mNavigationView!!)
    }

    private fun openSettingsDrawer() {
        mDrawerLayout!!.openDrawer(mNavigationView!!)
    }

    fun showSnackBar(noNetwork: Boolean, noResponse: Boolean) {
        var snackText = ""
        var button = ""
        var buttonSeq: CharSequence = ""
        var snackSeq: CharSequence = ""
        if (noNetwork) {
            snackText = getString(R.string.error_msg_no_connection_warning)
            button = getString(R.string.enable_setting)
        } else if (noResponse) {
            snackText = getString(R.string.response_error)
            button = getString(R.string.hide)
        }

        buttonSeq = button
        snackSeq = snackText
        Snackbar.make(coordinatorLayout, snackSeq, Snackbar.LENGTH_LONG)
            .setAction(buttonSeq) {
                if (noNetwork)
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }

}