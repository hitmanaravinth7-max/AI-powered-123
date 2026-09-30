import React from 'react';
import { LayoutDashboard, LineChart, CheckSquare, MessageSquareText, FileSpreadsheet, Settings, Sparkles, UserCheck } from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab, businessData, onOpenAuth, user }) {
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'analytics', label: 'Analytics & Forecast', icon: LineChart },
    { id: 'recommendations', label: 'Action Plans', icon: CheckSquare },
    { id: 'chat', label: 'BizAdvisor AI', icon: MessageSquareText },
    { id: 'reports', label: 'Reports', icon: FileSpreadsheet },
    { id: 'settings', label: 'Settings', icon: Settings },
  ];

  return (
    <header className="sticky top-0 z-40 bg-slate-900/90 backdrop-blur-md border-b border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo & Company Title */}
          <div className="flex items-center space-x-3 cursor-pointer" onClick={() => setActiveTab('dashboard')}>
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-sky-600 to-emerald-500 flex items-center justify-center shadow-lg shadow-sky-500/20">
              <Sparkles className="w-5 h-5 text-white" />
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-bold text-white text-lg tracking-tight">BizConsult AI</span>
                <span className="bg-emerald-500/10 text-emerald-400 text-xs px-2 py-0.5 rounded-full font-medium border border-emerald-500/20">PRO</span>
              </div>
              <p className="text-xs text-slate-400 font-medium">{businessData.title} &bull; {businessData.industry}</p>
            </div>
          </div>

          {/* Navigation Links */}
          <nav className="hidden md:flex space-x-1">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id)}
                  className={`flex items-center space-x-2 px-3.5 py-2 rounded-lg text-sm font-medium transition-all ${
                    isActive
                      ? 'bg-sky-500/15 text-sky-400 border border-sky-500/30'
                      : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </nav>

          {/* User Profile / Auth Status */}
          <div className="flex items-center space-x-3">
            {user ? (
              <button
                onClick={() => setActiveTab('settings')}
                className="flex items-center space-x-2 bg-slate-800 hover:bg-slate-700 text-slate-200 px-3 py-1.5 rounded-lg text-xs font-medium border border-slate-700 transition-colors"
              >
                <div className="w-6 h-6 rounded-full bg-sky-500/20 text-sky-400 flex items-center justify-center font-bold">
                  {user.name[0]}
                </div>
                <span className="hidden sm:inline">{user.name}</span>
              </button>
            ) : (
              <button
                onClick={onOpenAuth}
                className="bg-sky-600 hover:bg-sky-500 text-white text-xs font-semibold px-3.5 py-2 rounded-lg transition-colors flex items-center space-x-1.5 shadow-sm"
              >
                <UserCheck className="w-4 h-4" />
                <span>Sign In / Demo</span>
              </button>
            )}
          </div>
        </div>
      </div>
      
      {/* Mobile nav bar */}
      <div className="md:hidden flex overflow-x-auto border-t border-slate-800 px-2 py-1 space-x-1 scrollbar-none">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`flex items-center space-x-1.5 px-3 py-1.5 rounded-md text-xs whitespace-nowrap ${
                isActive ? 'bg-sky-500/20 text-sky-400 font-semibold' : 'text-slate-400'
              }`}
            >
              <Icon className="w-3.5 h-3.5" />
              <span>{item.label}</span>
            </button>
          );
        })}
      </div>
    </header>
  );
}
