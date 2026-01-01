const express = require('express');
const router = express.Router();
const { v4: uuidv4 } = require('uuid');
const jwt = require('jsonwebtoken');

// In-memory storage for development (use Redis/DB in production)
const otpStore = new Map();
const userStore = new Map();
const sessionStore = new Map();

// OTP generation
function generateOTP() {
    return Math.floor(1000 + Math.random() * 9000).toString();
}

// Request OTP
router.post('/request-otp', async (req, res) => {
    try {
        const { phoneNumber } = req.body;

        if (!phoneNumber || phoneNumber.length < 10) {
            return res.status(400).json({ error: 'Invalid phone number' });
        }

        const otp = generateOTP();
        const expiresAt = Date.now() + 5 * 60 * 1000; // 5 minutes

        // Store OTP
        otpStore.set(phoneNumber, { otp, expiresAt });

        // In development, log OTP. In production, send via Twilio
        console.log(`📱 OTP for ${phoneNumber}: ${otp}`);

        // TODO: Uncomment for production Twilio integration
        /*
        const twilio = require('twilio')(
            process.env.TWILIO_ACCOUNT_SID,
            process.env.TWILIO_AUTH_TOKEN
        );
        await twilio.messages.create({
            body: `Your One Message verification code is: ${otp}`,
            from: process.env.TWILIO_PHONE_NUMBER,
            to: phoneNumber
        });
        */

        res.json({
            success: true,
            message: 'OTP sent successfully',
            // Remove in production:
            devOtp: process.env.NODE_ENV === 'development' ? otp : undefined
        });
    } catch (error) {
        console.error('OTP request error:', error);
        res.status(500).json({ error: 'Failed to send OTP' });
    }
});

// Verify OTP
router.post('/verify-otp', async (req, res) => {
    try {
        const { phoneNumber, otp } = req.body;

        if (!phoneNumber || !otp) {
            return res.status(400).json({ error: 'Phone number and OTP required' });
        }

        const stored = otpStore.get(phoneNumber);

        if (!stored) {
            return res.status(400).json({ error: 'No OTP found. Please request a new one.' });
        }

        if (Date.now() > stored.expiresAt) {
            otpStore.delete(phoneNumber);
            return res.status(400).json({ error: 'OTP expired. Please request a new one.' });
        }

        if (stored.otp !== otp) {
            return res.status(400).json({ error: 'Invalid OTP' });
        }

        // OTP verified - create or get user
        let user = userStore.get(phoneNumber);
        if (!user) {
            user = {
                id: uuidv4(),
                phoneNumber,
                displayName: null,
                createdAt: new Date().toISOString()
            };
            userStore.set(phoneNumber, user);
        }

        // Generate JWT token
        const token = jwt.sign(
            { userId: user.id, phoneNumber },
            process.env.JWT_SECRET || 'dev-secret-change-in-production',
            { expiresIn: '30d' }
        );

        // Store session
        sessionStore.set(token, { userId: user.id, createdAt: Date.now() });

        // Clean up OTP
        otpStore.delete(phoneNumber);

        res.json({
            success: true,
            user: {
                id: user.id,
                phoneNumber: user.phoneNumber,
                displayName: user.displayName
            },
            token
        });
    } catch (error) {
        console.error('OTP verification error:', error);
        res.status(500).json({ error: 'Verification failed' });
    }
});

// Register device (for multi-device support)
router.post('/register-device', async (req, res) => {
    try {
        const { deviceId, publicKey, pushToken } = req.body;
        const token = req.headers.authorization?.replace('Bearer ', '');

        if (!token) {
            return res.status(401).json({ error: 'Authentication required' });
        }

        // Verify JWT
        let decoded;
        try {
            decoded = jwt.verify(token, process.env.JWT_SECRET || 'dev-secret-change-in-production');
        } catch {
            return res.status(401).json({ error: 'Invalid token' });
        }

        // Store device info (in production, store in database)
        console.log(`📱 Device registered for user ${decoded.userId}: ${deviceId}`);

        res.json({
            success: true,
            message: 'Device registered successfully'
        });
    } catch (error) {
        console.error('Device registration error:', error);
        res.status(500).json({ error: 'Registration failed' });
    }
});

// Update profile
router.put('/profile', async (req, res) => {
    try {
        const { displayName } = req.body;
        const token = req.headers.authorization?.replace('Bearer ', '');

        if (!token) {
            return res.status(401).json({ error: 'Authentication required' });
        }

        let decoded;
        try {
            decoded = jwt.verify(token, process.env.JWT_SECRET || 'dev-secret-change-in-production');
        } catch {
            return res.status(401).json({ error: 'Invalid token' });
        }

        // Update user (in production, update database)
        const user = Array.from(userStore.values()).find(u => u.id === decoded.userId);
        if (user) {
            user.displayName = displayName;
            userStore.set(user.phoneNumber, user);
        }

        res.json({
            success: true,
            user: {
                id: decoded.userId,
                displayName
            }
        });
    } catch (error) {
        console.error('Profile update error:', error);
        res.status(500).json({ error: 'Update failed' });
    }
});

module.exports = router;
