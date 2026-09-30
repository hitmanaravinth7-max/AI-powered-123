import React, { useState } from 'react';
import { Building, Key, RefreshCw, Save, Check } from 'lucide-react';
import { PRESETS } from '../data/mockData';

export default function Settings({ businessData, setBusinessData, customApiKey, setCustomApiKey, onSelectPreset }) {
  const [formData, setFormData] = useState({
    title: businessData.title,
    industry: businessData.industry,
    monthlyTarget: businessData.monthlyTarget,
    monthlyBudget: businessData.monthlyBudget,
    growthGoal: businessData.growthGoal,
    targetAudience: businessData.targetAudience,
    teamSize: businessData.teamSize,
    primaryChannel: businessData.primaryChannel
  });

  const [saved, setSaved] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setBusinessData({
      ...businessData,
      ...formData,
      monthlyTarget: Number(formData.monthlyTarget),
      monthlyBudget: Number(formData.monthlyBudget)
    });
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  return (
    <div className="space-y-6 max-w-4xl mx-auto pb-12">
      {/* Header */}
      <div className="bg-slate-800/60 border border-slate-750 p-6 rounded-2xl">
        <h2 className="text-xl font-bold text-white">Business Profile & Integration Settings</h2>
        <p className="text-xs text-slate-400 mt-1">Configure company baseline targets, Gemini AI keys, and test industry scenarios</p>
      </div>

      {/* Profile Form */}
      <form onSubmit={handleSubmit} className="bg-slate-800/60 border border-slate-750 p-6 rounded-2xl space-y-4">
        <div className="flex items-center space-x-2 text-sky-400 font-bold text-sm mb-2">
          <Building className="w-4 h-4" />
          <span>Core Company Parameters</span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="text-xs font-semibold text-slate-300">Company Name</label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
            />
          </div>
          <div>
            <label className="text-xs font-semibold text-slate-300">Industry Sector</label>
            <input
              type="text"
              value={formData.industry}
              onChange={(e) => setFormData({ ...formData, industry: e.target.value })}
              className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
            />
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="text-xs font-semibold text-slate-300">Monthly Revenue Target ({businessData.currency})</label>
            <input
              type="number"
              value={formData.monthlyTarget}
              onChange={(e) => setFormData({ ...formData, monthlyTarget: e.target.value })}
              className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
            />
          </div>
          <div>
            <label className="text-xs font-semibold text-slate-300">Monthly Operating Budget ({businessData.currency})</label>
            <input
              type="number"
              value={formData.monthlyBudget}
              onChange={(e) => setFormData({ ...formData, monthlyBudget: e.target.value })}
              className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
            />
          </div>
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-300">Primary Strategic Growth Objective</label>
          <input
            type="text"
            value={formData.growthGoal}
            onChange={(e) => setFormData({ ...formData, growthGoal: e.target.value })}
            className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
          />
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-300">Target Customer Demographic</label>
          <input
            type="text"
            value={formData.targetAudience}
            onChange={(e) => setFormData({ ...formData, targetAudience: e.target.value })}
            className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
          />
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-300">Primary Marketing / Acquisition Channel</label>
          <input
            type="text"
            value={formData.primaryChannel}
            onChange={(e) => setFormData({ ...formData, primaryChannel: e.target.value })}
            className="w-full mt-1 bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
          />
        </div>

        <div className="pt-2 flex justify-end">
          <button
            type="submit"
            className="flex items-center space-x-2 bg-sky-600 hover:bg-sky-500 text-white text-xs font-bold px-4 py-2.5 rounded-xl transition-colors shadow-md"
          >
            {saved ? <Check className="w-4 h-4 text-emerald-300" /> : <Save className="w-4 h-4" />}
            <span>{saved ? 'Changes Saved!' : 'Save Business Profile'}</span>
          </button>
        </div>
      </form>

      {/* Gemini API Key */}
      <div className="bg-slate-800/60 border border-slate-750 p-6 rounded-2xl space-y-3">
        <div className="flex items-center space-x-2 text-indigo-400 font-bold text-sm">
          <Key className="w-4 h-4" />
          <span>Gemini AI API Configuration</span>
        </div>
        <p className="text-xs text-slate-400 leading-relaxed">
          Provide an optional Gemini API Key to connect live language models. If left blank, BizAdvisor automatically runs on its built-in algorithmic financial diagnostic engine.
        </p>
        <input
          type="password"
          placeholder="AIzaSy... (Optional)"
          value={customApiKey}
          onChange={(e) => setCustomApiKey(e.target.value)}
          className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-sky-500"
        />
      </div>

      {/* Switch Preset Scenarios */}
      <div className="bg-slate-800/60 border border-slate-750 p-6 rounded-2xl space-y-3">
        <div className="flex items-center space-x-2 text-emerald-400 font-bold text-sm">
          <RefreshCw className="w-4 h-4" />
          <span>Demo Industry Datasets</span>
        </div>
        <p className="text-xs text-slate-400">Instantly switch the platform dataset to evaluate different business archetypes:</p>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-1">
          {Object.values(PRESETS).map((preset) => (
            <div
              key={preset.id}
              onClick={() => onSelectPreset(preset.id)}
              className="bg-slate-900/60 hover:bg-slate-900 border border-slate-800 hover:border-sky-500/50 p-4 rounded-xl cursor-pointer transition-all flex items-center justify-between group"
            >
              <div>
                <h4 className="font-bold text-white text-sm group-hover:text-sky-400 transition-colors">{preset.title}</h4>
                <p className="text-xs text-slate-400">{preset.industry}</p>
              </div>
              <span className="text-xs bg-slate-800 group-hover:bg-sky-600 text-slate-300 group-hover:text-white px-2.5 py-1 rounded-lg font-semibold transition-colors">
                Load
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
