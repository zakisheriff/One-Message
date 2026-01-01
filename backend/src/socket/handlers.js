const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');

function setupSocketHandlers(io, socket, activeConnections) {
    let authenticatedUser = null;

    // Authenticate socket connection
    socket.on('authenticate', (data) => {
        try {
            const { token } = data;
            const decoded = jwt.verify(token, process.env.JWT_SECRET || 'dev-secret-change-in-production');

            authenticatedUser = decoded;
            activeConnections.set(decoded.userId, socket.id);

            socket.emit('authenticated', {
                success: true,
                userId: decoded.userId
            });

            console.log(`✅ User ${decoded.userId} authenticated via WebSocket`);
        } catch (error) {
            socket.emit('authenticated', {
                success: false,
                error: 'Invalid token'
            });
        }
    });

    // Send message
    socket.on('message:send', (data) => {
        if (!authenticatedUser) {
            socket.emit('error', { message: 'Not authenticated' });
            return;
        }

        const { recipientId, encryptedContent, messageType = 'text' } = data;

        const message = {
            id: uuidv4(),
            senderId: authenticatedUser.userId,
            recipientId,
            encryptedContent,
            messageType,
            timestamp: new Date().toISOString()
        };

        // Send acknowledgment to sender
        socket.emit('message:sent', {
            id: message.id,
            status: 'sent',
            timestamp: message.timestamp
        });

        // Deliver to recipient if online
        const recipientSocketId = activeConnections.get(recipientId);
        if (recipientSocketId) {
            io.to(recipientSocketId).emit('message:receive', message);

            // Notify sender of delivery
            socket.emit('message:delivered', {
                messageId: message.id,
                deliveredAt: new Date().toISOString()
            });
        }

        console.log(`💬 Message ${message.id} from ${authenticatedUser.userId} to ${recipientId}`);
    });

    // Message read receipt
    socket.on('message:read', (data) => {
        if (!authenticatedUser) return;

        const { messageId, senderId } = data;
        const senderSocketId = activeConnections.get(senderId);

        if (senderSocketId) {
            io.to(senderSocketId).emit('message:read', {
                messageId,
                readBy: authenticatedUser.userId,
                readAt: new Date().toISOString()
            });
        }
    });

    // Typing indicators
    socket.on('typing:start', (data) => {
        if (!authenticatedUser) return;

        const { recipientId } = data;
        const recipientSocketId = activeConnections.get(recipientId);

        if (recipientSocketId) {
            io.to(recipientSocketId).emit('typing:start', {
                userId: authenticatedUser.userId
            });
        }
    });

    socket.on('typing:stop', (data) => {
        if (!authenticatedUser) return;

        const { recipientId } = data;
        const recipientSocketId = activeConnections.get(recipientId);

        if (recipientSocketId) {
            io.to(recipientSocketId).emit('typing:stop', {
                userId: authenticatedUser.userId
            });
        }
    });

    // Presence
    socket.on('presence:update', (data) => {
        if (!authenticatedUser) return;

        // Broadcast presence to interested parties
        socket.broadcast.emit('presence:update', {
            userId: authenticatedUser.userId,
            status: data.status,
            lastSeen: new Date().toISOString()
        });
    });

    // Handle disconnection
    socket.on('disconnect', () => {
        if (authenticatedUser) {
            activeConnections.delete(authenticatedUser.userId);

            // Broadcast offline status
            socket.broadcast.emit('presence:update', {
                userId: authenticatedUser.userId,
                status: 'offline',
                lastSeen: new Date().toISOString()
            });

            console.log(`👋 User ${authenticatedUser.userId} disconnected`);
        }
    });
}

module.exports = { setupSocketHandlers };
