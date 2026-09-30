package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.SalesRecordEntity
import com.example.data.repository.BusinessAnalyticsEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BizConsult", appName)
  }

  @Test
  fun `verify business analytics calculation`() {
    val profile = BusinessProfileEntity(
      businessName = "Test Corp",
      industry = "Tech",
      monthlyRevenueTarget = 10000.0,
      monthlyBudget = 5000.0,
      growthGoal = "Scale",
      targetAudience = "SMB",
      teamSize = "1-5",
      primaryChannel = "Direct"
    )
    val sales = listOf(
      SalesRecordEntity(
        date = "2026-09-01",
        monthIndex = 9,
        productName = "SaaS Pro",
        category = "Subscription",
        channel = "Direct",
        amount = 5000.0,
        customerSegment = "Champions",
        unitsSold = 5
      )
    )

    val kpi = BusinessAnalyticsEngine.calculateKpiSummary(
      profile = profile,
      sales = sales,
      expenses = emptyList(),
      marketing = emptyList()
    )

    assertEquals(5000.0, kpi.totalRevenue, 0.01)
    assertEquals(100.0, kpi.profitMarginPct, 0.01)
    assertTrue(kpi.totalTransactions == 1)
  }
}
