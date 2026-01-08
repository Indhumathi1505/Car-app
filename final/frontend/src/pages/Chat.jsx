import { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import axios from "axios";
import "./Chat.css";

const SOCKET_URL = "http://localhost:8080/ws";

export default function Chat({
  carId,
  user,        // logged-in email (buyer or seller)
  role,        // "BUYER" or "SELLER"
  receiver, 
   buyerEmail,
  sellerEmail   // other person's email
    // seller display name / email
}) {

  // ================= BASIC SAFETY =================
  const loggedUser = JSON.parse(localStorage.getItem("user"));
const username = loggedUser?.email;

  

  // ================= STATE =================
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [connected, setConnected] = useState(false);

  const clientRef = useRef(null);
  const bottomRef = useRef(null);

  // =================================================
  // 1️⃣ LOAD CHAT HISTORY (VERY IMPORTANT FOR REFRESH)
  // =================================================
  useEffect(() => {
    if (!carId || !username ||!receiver ) return;

    // 🔵 BUYER loads only his chat
    if (role === "BUYER") {
  axios.get("http://localhost:8080/api/chat/buyer", {
    params: {
      carId,
      sellerEmail: receiver,   // ✅ seller email
      buyerEmail: username     // ✅ buyer email
    }
  })
  .then(res => setMessages(res.data))
  .catch(err => console.error("Load buyer chat error", err.response || err));
}

    // 🟢 SELLER loads full car chat (filtered later)
  if (role === "SELLER" && receiver) {
  axios.get("http://localhost:8080/api/chat/buyer", {
    params: {
      carId,
      sellerEmail: username,
      buyerEmail: receiver
    }
  })
  .then(res => setMessages(res.data))
  .catch(err => console.error(err));
}
}, [carId, username, receiver,role]);



  // =====================================
  // 2️⃣ WEBSOCKET CONNECTION (REAL-TIME)
  // =====================================
  useEffect(() => {
    if (!carId || !username ||!receiver) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(SOCKET_URL),
      reconnectDelay: 5000,
      debug: () => {}
    });

    client.onConnect = () => {
      setConnected(true);

      // 🔥 EACH USER HAS OWN TOPIC
const safeEmail = username.replace(/\./g, "_");
client.subscribe(
  `/topic/chat/${carId}/${safeEmail}`,
  msg => {
    console.log("📩 Message received on topic:", `/topic/chat/${carId}/${safeEmail}`);
    console.log("📩 Raw message:", msg.body);

    const body = JSON.parse(msg.body);
    setMessages(prev => [...prev, body]);
  }
);



    };


    client.onDisconnect = () => setConnected(false);

    client.activate();
    clientRef.current = client;

    return () => {
      clientRef.current?.deactivate();
      clientRef.current = null;
    };
  }, [carId, username,receiver]);

  // ================= AUTO SCROLL =================
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // ================= SEND MESSAGE =================
  // ================= SEND MESSAGE =================
  const sendMessage = e => {
  e.preventDefault();
  if (!connected || !text.trim()) return;

  const payload = {
    carId,
    message: text,
    sender: username,
    buyerEmail,
    sellerEmail
  };

  console.log("📤 Sending payload:", payload);

  clientRef.current.publish({
    destination: `/app/chat/${carId}`,
    body: JSON.stringify(payload)
  });

  setText("");
};



// ================= FILTER DISPLAY =================



  // ================= FILTER DISPLAY =================
  /*const displayedMessages = messages.filter(
  m =>
    (m.buyerEmail === username && m.sellerEmail === receiver) ||
    (m.buyerEmail === receiver && m.sellerEmail === username)
);*/
const displayedMessages = messages.filter(
  m =>
    m.carId === carId &&
    (
      (m.buyerEmail === username && m.sellerEmail === receiver) ||
      (m.buyerEmail === receiver && m.sellerEmail === username)
    )
);





     
      if (!user) {
  return <div className="chat-error">Please login</div>;
}


  // ================= UI =================
  return (
    <div className="chat-container">

      <div className="chat-header">
        <span>
          {role === "BUYER"
            ? `Chat with ${receiver}`
            : `Chat with ${receiver}`}
        </span>
        <span className={connected ? "online" : "offline"}>
          {connected ? "● Online" : "● Connecting"}
        </span>
      </div>

      <div className="chat-body">
        {displayedMessages.map((m) => (
  <div
    key={m.id || m._id || m.timestamp}
    className={`chat-message ${m.sender === username ? "me" : "other"}`}
  >

            <b>{m.sender}</b>
            <p>{m.message}</p>
           <small>
  {m.timestamp ? new Date(m.timestamp).toLocaleString() : ""}
</small>

          </div>
        ))}
        <div ref={bottomRef} />
      </div>

      <form className="chat-footer" onSubmit={sendMessage}>
        <input
          value={text}
          onChange={e => setText(e.target.value)}
          placeholder="Type a message..."
          disabled={!connected}
        />
        <button disabled={!connected || !text.trim()}>
          Send
        </button>
      </form>

    </div>
  );
}
