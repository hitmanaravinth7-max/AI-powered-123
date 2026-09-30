import React, { useState } from 'react';
import { X, Lock, Mail, User, Shield, ArrowRight } from 'lucide-react';

export default function AuthModal({ isOpen, onClose, onLogin }) {
  const [isSignUp, setIsSignUp] = useState(false);
  const [email, setEmail] = useState('elena@urbanstyle.com');
  const [name, setName] = useState('Elena Vance');
  const [role, setRole] = useState('Founder & Managing Director');
  const [password, setPassword] = useState('consultant2026');

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onLogin({ name, email, role });
    onClose();
  };

  const handleDemoLogin = () => {
    onLogin({
      name: 'Elena Vance',
      email: 'elena@urbanstyle.com',
      role: 'Founder & CEO'
    });
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-slate-900 border border-slate-800 rounded-3xl max-w-md w-full p-6 sm:p-8 shadow-2xl relative">
        <button
          onClick={onClose}
          className="absolute top-5 right-5 text-slate-400 hover:text-white p-1.5 rounded-lg hover:bg-slate-800"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="text-center mb-6">
          <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-sky-600 to-emerald-500 mx-auto flex items-center justify-center text-white shadow-lg mb-3">
            <Shield className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold text-white">BizConsult AI Portal</h3>
          <p className="text-xs text-slate-400 mt-1">Intelligent Decision Support Platform</p>
        </div>

        {/* Tab switch */}
        <div className="flex bg-slate-800/80 p-1 rounded-xl mb-5">
          <button
            onClick={() => setIsSignUp(false)}
            className={`flex-1 py-1.5 text-xs font-bold rounded-lg transition-all ${
              !isSignUp ? 'bg-sky-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Sign In
          </button>
          <button
            onClick={() => setIsSignUp(true)}
            className={`flex-1 py-1.5 text-xs font-bold rounded-lg transition-all ${
              isSignUp ? 'bg-sky-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Create Account
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-3.5">
          {isSignUp && (
            <>
              <div>
                <label className="text-xs font-semibold text-slate-300">Full Name</label>
                <div className="mt-1 relative">
                  <User className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
                  <input
                    type="text"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full bg-slate-800/80 border border-slate-700 rounded-xl pl-9 pr-3 py-2 text-xs text-white focus:outline-none focus:border-sky-500"
                  />
                </div>
              </div>
              <div>
                <label className="text-xs font-semibold text-slate-300">Company Role</label>
                <input
                  type="text"
                  required
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  className="w-full mt-1 bg-slate-800/80 border border-slate-700 rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-sky-500"
                />
              </div>
            </>
          )}

          <div>
            <label className="text-xs font-semibold text-slate-300">Business Email</label>
            <div className="mt-1 relative">
              <Mail className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full bg-slate-800/80 border border-slate-700 rounded-xl pl-9 pr-3 py-2 text-xs text-white focus:outline-none focus:border-sky-500"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-slate-300">Password</label>
            <div className="mt-1 relative">
              <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-slate-800/80 border border-slate-700 rounded-xl pl-9 pr-3 py-2 text-xs text-white focus:outline-none focus:border-sky-500"
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full mt-2 bg-sky-600 hover:bg-sky-500 text-white text-xs font-bold py-2.5 rounded-xl transition-colors shadow-md flex items-center justify-center space-x-1"
          >
            <span>{isSignUp ? 'Create Business Account' : 'Sign In to Dashboard'}</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </form>

        <div className="mt-4 pt-4 border-t border-slate-800">
          <button
            type="button"
            onClick={handleDemoLogin}
            className="w-full bg-slate-800 hover:bg-slate-750 text-slate-200 text-xs font-bold py-2.5 rounded-xl transition-colors border border-slate-700 flex items-center justify-center space-x-2"
          >
            <span>⚡ 1-Click Instant Demo Login (Founder)</span>
          </button>
        </div>
      </div>
    </div>
  );
}
