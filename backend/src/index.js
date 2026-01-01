require('dotenv').config();
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const cors = require('cors');

// Import routes
const authRoutes = require('./routes/auth');
const messageRoutes = require('./routes/messages');

// Import socket handlers
const { setupSocketHandlers } = require('./socket/handlers');

const app = express();
const httpServer = createServer(app);

// Socket.IO setup with CORS
const io = new Server(httpServer, {
    cors: {
        origin: '*', // In production, specify allowed origins
        methods: ['GET', 'POST']
    }
});

// Middleware
app.use(cors());
app.use(express.json());

// Store active connections (userId -> socket mapping)
const activeConnections = new Map();

// Make io and connections available to routes
app.set('io', io);
app.set('activeConnections', activeConnections);

// Health check endpoint
app.get('/health', (req, res) => {
    res.json({
        status: 'ok',
        service: 'One Message API',
        version: '1.0.0',
        timestamp: new Date().toISOString()
    });
});

// Routes
app.use('/auth', authRoutes);
app.use('/messages', messageRoutes);

// Socket.IO connection handling
io.on('connection', (socket) => {
    console.log(`Client connected: ${socket.id}`);
    setupSocketHandlers(io, socket, activeConnections);
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        error: 'Internal server error',
        message: process.env.NODE_ENV === 'development' ? err.message : undefined
    });
});

// Start server
const PORT = process.env.PORT || 3000;
httpServer.listen(PORT, () => {
    console.log(`🚀 One Message Server running on port ${PORT}`);
    console.log(`📡 WebSocket server ready`);
    console.log(`💬 Ready to handle messages`);
});

module.exports = { app, io };
