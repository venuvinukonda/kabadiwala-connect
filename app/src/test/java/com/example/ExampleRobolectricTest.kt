package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.MaterialCondition
import com.example.service.ai.MockMaterialClassificationService
import com.example.service.ai.MockPriceEstimationService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kabadiwala Connect", appName)
  }

  @Test
  fun `test ai material classification service`() = runBlocking {
    val service = MockMaterialClassificationService()
    val laptopResult = service.classifyMaterial(null, "Dell office laptops", 10.0, "Mumbai")
    assertEquals("Laptops", laptopResult.category)
    assertTrue(laptopResult.confidence > 90)

    val phoneResult = service.classifyMaterial(null, "old samsung smartphone", 1.0, "Delhi")
    assertEquals("Mobile Phones", phoneResult.category)
  }

  @Test
  fun `test price estimation service calculation`() = runBlocking {
    val service = MockPriceEstimationService()
    val estimate = service.estimatePrice(
      category = "Laptops",
      subCategory = "Laptop Computer",
      condition = MaterialCondition.WORKING,
      weightKg = 10.0,
      location = "Mumbai"
    )
    assertNotNull(estimate)
    assertTrue(estimate.estimatedPricePerKg > 0)
    assertTrue(estimate.estimatedTotalValue > 0)
  }

  @Test
  fun `test route optimization calculates shortest tour`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.ui.viewmodel.KabadiwalaViewModel(context)
    val testPickups = listOf(
      com.example.data.model.PickupRequestEntity(
        id = 1L, lotId = "L1", collectorId = "c1", collectorName = "C", collectorPhone = "123",
        recyclerId = "r1", recyclerName = "R", materialCategory = "Laptops", weightKg = 10.0,
        agreedPricePerKg = 200.0, totalValue = 2000.0, pickupAddress = "Andheri",
        gpsLat = 19.1136, gpsLng = 72.8697, preferredDateTime = "Today", status = "ACCEPTED"
      ),
      com.example.data.model.PickupRequestEntity(
        id = 2L, lotId = "L2", collectorId = "c1", collectorName = "C", collectorPhone = "123",
        recyclerId = "r1", recyclerName = "R", materialCategory = "Batteries", weightKg = 20.0,
        agreedPricePerKg = 150.0, totalValue = 3000.0, pickupAddress = "Bandra",
        gpsLat = 19.0596, gpsLng = 72.8295, preferredDateTime = "Today", status = "ACCEPTED"
      )
    )
    val result = vm.optimizeRouteForPickups(testPickups)
    assertEquals(2, result.stops.size)
    assertTrue(result.totalDistanceKm > 0.0)
    assertEquals(30.0, result.totalWeightKg, 0.1)
    assertEquals(5000.0, result.totalEstimatedValue, 0.1)
  }
}
