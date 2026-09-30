import React, { useState } from 'react';
import { Sparkles, CheckCircle, Clock, ArrowRight, Zap, Target } from 'lucide-react';

export default function Recommendations({ businessData, onToggleRecommendation, onConsultAi }) {
  const [filter, setFilter] = useState('ALL');
  const { recommendations } = businessData;

  const categories = [
    { id: 'ALL', label: 'All Actions' },
    { id: 'REVENUE', label: 'Revenue Growth' },
    { id: 'COST_REDUCTION', label: 'Cost Optimization' },
    { id: 'MARKETING', label: 'Marketing Strategy' },
    { id: 'RISK_MITIGATION', label: 'Risk Defense' }
  ];

  const filteredRecs = filter === 'ALL' ? recommendations : recommendations.filter(r => r.category === filter);

  return (
    <div className="space-y-6 pb-12">
      {/* Header Banner */}
      <div className="bg-gradient-to-r from-sky-950/60 via-slate-900 to-slate-900 border border-slate-800 p-6 rounded-2xl">
        <div className="flex items-center space-x-2 text-sky-400 font-bold text-xs uppercase tracking-wider mb-2">
          <Sparkles className="w-4 h-4" />
          <span>Algorithmic Recommendation Engine</span>
        </div>
        <h2 className="text-xl font-extrabold text-white">Prioritized Strategic Action Plans</h2>
        <p className="text-xs text-slate-300 mt-1 max-w-2xl">
          Actionable, high-ROI business initiatives ranked by impact vs effort. Execute these items to boost margins and optimize customer acquisition.
        </p>
      </div>

      {/* Category Filter Chips */}
      <div className="flex flex-wrap gap-2">
        {categories.map((c) => (
          <button
            key={c.id}
            onClick={() => setFilter(c.id)}
            className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold transition-all ${
              filter === c.id
                ? 'bg-sky-500/20 text-sky-400 border border-sky-500/30'
                : 'bg-slate-800/80 text-slate-400 hover:text-slate-200 border border-slate-750'
            }`}
          >
            {c.label}
          </button>
        ))}
      </div>

      {/* Recommendations Cards List */}
      <div className="space-y-4">
        {filteredRecs.map((rec) => {
          const isDone = rec.status === 'COMPLETED';
          const isInProgress = rec.status === 'IN_PROGRESS';

          return (
            <div
              key={rec.id}
              className={`border rounded-2xl p-5 transition-all ${
                isDone
                  ? 'bg-slate-900/40 border-slate-800/60 opacity-80'
                  : 'bg-slate-800/60 border-slate-750 shadow-sm hover:border-slate-700'
              }`}
            >
              <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 mb-3">
                <div className="flex flex-wrap items-center gap-2">
                  <span className={`text-[10px] font-extrabold px-2.5 py-1 rounded-md border ${
                    rec.impact === 'HIGH' ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20' : 'bg-sky-500/10 text-sky-400 border-sky-500/20'
                  }`}>
                    {rec.impact} IMPACT
                  </span>
                  <span className="text-[10px] font-bold px-2.5 py-1 rounded-md bg-slate-750 text-slate-300 border border-slate-700">
                    {rec.effort} EFFORT
                  </span>
                  <span className="text-[10px] font-bold px-2.5 py-1 rounded-md bg-indigo-500/10 text-indigo-400 border border-indigo-500/20">
                    {rec.estimatedImpact}
                  </span>
                </div>

                {/* Status Toggle Button */}
                <button
                  onClick={() => onToggleRecommendation(rec.id)}
                  className={`text-xs px-3 py-1.5 rounded-xl font-bold flex items-center space-x-1.5 transition-colors ${
                    isDone
                      ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30'
                      : isInProgress
                      ? 'bg-amber-500/20 text-amber-400 border border-amber-500/30'
                      : 'bg-slate-800 text-slate-300 hover:bg-slate-750 border border-slate-700'
                  }`}
                >
                  {isDone ? <CheckCircle className="w-3.5 h-3.5" /> : <Clock className="w-3.5 h-3.5" />}
                  <span>{isDone ? 'Completed ✓' : isInProgress ? 'In Progress' : 'Start Action'}</span>
                </button>
              </div>

              <h3 className="text-base font-bold text-white">{rec.title}</h3>
              <p className="text-xs text-slate-300 mt-2 leading-relaxed">{rec.description}</p>

              {/* Implementation Roadmap Steps */}
              {rec.steps && rec.steps.length > 0 && (
                <div className="mt-4 bg-slate-900/60 rounded-xl p-3.5 border border-slate-800">
                  <h4 className="text-xs font-bold text-slate-200 mb-2">Tactical Implementation Roadmap:</h4>
                  <div className="space-y-1.5">
                    {rec.steps.map((step, idx) => (
                      <div key={idx} className="flex items-start text-xs text-slate-400">
                        <span className="w-4 h-4 rounded-full bg-sky-500/10 text-sky-400 flex items-center justify-center font-bold text-[10px] mr-2 mt-0.5 shrink-0">
                          {idx + 1}
                        </span>
                        <span>{step}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Ask BizAdvisor Button */}
              <div className="mt-4 pt-3 border-t border-slate-800/80 flex justify-end">
                <button
                  onClick={() => onConsultAi(`How can we implement this recommendation: "${rec.title}"?`)}
                  className="text-xs text-sky-400 hover:text-sky-300 font-semibold flex items-center space-x-1 group"
                >
                  <Sparkles className="w-3.5 h-3.5 mr-1" />
                  <span>Ask BizAdvisor for Step-by-Step Playbook</span>
                  <ArrowRight className="w-3.5 h-3.5 transition-transform group-hover:translate-x-1" />
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
