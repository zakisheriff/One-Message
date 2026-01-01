const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');

// Middleware to verify JWT
const authenticate = (req, res, next) => {
    const token = req.headers.authorization?.replace('Bearer ', '');

    if (!token) {
        return res.status(401).json({ error: 'Authentication required' });
    }

    try {
        const decoded = jwt.verify(token, process.env.JWT_SECRET || 'dev-secret-change-in-production');
        req.user = decoded;
        next();
    } catch {
        return res.status(401).json({ error: 'Invalid token' });
    }
};

// HTTP fallback for sending messages (when WebSocket unavailable)
router.post('/send', authenticate, async (req, res) => {
    try {
        const { recipientId, encryptedContent, messageType = 'text' } = req.body;
        const io = req.app.get('io');
        const activeConnections = req.app.get('activeConnections');

        if (!recipientId || !encryptedContent) {
            return res.status(400).json({ error: 'Recipient and content required' });
        }

        const message = {
            id: require('uuid').v4(),
            senderId: req.user.userId,
            recipientId,
            encryptedContent,
            messageType,
            timestamp: new Date().toISOString(),
            status: 'sent'
        };

        // Try to deliver via WebSocket if recipient is online
        const recipientSocket = activeConnections.get(recipientId);
        if (recipientSocket) {
            io.to(recipientSocket).emit('message:receive', message);
            message.status = 'delivered';
        }

        res.json({
            success: true,
            message: {
                id: message.id,
                status: message.status,
                timestamp: message.timestamp
            }
        });
    } catch (error) {
        console.error('Message send error:', error);
        res.status(500).json({ error: 'Failed to send message' });
    }
});

// Get pending messages for a device
router.get('/pending', authenticate, async (req, res) => {
    try {
        // In production, fetch from Redis/DB
        // For now, return empty (messages delivered via WebSocket)
        res.json({
            success: true,
            messages: []
        });
    } catch (error) {
        console.error('Pending messages error:', error);
        res.status(500).json({ error: 'Failed to fetch messages' });
    }
});

module.exports = router;
