export const PRESETS = {
  ECOMMERCE: {
    id: 'ECOMMERCE',
    title: 'UrbanStyle Apparel',
    subtitle: 'Direct-to-Consumer Sustainable Fashion',
    industry: 'E-Commerce & Retail',
    currency: '$',
    monthlyTarget: 85000,
    monthlyBudget: 52000,
    growthGoal: 'Scale Revenue to $100k/mo & Boost Customer Lifetime Value',
    targetAudience: 'Urban professionals aged 24-42 seeking premium sustainable clothing',
    teamSize: '6-15 (Growing team)',
    primaryChannel: 'Meta Ads & Direct Web Storefront',
    metrics: {
      totalRevenue: 71400,
      totalExpenses: 53600,
      netProfit: 17800,
      profitMargin: 24.9,
      revenueGrowth: 5.3,
      avgOrderValue: 84.50,
      totalOrders: 845,
      marketingSpend: 18750,
      marketingRoi: 3.81,
      riskLevel: 'LOW',
      riskScore: 22,
      riskFactors: [
        'Marketing reliance on Meta Ads (60% of total paid acquisition).',
        'Autumn inventory wholesale advance requires 45-day cash commitment.'
      ]
    },
    monthlyTrends: [
      { month: 'Jan', revenue: 48000, expenses: 38500, profit: 9500 },
      { month: 'Feb', revenue: 51500, expenses: 40200, profit: 11300 },
      { month: 'Mar', revenue: 54200, expenses: 42100, profit: 12100 },
      { month: 'Apr', revenue: 56800, expenses: 44000, profit: 12800 },
      { month: 'May', revenue: 61000, expenses: 47200, profit: 13800 },
      { month: 'Jun', revenue: 59500, expenses: 46800, profit: 12700 },
      { month: 'Jul', revenue: 64200, expenses: 49500, profit: 14700 },
      { month: 'Aug', revenue: 67800, expenses: 51400, profit: 16400 },
      { month: 'Sep', revenue: 71400, expenses: 53600, profit: 17800 },
      { month: 'Oct*', revenue: 75200, expenses: 55800, profit: 19400, isForecast: true },
      { month: 'Nov*', revenue: 79600, expenses: 58200, profit: 21400, isForecast: true },
      { month: 'Dec*', revenue: 86400, expenses: 62000, profit: 24400, isForecast: true }
    ],
    customerSegments: [
      { name: 'Champions', count: 214, percentage: 38.2, spend: 27280, color: '#10B981', action: 'VIP early catalog drops & private loyalty rewards' },
      { name: 'Loyal', count: 328, percentage: 29.5, spend: 21060, color: '#0284C7', action: 'Bundle cross-sells on complementary categories' },
      { name: 'New', count: 185, percentage: 16.4, spend: 11710, color: '#8B5CF6', action: 'Automated 14-day post-delivery onboarding series' },
      { name: 'At-Risk', count: 88, percentage: 11.2, spend: 7990, color: '#F59E0B', action: 'High-urgency winback campaign with free shipping' },
      { name: 'Hibernating', count: 30, percentage: 4.7, spend: 3360, color: '#EF4444', action: 'Automated sunset list clean or 25% blowout voucher' }
    ],
    topProducts: [
      { name: 'Merino Wool Knitwear', category: 'Apparel', revenue: 22800, units: 182, share: 31.9 },
      { name: 'Selvedge Denim Jeans', category: 'Apparel', revenue: 18400, units: 167, share: 25.8 },
      { name: 'Canvas Weekender Duffle', category: 'Accessories', revenue: 14200, units: 98, share: 19.9 },
      { name: 'Classic Tailored Oxford', category: 'Apparel', revenue: 10800, units: 127, share: 15.1 },
      { name: 'Minimalist Leather Belt', category: 'Accessories', revenue: 5200, units: 94, share: 7.3 }
    ],
    marketingChannels: [
      { channel: 'Meta Ads (IG/FB)', spend: 11200, revenue: 39800, cac: 29.47, roi: 3.55 },
      { channel: 'Google Shopping / SEM', spend: 4200, revenue: 18400, cac: 25.45, roi: 4.38 },
      { channel: 'Klaviyo Email Flows', spend: 950, revenue: 22100, cac: 3.28, roi: 23.26 },
      { channel: 'TikTok Influencers', spend: 2400, revenue: 6900, cac: 35.29, roi: 2.87 }
    ],
    recommendations: [
      {
        id: 1,
        title: 'Scale High-ROI Email Automations',
        category: 'MARKETING',
        impact: 'HIGH',
        effort: 'LOW',
        estimatedImpact: '+$6,800/mo incremental',
        description: 'Klaviyo flows yield a 23.2x ROI with negligible acquisition cost. Deploy browse abandonment and VIP early access campaigns to capture high-intent organic visitors.',
        steps: ['Set up 3-stage browse abandonment triggers', 'Deploy SMS VIP product announcements', 'Provide post-purchase 10% bundle coupon'],
        status: 'PENDING'
      },
      {
        id: 2,
        title: 'Bundle Oxford Shirts with Selvedge Denim',
        category: 'REVENUE',
        impact: 'HIGH',
        effort: 'LOW',
        estimatedImpact: '+18% Average Order Value',
        description: '41% of denim buyers purchase an additional top within 45 days. Bundling them at a $175 capsule price lifts basket size immediately.',
        steps: ['Create Capsule Essentials page', 'Implement 1-click cart drawer upsell', 'Feature bundle in welcome series'],
        status: 'IN_PROGRESS'
      },
      {
        id: 3,
        title: 'Trim Underperforming TikTok Ad Creative',
        category: 'COST_REDUCTION',
        impact: 'MEDIUM',
        effort: 'LOW',
        estimatedImpact: 'Save $1,400/mo ad waste',
        description: 'TikTok acquisition costs are 38% higher than Google Search with half the conversion rate. Shift capital to scaling top Instagram lookalike audiences.',
        steps: ['Pause lowest performing TikTok ad groups', 'Cap TikTok test budget at $1,000/mo', 'Reallocate $1,400 to Instagram Reels retargeting'],
        status: 'PENDING'
      },
      {
        id: 4,
        title: 'Launch Re-engagement for 88 At-Risk Buyers',
        category: 'RISK_MITIGATION',
        impact: 'HIGH',
        effort: 'MEDIUM',
        estimatedImpact: 'Recover ~$4,200 churned GMV',
        description: '88 previously loyal customers have not made an order in over 75 days. Deploying a personalized reminder prevents permanent churn.',
        steps: ['Segment by previous product preference', 'Send personalized discount code', 'Follow up via SMS after 72 hours'],
        status: 'PENDING'
      }
    ]
  },
  SAAS: {
    id: 'SAAS',
    title: 'CloudFlow Solutions',
    subtitle: 'B2B Workflow Automation Platform',
    industry: 'SaaS & Software',
    currency: '$',
    monthlyTarget: 60000,
    monthlyBudget: 38000,
    growthGoal: 'Reach $65k MRR & Cut User Churn Below 2%',
    targetAudience: 'Remote tech teams and mid-market project operations',
    teamSize: '6-15 (Growing team)',
    primaryChannel: 'Inbound Content & Google Search SEM',
    metrics: {
      totalRevenue: 48500,
      totalExpenses: 33900,
      netProfit: 14600,
      profitMargin: 30.1,
      revenueGrowth: 7.3,
      avgOrderValue: 242.50,
      totalOrders: 200,
      marketingSpend: 11400,
      marketingRoi: 4.25,
      riskLevel: 'LOW',
      riskScore: 18,
      riskFactors: ['High concentration in monthly subscriptions vs annual upfront lock-in.']
    },
    monthlyTrends: [
      { month: 'Jan', revenue: 29000, expenses: 24500, profit: 4500 },
      { month: 'Feb', revenue: 31200, expenses: 25800, profit: 5400 },
      { month: 'Mar', revenue: 33500, expenses: 26900, profit: 6600 },
      { month: 'Apr', revenue: 35800, expenses: 28000, profit: 7800 },
      { month: 'May', revenue: 38100, expenses: 29200, profit: 8900 },
      { month: 'Jun', revenue: 40400, expenses: 30100, profit: 10300 },
      { month: 'Jul', revenue: 42900, expenses: 31200, profit: 11700 },
      { month: 'Aug', revenue: 45200, expenses: 32500, profit: 12700 },
      { month: 'Sep', revenue: 48500, expenses: 33900, profit: 14600 },
      { month: 'Oct*', revenue: 52100, expenses: 35400, profit: 16700, isForecast: true },
      { month: 'Nov*', revenue: 56000, expenses: 37000, profit: 19000, isForecast: true },
      { month: 'Dec*', revenue: 60400, expenses: 38900, profit: 21500, isForecast: true }
    ],
    customerSegments: [
      { name: 'Champions', count: 65, percentage: 54.0, spend: 26190, color: '#10B981', action: 'Upsell enterprise API packs & custom security tiers' },
      { name: 'Loyal', count: 85, percentage: 32.0, spend: 15520, color: '#0284C7', action: 'Incentivize annual payment switch with 20% discount' },
      { name: 'New', count: 35, percentage: 10.0, spend: 4850, color: '#8B5CF6', action: 'Dedicated onboarding specialist check-in call' },
      { name: 'At-Risk', count: 15, percentage: 4.0, spend: 1940, color: '#F59E0B', action: 'Offer complimentary workflow review to unblock usage' }
    ],
    topProducts: [
      { name: 'Enterprise Tier Annual', category: 'Subscription', revenue: 21950, units: 44, share: 45.3 },
      { name: 'Team Pro Monthly', category: 'Subscription', revenue: 16800, units: 170, share: 34.6 },
      { name: 'API Integration Pack', category: 'Add-on', revenue: 5960, units: 40, share: 12.3 },
      { name: 'Starter Team Seat', category: 'Subscription', revenue: 3790, units: 130, share: 7.8 }
    ],
    marketingChannels: [
      { channel: 'Google Search SEM', spend: 3200, revenue: 18500, cac: 71.11, roi: 5.78 },
      { channel: 'ProductLed Inbound / SEO', spend: 800, revenue: 20600, cac: 7.27, roi: 25.75 },
      { channel: 'LinkedIn Sponsored Ads', spend: 2200, revenue: 9400, cac: 122.22, roi: 4.27 }
    ],
    recommendations: [
      {
        id: 1,
        title: 'Incentivize Annual Upfront Commitments',
        category: 'REVENUE',
        impact: 'HIGH',
        effort: 'LOW',
        estimatedImpact: '+$28,000 Upfront Cashflow',
        description: 'Offering 2 months free for annual contracts locks in customer retention and protects against monthly credit card churn.',
        steps: ['Default annual toggle to ON', 'Trigger in-app discount prompt on day 45 of monthly billing'],
        status: 'PENDING'
      }
    ]
  }
};
