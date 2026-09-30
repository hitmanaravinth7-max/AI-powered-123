package com.example.data.repository

import com.example.data.model.BusinessPreset
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.ExpenseRecordEntity
import com.example.data.model.MarketingMetricEntity
import com.example.data.model.RecommendationEntity
import com.example.data.model.SalesRecordEntity

object SampleDataGenerator {

    fun generatePreset(preset: BusinessPreset): PresetBundle {
        return when (preset) {
            BusinessPreset.ECOMMERCE -> generateEcommercePreset()
            BusinessPreset.SAAS -> generateSaasPreset()
            BusinessPreset.CAFE -> generateCafePreset()
            BusinessPreset.CONSULTING -> generateConsultingPreset()
        }
    }

    private fun generateEcommercePreset(): PresetBundle {
        val profile = BusinessProfileEntity(
            id = 1L,
            businessName = "UrbanStyle Apparel",
            industry = "E-Commerce & Retail",
            monthlyRevenueTarget = 85000.0,
            monthlyBudget = 52000.0,
            growthGoal = "Scale Revenue to $100k/mo & Boost Customer Lifetime Value",
            targetAudience = "Urban professionals aged 24-42 seeking premium sustainable clothing",
            teamSize = "6-15 (Growing team)",
            primaryChannel = "Meta Ads & Direct Web Storefront",
            currencySymbol = "$"
        )

        val products = listOf(
            Triple("Merino Wool Knitwear", "Apparel", 125.0),
            Triple("Selvedge Denim Jeans", "Apparel", 110.0),
            Triple("Canvas Weekender Duffle", "Accessories", 145.0),
            Triple("Classic Tailored Oxford", "Apparel", 85.0),
            Triple("Minimalist Leather Belt", "Accessories", 55.0)
        )

        val segments = listOf("Champions", "Loyal", "At-Risk", "New", "Hibernating")
        val channels = listOf("Meta Ads", "Google Search", "Email Newsletter", "Organic Direct", "Referral")

        val sales = mutableListOf<SalesRecordEntity>()
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep")
        val baseRev = listOf(48000.0, 51500.0, 54200.0, 56800.0, 61000.0, 59500.0, 64200.0, 67800.0, 71400.0)

        for (m in months.indices) {
            val totalForMonth = baseRev[m]
            val recordCount = 14
            val portion = totalForMonth / recordCount
            for (i in 0 until recordCount) {
                val prod = products[i % products.size]
                val channel = channels[i % channels.size]
                val seg = segments[(i + m) % segments.size]
                val variance = 0.85 + (i * 0.02)
                sales.add(
                    SalesRecordEntity(
                        date = "2026-0${m + 1}-${10 + (i % 18)}",
                        monthIndex = m + 1,
                        productName = prod.first,
                        category = prod.second,
                        channel = channel,
                        amount = portion * variance,
                        customerSegment = seg,
                        unitsSold = (portion * variance / prod.third).toInt().coerceAtLeast(1)
                    )
                )
            }
        }

        val expenses = listOf(
            ExpenseRecordEntity(date = "2026-09-01", monthIndex = 9, category = "Marketing & Ads", amount = 16800.0, description = "Meta & Google Ads Campaign Spend", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-02", monthIndex = 9, category = "COGS / Inventory", amount = 22400.0, description = "Autumn Apparel Manufacturing & Materials", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-03", monthIndex = 9, category = "Salaries & Payroll", amount = 9500.0, description = "Design, Support & Fulfillment Staff", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-04", monthIndex = 9, category = "Software & Tools", amount = 2100.0, description = "Shopify Plus, Klaviyo, Gorgias CRM", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-05", monthIndex = 9, category = "Rent & Utilities", amount = 2800.0, description = "Micro-fulfillment Warehouse & Studio", isRecurring = true)
        )

        val marketing = listOf(
            MarketingMetricEntity(channel = "Meta Ads (IG/FB)", spend = 11200.0, conversions = 380, revenueGenerated = 39800.0, cac = 29.47),
            MarketingMetricEntity(channel = "Google Shopping / Search", spend = 4200.0, conversions = 165, revenueGenerated = 18400.0, cac = 25.45),
            MarketingMetricEntity(channel = "Klaviyo Email Flows", spend = 950.0, conversions = 290, revenueGenerated = 22100.0, cac = 3.28),
            MarketingMetricEntity(channel = "TikTok Influencers", spend = 2400.0, conversions = 68, revenueGenerated = 6900.0, cac = 35.29)
        )

        val recommendations = listOf(
            RecommendationEntity(
                title = "Scale High-ROI Email Automations",
                category = "MARKETING",
                impact = "HIGH",
                effort = "LOW",
                estimatedImpact = "+$6,800/mo incremental",
                description = "Klaviyo flows yield a 23.2x ROI (CAC of only $3.28). Adding browse abandonment and cross-sell post-purchase flows can capture high-intent shoppers with minimal cost.",
                actionSteps = "1. Deploy 3-stage browse abandonment email; 2. Add SMS VIP exclusive early-access; 3. Offer 10% bundle incentive 14 days post-delivery.",
                status = "PENDING",
                priorityScore = 9
            ),
            RecommendationEntity(
                title = "Bundle Oxford Shirts with Selvedge Denim",
                category = "REVENUE",
                impact = "HIGH",
                effort = "LOW",
                estimatedImpact = "+18% Average Order Value",
                description = "41% of denim buyers purchase a second top within 45 days. Bundling them at a $175 package (saving $20) will instantly lift initial basket size.",
                actionSteps = "1. Create 'Capsule Essentials' bundle on product page; 2. Set 1-click upsell during cart drawer checkout; 3. Feature bundle in welcome email series.",
                status = "IN_PROGRESS",
                priorityScore = 8
            ),
            RecommendationEntity(
                title = "Trim Underperforming TikTok Campaign Spend",
                category = "COST_REDUCTION",
                impact = "MEDIUM",
                effort = "LOW",
                estimatedImpact = "Save $1,400/mo ad waste",
                description = "TikTok CAC sits at $35.29 with a 2.87x ROAS, trailing Meta Ads (3.55x ROAS). Shift 60% of TikTok budget to retargeting high-performing Meta creative.",
                actionSteps = "1. Pause bottom 3 TikTok ad creatives; 2. Cap TikTok testing budget at $1,000/mo; 3. Move $1,400 to top-funnel Instagram Reels lookalikes.",
                status = "PENDING",
                priorityScore = 7
            ),
            RecommendationEntity(
                title = "Launch Re-engagement for 312 At-Risk Customers",
                category = "RISK_MITIGATION",
                impact = "HIGH",
                effort = "MEDIUM",
                estimatedImpact = "Recover ~$4,200 in churned GMV",
                description = "Customer segmentation indicates 312 once-active buyers haven't purchased in 90+ days. Without intervention, 85% will lapse permanently.",
                actionSteps = "1. Trigger 'We miss you' personalized email offering free shipping; 2. Segment by past category preference; 3. Offer time-sensitive gift voucher.",
                status = "PENDING",
                priorityScore = 8
            ),
            RecommendationEntity(
                title = "Consolidate Packaging & Negotiate 3PL Tier",
                category = "COST_REDUCTION",
                impact = "MEDIUM",
                effort = "MEDIUM",
                estimatedImpact = "Improve gross margin by 2.4%",
                description = "Volume over the last 90 days crossed 1,200 monthly shipments, qualifying for Tier 2 fulfillment and bulk recycled mailer pricing.",
                actionSteps = "1. Audit current packaging unit economics; 2. Request Tier 2 rate card from fulfillment partner; 3. Commit to 6-month bulk volume for discount.",
                status = "COMPLETED",
                priorityScore = 6
            )
        )

        return PresetBundle(profile, sales, expenses, marketing, recommendations)
    }

