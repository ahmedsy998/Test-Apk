package com.example

import com.example.model.AppScreen
import com.example.model.OfferCategory
import com.example.model.OperationCategory
import com.example.viewmodel.SyriaTelViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testCrossScreenSynchronizationAndOperationsLogging() {
    val vm = SyriaTelViewModel()

    // 1. Splash -> Login -> Home
    assertEquals(AppScreen.SPLASH, vm.uiState.value.currentScreen)
    vm.onSplashFinished()
    assertEquals(AppScreen.LOGIN, vm.uiState.value.currentScreen)

    vm.completeLogin("0933998877", true)
    assertEquals(AppScreen.HOME, vm.uiState.value.currentScreen)
    assertEquals("0933998877", vm.uiState.value.userProfile.phoneNumber)

    // 2. Balance Recharge updates balance & logs operation
    val initialBalance = vm.uiState.value.balanceSyp
    val initialTxCount = vm.uiState.value.transactions.size
    vm.rechargeBalance("0933998877", 10000)
    assertEquals(initialBalance + 10000, vm.uiState.value.balanceSyp)
    assertEquals(initialTxCount + 1, vm.uiState.value.transactions.size)
    assertEquals(OperationCategory.RECHARGE, vm.uiState.value.transactions.first().category)

    // 3. Service Toggle updates active count & logs operation
    val initialActiveCount = vm.uiState.value.activeServicesCount
    val firstService = vm.uiState.value.services.first()
    vm.toggleServiceStatus(firstService.id)
    assertEquals(initialActiveCount - 1, vm.uiState.value.activeServicesCount)
    assertEquals(OperationCategory.SERVICES, vm.uiState.value.transactions.first().category)

    // 4. Offer Subscription updates package data & logs operation
    val netOffer = vm.uiState.value.offers.first { it.category == OfferCategory.INTERNET }
    val prevNetTotal = vm.uiState.value.internetTotalGb
    vm.subscribeToOffer(netOffer)
    assertEquals(prevNetTotal + netOffer.internetBonusGb, vm.uiState.value.internetTotalGb, 0.01f)
    assertEquals(OperationCategory.PACKAGES, vm.uiState.value.transactions.first().category)

    // 5. Profile Update synchronizes user info & logs operation
    vm.updateProfile("سامر", "العلي", 2)
    assertEquals("سامر", vm.uiState.value.userProfile.firstName)
    assertEquals("العلي", vm.uiState.value.userProfile.lastName)

    // 6. Language & Dark Mode toggles
    vm.setLanguage(false)
    assertFalse(vm.uiState.value.isArabic)
    vm.toggleDarkMode()
    assertTrue(vm.uiState.value.isDarkMode)
  }
}
