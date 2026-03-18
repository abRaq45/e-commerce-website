import { useState, useEffect, useRef } from "react";

const ChatPage = ({ user }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const chatEndRef = useRef(null);

  // ✅ Get userId safely
  const userId = user?.userId;

  // ❗ Prevent crash if user not available
  if (!userId) {
    return <div style={{ padding: "20px" }}>Please login first</div>;
  }

  // 🔹 Send message
  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage = { sender: "user", text: input };
    setMessages((prev) => [...prev, userMessage]);

    try {
      const response = await fetch(
        `http://localhost:8080/api/chat?userId=${userId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "text/plain",
          },
          body: input,
        }
      );

      const data = await response.text();

      const botMessage = { sender: "bot", text: data };

      setMessages((prev) => [...prev, botMessage]);
    } catch (error) {
      setMessages((prev) => [
        ...prev,
        { sender: "bot", text: "⚠️ Error connecting to server" },
      ]);
    }

    setInput("");
  };

  // 🔹 Auto scroll
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

 return (
  <div className="chat-container">

    <div className="chat-wrapper">

      {/* Header */}
      <div className="chat-header">
        🛒 Smart Assistant
      </div>

      {/* Messages */}
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

      {/* Input */}
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