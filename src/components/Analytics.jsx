import React, { useState } from 'react';
import { TrendingUp, Plus, Upload, Check, RefreshCw, BarChart3, Package, ArrowUpRight } from 'lucide-react';
import { PRESETS } from '../data/mockData';

export default function Analytics({ businessData, setBusinessData, onAddTransaction, onAddExpense, onImportCsv }) {
  const [showSaleModal, setShowSaleModal] = useState(false);
  const [showExpenseModal, setShowExpenseModal] = useState(false);
  const [showCsvModal, setShowCsvModal] = useState(false);
  const [csvInput, setCsvInput] = useState(`Product Name, Amount, Category, Channel, Segment, Units\nSilk Touch Scarf, 85.00, Accessories, Meta Ads, Champions, 1\nCasual Chino Pant, 110.00, Apparel, Google, Loyal, 2\nEveryday Canvas Tote, 45.00, Accessories, Email, New, 1`);

  const [saleForm, setSaleForm] = useState({ product: '', amount: '125', category: 'Apparel', channel: 'Meta Ads', segment: 'Loyal', units: '1' });
  const [expenseForm, setExpenseForm] = useState({ description: '', amount: '850', category: 'Marketing & Ads' });

  const { currency, monthlyTrends, topProducts, marketingChannels } = businessData;

  const handleSaleSubmit = (e) => {
    e.preventDefault();
    if (!saleForm.product) return;
    onAddTransaction(saleForm);
    setShowSaleModal(false);
    setSaleForm({ product: '', amount: '125', category: 'Apparel', channel: 'Meta Ads', segment: 'Loyal', units: '1' });
  };

  const handleExpenseSubmit = (e) => {
    e.preventDefault();
    if (!expenseForm.description) return;
    onAddExpense(expenseForm);
    setShowExpenseModal(false);
    setExpenseForm({ description: '', amount: '850', category: 'Marketing & Ads' });
  };

  const handleCsvImport = () => {
    onImportCsv(csvInput);
    setShowCsvModal(false);
  };

  return (
    <div className="space-y-6 pb-12">
      {/* Header and Quick Data Buttons */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 bg-slate-800/60 border border-slate-750 p-6 rounded-2xl">
        <div>
          <h2 className="text-xl font-bold text-white">Predictive Analytics & Data Input</h2>
          <p className="text-xs text-slate-400 mt-1">Linear regression forecasting, top product distribution, and manual transaction logging</p>
        </div>
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => setShowSaleModal(true)}
            className="flex items-center space-x-1.5 bg-sky-600 hover:bg-sky-500 text-white text-xs font-bold px-3.5 py-2 rounded-xl transition-colors shadow-sm"
          >
            <Plus className="w-4 h-4" />
            <span>Add Sale</span>
          </button>
          <button
            onClick={() => setShowExpenseModal(true)}
            className="flex items-center space-x-1.5 bg-amber-600 hover:bg-amber-500 text-white text-xs font-bold px-3.5 py-2 rounded-xl transition-colors shadow-sm"
          >
            <Plus className="w-4 h-4" />
            <span>Add Expense</span>
          </button>
          <button
            onClick={() => setShowCsvModal(true)}
            className="flex items-center space-x-1.5 bg-slate-700 hover:bg-slate-650 text-slate-200 text-xs font-bold px-3.5 py-2 rounded-xl transition-colors border border-slate-600"
          >
            <Upload className="w-4 h-4" />
            <span>Import CSV</span>
          </button>
        </div>
      </div>

      {/* Regression Forecast Section */}
      <div className="bg-slate-800/60 border border-slate-750 p-6 rounded-2xl shadow-sm">
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-2 mb-4">
          <div className="flex items-center space-x-2.5">
            <div className="p-2 rounded-lg bg-indigo-500/10 text-indigo-400">
              <TrendingUp className="w-5 h-5" />
            </div>
            <div>
              <h3 className="font-bold text-white text-base">Predictive Revenue Time-Series Forecast</h3>
              <p className="text-xs text-slate-400">Ordinary least squares regression projection with 87% statistical confidence</p>
            </div>
          </div>
          <span className="bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs px-3 py-1 rounded-full font-bold">
            Steady Upward Velocity (+5.8%/mo)
          </span>
        </div>

        {/* Projected Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
          <div className="bg-slate-900/60 p-4 rounded-xl border border-slate-800">
            <span className="text-xs text-slate-400 font-medium">Projected Next Month (Oct)</span>
            <div className="text-xl font-extrabold text-sky-400 mt-1">{currency}75,200</div>
            <span className="text-[11px] text-emerald-400 font-semibold">+5.3% growth vs current</span>
          </div>
          <div className="bg-slate-900/60 p-4 rounded-xl border border-slate-800">
            <span className="text-xs text-slate-400 font-medium">Projected Q4 Gross Total</span>
            <div className="text-xl font-extrabold text-indigo-400 mt-1">{currency}241,200</div>
            <span className="text-[11px] text-slate-400">Oct, Nov & Dec aggregate</span>
          </div>
          <div className="bg-slate-900/60 p-4 rounded-xl border border-slate-800">
            <span className="text-xs text-slate-400 font-medium">Model R² Goodness of Fit</span>
            <div className="text-xl font-extrabold text-emerald-400 mt-1">0.91 / 1.0</div>
            <span className="text-[11px] text-slate-400">Strong historical correlation</span>
          </div>
        </div>

        {/* Forecast Visual Line Chart */}
        <div className="bg-slate-900/40 p-4 rounded-xl border border-slate-800/80">
          <div className="h-44 w-full flex items-end justify-between pt-4 pb-2 px-4 border-b border-slate-700/60">
            {monthlyTrends.map((t, idx) => {
              const height = (t.revenue / 95000) * 100;
              return (
                <div key={idx} className="flex-1 flex flex-col items-center h-full justify-end px-1 group">
                  <div
                    className={`w-full max-w-[28px] rounded-t transition-all ${
                      t.isForecast
                        ? 'bg-gradient-to-t from-indigo-600 to-sky-400 border border-sky-300/40'
                        : 'bg-sky-600 hover:bg-sky-500'
                    }`}
                    style={{ height: `${height}%` }}
                    title={`${t.month}: ${currency}${t.revenue.toLocaleString()}`}
                  />
                  <span className={`text-[11px] mt-2 font-medium ${t.isForecast ? 'text-sky-400 font-bold' : 'text-slate-400'}`}>
                    {t.month}
                  </span>
                </div>
              );
            })}
          </div>
          <div className="flex justify-between text-xs text-slate-400 mt-2 px-2">
            <span>Historical 9 Months</span>
            <span className="text-sky-400 font-bold">* Projected Q4 Run-Rate</span>
          </div>
        </div>
      </div>

      {/* Top Products Breakdown */}
      <div className="bg-slate-800/60 border border-slate-750 p-6 rounded-2xl shadow-sm">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-2">
            <Package className="w-5 h-5 text-sky-400" />
            <h3 className="font-bold text-white text-base">Top Performing Offerings</h3>
          </div>
          <span className="text-xs text-slate-400">{topProducts.length} core lines tracked</span>
        </div>

        <div className="space-y-3">
          {topProducts.map((p, idx) => (
            <div key={idx} className="bg-slate-900/60 p-3.5 rounded-xl border border-slate-800 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3">
              <div className="flex items-center space-x-3">
                <div className="w-7 h-7 rounded-lg bg-sky-500/10 text-sky-400 flex items-center justify-center font-bold text-xs">
                  #{idx + 1}
                </div>
                <div>
                  <h4 className="font-bold text-white text-sm">{p.name}</h4>
                  <span className="text-xs text-slate-400">{p.category} &bull; {p.units} units sold</span>
                </div>
              </div>
              <div className="w-full sm:w-60 flex flex-col items-end">
                <div className="flex justify-between w-full text-xs font-semibold mb-1">
                  <span className="text-white">{currency}{p.revenue.toLocaleString()}</span>
                  <span className="text-sky-400 font-bold">{p.share}%</span>
                </div>
                <div className="w-full bg-slate-800 rounded-full h-1.5 overflow-hidden">
                  <div className="h-full bg-sky-500 rounded-full" style={{ width: `${p.share}%` }} />
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Modal: Add Sale */}
      {showSaleModal && (
        <div className="fixed inset-0 z-50 bg-black/70 flex items-center justify-center p-4">
          <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 max-w-md w-full shadow-2xl">
            <h3 className="font-bold text-white text-lg mb-4">Log New Sale Transaction</h3>
            <form onSubmit={handleSaleSubmit} className="space-y-3">
              <div>
                <label className="text-xs font-semibold text-slate-300">Product / Offering</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Classic Trench Coat"
                  value={saleForm.product}
                  onChange={(e) => setSaleForm({ ...saleForm, product: e.target.value })}
                  className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
                />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-xs font-semibold text-slate-300">Amount ({currency})</label>
                  <input
                    type="number"
                    required
                    value={saleForm.amount}
                    onChange={(e) => setSaleForm({ ...saleForm, amount: e.target.value })}
                    className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
                  />
                </div>
                <div>
                  <label className="text-xs font-semibold text-slate-300">Units</label>
                  <input
                    type="number"
                    value={saleForm.units}
                    onChange={(e) => setSaleForm({ ...saleForm, units: e.target.value })}
                    className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
                  />
                </div>
              </div>
              <div>
                <label className="text-xs font-semibold text-slate-300">Channel</label>
                <input
                  type="text"
                  value={saleForm.channel}
                  onChange={(e) => setSaleForm({ ...saleForm, channel: e.target.value })}
                  className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
                />
              </div>
              <div className="flex justify-end space-x-2 pt-3">
                <button
                  type="button"
                  onClick={() => setShowSaleModal(false)}
                  className="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-700 hover:bg-slate-650 text-slate-300"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 rounded-xl text-xs font-semibold bg-sky-600 hover:bg-sky-500 text-white"
                >
                  Save Transaction
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal: Add Expense */}
      {showExpenseModal && (
        <div className="fixed inset-0 z-50 bg-black/70 flex items-center justify-center p-4">
          <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 max-w-md w-full shadow-2xl">
            <h3 className="font-bold text-white text-lg mb-4">Log Operating Expense</h3>
            <form onSubmit={handleExpenseSubmit} className="space-y-3">
              <div>
                <label className="text-xs font-semibold text-slate-300">Expense Description / Vendor</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. AWS Cloud Infrastructure or Ad Spend"
                  value={expenseForm.description}
                  onChange={(e) => setExpenseForm({ ...expenseForm, description: e.target.value })}
                  className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
                />
              </div>
              <div>
                <label className="text-xs font-semibold text-slate-300">Amount ({currency})</label>
                <input
                  type="number"
                  required
                  value={expenseForm.amount}
                  onChange={(e) => setExpenseForm({ ...expenseForm, amount: e.target.value })}
                  className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
                />
              </div>
              <div className="flex justify-end space-x-2 pt-3">
                <button
                  type="button"
                  onClick={() => setShowExpenseModal(false)}
                  className="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-700 text-slate-300"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 rounded-xl text-xs font-semibold bg-amber-600 hover:bg-amber-500 text-white"
                >
                  Save Expense
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal: CSV Upload */}
      {showCsvModal && (
        <div className="fixed inset-0 z-50 bg-black/70 flex items-center justify-center p-4">
          <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 max-w-lg w-full shadow-2xl">
            <h3 className="font-bold text-white text-lg mb-2">Import CSV Sales Records</h3>
            <p className="text-xs text-slate-400 mb-3">Paste comma-separated transaction records (Product, Amount, Category, Channel, Segment, Units):</p>
            <textarea
              rows={6}
              value={csvInput}
              onChange={(e) => setCsvInput(e.target.value)}
              className="w-full bg-slate-900 border border-slate-700 rounded-xl p-3 text-xs font-mono text-slate-200 focus:outline-none focus:border-sky-500"
            />
            <div className="flex justify-end space-x-2 pt-3">
              <button
                onClick={() => setShowCsvModal(false)}
                className="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-700 text-slate-300"
              >
                Cancel
              </button>
              <button
                onClick={handleCsvImport}
                className="px-4 py-2 rounded-xl text-xs font-semibold bg-sky-600 hover:bg-sky-500 text-white"
              >
                Process Ingestion
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
