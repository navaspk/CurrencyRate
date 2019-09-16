package com.luxoft.navaspk.currencyrate

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.luxoft.navaspk.currencyrate.presenter.CurrencyRatePresenter
import com.luxoft.navaspk.currencyrate.presenter.Rates
import com.luxoft.navaspk.currencyrate.presenter.Response
import com.luxoft.navaspk.currencyrate.presenter.UpdateView

class CurrencyRateFragment : Fragment(), UpdateView {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: CurrencyRateAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mSampleItem: Rates? = null
    private var mProgress: ProgressBar? = null
    private var mManager: ConnectivityManager? = null
    private val SHOW_PROGRESS = 1001
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mCurrencyRatePresenter : CurrencyRatePresenter? = null

    /**
     * In order to show progress bar when launching screen
     */
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == SHOW_PROGRESS) {
                progressBarVisibility(View.VISIBLE)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mSampleItem = Rates()
        mManager = getActivity()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mManager?.registerDefaultNetworkCallback(networkCallback)
        mCurrencyRatePresenter = CurrencyRatePresenter(this)
        makeNetworkCall()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.recyclerview)
        mProgress = view.findViewById(R.id.progress)
        mRecyclerView!!.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(getActivity())

        val dividerItemDecoration = DividerItemDecoration(
            mRecyclerView!!.context,
            mLayoutManager!!.orientation
        )
        mRecyclerView!!.addItemDecoration(dividerItemDecoration)

        //size lookup may be required
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = CurrencyRateAdapter(getContext()!!, mSampleItem)
        mRecyclerView!!.adapter = mAdapter
        if (!(getActivity() as CurrencyRateActivity).isNetworkConnected()) {
            progressBarVisibility(View.GONE)
        }

        //swipe to refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setOnRefreshListener(mSwipeRefreshListener)
    }

    /**
     * Progress bar visibility functionality
     *
     * @param visibility
     */
    private fun progressBarVisibility(visibility: Int) {
        if (mProgress != null)
            mProgress!!.visibility = visibility
    }

    /**
     * Network connectivity listener. This will take care to show the snack bar based on connection
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            // we've got a connection, remove callbacks (if we have posted any)
            mHandler.removeCallbacks(endCall)
            if (mSampleItem == null && isVisible && context != null) {
                mHandler.sendEmptyMessage(SHOW_PROGRESS)
                // in case data is not there inittially and network also not available, then once network is up
                // then call to server for the data.
                mCurrencyRatePresenter?.makeNetworkCallForResponse(context!!)
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            // Schedule an event to take place in a second
            mHandler.postDelayed(endCall, 1000)
        }
    }

    private val endCall = Runnable {
        // if execution has reached here - feel free to cancel the call
        // because no connection was established in a second
        (activity as CurrencyRateActivity).showSnackBar(true, false)
    }

    /**
     * This runnbale take care to call network if user is stayed in same screen and which is
     * running periodically in each 45 seconds
     */
    private val periodicUpdate = Runnable {
        if (isVisible) {
            showProgress(View.VISIBLE)
            makeNetworkCall()
        }
    }

    /**
     * call back when swipe to refresh - not implemented completely :)
     */
    internal var mSwipeRefreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        if ((getActivity() as CurrencyRateActivity).isNetworkConnected()) {
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout!!.isRefreshing)
                mSwipeRefreshLayout!!.isRefreshing = false
        } else {
            mSwipeRefreshLayout!!.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mManager!!.unregisterNetworkCallback(networkCallback)
        mHandler.removeCallbacks(endCall)
        mHandler.removeMessages(SHOW_PROGRESS)
        mHandler.removeCallbacks(periodicUpdate)
    }

    override fun showProgress(visibility: Int) {
        progressBarVisibility(visibility)
    }

    override fun showUpdatedContent(response: Response) {
        mAdapter?.updateContent(response.rates)
        mAdapter?.notifyDataSetChanged()
    }

    override fun sowSnackBarOnIssue(network : Boolean, response: Boolean) {
        if (context != null)
            (context as CurrencyRateActivity).showSnackBar(network, response)
    }

    /**
     * Make a network call and add another call to message queue, added call will execute in 45s
     */
    private fun makeNetworkCall() {
        mCurrencyRatePresenter?.makeNetworkCallForResponse(context!!)
        mHandler.postDelayed(periodicUpdate, 45000)
    }
}