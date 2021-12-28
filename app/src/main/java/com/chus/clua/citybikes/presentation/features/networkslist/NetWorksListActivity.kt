package com.chus.clua.citybikes.presentation.features.networkslist

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.chus.clua.citybikes.presentation.base.BaseActivity
import com.chus.clua.citybikes.R
import com.chus.clua.citybikes.databinding.ActivityNetworksListBinding
import com.chus.clua.citybikes.presentation.extension.countryCode
import com.chus.clua.citybikes.presentation.extension.hide
import com.chus.clua.citybikes.presentation.extension.show
import com.chus.clua.citybikes.presentation.features.map.MapActivity
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel
import com.chus.clua.citybikes.presentation.utils.doRequestPermissions
import com.chus.clua.citybikes.presentation.utils.hasPermissions
import com.chus.clua.citybikes.presentation.utils.shouldShowRationale
import javax.inject.Inject

class NetWorksListActivity : BaseActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var currentLocation: Location? = null

    private var searchView: NetWorkSearchView? = null

    private val networksAdapter by lazy { initNetworksAdapter() }

    private val viewModel by lazy { initViewModel() }

    private val geocoder: Geocoder by lazy { Geocoder(this) }

    private lateinit var binding: ActivityNetworksListBinding

    @Inject
    lateinit var factory: NetWorksListViewModelFactory
    private fun initViewModel(): NetWorksListViewModel =
        factory.create(NetWorksListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        getComponent()?.inject(this)
        binding = ActivityNetworksListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        initRecyclerView()
        checkPermission()
        observeViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.networks_list_menu, menu)
        searchView = menu?.findItem(R.id.search)?.actionView as? NetWorkSearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onEvent(NetWorkListEvent.FilterNetWorksByQuery(query))
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onEvent(NetWorkListEvent.FilterNetWorksByQuery(newText))
                return false
            }
        })

        searchView?.findViewById<View>(R.id.search_close_btn)?.setOnClickListener {
            findViewById<EditText>(R.id.search_src_text)?.text = null
        }

        searchView?.setOnActionViewCollapsed {
            viewModel.onEvent(NetWorkListEvent.FilterNetWorksByQuery())
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, Observer(::render))
    }

    private fun render(state: NetWorkListState) {
        when (state) {
            NetWorkListState.Loading -> onLoading()
            is NetWorkListState.Success -> onSuccess(state.netWorks)
            is NetWorkListState.Error -> onError(state.message)
        }
    }

    private fun onLoading() {
        startShimmer()
    }

    private fun onSuccess(netWorks: List<NetWorkListUiModel>) {
        stopShimmer()
        binding.emptyView.emptyContent.hide()
        networksAdapter.submitList(netWorks)
    }

    private fun onError(message: Any?) {
        stopShimmer()
        binding.emptyView.emptyContent.show()
        binding.emptyView.contentEmptyListSubtitle.text = when (message) {
            is String -> message
            is Int -> getString(message)
            else -> throw IllegalArgumentException("Provided {$message} must be either an String or a Int")
        }
        networksAdapter.submitList(listOf())
    }

    override fun onResume() {
        super.onResume()
        if (hasPermissions(ACCESS_FINE_LOCATION, this)) {
            requestLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if (hasPermissions(ACCESS_FINE_LOCATION, this)) {
            locationManager.removeUpdates(this)
        }
    }

    //region loading
    private fun startShimmer() {
        binding.shimmerViewContainer.show()
        binding.shimmerViewContainer.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.hide()
    }
    //endregion

    private fun countryCode(location: Location?) = geocoder.countryCode(location)

    //region RecyclerView
    private fun initNetworksAdapter() = NetworksAdapter { netWorkId ->
        MapActivity.start(this, netWorkId)
        overridePendingTransition(R.anim.right_side_in, R.anim.hold)
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = networksAdapter
            val divider =
                DividerItemDecoration(this@NetWorksListActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
        }
    }
    //endregion

    //region Permission
    private fun checkPermission() {
        if (!hasPermissions(ACCESS_FINE_LOCATION, this)) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (shouldShowRationale(ACCESS_FINE_LOCATION, this)) {
            showSnackBar(getString(R.string.location_request_message), getString(R.string.ok),
                View.OnClickListener {
                    doRequestPermissions(ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE, this)
                }
            )
        } else {
            doRequestPermissions(ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestLocationUpdates()
                } else {
                    viewModel.onEvent(NetWorkListEvent.LoadAllNetWorks)
                    showToast(getString(R.string.location_request_denied))
                }
            }
        }
    }
    //endregion

    //region Location updates
    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            TIME_TO_REFRESH,
            METERS_TO_REFRESH,
            this
        )
    }
    //endregion

    //region Location listener
    override fun onLocationChanged(location: Location?) {
        location?.let {
            searchView?.let { search ->
                if (search.isIconified)
                    viewModel.onEvent(NetWorkListEvent.LoadNearbyNetWorks(countryCode(it), it))
            } ?: run {
                viewModel.onEvent(NetWorkListEvent.LoadNearbyNetWorks(countryCode(it), it))
            }
            currentLocation = it
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
    //endregion

    companion object {
        const val LOCATION_REQUEST_CODE = 101
        const val TIME_TO_REFRESH = 1000 * 60L
        const val METERS_TO_REFRESH = 100f
        fun start(from: Activity) {
            Intent(from, NetWorksListActivity::class.java).run {
                from.startActivity(this)
                from.onBackPressed()
            }
        }
    }

}