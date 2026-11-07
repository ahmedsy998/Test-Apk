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

    // 7. Promotional Banners have distinct valid drawable image resources
    val banners = com.example.model.SyriaTelSampleData.promoBanners
    assertEquals(6, banners.size)
    assertEquals(6, banners.map { it.imageRes }.distinct().size)

    // 8. Multi-Account System: Add second account, verify isolated data, switch back and forth, and remove
    val account1Balance = vm.uiState.value.balanceSyp
    val account1Phone = vm.uiState.value.userProfile.phoneNumber
    assertTrue(vm.uiState.value.canAddAnotherAccount)
    assertEquals(1, vm.uiState.value.savedAccounts.size)

    vm.addSecondAccount("سارة", "الحموي", "0988654321")
    assertEquals(2, vm.uiState.value.savedAccounts.size)
    assertFalse(vm.uiState.value.canAddAnotherAccount)
    assertEquals("acc_2", vm.uiState.value.activeAccountId)
    assertEquals("0988654321", vm.uiState.value.userProfile.phoneNumber)
    assertNotEquals(account1Balance, vm.uiState.value.balanceSyp)

    // Recharge on Account 2 should not affect Account 1
    val acc2InitialBal = vm.uiState.value.balanceSyp
    vm.rechargeBalance("0988654321", 5000)
    assertEquals(acc2InitialBal + 5000, vm.uiState.value.balanceSyp)

    // Switch back to Account 1
    vm.switchAccount("acc_1")
    assertEquals("acc_1", vm.uiState.value.activeAccountId)
    assertEquals(account1Phone, vm.uiState.value.userProfile.phoneNumber)
    assertEquals(account1Balance, vm.uiState.value.balanceSyp)

    // Remove inactive Account 2
    vm.removeAccount("acc_2")
    assertEquals(1, vm.uiState.value.savedAccounts.size)
    assertTrue(vm.uiState.value.canAddAnotherAccount)
  }
}
