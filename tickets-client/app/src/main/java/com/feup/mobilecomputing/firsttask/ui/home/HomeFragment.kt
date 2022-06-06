package com.feup.mobilecomputing.firsttask.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.api.CallbackAPI
import com.feup.mobilecomputing.firsttask.api.PerformancesAPI
import com.feup.mobilecomputing.firsttask.models.PerformanceType
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var numPage = 0
    private var loading = true
    private lateinit var mAdapter: PerformancesListAdapter
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val homeViewModel = HomeViewModel()

    companion object {
        val ITEMS_PER_LOAD = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (numPage == 0) fetchPerformancesList()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPerformancesListViews()
    }

    private fun fetchPerformancesList() {
        loading = false
        swipeRefreshLayout?.isRefreshing = true
        val pa = PerformancesAPI(requireContext())
        pa.fetchAllPerformances(numPage++, object: CallbackAPI<Array<PerformanceType>> {
            override fun onSuccess(response: Array<PerformanceType>) {
                homeViewModel.performanceList.value = mAdapter.addNewItems(response)
                loading = (response.size == ITEMS_PER_LOAD)
                swipeRefreshLayout?.isRefreshing = false
            }
            override fun onError(errorMsg: String?) {
                Log.e("error", errorMsg ?: "")
                numPage--
                loading = true
                swipeRefreshLayout?.isRefreshing = false
                if (view != null) {
                    Snackbar.make(
                        requireView(),
                        "Error loading the performances list",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun loadPerformancesListViews(){
        mAdapter = PerformancesListAdapter(homeViewModel.performanceList.value)
        mAdapter.setOnItemClickListener(object : PerformancesListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val performance = homeViewModel.performanceList.value!![position]
                if (performance.seatsLeft <= 0) return
                val intent = Intent(context, SinglePerformance::class.java)
                intent.putExtra("performance", Gson().toJson(performance))
                startActivity(intent)
            }
        })
        recyclerView = requireView().findViewById(R.id.performances_list)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(recyclerView.context, (layoutManager as LinearLayoutManager).orientation)
            addItemDecoration(dividerItemDecoration)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            adapter = mAdapter
        }

        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = recyclerView.layoutManager?.itemCount!!
                    pastVisiblesItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            fetchPerformancesList()
                        }
                    }
                }
            }
        })

        swipeRefreshLayout = requireView().findViewById(R.id.swiperefresh)
        swipeRefreshLayout?.isRefreshing = true
        swipeRefreshLayout?.setOnRefreshListener {
            numPage = 0
            homeViewModel.performanceList.value = arrayOf()
            mAdapter.resetList()
            fetchPerformancesList()
        }
    }

    override fun onDestroy() {
        swipeRefreshLayout?.isRefreshing = false
        super.onDestroy()
    }

}