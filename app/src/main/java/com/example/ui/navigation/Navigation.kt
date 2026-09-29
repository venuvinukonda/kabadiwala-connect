package com.example.ui.navigation

import com.example.data.model.UserRole

sealed class AppScreen {
    object RoleSelection : AppScreen()
    data class Login(val selectedRole: UserRole) : AppScreen()
    object CollectorRegister : AppScreen()
    object RecyclerRegister : AppScreen()
    data class Register(val initialRole: UserRole? = null) : AppScreen()
    object CollectorHome : AppScreen()
    object CollectEWasteWizard : AppScreen()
    object MyLotsList : AppScreen()
    object CollectorPickups : AppScreen()
    object CollectorPickupsMap : AppScreen()
    object CollectorEarnings : AppScreen()
    object TransactionHistory : AppScreen()
    object PriceHistory : AppScreen()
    object SafetyInstructions : AppScreen()
    object RecyclerHome : AppScreen()
    object RecyclerLotRequests : AppScreen()
    object RecyclerRatesConfig : AppScreen()
    object RecyclerCertificate : AppScreen()
    object AdminHome : AppScreen()
    object AdminUserApprovals : AppScreen()
    object AdminTransactions : AppScreen()
    object AdminAnalytics : AppScreen()
    data class HandoverScreen(val pickupRequestId: Long) : AppScreen()
    object ProfileView : AppScreen()
}