    private fun generateSaasPreset(): PresetBundle {
        val profile = BusinessProfileEntity(
            id = 1L,
            businessName = "CloudFlow Solutions",
            industry = "SaaS & Software",
            monthlyRevenueTarget = 60000.0,
            monthlyBudget = 38000.0,
            growthGoal = "Reach $65k MRR & Cut User Churn Below 2%",
            targetAudience = "Remote tech teams and mid-market project operations",
            teamSize = "6-15 (Growing team)",
            primaryChannel = "Inbound Content & Google Search",
            currencySymbol = "$"
        )

        val products = listOf(
            Triple("Enterprise Tier Annual", "Subscription", 499.0),
            Triple("Team Pro Monthly", "Subscription", 99.0),
            Triple("Starter Team Seat", "Subscription", 29.0),
            Triple("API Integration Pack", "Add-on", 149.0)
        )

        val sales = mutableListOf<SalesRecordEntity>()
        val baseRev = listOf(29000.0, 31200.0, 33500.0, 35800.0, 38100.0, 40400.0, 42900.0, 45200.0, 48500.0)

        for (m in baseRev.indices) {
            val totalForMonth = baseRev[m]
            val recordCount = 10
            val portion = totalForMonth / recordCount
            for (i in 0 until recordCount) {
                val prod = products[i % products.size]
                sales.add(
                    SalesRecordEntity(
                        date = "2026-0${m + 1}-${12 + (i % 15)}",
                        monthIndex = m + 1,
                        productName = prod.first,
                        category = prod.second,
                        channel = if (i % 2 == 0) "Direct Web Signup" else "Sales Demo",
                        amount = portion,
                        customerSegment = if (i < 4) "Champions" else "Loyal",
                        unitsSold = (portion / prod.third).toInt().coerceAtLeast(1)
                    )
                )
            }
        }

        val expenses = listOf(
            ExpenseRecordEntity(date = "2026-09-01", monthIndex = 9, category = "Salaries & Payroll", amount = 19500.0, description = "Engineering, Product & Customer Success", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-02", monthIndex = 9, category = "Software & Tools", amount = 6800.0, description = "AWS Cloud Servers, Datadog & OpenAI API", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-03", monthIndex = 9, category = "Marketing & Ads", amount = 5400.0, description = "Google SEM & LinkedIn Ads", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-04", monthIndex = 9, category = "Rent & Utilities", amount = 2200.0, description = "WeWork Coworking & Fiber Internet", isRecurring = true)
        )

        val marketing = listOf(
            MarketingMetricEntity(channel = "Google Search SEM", spend = 3200.0, conversions = 45, revenueGenerated = 18500.0, cac = 71.11),
            MarketingMetricEntity(channel = "LinkedIn Sponsored", spend = 2200.0, conversions = 18, revenueGenerated = 9400.0, cac = 122.22),
            MarketingMetricEntity(channel = "ProductLed Inbound", spend = 800.0, conversions = 110, revenueGenerated = 20600.0, cac = 7.27)
        )

        val recommendations = listOf(
            RecommendationEntity(
                title = "Incentivize Annual Upfront Conversions",
                category = "REVENUE",
                impact = "HIGH",
                effort = "LOW",
                estimatedImpact = "+$28,000 Immediate Cashflow",
                description = "Offering 2 months free (16.7% discount) for upfront annual commitment locks in retention and reduces involuntary credit card churn.",
                actionSteps = "1. Add Annual toggle defaulted to ON on pricing page; 2. Send upgrade prompt to users on day 45 of monthly billing.",
                status = "PENDING",
                priorityScore = 9
            ),
            RecommendationEntity(
                title = "Deploy Day-3 Activation Checklist in App",
                category = "RISK_MITIGATION",
                impact = "HIGH",
                effort = "MEDIUM",
                estimatedImpact = "-35% Trial Drop-off",
                description = "Users who connect at least 1 integration within 72 hours show an 82% 90-day retention versus 28% for those who do not.",
                actionSteps = "1. Build 4-step checklist: invite colleague, connect Slack, create first workflow; 2. Add in-app guidance modal.",
                status = "PENDING",
                priorityScore = 8
            )
        )

        return PresetBundle(profile, sales, expenses, marketing, recommendations)
    }

    private fun generateCafePreset(): PresetBundle {
        val profile = BusinessProfileEntity(
            id = 1L,
            businessName = "Artisan Roast & Bakery",
            industry = "Food & Hospitality",
            monthlyRevenueTarget = 35000.0,
            monthlyBudget = 24000.0,
            growthGoal = "Expand Morning Catering & Boost Weekend Footfall",
            targetAudience = "Neighborhood residents, telecommuters, and specialty coffee lovers",
            teamSize = "6-15 (Baristas & Bakers)",
            primaryChannel = "Physical Storefront & Local Google Maps",
            currencySymbol = "$"
        )

        val products = listOf(
            Triple("Single Origin Pour Over", "Coffee", 6.50),
            Triple("Signature Cold Brew Bottle", "Coffee", 7.00),
            Triple("Almond Croissant & Pastries", "Bakery", 5.50),
            Triple("Whole Bean 250g Retail Bag", "Retail", 18.00),
            Triple("Breakfast Brioche Sandwich", "Food", 9.50)
        )

        val sales = mutableListOf<SalesRecordEntity>()
        val baseRev = listOf(22000.0, 23500.0, 24800.0, 26200.0, 27500.0, 28900.0, 29400.0, 30800.0, 32400.0)

        for (m in baseRev.indices) {
            val totalForMonth = baseRev[m]
            val recordCount = 10
            val portion = totalForMonth / recordCount
            for (i in 0 until recordCount) {
                val prod = products[i % products.size]
                sales.add(
                    SalesRecordEntity(
                        date = "2026-0${m + 1}-${10 + (i % 16)}",
                        monthIndex = m + 1,
                        productName = prod.first,
                        category = prod.second,
                        channel = "Point of Sale (Square)",
                        amount = portion,
                        customerSegment = if (i % 3 == 0) "Champions" else "Loyal",
                        unitsSold = (portion / prod.third).toInt().coerceAtLeast(1)
                    )
                )
            }
        }

        val expenses = listOf(
            ExpenseRecordEntity(date = "2026-09-01", monthIndex = 9, category = "COGS / Inventory", amount = 9800.0, description = "Green Coffee Beans, Dairy & Organic Flour", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-02", monthIndex = 9, category = "Salaries & Payroll", amount = 8900.0, description = "Head Baker, Baristas & Shift Leads", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-03", monthIndex = 9, category = "Rent & Utilities", amount = 4200.0, description = "Downtown Lease, Commercial Power & Water", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-04", monthIndex = 9, category = "Marketing & Ads", amount = 650.0, description = "Local Instagram Geo-targeting & Loyalty App", isRecurring = true)
        )

        val marketing = listOf(
            MarketingMetricEntity(channel = "Local Google Business", spend = 150.0, conversions = 420, revenueGenerated = 14200.0, cac = 0.36),
            MarketingMetricEntity(channel = "Instagram Geo-ads", spend = 500.0, conversions = 110, revenueGenerated = 4800.0, cac = 4.54)
        )

        val recommendations = listOf(
            RecommendationEntity(
                title = "Launch Corporate Office Coffee & Pastry Subscription",
                category = "REVENUE",
                impact = "HIGH",
                effort = "MEDIUM",
                estimatedImpact = "+$4,500/mo recurring",
                description = "Local law firms and tech offices within 1 mile spend $300-$800/mo on morning meetings. Offering a weekly scheduled pastry + cold brew drop creates predictable B2B cashflow.",
                actionSteps = "1. Create simple 2-page catering menu PDF; 2. Sample 10 target offices with complimentary breakfast box; 3. Offer corporate invoicing.",
                status = "PENDING",
                priorityScore = 9
            )
        )

        return PresetBundle(profile, sales, expenses, marketing, recommendations)
    }

    private fun generateConsultingPreset(): PresetBundle {
        val profile = BusinessProfileEntity(
            id = 1L,
            businessName = "Vanguard Growth Advisory",
            industry = "Professional Services",
            monthlyRevenueTarget = 50000.0,
            monthlyBudget = 26000.0,
            growthGoal = "Double Monthly Retainer Clients to 8 & Hire Senior Strategist",
            targetAudience = "Series A/B Startups and high-growth mid-market executives",
            teamSize = "1-5 (Boutique agency)",
            primaryChannel = "B2B Direct Referrals & Thought Leadership",
            currencySymbol = "$"
        )

        val products = listOf(
            Triple("Growth Strategy Retainer", "Advisory", 7500.0),
            Triple("Executive AI Diagnostic Audit", "Project", 5000.0),
            Triple("Fractional CMO Sprint", "Advisory", 9000.0)
        )

        val sales = mutableListOf<SalesRecordEntity>()
        val baseRev = listOf(28000.0, 30500.0, 32000.0, 35000.0, 37500.0, 39000.0, 42000.0, 44000.0, 46500.0)

        for (m in baseRev.indices) {
            val totalForMonth = baseRev[m]
            val recordCount = 4
            val portion = totalForMonth / recordCount
            for (i in 0 until recordCount) {
                val prod = products[i % products.size]
                sales.add(
                    SalesRecordEntity(
                        date = "2026-0${m + 1}-${15 + (i % 10)}",
                        monthIndex = m + 1,
                        productName = prod.first,
                        category = prod.second,
                        channel = "Referral / Network",
                        amount = portion,
                        customerSegment = "Champions",
                        unitsSold = 1
                    )
                )
            }
        }

        val expenses = listOf(
            ExpenseRecordEntity(date = "2026-09-01", monthIndex = 9, category = "Salaries & Payroll", amount = 14500.0, description = "Principal Consultant & Associate Analyst", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-02", monthIndex = 9, category = "Software & Tools", amount = 1800.0, description = "Pitch, Figma, Slack, PitchBook & Zoom", isRecurring = true),
            ExpenseRecordEntity(date = "2026-09-03", monthIndex = 9, category = "Marketing & Ads", amount = 2200.0, description = "LinkedIn Content, Podcast Sponsorship", isRecurring = true)
        )

        val marketing = listOf(
            MarketingMetricEntity(channel = "LinkedIn Thought Leadership", spend = 1200.0, conversions = 4, revenueGenerated = 28000.0, cac = 300.0),
            MarketingMetricEntity(channel = "Partner Referral Commission", spend = 1000.0, conversions = 3, revenueGenerated = 18500.0, cac = 333.33)
        )

        val recommendations = listOf(
            RecommendationEntity(
                title = "Productize AI Readiness Audit as Low-Barrier Entry",
                category = "REVENUE",
                impact = "HIGH",
                effort = "LOW",
                estimatedImpact = "3x Lead Velocity",
                description = "High ticket retainers ($7.5k+) have long sales cycles (60+ days). A fixed-price $3,500 5-day audit converts warm leads fast and serves as a high-margin gateway.",
                actionSteps = "1. Package 5-day audit deliverables into standardized template; 2. Offer to warm pipeline leads with money-back guarantee.",
                status = "PENDING",
                priorityScore = 9
            )
        )

        return PresetBundle(profile, sales, expenses, marketing, recommendations)
    }
}

data class PresetBundle(
    val profile: BusinessProfileEntity,
    val sales: List<SalesRecordEntity>,
    val expenses: List<ExpenseRecordEntity>,
    val marketing: List<MarketingMetricEntity>,
    val recommendations: List<RecommendationEntity>
)
