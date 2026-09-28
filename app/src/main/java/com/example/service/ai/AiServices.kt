package com.example.service.ai

import com.example.data.model.MaterialCondition
import com.example.data.model.RecyclerProfileEntity
import kotlin.math.*

// =========================================================================
// 1. MATERIAL CLASSIFICATION SERVICE
// =========================================================================

data class MaterialClassificationResult(
    val category: String,
    val subcategory: String,
    val confidence: Int // e.g. 94%
)

/**
 * Clean pluggable interface for AI Material Classification.
 * Replace with real backend or on-device model when available.
 */
interface MaterialClassificationService {
    // TODO: Integrate custom material classification model
    suspend fun classifyMaterial(
        photoUri: String?,
        description: String?,
        weightKg: Double?,
        location: String?
    ): MaterialClassificationResult
}

class MockMaterialClassificationService : MaterialClassificationService {
    // TODO: Integrate custom material classification model
    override suspend fun classifyMaterial(
        photoUri: String?,
        description: String?,
        weightKg: Double?,
        location: String?
    ): MaterialClassificationResult {
        val desc = (description ?: "").lowercase()
        return when {
            desc.contains("laptop") || desc.contains("notebook") || desc.contains("macbook") ->
                MaterialClassificationResult("Laptops", "Laptop Computer / Notebook", 94)
            desc.contains("phone") || desc.contains("mobile") || desc.contains("smartphone") ->
                MaterialClassificationResult("Mobile Phones", "Smartphones & Feature Phones", 96)
            desc.contains("battery") || desc.contains("cell") || desc.contains("li-ion") ->
                MaterialClassificationResult("Batteries", "Lithium-Ion / Lead Acid Batteries", 91)
            desc.contains("board") || desc.contains("pcb") || desc.contains("motherboard") ->
                MaterialClassificationResult("Circuit Boards", "Printed Circuit Boards (PCB)", 95)
            desc.contains("cable") || desc.contains("wire") || desc.contains("cord") ->
                MaterialClassificationResult("Cables", "Copper Core & Insulated Wiring", 89)
            desc.contains("tv") || desc.contains("television") || desc.contains("screen") || desc.contains("monitor") ->
                MaterialClassificationResult("Televisions", "LED / LCD / CRT Display Units", 93)
            desc.contains("print") || desc.contains("cartridge") ->
                MaterialClassificationResult("Printers", "LaserJet / Inkjet Printers & Scanners", 88)
            desc.contains("fridge") || desc.contains("refrigerator") ->
                MaterialClassificationResult("Refrigerators", "Cooling Equipment / Compressors", 90)
            desc.contains("wash") ->
                MaterialClassificationResult("Washing Machines", "Appliance Motors & Controls", 89)
            desc.contains("charge") || desc.contains("adapter") ->
                MaterialClassificationResult("Chargers", "AC/DC Power Adapters & Chargers", 92)
            desc.contains("computer") || desc.contains("pc") || desc.contains("cpu") ->
                MaterialClassificationResult("Computers", "Desktop CPU Towers & Servers", 94)
            else -> {
                // If weight is high, suggest Computer/Appliance; if small, suggest Mobile/Laptop
                val weight = weightKg ?: 5.0
                if (weight > 15.0) {
                    MaterialClassificationResult("Computers", "Desktop CPU Towers & Servers", 87)
                } else if (weight > 5.0) {
                    MaterialClassificationResult("Laptops", "Laptop Computer", 92)
                } else {
                    MaterialClassificationResult("Mobile Phones", "Mobile Handsets & Tablets", 93)
                }
            }
        }
    }
}


// =========================================================================
// 2. PRICE ESTIMATION SERVICE
// =========================================================================

data class PriceEstimateResult(
    val estimatedPricePerKg: Double,
    val estimatedTotalValue: Double,
    val minPricePerKg: Double,
    val maxPricePerKg: Double,
    val confidence: Int, // e.g. 92%
    val isGuaranteed: Boolean = false // As per spec: "Clearly mark this as AI/ML Estimated Price. Do not claim the estimate is guaranteed."
)

/**
 * Clean pluggable interface for ML Price Prediction.
 * Replace with real backend model when available.
 */
interface PriceEstimationService {
    // TODO: Integrate custom price prediction model
    suspend fun estimatePrice(
        category: String,
        subCategory: String,
        condition: MaterialCondition,
        weightKg: Double,
        location: String
    ): PriceEstimateResult
}

