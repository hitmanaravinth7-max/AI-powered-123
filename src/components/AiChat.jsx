import React, { useState, useRef, useEffect } from 'react';
import { Sparkles, Send, Trash2, Bot, User, Loader2, Copy, Check, AlertCircle } from 'lucide-react';
import { consultBizAdvisor } from '../services/geminiService';
import MarkdownRenderer from './MarkdownRenderer';

export default function AiChat({ businessData, initialQuery, customApiKey }) {
  const [messages, setMessages] = useState([
    {
      id: 'welcome-1',
      role: 'assistant',
      content: `### 💼 Welcome to BizAdvisor Executive Intelligence

Hello! I am **BizAdvisor**, your virtual senior business consultant. I have analyzed your company baseline for **${businessData.title}** (${businessData.industry}):

• **Gross Revenue:** **${businessData.currency}${businessData.metrics.totalRevenue.toLocaleString()}** (MoM Growth: **+${businessData.metrics.revenueGrowth}%**)
• **Net Margin:** **${businessData.metrics.profitMargin}%** (Net Profit: **${businessData.currency}${businessData.metrics.netProfit.toLocaleString()}**)
• **Marketing Efficiency:** **${businessData.metrics.marketingRoi}x ROI** across ${businessData.marketingChannels.length} active channels
• **Risk Status:** **${businessData.metrics.riskLevel}** (${businessData.metrics.riskScore}/100)

How can I assist your leadership team today? You may ask about **profitability strategies**, **ad spend reallocation**, **churn reduction**, or select a prompt below.`
    }
  ]);

  const [input, setInput] = useState(initialQuery || '');
  const [loading, setLoading] = useState(false);
  const [copiedId, setCopiedId] = useState(null);
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
    const textToSend = (queryText || input).trim();
    if (!textToSend || loading) return;

    const userMessageId = `user-${Date.now()}`;
    const userMessage = {
      id: userMessageId,
      role: 'user',
      content: textToSend,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };

    // Append user message immediately
    setMessages(prev => [...prev, userMessage]);
    setInput('');
    setLoading(true);

    try {
      const advice = await consultBizAdvisor({
        prompt: textToSend,
        businessData,
        customApiKey
      });

      const assistantMessageId = `assistant-${Date.now()}`;
      const assistantMessage = {
        id: assistantMessageId,
        role: 'assistant',
        content: advice && advice.trim().length > 0 ? advice : 'No advice was generated for this inquiry. Please try rephrasing your question.',
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      };

      // Append assistant message in strict chronological order
      setMessages(prev => [...prev, assistantMessage]);
    } catch (err) {
      const errorMessage = {
        id: `err-${Date.now()}`,
        role: 'assistant',
        content: `### ⚠️ Advisory Connection Notice\n\nUnable to reach external language services: **${err.message || 'Network Timeout'}**.\n\nPlease check your API key in **Settings** or re-try your question.`,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        isError: true
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setLoading(false);
    }
  };

  const handleCopyMessage = (id, text) => {
    navigator.clipboard.writeText(text);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  return (
    <div className="h-[calc(100vh-12rem)] min-h-[520px] flex flex-col bg-slate-800/60 border border-slate-750 rounded-2xl overflow-hidden shadow-xl">
      {/* Consultant Header */}
      <div className="p-4 bg-slate-900/90 border-b border-slate-800 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-sky-600 to-indigo-600 flex items-center justify-center text-white shadow-md shadow-sky-500/10">
            <Bot className="w-5 h-5" />
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <h3 className="font-bold text-white text-sm">BizAdvisor AI Consultant</h3>
              <span className="flex items-center space-x-1 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[10px] px-2 py-0.5 rounded-full font-bold">
                <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
                <span>ONLINE</span>
              </span>
            </div>
            <p className="text-xs text-slate-400 font-medium">Grounded in live metrics for {businessData.title}</p>
          </div>
        </div>

        {messages.length > 1 && (
          <button
            onClick={() => setMessages([messages[0]])}
            className="text-xs text-slate-400 hover:text-rose-400 p-2 rounded-xl hover:bg-slate-800/80 transition-colors flex items-center space-x-1.5 border border-transparent hover:border-slate-700"
            title="Clear Chat History"
          >
            <Trash2 className="w-4 h-4" />
            <span className="hidden sm:inline font-medium">Clear History</span>
          </button>
        )}
      </div>

      {/* Messages Scroll Area */}
      <div className="flex-1 overflow-y-auto p-4 sm:p-6 space-y-5">
        {messages.map((msg) => {
          const isUser = msg.role === 'user';

          return (
            <div
              key={msg.id}
              className={`flex items-start space-x-3 ${isUser ? 'justify-end' : 'justify-start'}`}
            >
              {/* Avatar on AI responses */}
              {!isUser && (
                <div className="w-8 h-8 rounded-xl bg-slate-900 border border-slate-800 text-sky-400 flex items-center justify-center shrink-0 mt-1 shadow-sm">
                  <Bot className="w-4 h-4" />
                </div>
              )}

              {/* Message Bubble Container */}
              <div
                className={`max-w-2xl flex flex-col ${
                  isUser ? 'items-end' : 'items-start'
                }`}
              >
                {/* Sender Title / Role */}
                <div className="flex items-center space-x-2 mb-1 px-1 text-[11px] text-slate-400">
                  <span className="font-semibold text-slate-300">
                    {isUser ? 'You' : 'BizAdvisor'}
                  </span>
                  {msg.timestamp && <span>&bull; {msg.timestamp}</span>}
                </div>

                {/* Bubble Body */}
                <div
                  className={`rounded-2xl p-4 sm:p-5 text-xs sm:text-sm leading-relaxed shadow-md transition-all ${
                    isUser
                      ? 'bg-sky-600 text-white rounded-tr-none font-medium'
                      : msg.isError
                      ? 'bg-rose-950/40 border border-rose-800 text-rose-200 rounded-tl-none'
                      : 'bg-slate-900/90 border border-slate-750 text-slate-200 rounded-tl-none'
                  }`}
                >
                  {isUser ? (
                    <p className="whitespace-pre-wrap">{msg.content}</p>
                  ) : (
                    <MarkdownRenderer content={msg.content} />
                  )}
                </div>

                {/* Actions below AI bubble */}
                {!isUser && (
                  <div className="flex items-center space-x-2 mt-1.5 px-1">
                    <button
                      onClick={() => handleCopyMessage(msg.id, msg.content)}
                      className="text-[11px] text-slate-400 hover:text-slate-200 flex items-center space-x-1 py-0.5 px-1.5 rounded hover:bg-slate-800 transition-colors"
                    >
                      {copiedId === msg.id ? (
                        <>
                          <Check className="w-3 h-3 text-emerald-400" />
                          <span className="text-emerald-400 font-semibold">Copied</span>
                        </>
                      ) : (
                        <>
                          <Copy className="w-3 h-3" />
                          <span>Copy Response</span>
                        </>
                      )}
                    </button>
                  </div>
                )}
              </div>

              {/* Avatar on User responses */}
              {isUser && (
                <div className="w-8 h-8 rounded-xl bg-slate-800 border border-slate-700 text-slate-200 flex items-center justify-center shrink-0 mt-1 shadow-sm">
                  <User className="w-4 h-4" />
                </div>
              )}
            </div>
          );
        })}

        {/* Loading Indicator */}
        {loading && (
          <div className="flex items-start space-x-3">
            <div className="w-8 h-8 rounded-xl bg-slate-900 border border-slate-800 text-sky-400 flex items-center justify-center shrink-0 mt-1">
              <Bot className="w-4 h-4" />
            </div>
            <div className="bg-slate-900/90 border border-slate-750 rounded-2xl rounded-tl-none p-4 text-xs text-slate-300 flex items-center space-x-3 shadow-sm">
              <Loader2 className="w-4 h-4 text-sky-400 animate-spin shrink-0" />
              <span>BizAdvisor is synthesizing financial ratios & strategic priorities...</span>
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Suggested Prompt Chips */}
      {messages.length === 1 && (
        <div className="px-4 sm:px-6 py-2 bg-slate-900/40 border-t border-slate-800/80">
          <p className="text-[11px] font-bold uppercase tracking-wider text-slate-400 mb-2 flex items-center space-x-1.5">
            <Sparkles className="w-3.5 h-3.5 text-sky-400" />
            <span>Recommended Strategic Questions:</span>
          </p>
          <div className="flex flex-wrap gap-2">
            {suggestedPrompts.map((p, i) => (
              <button
                key={i}
                onClick={() => handleSend(p)}
                className="text-xs bg-slate-800/80 hover:bg-slate-750 text-slate-300 hover:text-white border border-slate-700/80 hover:border-sky-500/40 px-3 py-1.5 rounded-xl transition-all text-left"
              >
                {p}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Input Bar */}
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
            placeholder="Ask about margins, cashflow runway, CAC optimization, product bundling..."
            className="flex-1 bg-slate-800/90 border border-slate-700 rounded-xl px-4 py-2.5 text-xs sm:text-sm text-white placeholder-slate-400 focus:outline-none focus:border-sky-500 focus:ring-1 focus:ring-sky-500/50 transition-all"
          />
          <button
            type="submit"
            disabled={!input.trim() || loading}
            className="bg-sky-600 hover:bg-sky-500 disabled:opacity-40 disabled:cursor-not-allowed text-white p-2.5 rounded-xl transition-all shrink-0 shadow-md shadow-sky-600/20"
            title="Send Message"
          >
            <Send className="w-4 h-4" />
          </button>
        </form>
      </div>
    </div>
  );
}
