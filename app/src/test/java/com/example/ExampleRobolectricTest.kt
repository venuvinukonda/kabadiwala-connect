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
}