class MockPriceEstimationService : PriceEstimationService {
    // Baseline raw commodity rates in INR per kg for e-waste in India
    private val baseRates = mapOf(
        "Laptops" to 280.0,
        "Computers" to 220.0,
        "Mobile Phones" to 350.0,
        "Circuit Boards" to 420.0,
        "Batteries" to 140.0,
        "Cables" to 190.0,
        "Televisions" to 85.0,
        "Printers" to 65.0,
        "Refrigerators" to 45.0,
        "Washing Machines" to 50.0,
        "Chargers" to 110.0,
        "Other Electronics" to 75.0
    )

    // TODO: Integrate custom price prediction model
    override suspend fun estimatePrice(
        category: String,
        subCategory: String,
        condition: MaterialCondition,
        weightKg: Double,
        location: String
    ): PriceEstimateResult {
        val base = baseRates[category] ?: 100.0
        val adjustedPricePerKg = round(base * condition.priceMultiplier)
        val totalValue = round(adjustedPricePerKg * max(weightKg, 0.1))
        val minPrice = round(adjustedPricePerKg * 0.9)
        val maxPrice = round(adjustedPricePerKg * 1.15)

        return PriceEstimateResult(
            estimatedPricePerKg = adjustedPricePerKg,
            estimatedTotalValue = totalValue,
            minPricePerKg = minPrice,
            maxPricePerKg = maxPrice,
            confidence = 91,
            isGuaranteed = false
        )
    }
}


// =========================================================================
// 3. RECYCLER MATCHING SYSTEM
// =========================================================================

data class RecyclerMatchResult(
    val recycler: RecyclerProfileEntity,
    val distanceKm: Double,
    val offeredPricePerKg: Double,
    val totalEstimatedPayout: Double,
    val matchScore: Int,
    val pickupAvailable: Boolean,
    val meetsMinQuantity: Boolean
)

/**
 * Clean pluggable interface for Recycler Ranking.
 * Replace with real backend ranking model when available.
 */
interface RecyclerMatchingService {
    // TODO: Integrate custom recycler ranking model
    suspend fun findMatchingRecyclers(
        category: String,
        weightKg: Double,
        collectorLat: Double,
        collectorLng: Double,
        allRecyclers: List<RecyclerProfileEntity>
    ): List<RecyclerMatchResult>
}

class MockRecyclerMatchingService : RecyclerMatchingService {
    // TODO: Integrate custom recycler ranking model
    override suspend fun findMatchingRecyclers(
        category: String,
        weightKg: Double,
        collectorLat: Double,
        collectorLng: Double,
        allRecyclers: List<RecyclerProfileEntity>
    ): List<RecyclerMatchResult> {
        return allRecyclers
            // Only approved and verified recyclers appear
            .filter { it.certStatus == "APPROVED" }
            .map { recycler ->
                // Calculate distance using haversine formula
                val dist = calculateDistance(collectorLat, collectorLng, recycler.gpsLat, recycler.gpsLng)
                val acceptedList = recycler.acceptedCategoriesCsv.split(",").map { it.trim() }
                val isCategoryAccepted = acceptedList.any { it.equals(category, ignoreCase = true) || it.equals("All", ignoreCase = true) }

                // Parse offered rate from ratesJson or fallback
                val offeredRate = parseRate(recycler.ratesJson, category)
                val meetsMin = weightKg >= recycler.minQuantityKg

                // Score based on distance, rate, and availability
                var score = 70
                if (isCategoryAccepted) score += 15
                if (meetsMin) score += 10
                if (recycler.pickupAvailable && dist <= recycler.pickupRadiusKm) score += 10
                if (dist < 10.0) score += 5

                RecyclerMatchResult(
                    recycler = recycler,
                    distanceKm = round(dist * 10.0) / 10.0,
                    offeredPricePerKg = offeredRate,
                    totalEstimatedPayout = round(offeredRate * weightKg),
                    matchScore = min(score, 99),
                    pickupAvailable = recycler.pickupAvailable && dist <= recycler.pickupRadiusKm,
                    meetsMinQuantity = meetsMin
                )
            }
            .filter { it.matchScore >= 50 } // Must have basic compatibility
            .sortedByDescending { it.offeredPricePerKg }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        if (lat1 == 0.0 || lat2 == 0.0) return 4.5 // Default reasonable city distance
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    private fun parseRate(ratesJson: String, category: String): Double {
        // Simple parser for key:value or map representation
        val clean = ratesJson.replace("{", "").replace("}", "").replace("\"", "")
        val pairs = clean.split(",")
        for (pair in pairs) {
            val parts = pair.split(":")
            if (parts.size == 2 && parts[0].trim().equals(category, ignoreCase = true)) {
                return parts[1].trim().toDoubleOrNull() ?: 240.0
            }
        }
        return 220.0
    }
}
