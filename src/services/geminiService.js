export async function consultBizAdvisor({ prompt, businessData, customApiKey }) {
  const apiKey = customApiKey || (typeof import.meta !== 'undefined' && import.meta.env?.VITE_GEMINI_API_KEY);

  const contextData = `
Company: ${businessData.title} (${businessData.industry})
Gross Revenue: ${businessData.currency}${businessData.metrics.totalRevenue.toLocaleString()}
Operating Expenses: ${businessData.currency}${businessData.metrics.totalExpenses.toLocaleString()}
Net Profit Margin: ${businessData.metrics.profitMargin}% (Net Profit: ${businessData.currency}${businessData.metrics.netProfit.toLocaleString()})
Monthly Growth: ${businessData.metrics.revenueGrowth}%
Marketing ROI: ${businessData.metrics.marketingRoi}x across ${businessData.marketingChannels.length} channels
Risk Status: ${businessData.metrics.riskLevel} (Score: ${businessData.metrics.riskScore}/100)
Top Products: ${businessData.topProducts.map(p => `${p.name} ($${p.revenue.toLocaleString()})`).join(', ')}
Customer Cohorts: ${businessData.customerSegments.map(s => `${s.name} (${s.percentage}%)`).join(', ')}
`;

  if (apiKey && apiKey !== 'MY_GEMINI_API_KEY' && apiKey.length > 10) {
    try {
      const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [{
            parts: [{
              text: `You are BizAdvisor, an elite senior business & financial intelligence consultant.
Base your advice directly on the client's metrics:
${contextData}

Question: ${prompt}

Structure your response with:
1. Executive Assessment
2. Key Data Findings (citing exact numbers)
3. 2-3 High-Impact Strategic Actions
4. Risk / Opportunity Outlook.`
            }]
          }]
        })
      });

      if (response.ok) {
        const json = await response.json();
        const text = json?.candidates?.[0]?.content?.parts?.[0]?.text;
        if (text) return text;
      }
    } catch (err) {
      console.warn('Gemini API call failed, falling back to local heuristic advisor:', err);
    }
  }

  // Heuristic Advisor Fallback grounded in live company numbers
  return generateHeuristicAdvice(prompt, businessData);
}

function generateHeuristicAdvice(prompt, data) {
  const q = prompt.toLowerCase();
  const curr = data.currency;
  const m = data.metrics;

  if (q.includes('profit') || q.includes('margin') || q.includes('cost')) {
    return `### 📊 Profitability & Margin Optimization Strategy
**Executive Assessment:**
Your current net profit margin is **${m.profitMargin}%** on gross intake of **${curr}${m.totalRevenue.toLocaleString()}** against operating costs of **${curr}${m.totalExpenses.toLocaleString()}**.

**Core Data Findings:**
• **Margin Benchmark:** In the ${data.industry} sector, top quartile performers achieve 22-28% net margins.
• **Expense Ratio:** Costs currently consume ${((m.totalExpenses / m.totalRevenue) * 100).toFixed(1)}% of top-line revenue.

**Actionable Steps:**
1. **Renegotiate Recurring Vendor Costs:** Audit supplier unit costs and SaaS licenses to capture an estimated 8-12% in immediate cost savings.
2. **Promote Highest Margin Lines:** Channel traffic toward **${data.topProducts[0]?.name}**, which delivers ${data.topProducts[0]?.share}% of current gross revenue.
3. **Trim Underperforming Ad Spend:** Shift budget away from lower-yield channels to lower overall blended CAC.`;
  }

  if (q.includes('market') || q.includes('ad') || q.includes('spend') || q.includes('cac')) {
    const topChannel = data.marketingChannels.reduce((max, c) => c.roi > max.roi ? c : max, data.marketingChannels[0]);
    const bottomChannel = data.marketingChannels.reduce((min, c) => c.roi < min.roi ? c : min, data.marketingChannels[0]);

    return `### 🎯 Marketing Capital Allocation Analysis
**Executive Assessment:**
Your marketing portfolio has achieved an overall **${m.marketingRoi}x ROI** on total spend of **${curr}${m.marketingSpend.toLocaleString()}**.

**Channel Performance:**
• **Top Channel:** **${topChannel.channel}** leads with an extraordinary **${topChannel.roi}x ROI** (CAC: ${curr}${topChannel.cac}).
• **Lagging Channel:** **${bottomChannel.channel}** lags at **${bottomChannel.roi}x ROI** (CAC: ${curr}${bottomChannel.cac}).

**Actionable Steps:**
1. **Reallocate 25% Ad Capital:** Shift $1,500/mo from ${bottomChannel.channel} directly into scaling ${topChannel.channel}.
2. **Maximize Automated Retention:** Email/SMS workflows have negligible marginal cost; expand post-purchase flows to capture high-intent buyers.`;
  }

  return `### 💡 Strategic Advisory Overview
**Company:** ${data.title} (${data.industry})
**Current Financial Health:**
• **Gross Revenue:** ${curr}${m.totalRevenue.toLocaleString()} (Growth: +${m.revenueGrowth}% MoM)
• **Net Margin:** ${m.profitMargin}% (Net Profit: ${curr}${m.netProfit.toLocaleString()})
• **Risk Index:** ${m.riskLevel} (${m.riskScore}/100)

**Consultant Assessment on "${prompt}":**
Your business demonstrates healthy fundamentals with strong product concentration in **${data.topProducts[0]?.name}**. The most immediate unlock is lifting Average Order Value (currently ${curr}${m.avgOrderValue}) via smart product bundling and recovering at-risk customer cohorts before permanent churn occurs.`;
}
