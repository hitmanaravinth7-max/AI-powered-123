import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import Dashboard from './components/Dashboard';
import Analytics from './components/Analytics';
import Recommendations from './components/Recommendations';
import AiChat from './components/AiChat';
import Reports from './components/Reports';
import Settings from './components/Settings';
import AuthModal from './components/AuthModal';
import { PRESETS } from './data/mockData';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [businessData, setBusinessData] = useState(() => {
    const saved = localStorage.getItem('bizconsult_data');
    return saved ? JSON.parse(saved) : PRESETS.ECOMMERCE;
  });

  const [customApiKey, setCustomApiKey] = useState(() => {
    return localStorage.getItem('bizconsult_api_key') || '';
  });

  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('bizconsult_user');
    return saved ? JSON.parse(saved) : { name: 'Elena Vance', email: 'elena@urbanstyle.com', role: 'Founder & CEO' };
  });

  const [authModalOpen, setAuthModalOpen] = useState(false);
  const [chatInitialQuery, setChatInitialQuery] = useState(null);

  useEffect(() => {
    localStorage.setItem('bizconsult_data', JSON.stringify(businessData));
  }, [businessData]);

  useEffect(() => {
    localStorage.setItem('bizconsult_api_key', customApiKey);
  }, [customApiKey]);

  useEffect(() => {
    if (user) {
      localStorage.setItem('bizconsult_user', JSON.stringify(user));
    } else {
      localStorage.removeItem('bizconsult_user');
    }
  }, [user]);

  const handleSelectPreset = (presetId) => {
    if (PRESETS[presetId]) {
      setBusinessData(PRESETS[presetId]);
    }
  };

  const handleToggleRecommendation = (id) => {
    setBusinessData(prev => ({
      ...prev,
      recommendations: prev.recommendations.map(r => {
        if (r.id === id) {
          const nextStatus = r.status === 'COMPLETED' ? 'PENDING' : r.status === 'PENDING' ? 'IN_PROGRESS' : 'COMPLETED';
          return { ...r, status: nextStatus };
        }
        return r;
      })
    }));
  };

  const handleAddTransaction = (sale) => {
    const amt = Number(sale.amount) || 100;
    setBusinessData(prev => {
      const newRev = prev.metrics.totalRevenue + amt;
      const newProfit = newRev - prev.metrics.totalExpenses;
      return {
        ...prev,
        metrics: {
          ...prev.metrics,
          totalRevenue: newRev,
          netProfit: newProfit,
          profitMargin: Number(((newProfit / newRev) * 100).toFixed(1)),
          totalOrders: prev.metrics.totalOrders + 1
        },
        topProducts: prev.topProducts.map((p, i) => i === 0 ? { ...p, revenue: p.revenue + amt, units: p.units + 1 } : p)
      };
    });
  };

  const handleAddExpense = (expense) => {
    const amt = Number(expense.amount) || 500;
    setBusinessData(prev => {
      const newExp = prev.metrics.totalExpenses + amt;
      const newProfit = prev.metrics.totalRevenue - newExp;
      return {
        ...prev,
        metrics: {
          ...prev.metrics,
          totalExpenses: newExp,
          netProfit: newProfit,
          profitMargin: Number(((newProfit / prev.metrics.totalRevenue) * 100).toFixed(1))
        }
      };
    });
  };

  const handleImportCsv = (csvText) => {
    const rows = csvText.split('\n').filter(r => r.trim().length > 0);
    const addedCount = rows.length > 1 ? rows.length - 1 : 1;
    const addedRev = addedCount * 110;
    setBusinessData(prev => ({
      ...prev,
      metrics: {
        ...prev.metrics,
        totalRevenue: prev.metrics.totalRevenue + addedRev,
        totalOrders: prev.metrics.totalOrders + addedCount
      }
    }));
  };

  const handleConsultAiFromRec = (prompt) => {
    setChatInitialQuery(prompt);
    setActiveTab('chat');
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans">
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        businessData={businessData}
        onOpenAuth={() => setAuthModalOpen(true)}
        user={user}
      />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {activeTab === 'dashboard' && (
          <Dashboard
            businessData={businessData}
            setActiveTab={setActiveTab}
            onToggleRecommendation={handleToggleRecommendation}
          />
        )}

        {activeTab === 'analytics' && (
          <Analytics
            businessData={businessData}
            setBusinessData={setBusinessData}
            onAddTransaction={handleAddTransaction}
            onAddExpense={handleAddExpense}
            onImportCsv={handleImportCsv}
          />
        )}

        {activeTab === 'recommendations' && (
          <Recommendations
            businessData={businessData}
            onToggleRecommendation={handleToggleRecommendation}
            onConsultAi={handleConsultAiFromRec}
          />
        )}

        {activeTab === 'chat' && (
          <AiChat
            businessData={businessData}
            initialQuery={chatInitialQuery}
            customApiKey={customApiKey}
          />
        )}

        {activeTab === 'reports' && (
          <Reports
            businessData={businessData}
          />
        )}

        {activeTab === 'settings' && (
          <Settings
            businessData={businessData}
            setBusinessData={setBusinessData}
            customApiKey={customApiKey}
            setCustomApiKey={setCustomApiKey}
            onSelectPreset={handleSelectPreset}
          />
        )}
      </main>

      <AuthModal
        isOpen={authModalOpen}
        onClose={() => setAuthModalOpen(false)}
        onLogin={(userData) => setUser(userData)}
      />
    </div>
  );
}
