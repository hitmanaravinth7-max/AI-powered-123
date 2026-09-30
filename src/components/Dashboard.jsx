import React from 'react';
import { TrendingUp, DollarSign, Users, Target, ShieldAlert, ShieldCheck, ArrowUpRight, ArrowDownRight, Sparkles, CheckCircle, ChevronRight, MessageSquare } from 'lucide-react';

export default function Dashboard({ businessData, setActiveTab, onToggleRecommendation }) {
  const { metrics, monthlyTrends, customerSegments, marketingChannels, recommendations, currency } = businessData;

  const maxChartValue = Math.max(...monthlyTrends.slice(-7).map(t => Math.max(t.revenue, t.expenses))) * 1.15;
  const recentTrends = monthlyTrends.slice(-7);

  return (
    <div className="space-y-6 pb-12">
      {/* Top Hero Banner */}
      <div className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-slate-900 via-sky-950 to-slate-900 border border-slate-800 p-6 sm:p-8 shadow-xl">
        <div className="relative z-10 max-w-2xl">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-semibold mb-3">
            <Sparkles className="w-3.5 h-3.5" />
            <span>AI DECISION SUPPORT ENGINE ACTIVE</span>
          </div>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
            Executive Decision Dashboard
          </h1>
          <p className="mt-2 text-sm text-slate-300">
            Real-time diagnostic analysis across sales velocity, customer cohorts, and marketing capital allocation for <span className="font-semibold text-sky-400">{businessData.title}</span>.
          </p>
        </div>
        <div className="mt-4 flex flex-wrap gap-3">
          <button
            onClick={() => setActiveTab('chat')}
            className="inline-flex items-center space-x-2 bg-sky-600 hover:bg-sky-500 text-white px-4 py-2 rounded-xl text-sm font-semibold shadow-md transition-colors"
          >
            <MessageSquare className="w-4 h-4" />
            <span>Ask BizAdvisor AI</span>
          </button>
          <button
            onClick={() => setActiveTab('analytics')}
            className="inline-flex items-center space-x-2 bg-slate-800 hover:bg-slate-700 text-slate-200 px-4 py-2 rounded-xl text-sm font-semibold border border-slate-700 transition-colors"
          >
            <TrendingUp className="w-4 h-4" />
            <span>View Predictive Forecast</span>
          </button>
        </div>
      </div>

      {/* 4 Metric KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Revenue */}
        <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Gross Revenue</span>
            <div className="p-2 rounded-lg bg-sky-500/10 text-sky-400">
              <DollarSign className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2 flex items-baseline justify-between">
            <span className="text-2xl font-bold text-white">{currency}{metrics.totalRevenue.toLocaleString()}</span>
            <span className="inline-flex items-center text-xs font-bold text-emerald-400">
              <ArrowUpRight className="w-3.5 h-3.5 mr-0.5" />+{metrics.revenueGrowth}%
            </span>
          </div>
          <p className="mt-1 text-xs text-slate-400">Target: {currency}{businessData.monthlyTarget.toLocaleString()} / mo</p>
        </div>

        {/* Net Profit Margin */}
        <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Net Profit Margin</span>
            <div className="p-2 rounded-lg bg-emerald-500/10 text-emerald-400">
              <TrendingUp className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2 flex items-baseline justify-between">
            <span className="text-2xl font-bold text-white">{metrics.profitMargin}%</span>
            <span className="text-xs text-emerald-400 font-semibold">{currency}{metrics.netProfit.toLocaleString()} net</span>
          </div>
          <p className="mt-1 text-xs text-slate-400">Expenses: {currency}{metrics.totalExpenses.toLocaleString()}</p>
        </div>

        {/* Average Order Value */}
        <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Avg Order Value</span>
            <div className="p-2 rounded-lg bg-indigo-500/10 text-indigo-400">
              <Target className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2 flex items-baseline justify-between">
            <span className="text-2xl font-bold text-white">{currency}{metrics.avgOrderValue.toFixed(2)}</span>
            <span className="text-xs text-slate-300 font-medium">{metrics.totalOrders} total orders</span>
          </div>
          <p className="mt-1 text-xs text-slate-400">Across all catalog categories</p>
        </div>

        {/* Marketing ROI */}
        <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Marketing ROI</span>
            <div className="p-2 rounded-lg bg-amber-500/10 text-amber-400">
              <Users className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-2 flex items-baseline justify-between">
            <span className="text-2xl font-bold text-white">{metrics.marketingRoi}x</span>
            <span className="inline-flex items-center text-xs font-bold text-emerald-400">
              High Efficiency
            </span>
          </div>
          <p className="mt-1 text-xs text-slate-400">Total Spend: {currency}{metrics.marketingSpend.toLocaleString()}</p>
        </div>
      </div>

      {/* Risk Assessment Banner */}
      <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div className="flex items-start space-x-3.5">
          <div className={`p-2.5 rounded-xl ${metrics.riskLevel === 'LOW' ? 'bg-emerald-500/15 text-emerald-400' : 'bg-amber-500/15 text-amber-400'}`}>
            {metrics.riskLevel === 'LOW' ? <ShieldCheck className="w-5 h-5" /> : <ShieldAlert className="w-5 h-5" />}
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <h3 className="font-bold text-white text-base">Business Risk Index: {metrics.riskScore}/100</h3>
              <span className={`text-xs px-2 py-0.5 rounded-full font-bold ${
                metrics.riskLevel === 'LOW' ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-amber-500/10 text-amber-400 border border-amber-500/20'
              }`}>
                {metrics.riskLevel} RISK - STABLE
              </span>
            </div>
            <p className="text-xs text-slate-400 mt-1">
              {metrics.riskFactors[0] || 'Core financial indicators remain within safe operational bounds.'}
            </p>
          </div>
        </div>
        <div className="w-full md:w-48">
          <div className="flex justify-between text-xs text-slate-400 mb-1">
            <span>Safety Corridor</span>
            <span className="font-bold text-white">{(100 - metrics.riskScore)}% Buffer</span>
          </div>
          <div className="w-full bg-slate-700/60 rounded-full h-2 overflow-hidden">
            <div
              className={`h-full rounded-full ${metrics.riskLevel === 'LOW' ? 'bg-emerald-500' : 'bg-amber-500'}`}
              style={{ width: `${metrics.riskScore}%` }}
            />
          </div>
        </div>
      </div>

      {/* Main Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Revenue & Expenses SVG Bar Chart */}
        <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="font-bold text-white text-base">Monthly Revenue vs Expenses</h3>
              <p className="text-xs text-slate-400">Intake compared to operating overhead</p>
            </div>
            <div className="flex items-center space-x-4 text-xs">
              <div className="flex items-center space-x-1.5">
                <span className="w-3 h-3 rounded bg-sky-500 inline-block" />
                <span className="text-slate-300">Revenue</span>
              </div>
              <div className="flex items-center space-x-1.5">
                <span className="w-3 h-3 rounded bg-amber-500 inline-block" />
                <span className="text-slate-300">Expenses</span>
              </div>
            </div>
          </div>

          {/* SVG Bar Chart */}
          <div className="h-56 w-full flex items-end justify-between pt-4 pb-2 px-2 border-b border-slate-700/60">
            {recentTrends.map((t, idx) => {
              const revHeight = (t.revenue / maxChartValue) * 100;
              const expHeight = (t.expenses / maxChartValue) * 100;
              return (
                <div key={idx} className="flex-1 flex flex-col items-center h-full justify-end group px-1">
                  <div className="w-full flex items-end justify-center space-x-1 h-full">
                    {/* Rev Bar */}
                    <div
                      className="w-1/2 bg-sky-500 hover:bg-sky-400 rounded-t transition-all relative group-hover:brightness-110"
                      style={{ height: `${revHeight}%` }}
                      title={`${t.month} Revenue: ${currency}${t.revenue.toLocaleString()}`}
                    />
                    {/* Exp Bar */}
                    <div
                      className="w-1/2 bg-amber-500/80 hover:bg-amber-400 rounded-t transition-all relative group-hover:brightness-110"
                      style={{ height: `${expHeight}%` }}
                      title={`${t.month} Expenses: ${currency}${t.expenses.toLocaleString()}`}
                    />
                  </div>
                  <span className={`text-[11px] mt-2 font-medium ${t.isForecast ? 'text-sky-400 font-bold' : 'text-slate-400'}`}>
                    {t.month}
                  </span>
                </div>
              );
            })}
          </div>
          <div className="mt-3 flex justify-between text-xs text-slate-400">
            <span>* Projected future months</span>
            <button onClick={() => setActiveTab('analytics')} className="text-sky-400 hover:underline flex items-center">
              Detailed breakdown <ChevronRight className="w-3.5 h-3.5 ml-0.5" />
            </button>
          </div>
        </div>

        {/* Customer Segmentation */}
        <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="font-bold text-white text-base">Customer Segmentation (RFM)</h3>
              <p className="text-xs text-slate-400">Value distribution across buyer cohorts</p>
            </div>
            <span className="text-xs bg-slate-700/60 text-slate-300 px-2.5 py-1 rounded-full font-semibold">
              {customerSegments.reduce((sum, s) => sum + s.count, 0)} Active Buyers
            </span>
          </div>

          <div className="space-y-3 my-auto">
            {customerSegments.map((seg, idx) => (
              <div key={idx} className="bg-slate-900/40 p-2.5 rounded-xl border border-slate-800/80">
                <div className="flex items-center justify-between text-xs font-semibold mb-1">
                  <div className="flex items-center space-x-2">
                    <span className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: seg.color }} />
                    <span className="text-white">{seg.name}</span>
                    <span className="text-slate-400 font-normal">({seg.count} buyers)</span>
                  </div>
                  <span className="text-white">{seg.percentage}% &bull; {currency}{seg.spend.toLocaleString()}</span>
                </div>
                <div className="w-full bg-slate-800 rounded-full h-1.5 overflow-hidden">
                  <div className="h-full rounded-full" style={{ width: `${seg.percentage}%`, backgroundColor: seg.color }} />
                </div>
                <p className="text-[11px] text-slate-400 mt-1">
                  <span className="font-semibold text-slate-300">Action:</span> {seg.action}
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Prioritized Action Recommendations */}
      <div className="bg-slate-800/60 border border-slate-750 rounded-2xl p-5 shadow-sm">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-2">
            <Sparkles className="w-5 h-5 text-sky-400" />
            <h3 className="font-bold text-white text-base">Prioritized Strategic Actions</h3>
          </div>
          <button
            onClick={() => setActiveTab('recommendations')}
            className="text-xs text-sky-400 hover:text-sky-300 font-bold flex items-center space-x-1"
          >
            <span>View All ({recommendations.length})</span>
            <ChevronRight className="w-3.5 h-3.5" />
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {recommendations.slice(0, 2).map((rec) => {
            const isCompleted = rec.status === 'COMPLETED';
            return (
              <div
                key={rec.id}
                className="bg-slate-900/60 border border-slate-800 rounded-xl p-4 flex flex-col justify-between hover:border-slate-700 transition-colors"
              >
                <div>
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center space-x-2">
                      <span className="text-[10px] font-extrabold px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                        {rec.impact} IMPACT
                      </span>
                      <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-sky-500/10 text-sky-400">
                        {rec.estimatedImpact}
                      </span>
                    </div>
                    <button
                      onClick={() => onToggleRecommendation(rec.id)}
                      className={`text-xs px-2.5 py-1 rounded-lg font-bold flex items-center space-x-1 transition-colors ${
                        isCompleted
                          ? 'bg-emerald-500/20 text-emerald-400'
                          : 'bg-slate-800 text-slate-300 hover:bg-slate-750'
                      }`}
                    >
                      {isCompleted && <CheckCircle className="w-3.5 h-3.5" />}
                      <span>{isCompleted ? 'Completed' : 'Mark Done'}</span>
                    </button>
                  </div>
                  <h4 className="font-bold text-white text-sm">{rec.title}</h4>
                  <p className="text-xs text-slate-400 mt-1 line-clamp-2">{rec.description}</p>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
