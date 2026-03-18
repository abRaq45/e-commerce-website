import { useState, useEffect, useRef } from "react";
import API from "../api/axios"; // ✅ import this

const ChatPage = ({ user }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const chatEndRef = useRef(null);

  const userId = user?.userId;

  if (!userId) {
    return <div style={{ padding: "20px" }}>Please login first</div>;
  }

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage = { sender: "user", text: input };
    setMessages((prev) => [...prev, userMessage]);

    try {
      // ✅ FIXED: using API (no localhost)
      const response = await API.post(
        `/chat?userId=${userId}`,
        input,
        {
          headers: {
            "Content-Type": "text/plain",
          },
        }
      );

      const botMessage = { sender: "bot", text: response.data };

      setMessages((prev) => [...prev, botMessage]);

    } catch (error) {
      setMessages((prev) => [
        ...prev,
        { sender: "bot", text: "⚠️ Error connecting to server" },
      ]);
    }

    setInput("");
  };

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="chat-container">
      <div className="chat-wrapper">

        <div className="chat-header">
          🛒 Smart Assistant
        </div>

        <div className="chat-box">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`message ${msg.sender === "user" ? "user" : "bot"}`}
            >
              {msg.text}
            </div>
          ))}
          <div ref={chatEndRef}></div>
        </div>

        <div className="input-box">
          <input
            type="text"
            placeholder="Ask something..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          />
          <button onClick={sendMessage}>Send</button>
        </div>

      </div>
    </div>
  );
};

export default ChatPage;
