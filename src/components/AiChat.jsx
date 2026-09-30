import React, { useState, useRef, useEffect } from 'react';
import { Sparkles, Send, Trash2, Bot, User, Loader2 } from 'lucide-react';
import { consultBizAdvisor } from '../services/geminiService';

export default function AiChat({ businessData, initialQuery, customApiKey }) {
  const [messages, setMessages] = useState([
    {
      role: 'assistant',
      content: `Hello! I am **BizAdvisor**, your senior virtual management consultant. I have reviewed your current gross revenue of **${businessData.currency}${businessData.metrics.totalRevenue.toLocaleString()}**, net profit margin of **${businessData.metrics.profitMargin}%**, and customer cohort metrics. What strategic challenge can I help you analyze today?`
    }
  ]);
  const [input, setInput] = useState(initialQuery || '');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

  const suggestedPrompts = [
    'How do we increase our net margin by 5%?',
    'Should we reallocate ad spend from lower ROI channels?',
    'What are our top financial risks right now?',
    'How can we reduce churn among At-Risk buyers?',
    'What should our Q4 sales and inventory strategy be?'
  ];

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, loading]);

  useEffect(() => {
    if (initialQuery) {
      handleSend(initialQuery);
    }
  }, [initialQuery]);

  const handleSend = async (queryText) => {
    const textToSend = queryText || input;
    if (!textToSend.trim() || loading) return;

    const userMessage = { role: 'user', content: textToSend };
    setMessages(prev => [...prev, userMessage]);
    setInput('');
    setLoading(true);

    try {
      const advice = await consultBizAdvisor({
        prompt: textToSend,
        businessData,
        customApiKey
      });
      setMessages(prev => [...prev, { role: 'assistant', content: advice }]);
    } catch (err) {
      setMessages(prev => [...prev, {
        role: 'assistant',
        content: `Error processing consultation request: ${err.message}`
      }]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="h-[calc(100vh-12rem)] flex flex-col bg-slate-800/60 border border-slate-750 rounded-2xl overflow-hidden shadow-lg">
      {/* Header */}
      <div className="p-4 bg-slate-900/80 border-b border-slate-800 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-sky-600 to-indigo-600 flex items-center justify-center text-white shadow-md">
            <Bot className="w-5 h-5" />
          </div>
          <div>
            <h3 className="font-bold text-white text-sm flex items-center space-x-1.5">
              <span>BizAdvisor Intelligence Assistant</span>
              <span className="w-2 h-2 rounded-full bg-emerald-400 inline-block animate-pulse" />
            </h3>
            <p className="text-xs text-slate-400">Grounded in live financials for {businessData.title}</p>
          </div>
        </div>
        {messages.length > 1 && (
          <button
            onClick={() => setMessages([messages[0]])}
            className="text-xs text-slate-400 hover:text-slate-200 p-2 rounded-lg hover:bg-slate-800 flex items-center space-x-1"
            title="Clear Chat"
          >
            <Trash2 className="w-4 h-4" />
            <span className="hidden sm:inline">Clear History</span>
          </button>
        )}
      </div>

      {/* Messages Scroll Area */}
      <div className="flex-1 overflow-y-auto p-4 sm:p-6 space-y-4">
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`flex items-start space-x-3 ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}
          >
            {msg.role === 'assistant' && (
              <div className="w-8 h-8 rounded-lg bg-sky-600/20 text-sky-400 flex items-center justify-center shrink-0 mt-0.5 border border-sky-500/20">
                <Bot className="w-4 h-4" />
              </div>
            )}
            <div
              className={`max-w-2xl rounded-2xl p-4 text-xs sm:text-sm leading-relaxed whitespace-pre-wrap ${
                msg.role === 'user'
                  ? 'bg-sky-600 text-white rounded-tr-none shadow-md font-medium'
                  : 'bg-slate-900/80 text-slate-200 border border-slate-800 rounded-tl-none shadow-sm'
              }`}
            >
              {msg.content}
            </div>
            {msg.role === 'user' && (
              <div className="w-8 h-8 rounded-lg bg-slate-700 text-slate-200 flex items-center justify-center shrink-0 mt-0.5">
                <User className="w-4 h-4" />
              </div>
            )}
          </div>
        ))}

        {loading && (
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 rounded-lg bg-sky-600/20 text-sky-400 flex items-center justify-center">
              <Loader2 className="w-4 h-4 animate-spin" />
            </div>
            <div className="bg-slate-900/60 text-slate-400 border border-slate-800 rounded-2xl rounded-tl-none p-3 text-xs italic flex items-center space-x-2">
              <span>BizAdvisor is analyzing business models & margins...</span>
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Suggested Prompts if only welcome message */}
      {messages.length === 1 && (
        <div className="px-4 pb-2">
          <p className="text-[11px] font-bold uppercase tracking-wider text-slate-400 mb-2">Suggested Inquiries:</p>
          <div className="flex flex-wrap gap-1.5">
            {suggestedPrompts.map((p, i) => (
              <button
                key={i}
                onClick={() => handleSend(p)}
                className="text-xs bg-slate-800/80 hover:bg-slate-750 text-slate-300 border border-slate-700 px-3 py-1.5 rounded-lg transition-colors text-left"
              >
                {p}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Input bar */}
      <div className="p-4 bg-slate-900 border-t border-slate-800">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSend();
          }}
          className="flex items-center space-x-2"
        >
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Ask about margins, CAC reduction, bundling, customer retention..."
            className="flex-1 bg-slate-800 border border-slate-700 rounded-xl px-4 py-2.5 text-xs sm:text-sm text-white placeholder-slate-400 focus:outline-none focus:border-sky-500"
          />
          <button
            type="submit"
            disabled={!input.trim() || loading}
            className="bg-sky-600 hover:bg-sky-500 disabled:opacity-50 text-white p-2.5 rounded-xl transition-colors shrink-0 shadow-md"
          >
            <Send className="w-4 h-4" />
          </button>
        </form>
      </div>
    </div>
  );
}
