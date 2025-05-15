import React, { useState } from 'react';
import '../style/Chat.css';

function Chat() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');

  const handleSend = () => {
    if (!input.trim()) return;
    const userMessage = { role: 'user', content: input };
    setMessages(prev => [...prev, userMessage]);

    fetch('http://localhost:8080/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ message: input })
    })
      .then(res => res.json())
      .then(data => {
        const botMessage = { role: 'bot', content: data.response };
        setMessages(prev => [...prev, botMessage]);
      })
      .catch(() => {
        const errorMessage = { role: 'bot', content: "Communication error with the server" };
        setMessages(prev => [...prev, errorMessage]);
      });

    setInput('');
  };

  return (
    <div className="chat-page">
      <header className="navbar">
        <div className="logo"><a href="/">Ethica</a></div>
        <ul className="nav">
          <li><a href="/dashboard">Home</a></li>
          <li><a href="/about">About Us</a></li>
          <li><a href="/founder">Price</a></li>
          <li><a href="/news">News</a></li>
          <li><a href="/signUp">Sign Up</a></li>
          <li><a href="/login">Deconnexion</a></li>
        </ul>
      </header>

      <main className="chat-container">
        <h2>Ethica Chatbot</h2>
        <div className="chat-box">
          {messages.map((msg, idx) => (
            <div key={idx} className={`message ${msg.role}`}>
              <strong>{msg.role === 'user' ? 'You' : 'Ethica Bot'}:</strong> {msg.content}
            </div>
          ))}
        </div>
        <div className="input-area">
          <input
            value={input}
            onChange={e => setInput(e.target.value)}
            placeholder="Ask your question about the market..."
          />
          <button onClick={handleSend}>Send</button>
        </div>
      </main>

      <footer className="footer">
        <div className="footer-container">
          <div className="footer-left">
            <h4>Ethica</h4>
            <p>&copy; 2025 Ethica Inc. All rights reserved.</p>
          </div>
          <div className="footer-links">
            <ul>
              <li><a href="/about">About Us</a></li>
              <li><a href="/privacy">Privacy Policy</a></li>
              <li><a href="/terms">Terms of Use</a></li>
              <li><a href="/contact">Contact</a></li>
            </ul>
          </div>
          <div className="footer-socials">
            <p>Follow us</p>
            <a href="https://linkedin.com" target="_blank">LinkedIn</a> |
            <a href="https://twitter.com" target="_blank">Twitter</a>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Chat;
