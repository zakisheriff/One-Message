package com.theoneatom.onemessage.ui.theme

import androidx.compose.ui.graphics.Color

// One Atom Design System Colors
object OneMessageColors {
    // Primary Colors
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)
    
    // Text Colors
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFE0E0E0)
    val TextMuted = Color(0xFFA0A0A0)
    
    // Accent Gradients (start/end colors)
    val AccentLight = Color(0xFFFFFFFF)
    val AccentDark = Color(0xFFCCCCCC)
    
    // Glass/Surface Colors
    val GlassBg = Color(0x0DFFFFFF) // rgba(255,255,255,0.05)
    val GlassBgHover = Color(0x14FFFFFF) // rgba(255,255,255,0.08)
    val GlassBorder = Color(0x1AFFFFFF) // rgba(255,255,255,0.1)
    
    // Status Colors
    val Online = Color(0xFF4ADE80) // Green for online status
    val Offline = Color(0xFF6B7280) // Gray for offline
    val Sending = Color(0xFF60A5FA) // Blue for sending
    val Error = Color(0xFFEF4444) // Red for errors
    
    // Message Bubble Colors
    val SentBubble = Color(0xFFFFFFFF)
    val SentBubbleText = Color(0xFF000000)
    val ReceivedBubble = Color(0x1AFFFFFF)
    val ReceivedBubbleText = Color(0xFFFFFFFF)
    
    // Surface Colors
    val Background = Color(0xFF000000)
    val Surface = Color(0xFF0A0A0A)
    val SurfaceVariant = Color(0xFF1A1A1A)
    val SurfaceElevated = Color(0xFF121212)
}