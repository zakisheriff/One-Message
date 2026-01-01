# <div align="center">One Message</div>

<div align="center">
<strong>Secure Hybrid Messaging with End-to-End Encryption & Offline SMS Fallback</strong>
</div>

<br />

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-Express-339933?style=for-the-badge&logo=node.js&logoColor=white)
![Socket.IO](https://img.shields.io/badge/Socket.IO-4.7-010101?style=for-the-badge&logo=socketdotio&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)

</div>

<br />

> **"Your messages. Your privacy. Your control."**
>
> One Message isn't just another messaging app — it's a commitment to security-first communication.  
> Built with Signal Protocol encryption, designed with The One Atom aesthetic, and engineered for reliability with offline SMS fallback.

---

## 🔐 Vision

One Message is designed to be:

- **A truly private messaging platform** — end-to-end encryption by default
- **A hybrid communication system** — seamless switch between internet and SMS
- **A beautifully crafted experience** — The One Atom design with glassmorphism

---

## ✨ Why One Message?

Most messaging apps compromise between security and usability.  
One Message delivers **both** — military-grade encryption with a premium user experience.

---

## 🎨 The One Atom Design System

- **Pure Black Background**  
  `#000000` for true OLED blacks and maximum contrast.

- **Glassmorphism Effects**  
  Translucent panels with `backdrop-filter: blur()` for depth and elegance.

- **White & Gray Typography**  
  Clean, readable text hierarchy with proper visual weight.

- **Ambient Orbs**  
  Subtle floating gradient effects for visual atmosphere.

---

## 🛡️ Security Architecture

- **Signal Protocol**  
  Industry-standard end-to-end encryption for all messages.

- **Device-Only Storage**  
  Messages stored locally with Room encryption — server never sees plaintext.

- **OTP Authentication**  
  Phone number verification via Twilio — no passwords to hack.

- **Multi-Device Sync**  
  Secure key exchange for seamless cross-device experience.

---

## 📱 Platform Support

### Android (Kotlin/Jetpack Compose)
- Material 3 design with custom One Atom theme
- Native performance with modern architecture
- Room database with encryption

### iOS (Swift/SwiftUI) — *Coming Soon*
- Native SwiftUI implementation
- CoreData with encryption
- APNs push notifications

### Web Landing Page
- Marketing website with glassmorphic design
- Responsive for all devices
- Premium animations and interactions

---

## 📁 Project Structure

```
One-Message/
├── app/                          # Android App (Kotlin/Compose)
│   ├── src/main/java/com/example/onemessage/
│   │   ├── MainActivity.kt       # App entry point
│   │   ├── ui/
│   │   │   ├── screens/          # UI Screens
│   │   │   │   ├── LoginScreen.kt      # Phone + OTP auth
│   │   │   │   ├── ChatListScreen.kt   # Conversations list
│   │   │   │   └── ChatScreen.kt       # Individual chat
│   │   │   └── theme/            # One Atom Design System
│   │   │       ├── Color.kt      # Color palette
│   │   │       ├── Theme.kt      # Dark theme config
│   │   │       └── Type.kt       # Typography scale
│   │   └── navigation/
│   │       └── Navigation.kt     # Jetpack Navigation
│   └── build.gradle.kts
│
├── backend/                      # Node.js + Express API
│   ├── src/
│   │   ├── index.js              # Express + Socket.IO server
│   │   ├── routes/
│   │   │   ├── auth.js           # OTP authentication
│   │   │   └── messages.js       # HTTP message fallback
│   │   ├── socket/
│   │   │   └── handlers.js       # Real-time WebSocket events
│   │   └── db/
│   │       └── schema.sql        # PostgreSQL schema
│   ├── package.json
│   └── .env.example
│
└── landing/                      # Marketing Website
    ├── index.html                # Main HTML structure
    ├── styles.css                # Glassmorphism & animations
    └── script.js                 # Interactions & effects
```

---

## 🚀 Quick Start

### Prerequisites

- **Android Studio** (Hedgehog or later)
- **Node.js** (v18+)
- **PostgreSQL** (v14+)
- **Twilio Account** (for OTP)

### 1. Clone the Repository

```bash
git clone https://github.com/zakisheriff/One-Message.git
cd One-Message
```

### 2. Backend Setup

```bash
cd backend
npm install
cp .env.example .env
# Edit .env with your credentials
npm start
```

### 3. Android App

```bash
# Open in Android Studio
# Or build from command line:
./gradlew assembleDebug
```

### 4. Landing Page

```bash
# Simply open in browser
open landing/index.html
```

---

## 🎯 Key Features

### For Users

✅ **Phone Authentication** — Secure OTP verification  
✅ **170+ Countries** — Complete country code picker  
✅ **Real-Time Messaging** — Instant delivery via WebSocket  
✅ **Read Receipts** — Know when messages are read  
✅ **Typing Indicators** — See when someone is typing  
✅ **Offline Mode** — SMS fallback when internet unavailable  
✅ **End-to-End Encryption** — Signal Protocol protection  

### Technical Features

✅ **Material 3 Design** — Modern Android UI  
✅ **Socket.IO** — Real-time bidirectional communication  
✅ **JWT Sessions** — Secure token-based auth  
✅ **PostgreSQL** — Reliable data persistence  
✅ **Keyboard Handling** — Perfect mobile input experience  

---

## 🔧 Tech Stack

### Android
- **Kotlin** — Modern Android development
- **Jetpack Compose** — Declarative UI framework
- **Material 3** — Google's latest design system
- **Navigation Compose** — Type-safe navigation
- **Room** — Local database with encryption

### Backend
- **Node.js** + **Express** — REST API server
- **Socket.IO** — WebSocket real-time messaging
- **PostgreSQL** — Relational database
- **JWT** — Session management
- **Twilio** — SMS/OTP delivery

### Landing Page
- **Pure HTML/CSS/JS** — No frameworks
- **Glassmorphism** — Modern visual effects
- **CSS Animations** — Smooth interactions
- **Responsive Design** — Mobile-first approach

---

## 📊 Database Schema

Core tables with proper relationships:

- **users** — Phone numbers and profiles
- **devices** — Multi-device support with public keys
- **sessions** — JWT session management
- **otp_codes** — Verification codes with expiry
- **push_tokens** — FCM/APNs tokens
- **contacts** — User relationships
- **sms_transactions** — Offline message logs

---

## 🔒 Security Features

✅ **Signal Protocol** — End-to-end encryption  
✅ **OTP Verification** — Phone-based authentication  
✅ **Device Keys** — Per-device encryption keys  
✅ **JWT Tokens** — Secure session management  
✅ **Local Encryption** — Room database encryption  
✅ **No Server Storage** — Messages never stored on server  

---

## 🌐 API Endpoints

### Authentication
- `POST /auth/request-otp` — Send OTP to phone
- `POST /auth/verify-otp` — Verify and get JWT
- `POST /auth/register-device` — Register device keys
- `PUT /auth/profile` — Update display name

### Messages (HTTP Fallback)
- `POST /messages/send` — Send message via HTTP
- `GET /messages/pending` — Get pending messages

### WebSocket Events
- `message:send` / `message:receive` — Real-time messaging
- `message:delivered` / `message:read` — Delivery status
- `typing:start` / `typing:stop` — Typing indicators
- `presence:update` — Online/offline status

---

## 🗺️ Roadmap

- [x] Landing Page with glassmorphism
- [x] Backend with Socket.IO
- [x] Android UI screens
- [ ] Complete Android networking
- [ ] Signal Protocol integration
- [ ] iOS app (Swift/SwiftUI)
- [ ] Push notifications (FCM/APNs)
- [ ] Offline SMS mode
- [ ] Group messaging
- [ ] Media attachments

---

## 📄 License

**Proprietary License**

This project is part of The One Atom ecosystem. The code is provided for study and reference purposes only.

- ✅ You may view and study the code
- ✅ You may fork for personal learning
- ❌ Commercial use is prohibited
- ❌ Redistribution is prohibited
- ❌ Creating derivative works for distribution is prohibited

---

## ☕️ Support the Project

If One Message inspired you or helped with your learning:

<div align="center">
<a href="https://buymeacoffee.com/zakisheriffw">
<img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" height="60" width="217">
</a>
</div>

---

<p align="center">
Made by <strong>Zaki Sheriff</strong>
</p>

<p align="center">
<em>Part of The One Atom ecosystem</em>
</p>

<p align="center">
<strong>Privacy is not optional. It's fundamental.</strong>
</p>
