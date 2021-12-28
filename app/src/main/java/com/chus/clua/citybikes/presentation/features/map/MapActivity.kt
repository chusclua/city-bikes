package com.chus.clua.citybikes.presentation.features.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.chus.clua.citybikes.presentation.base.BaseActivity
import com.chus.clua.citybikes.R
import com.chus.clua.citybikes.databinding.ActivityMapsBinding
import com.chus.clua.citybikes.presentation.extension.address
import com.chus.clua.citybikes.presentation.extension.hide
import com.chus.clua.citybikes.presentation.extension.show
import com.chus.clua.citybikes.presentation.models.NetWorkMapUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel
import com.chus.clua.citybikes.presentation.utils.hasPermissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject


class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private val bottomSheet: BottomSheetBehavior<ConstraintLayout> by lazy { initBottomSheet() }
    private val geocoder: Geocoder by lazy { Geocoder(this) }
    private val viewModel by lazy { initViewModel() }
    private lateinit var binding: ActivityMapsBinding

    @Inject
    lateinit var factory: MapViewModelFactory
    private fun initViewModel(): MapViewModel = factory.create(MapViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        getComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeMap()
        observeViewModel()
        viewModel.onEvent(MapEvent.LoadAllStations(intent.getStringExtra(NETWORK_ID)))
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, Observer(::render))
    }

    private fun render(state: MapViewState) = when (state) {
        MapViewState.Loading -> onLoading()
        is MapViewState.Success -> onSuccess(state.netWork)
        is MapViewState.Error -> onError(state.message)
        is MapViewState.StationDetail -> onShowStationDetail(state.stationUiModel)
    }

    private fun onLoading() = binding.progress.show()

    private fun onSuccess(netWorkMapUiModel: NetWorkMapUiModel?) {
        binding.progress.hide()
        bindToolbar(netWorkMapUiModel)
        addMarkers(netWorkMapUiModel)
    }

    private fun onError(message: String?) {
        binding.progress.hide()
        showToast(message)
    }

    private fun onShowStationDetail(netWorkStationUiModel: NetWorkStationUiModel?) {
        if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        populateBottomSheet(netWorkStationUiModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        viewModel.onEvent(MapEvent.MarkerClick(marker?.tag as? String))
        return false
    }

    override fun onMapClick(p0: LatLng?) {
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun bindToolbar(netWorkMapUiModel: NetWorkMapUiModel?) {
        supportActionBar?.title = netWorkMapUiModel?.name
        supportActionBar?.subtitle = netWorkMapUiModel?.companies
    }

    private fun addMarkers(netWorkMapUiModel: NetWorkMapUiModel?) {
        netWorkMapUiModel?.stationUiModels?.forEachIndexed { index, netWorkStationUiModel ->
            addMarker(netWorkStationUiModel)
            if (index == netWorkMapUiModel.stationUiModels.lastIndex) {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(netWorkStationUiModel.latLng, 12f)
                )
            }
        }
    }

    private fun addMarker(netWorkStationUiModel: NetWorkStationUiModel?) {
        netWorkStationUiModel?.let {
            mMap.addMarker(
                MarkerOptions()
                    .position(it.latLng ?: LatLng(0.0, 0.0))
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(it.markerColor ?: 0F))
            ).tag = it.id
        }
    }

    private fun populateBottomSheet(netWorkStationUiModel: NetWorkStationUiModel?) {
        val uiModel =
            netWorkStationUiModel?.copy(address = geocoder.address(netWorkStationUiModel.latLng))
        binding.networkStationDetail.stationName.text = uiModel?.name
        binding.networkStationDetail.stationAddress.text = uiModel?.address
        binding.networkStationDetail.stationFreeBikes.text = getString(R.string.free_bikes_label, uiModel?.freeBikes)
        binding.networkStationDetail.stationEmptySlots.text = getString(R.string.empty_slots_label, uiModel?.emptySlots)
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initBottomSheet() = BottomSheetBehavior.from(binding.networkStationDetail.root)

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings?.isZoomControlsEnabled = true
        if (hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION, this)) {
            mMap.isMyLocationEnabled = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.hold, R.anim.right_side_out)
    }

    companion object {
        private const val NETWORK_ID = "network_id"
        fun start(from: Context, networkId: String) =
            Intent(from, MapActivity::class.java).apply {
                putExtra(NETWORK_ID, networkId)
            }.run { from.startActivity(this) }
    }
}