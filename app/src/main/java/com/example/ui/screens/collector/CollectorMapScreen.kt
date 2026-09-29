package com.example.ui.screens.collector

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.PickupRequestEntity
import com.example.data.model.PickupStatus
import com.example.service.localization.LocalizationManager
import com.example.ui.components.KabadiwalaTopBar
import com.example.ui.components.StatusChip
import com.example.ui.theme.*
import com.example.ui.viewmodel.KabadiwalaViewModel
import com.example.ui.viewmodel.RouteOptimizationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlin.math.*

@Composable
fun CollectorMapScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onStartHandover: (Long) -> Unit,
    onLanguageClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val activePickups by viewModel.activeNearbyPickups.collectAsState()
    val collectorLocation by viewModel.collectorLocation.collectAsState()
    val optimizedRoute by viewModel.optimizedRoute.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    var selectedPickup by remember { mutableStateOf<PickupRequestEntity?>(null) }
    var selectedStatusFilter by remember { mutableStateOf<String?>(null) }
    var maxDistanceKm by remember { mutableStateOf<Double?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var isSatelliteView by remember { mutableStateOf(false) }
    var isTrafficEnabled by remember { mutableStateOf(false) }
    var isRadarView by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            hasLocationPermission = true
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        viewModel.updateCollectorLocation(loc.latitude, loc.longitude)
                    }
                }
            } catch (e: SecurityException) {
                // Handled gracefully
            }
        }
    }

    val collectorLatLng = remember(collectorLocation) {
        LatLng(collectorLocation.first, collectorLocation.second)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(collectorLatLng, 12f)
    }

    // Filter active pickups based on selected chips
    val filteredPickups = remember(activePickups, selectedStatusFilter, maxDistanceKm, selectedCategoryFilter, collectorLocation) {
        activePickups.filter { pickup ->
            val matchesStatus = selectedStatusFilter == null || pickup.status == selectedStatusFilter
            val matchesCategory = selectedCategoryFilter == null || pickup.materialCategory.equals(selectedCategoryFilter, ignoreCase = true)
            val distance = viewModel.calculateDistanceKm(pickup.gpsLat, pickup.gpsLng)
            val matchesDistance = maxDistanceKm == null || distance <= maxDistanceKm!!
            matchesStatus && matchesCategory && matchesDistance
        }
    }

    // List of pickups to display: if route is optimized, display in optimized sequence
    val displayPickups = remember(optimizedRoute, filteredPickups) {
        optimizedRoute?.stops?.filter { stop -> filteredPickups.any { it.id == stop.id } } ?: filteredPickups
    }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("pickup_map_title"),
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Main Map or Radar View
            if (isRadarView) {
                RadarFallbackView(
                    collectorLatLng = collectorLatLng,
                    pickups = displayPickups,
                    selectedPickup = selectedPickup,
                    optimizedRoute = optimizedRoute,
                    onSelectPickup = { selectedPickup = it }
                )
            } else {
                GoogleMapViewContainer(
                    cameraPositionState = cameraPositionState,
                    collectorLatLng = collectorLatLng,
                    pickups = displayPickups,
                    selectedPickup = selectedPickup,
                    optimizedRoute = optimizedRoute,
                    isSatellite = isSatelliteView,
                    isTraffic = isTrafficEnabled,
                    hasLocationPermission = hasLocationPermission,
                    onSelectPickup = { pickup ->
                        selectedPickup = pickup
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(pickup.gpsLat, pickup.gpsLng),
                                    14f
                                )
                            )
                        }
                    },
                    onMapClick = { selectedPickup = null }
                )
            }

            // Top Floating Overlay: Filters & Routing Efficiency Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .align(Alignment.TopCenter)
            ) {
                // Filter Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // View Mode Switcher
                    FilterChip(
                        selected = !isRadarView,
                        onClick = { isRadarView = false },
                        label = { Text("Google Map", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )

                    FilterChip(
                        selected = isRadarView,
                        onClick = { isRadarView = true },
                        label = { Text("Radar / Compass", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = TechBlueTertiary.copy(alpha = 0.3f)
                        )
                    )

                    FilterChip(
                        selected = selectedStatusFilter == null && maxDistanceKm == null,
                        onClick = {
                            selectedStatusFilter = null
                            maxDistanceKm = null
                            selectedCategoryFilter = null
                        },
                        label = { Text("All (${activePickups.size})", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )

                    FilterChip(
                        selected = selectedStatusFilter == PickupStatus.ACCEPTED.name,
                        onClick = {
                            selectedStatusFilter = if (selectedStatusFilter == PickupStatus.ACCEPTED.name) null else PickupStatus.ACCEPTED.name
                        },
                        label = { Text("Ready / Accepted", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DeepGreenPrimary, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = DeepGreenContainer
                        )
                    )

                    FilterChip(
                        selected = selectedStatusFilter == PickupStatus.REQUESTED.name,
                        onClick = {
                            selectedStatusFilter = if (selectedStatusFilter == PickupStatus.REQUESTED.name) null else PickupStatus.REQUESTED.name
                        },
                        label = { Text("Pending Request", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = WarningAmber.copy(alpha = 0.25f)
                        )
                    )

                    FilterChip(
                        selected = maxDistanceKm == 5.0,
                        onClick = { maxDistanceKm = if (maxDistanceKm == 5.0) null else 5.0 },
                        label = { Text("< 5 km", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = TechBlueTertiary.copy(alpha = 0.25f)
                        )
                    )

                    FilterChip(
                        selected = maxDistanceKm == 10.0,
                        onClick = { maxDistanceKm = if (maxDistanceKm == 10.0) null else 10.0 },
                        label = { Text("< 10 km", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            selectedContainerColor = TechBlueTertiary.copy(alpha = 0.25f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Routing Efficiency Banner
                RoutingEfficiencyBanner(
                    pickups = displayPickups,
                    optimizedRoute = optimizedRoute,
                    onOptimizeRoute = {
                        val result = viewModel.optimizeRouteForPickups(displayPickups)
                        if (result.stops.isNotEmpty()) {
                            selectedPickup = result.stops.first()
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(result.stops.first().gpsLat, result.stops.first().gpsLng),
                                        13f
                                    )
                                )
                            }
                        }
                    },
                    onResetRoute = { viewModel.clearOptimizedRoute() }
                )
            }

            // Right-side Floating Action Buttons for Map Controls
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Recenter to Collector Location
                FloatingActionButton(
                    onClick = {
                        if (!hasLocationPermission) {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(collectorLatLng, 13.5f)
                            )
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("map_recenter_fab"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "My Location", modifier = Modifier.size(22.dp))
                }

                // Satellite / Hybrid View Toggle
                FloatingActionButton(
                    onClick = { isSatelliteView = !isSatelliteView },
                    modifier = Modifier.size(46.dp),
                    containerColor = if (isSatelliteView) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    contentColor = if (isSatelliteView) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        if (isSatelliteView) Icons.Default.Layers else Icons.Default.LayersClear,
                        contentDescription = "Satellite View",
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Live Traffic Toggle
                FloatingActionButton(
                    onClick = { isTrafficEnabled = !isTrafficEnabled },
                    modifier = Modifier.size(46.dp),
                    containerColor = if (isTrafficEnabled) WarmOrangeSecondary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                    contentColor = if (isTrafficEnabled) WarmOrangeSecondary else MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.Traffic, contentDescription = "Traffic", modifier = Modifier.size(22.dp))
                }

                // Radar View Toggle (Reliable Offline / Schematic Mode)
                FloatingActionButton(
                    onClick = { isRadarView = !isRadarView },
                    modifier = Modifier.size(46.dp),
                    containerColor = if (isRadarView) TechBlueTertiary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                    contentColor = if (isRadarView) TechBlueTertiary else MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.Explore, contentDescription = "Radar Compass View", modifier = Modifier.size(22.dp))
                }
            }

            // Bottom Selected Pickup Detail Sheet
            AnimatedVisibility(
                visible = selectedPickup != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                selectedPickup?.let { pickup ->
                    PickupMapDetailCard(
                        pickup = pickup,
                        distanceKm = viewModel.calculateDistanceKm(pickup.gpsLat, pickup.gpsLng),
                        stopIndex = optimizedRoute?.stops?.indexOfFirst { it.id == pickup.id }?.takeIf { it >= 0 },
                        totalStops = optimizedRoute?.stops?.size,
                        onStartHandover = { onStartHandover(pickup.id) },
                        onNavigate = {
                            val uri = Uri.parse("google.navigation:q=${pickup.gpsLat},${pickup.gpsLng}&mode=d")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            try {
                                context.startActivity(mapIntent)
                            } catch (e: Exception) {
                                val genericUri = Uri.parse("geo:${pickup.gpsLat},${pickup.gpsLng}?q=${pickup.gpsLat},${pickup.gpsLng}(${Uri.encode(pickup.materialCategory)})")
                                context.startActivity(Intent(Intent.ACTION_VIEW, genericUri))
                            }
                        },
                        onCall = {
                            val phone = pickup.collectorPhone.ifEmpty { "+919820012345" }
                            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            try {
                                context.startActivity(callIntent)
                            } catch (e: Exception) {
                                viewModel.showMessage("Phone: $phone")
                            }
                        },
                        onClose = { selectedPickup = null },
                        onNextStop = {
                            val stops = optimizedRoute?.stops ?: displayPickups
                            val currentIndex = stops.indexOfFirst { it.id == pickup.id }
                            if (currentIndex in stops.indices) {
                                val nextIndex = (currentIndex + 1) % stops.size
                                val next = stops[nextIndex]
                                selectedPickup = next
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(next.gpsLat, next.gpsLng),
                                            14f
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Google Maps SDK container with reactive camera, markers, polyline and custom info window.
 */
@Composable
private fun GoogleMapViewContainer(
    cameraPositionState: CameraPositionState,
    collectorLatLng: LatLng,
    pickups: List<PickupRequestEntity>,
    selectedPickup: PickupRequestEntity?,
    optimizedRoute: RouteOptimizationResult?,
    isSatellite: Boolean,
    isTraffic: Boolean,
    hasLocationPermission: Boolean,
    onSelectPickup: (PickupRequestEntity) -> Unit,
    onMapClick: () -> Unit
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .testTag("google_map_view"),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = if (isSatellite) MapType.HYBRID else MapType.NORMAL,
            isTrafficEnabled = isTraffic,
            isMyLocationEnabled = hasLocationPermission
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            compassEnabled = true,
            mapToolbarEnabled = true
        ),
        onMapClick = { onMapClick() }
    ) {
        // Collector Base Marker
        Marker(
            state = rememberMarkerState(key = "collector_base", position = collectorLatLng),
            title = LocalizationManager.getString("my_location"),
            snippet = "Base Station (Dharavi, Mumbai)",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )

        // Draw Polyline for optimized tour
        if (optimizedRoute != null && optimizedRoute.polylinePoints.isNotEmpty()) {
            val polylineLatLngs = remember(optimizedRoute) {
                optimizedRoute.polylinePoints.map { LatLng(it.first, it.second) }
            }
            Polyline(
                points = polylineLatLngs,
                color = DeepGreenPrimary,
                width = 10f
            )
        }

        // Active Pickup Markers
        pickups.forEachIndexed { index, pickup ->
            val isSelected = selectedPickup?.id == pickup.id
            val stopIndex = optimizedRoute?.stops?.indexOfFirst { it.id == pickup.id }?.takeIf { it >= 0 }
            val stopPrefix = if (stopIndex != null) "Stop #${stopIndex + 1}: " else ""

            val markerHue = when (pickup.status) {
                PickupStatus.ACCEPTED.name -> BitmapDescriptorFactory.HUE_GREEN
                PickupStatus.PICKUP_SCHEDULED.name -> BitmapDescriptorFactory.HUE_CYAN
                PickupStatus.REQUESTED.name -> BitmapDescriptorFactory.HUE_ORANGE
                else -> BitmapDescriptorFactory.HUE_RED
            }

            Marker(
                state = rememberMarkerState(key = "pickup_${pickup.id}", position = LatLng(pickup.gpsLat, pickup.gpsLng)),
                title = "$stopPrefix${pickup.materialCategory} (${pickup.weightKg} kg)",
                snippet = "₹${pickup.totalValue.toInt()} • ${pickup.recyclerName}",
                icon = BitmapDescriptorFactory.defaultMarker(markerHue),
                onClick = {
                    onSelectPickup(pickup)
                    true
                }
            )
        }
    }
}

/**
 * Top floating card displaying routing efficiency statistics and the "Optimize Route" trigger.
 */
@Composable
private fun RoutingEfficiencyBanner(
    pickups: List<PickupRequestEntity>,
    optimizedRoute: RouteOptimizationResult?,
    onOptimizeRoute: () -> Unit,
    onResetRoute: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("routing_efficiency_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (optimizedRoute != null) DeepGreenContainer else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (optimizedRoute != null) Icons.Default.Route else Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = if (optimizedRoute != null) DeepGreenPrimary else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (optimizedRoute != null) LocalizationManager.getString("route_optimized") else LocalizationManager.getString("route_efficiency"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (optimizedRoute != null) DeepGreenPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${pickups.size} ${LocalizationManager.getString("active_pickups")}  •  ${pickups.sumOf { it.weightKg }.toInt()} kg scrap",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (optimizedRoute != null) {
                    TextButton(
                        onClick = onResetRoute,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(LocalizationManager.getString("clear_route"), fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metrics Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(LocalizationManager.getString("total_distance"), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = if (optimizedRoute != null) "${optimizedRoute.totalDistanceKm} km" else "Direct",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Column {
                    Text(LocalizationManager.getString("est_travel_time"), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = if (optimizedRoute != null) "~${optimizedRoute.estimatedDurationMinutes} mins" else "--",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Column {
                    Text("Total Payout", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "₹${pickups.sumOf { it.totalValue }.toInt()}",
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = onOptimizeRoute,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("optimize_route_button"),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = LocalizationManager.getString("optimize_route"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Bottom interactive detail card for the selected pickup stop.
 */
@Composable
private fun PickupMapDetailCard(
    pickup: PickupRequestEntity,
    distanceKm: Double,
    stopIndex: Int?,
    totalStops: Int?,
    onStartHandover: () -> Unit,
    onNavigate: () -> Unit,
    onCall: () -> Unit,
    onClose: () -> Unit,
    onNextStop: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pickup_map_detail_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (stopIndex != null && totalStops != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Stop ${stopIndex + 1} of $totalStops",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Text(
                        text = pickup.materialCategory,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusChip(status = pickup.status)
                    IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price & Quantity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${pickup.weightKg} kg  •  ₹${pickup.agreedPricePerKg.toInt()}/kg",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Payout: ₹${pickup.totalValue.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepGreenPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TechBlueTertiary.copy(alpha = 0.15f),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.NearMe, contentDescription = null, tint = TechBlueTertiary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$distanceKm km away",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TechBlueTertiary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Recycler and Location
            Text(
                text = "${LocalizationManager.getString("role_recycler")}: ${pickup.recyclerName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${LocalizationManager.getString("location")}: ${pickup.pickupAddress}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Navigate via Google Maps
                Button(
                    onClick = onNavigate,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("navigate_google_maps_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(LocalizationManager.getString("navigate_maps"), fontSize = 12.sp)
                }

                // Start Handover (if Accepted or Scheduled)
                if (pickup.status == PickupStatus.ACCEPTED.name || pickup.status == PickupStatus.PICKUP_SCHEDULED.name) {
                    FilledTonalButton(
                        onClick = onStartHandover,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .testTag("start_handover_map_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DeepGreenContainer, contentColor = DeepGreenOnContainer)
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Handover", fontSize = 12.sp)
                    }
                }

                // Call Partner
                OutlinedIconButton(
                    onClick = onCall,
                    modifier = Modifier.size(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                }

                // Next Stop
                IconButton(
                    onClick = onNextStop,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next Stop")
                }
            }
        }
    }
}

/**
 * Radar / Compass canvas fallback visualization:
 * Plots collector at center (0,0) and active pickup pins at calculated polar bearing and scaled distance.
 * This guarantees 100% routing visualization even in airplane mode or if Play Services is inactive.
 */
@Composable
private fun RadarFallbackView(
    collectorLatLng: LatLng,
    pickups: List<PickupRequestEntity>,
    selectedPickup: PickupRequestEntity?,
    optimizedRoute: RouteOptimizationResult?,
    onSelectPickup: (PickupRequestEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .pointerInput(pickups) {
                detectTapGestures { offset ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val maxRadius = min(size.width, size.height) * 0.42f
                    val maxDistanceKm = 20.0

                    // Find tapped pickup pin within 28dp radius
                    val hit = pickups.find { pickup ->
                        val (dx, dy) = calculateOffset(collectorLatLng, pickup, center, maxRadius, maxDistanceKm)
                        val touchRadius = 40f
                        val distToTouch = sqrt((offset.x - dx).pow(2) + (offset.y - dy).pow(2))
                        distToTouch <= touchRadius
                    }
                    if (hit != null) {
                        onSelectPickup(hit)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = min(size.width, size.height) * 0.42f
            val maxDistanceKm = 20.0

            // Concentric radar range rings
            val rings = listOf(0.25f to "5 km", 0.5f to "10 km", 0.75f to "15 km", 1.0f to "20 km")
            rings.forEach { (fraction, label) ->
                val r = maxRadius * fraction
                drawCircle(
                    color = Color.Cyan.copy(alpha = 0.25f),
                    radius = r,
                    center = center,
                    style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                )
            }

            // Crosshairs
            drawLine(
                color = Color.Cyan.copy(alpha = 0.2f),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 1.5f
            )
            drawLine(
                color = Color.Cyan.copy(alpha = 0.2f),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 1.5f
            )

            // Draw polyline if route is optimized
            if (optimizedRoute != null && optimizedRoute.stops.isNotEmpty()) {
                var prevPoint = center
                optimizedRoute.stops.forEach { stop ->
                    val (px, py) = calculateOffset(collectorLatLng, stop, center, maxRadius, maxDistanceKm)
                    val nextPoint = Offset(px, py)
                    drawLine(
                        color = Color(0xFF10B981),
                        start = prevPoint,
                        end = nextPoint,
                        strokeWidth = 4f
                    )
                    prevPoint = nextPoint
                }
            }

            // Collector Base Center Dot
            drawCircle(
                color = Color(0xFF38BDF8),
                radius = 14f,
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = center
            )

            // Plotted pickup markers
            pickups.forEachIndexed { index, pickup ->
                val (px, py) = calculateOffset(collectorLatLng, pickup, center, maxRadius, maxDistanceKm)
                val isSelected = selectedPickup?.id == pickup.id
                val pinColor = when (pickup.status) {
                    PickupStatus.ACCEPTED.name -> Color(0xFF10B981) // Green
                    PickupStatus.PICKUP_SCHEDULED.name -> Color(0xFF06B6D4) // Cyan
                    else -> Color(0xFFF59E0B) // Amber
                }

                if (isSelected) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.5f),
                        radius = 24f,
                        center = Offset(px, py)
                    )
                }

                drawCircle(
                    color = pinColor,
                    radius = 16f,
                    center = Offset(px, py)
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(px, py)
                )
            }
        }

        // Radar Overlay Title & Legend
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text("Radar Compass View (20 km Radius)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Center: Dharavi Base • Tap dots to inspect stops", color = Color.LightGray, fontSize = 11.sp)
        }
    }
}

/**
 * Calculates (x, y) canvas offset for a pickup relative to collector coordinates.
 */
private fun calculateOffset(
    centerLatLng: LatLng,
    pickup: PickupRequestEntity,
    canvasCenter: Offset,
    maxRadiusPx: Float,
    maxDistanceKm: Double
): Pair<Float, Float> {
    val dLat = pickup.gpsLat - centerLatLng.latitude
    val dLng = pickup.gpsLng - centerLatLng.longitude

    // 1 deg latitude is approx 111 km
    val dyKm = -dLat * 111.0 // negative because screen Y is down
    val dxKm = dLng * 111.0 * cos(Math.toRadians(centerLatLng.latitude))

    val distanceKm = sqrt(dxKm * dxKm + dyKm * dyKm)
    val angleRad = atan2(dyKm, dxKm)

    val scaledDistance = (distanceKm / maxDistanceKm).coerceIn(0.05, 1.0) * maxRadiusPx
    val x = canvasCenter.x + (scaledDistance * cos(angleRad)).toFloat()
    val y = canvasCenter.y + (scaledDistance * sin(angleRad)).toFloat()

    return Pair(x, y)
}
