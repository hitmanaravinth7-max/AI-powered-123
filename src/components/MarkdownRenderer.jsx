import React from 'react';

/**
 * Safely parses inline markdown (bold **text**, italic *text*, inline `code`)
 * into React elements without using dangerouslySetInnerHTML.
 */
function renderInline(text) {
  if (!text) return null;

  // Regex matches:
  // 1. **bold**
  // 2. `code`
  // 3. *italic*
  const tokenRegex = /(\*\*[\s\S]+?\*\*|`[\s\S]+?`|\*[^\s*][\s\S]*?\*)/g;
  const parts = [];
  let lastIndex = 0;
  let match;

  while ((match = tokenRegex.exec(text)) !== null) {
    // Push preceding plain text
    if (match.index > lastIndex) {
      parts.push(text.substring(lastIndex, match.index));
    }

    const token = match[0];
    if (token.startsWith('**') && token.endsWith('**')) {
      const inner = token.slice(2, -2);
      parts.push(
        <strong key={`b-${match.index}`} className="font-bold text-white">
          {renderInline(inner)}
        </strong>
      );
    } else if (token.startsWith('`') && token.endsWith('`')) {
      const inner = token.slice(1, -1);
      parts.push(
        <code
          key={`c-${match.index}`}
          className="bg-slate-800 text-sky-300 px-1.5 py-0.5 rounded font-mono text-[11px] border border-slate-700/60"
        >
          {inner}
        </code>
      );
    } else if (token.startsWith('*') && token.endsWith('*')) {
      const inner = token.slice(1, -1);
      parts.push(
        <em key={`i-${match.index}`} className="italic text-slate-200">
          {renderInline(inner)}
        </em>
      );
    } else {
      parts.push(token);
    }

    lastIndex = tokenRegex.lastIndex;
  }

  // Push trailing text
  if (lastIndex < text.length) {
    parts.push(text.substring(lastIndex));
  }

  return parts.length > 0 ? parts : text;
}

/**
 * Parses full message text into structured, safe React elements.
 * Supports:
 * - #, ##, ###, #### Headings
 * - Bullet lists (*, -, •)
 * - Numbered lists (1., 2., etc.)
 * - Blockquotes (>)
 * - Paragraphs with readable spacing
 */
export default function MarkdownRenderer({ content }) {
  if (!content || typeof content !== 'string') {
    return <span className="text-slate-400 italic">No content available.</span>;
  }

  const rawLines = content.split('\n');
  const blocks = [];
  let currentList = null; // { type: 'ul' | 'ol', items: [] }

  const flushList = () => {
    if (currentList) {
      if (currentList.type === 'ul') {
        blocks.push({
          type: 'ul',
          items: currentList.items,
          key: `ul-${blocks.length}`
        });
      } else if (currentList.type === 'ol') {
        blocks.push({
          type: 'ol',
          items: currentList.items,
          key: `ol-${blocks.length}`
        });
      }
      currentList = null;
    }
  };

  for (let i = 0; i < rawLines.length; i++) {
    const line = rawLines[i].trim();

    if (!line) {
      flushList();
      continue;
    }

    // Heading matches
    if (line.startsWith('#### ')) {
      flushList();
      blocks.push({
        type: 'h4',
        text: line.replace(/^####\s+/, ''),
        key: `h4-${i}`
      });
      continue;
    }

    if (line.startsWith('### ')) {
      flushList();
      blocks.push({
        type: 'h3',
        text: line.replace(/^###\s+/, ''),
        key: `h3-${i}`
      });
      continue;
    }

    if (line.startsWith('## ')) {
      flushList();
      blocks.push({
        type: 'h2',
        text: line.replace(/^##\s+/, ''),
        key: `h2-${i}`
      });
      continue;
    }

    if (line.startsWith('# ')) {
      flushList();
      blocks.push({
        type: 'h1',
        text: line.replace(/^#\s+/, ''),
        key: `h1-${i}`
      });
      continue;
    }

    // Bullet list match (*, -, •)
    const bulletMatch = line.match(/^[*•-]\s+(.*)$/);
    if (bulletMatch) {
      if (!currentList || currentList.type !== 'ul') {
        flushList();
        currentList = { type: 'ul', items: [] };
      }
      currentList.items.push(bulletMatch[1]);
      continue;
    }

    // Numbered list match (1., 2., etc.)
    const numberMatch = line.match(/^(\d+)\.\s+(.*)$/);
    if (numberMatch) {
      if (!currentList || currentList.type !== 'ol') {
        flushList();
        currentList = { type: 'ol', items: [] };
      }
      currentList.items.push({ num: numberMatch[1], text: numberMatch[2] });
      continue;
    }

    // Blockquote
    if (line.startsWith('> ')) {
      flushList();
      blocks.push({
        type: 'quote',
        text: line.replace(/^>\s+/, ''),
        key: `quote-${i}`
      });
      continue;
    }

    // Normal paragraph line
    flushList();
    blocks.push({
      type: 'p',
      text: line,
      key: `p-${i}`
    });
  }

  flushList();

  return (
    <div className="space-y-2.5 text-xs sm:text-sm text-slate-200 leading-relaxed break-words">
      {blocks.map((block) => {
        switch (block.type) {
          case 'h1':
            return (
              <h1 key={block.key} className="text-base sm:text-lg font-extrabold text-white tracking-tight pt-1 border-b border-slate-700/60 pb-1">
                {renderInline(block.text)}
              </h1>
            );
          case 'h2':
            return (
              <h2 key={block.key} className="text-sm sm:text-base font-bold text-sky-300 tracking-tight pt-1">
                {renderInline(block.text)}
              </h2>
            );
          case 'h3':
            return (
              <h3 key={block.key} className="text-xs sm:text-sm font-bold text-sky-400 tracking-tight pt-1 flex items-center space-x-1.5">
                <span>{renderInline(block.text)}</span>
              </h3>
            );
          case 'h4':
            return (
              <h4 key={block.key} className="text-xs font-semibold text-slate-300 pt-0.5">
                {renderInline(block.text)}
              </h4>
            );
          case 'ul':
            return (
              <ul key={block.key} className="space-y-1.5 my-1.5 pl-1">
                {block.items.map((item, idx) => (
                  <li key={idx} className="flex items-start space-x-2 text-slate-300">
                    <span className="w-1.5 h-1.5 rounded-full bg-sky-400 mt-2 shrink-0" />
                    <span className="flex-1">{renderInline(item)}</span>
                  </li>
                ))}
              </ul>
            );
          case 'ol':
            return (
              <ol key={block.key} className="space-y-1.5 my-1.5 pl-1">
                {block.items.map((item, idx) => (
                  <li key={idx} className="flex items-start space-x-2 text-slate-300">
                    <span className="w-4 h-4 rounded-full bg-sky-500/20 text-sky-400 border border-sky-500/30 text-[10px] font-bold flex items-center justify-center shrink-0 mt-0.5">
                      {item.num}
                    </span>
                    <span className="flex-1">{renderInline(item.text)}</span>
                  </li>
                ))}
              </ol>
            );
          case 'quote':
            return (
              <div key={block.key} className="border-l-2 border-sky-500/60 pl-3 py-1 bg-slate-800/40 rounded-r-lg my-1 text-slate-300 italic">
                {renderInline(block.text)}
              </div>
            );
          case 'p':
          default:
            return (
              <p key={block.key} className="leading-relaxed">
                {renderInline(block.text)}
              </p>
            );
        }
      })}
    </div>
  );
}
