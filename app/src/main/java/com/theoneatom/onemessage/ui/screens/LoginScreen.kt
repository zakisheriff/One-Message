package com.theoneatom.onemessage.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.theoneatom.onemessage.auth.FirebasePhoneAuth
import com.theoneatom.onemessage.ui.theme.OneMessageColors

// Country data class with phone number length
data class Country(
        val name: String,
        val code: String,
        val dialCode: String,
        val phoneLength: Int // Expected phone number length (without country code)
)

// Complete list of countries with dial codes and phone lengths
val allCountries =
        listOf(
                Country("Afghanistan", "AF", "+93", 9),
                Country("Albania", "AL", "+355", 9),
                Country("Algeria", "DZ", "+213", 9),
                Country("Andorra", "AD", "+376", 6),
                Country("Angola", "AO", "+244", 9),
                Country("Argentina", "AR", "+54", 10),
                Country("Armenia", "AM", "+374", 8),
                Country("Australia", "AU", "+61", 9),
                Country("Austria", "AT", "+43", 10),
                Country("Azerbaijan", "AZ", "+994", 9),
                Country("Bahrain", "BH", "+973", 8),
                Country("Bangladesh", "BD", "+880", 10),
                Country("Belarus", "BY", "+375", 9),
                Country("Belgium", "BE", "+32", 9),
                Country("Belize", "BZ", "+501", 7),
                Country("Benin", "BJ", "+229", 8),
                Country("Bhutan", "BT", "+975", 8),
                Country("Bolivia", "BO", "+591", 8),
                Country("Bosnia and Herzegovina", "BA", "+387", 8),
                Country("Botswana", "BW", "+267", 8),
                Country("Brazil", "BR", "+55", 11),
                Country("Brunei", "BN", "+673", 7),
                Country("Bulgaria", "BG", "+359", 9),
                Country("Burkina Faso", "BF", "+226", 8),
                Country("Burundi", "BI", "+257", 8),
                Country("Cambodia", "KH", "+855", 9),
                Country("Cameroon", "CM", "+237", 9),
                Country("Canada", "CA", "+1", 10),
                Country("Cape Verde", "CV", "+238", 7),
                Country("Central African Republic", "CF", "+236", 8),
                Country("Chad", "TD", "+235", 8),
                Country("Chile", "CL", "+56", 9),
                Country("China", "CN", "+86", 11),
                Country("Colombia", "CO", "+57", 10),
                Country("Comoros", "KM", "+269", 7),
                Country("Congo", "CG", "+242", 9),
                Country("Costa Rica", "CR", "+506", 8),
                Country("Croatia", "HR", "+385", 9),
                Country("Cuba", "CU", "+53", 8),
                Country("Cyprus", "CY", "+357", 8),
                Country("Czech Republic", "CZ", "+420", 9),
                Country("Denmark", "DK", "+45", 8),
                Country("Djibouti", "DJ", "+253", 8),
                Country("Dominican Republic", "DO", "+1", 10),
                Country("Ecuador", "EC", "+593", 9),
                Country("Egypt", "EG", "+20", 10),
                Country("El Salvador", "SV", "+503", 8),
                Country("Equatorial Guinea", "GQ", "+240", 9),
                Country("Eritrea", "ER", "+291", 7),
                Country("Estonia", "EE", "+372", 8),
                Country("Eswatini", "SZ", "+268", 8),
                Country("Ethiopia", "ET", "+251", 9),
                Country("Fiji", "FJ", "+679", 7),
                Country("Finland", "FI", "+358", 10),
                Country("France", "FR", "+33", 9),
                Country("Gabon", "GA", "+241", 7),
                Country("Gambia", "GM", "+220", 7),
                Country("Georgia", "GE", "+995", 9),
                Country("Germany", "DE", "+49", 11),
                Country("Ghana", "GH", "+233", 9),
                Country("Greece", "GR", "+30", 10),
                Country("Guatemala", "GT", "+502", 8),
                Country("Guinea", "GN", "+224", 9),
                Country("Guyana", "GY", "+592", 7),
                Country("Haiti", "HT", "+509", 8),
                Country("Honduras", "HN", "+504", 8),
                Country("Hong Kong", "HK", "+852", 8),
                Country("Hungary", "HU", "+36", 9),
                Country("Iceland", "IS", "+354", 7),
                Country("India", "IN", "+91", 10),
                Country("Indonesia", "ID", "+62", 11),
                Country("Iran", "IR", "+98", 10),
                Country("Iraq", "IQ", "+964", 10),
                Country("Ireland", "IE", "+353", 9),
                Country("Israel", "IL", "+972", 9),
                Country("Italy", "IT", "+39", 10),
                Country("Jamaica", "JM", "+1", 10),
                Country("Japan", "JP", "+81", 10),
                Country("Jordan", "JO", "+962", 9),
                Country("Kazakhstan", "KZ", "+7", 10),
                Country("Kenya", "KE", "+254", 9),
                Country("Kuwait", "KW", "+965", 8),
                Country("Kyrgyzstan", "KG", "+996", 9),
                Country("Laos", "LA", "+856", 10),
                Country("Latvia", "LV", "+371", 8),
                Country("Lebanon", "LB", "+961", 8),
                Country("Lesotho", "LS", "+266", 8),
                Country("Liberia", "LR", "+231", 9),
                Country("Libya", "LY", "+218", 10),
                Country("Liechtenstein", "LI", "+423", 7),
                Country("Lithuania", "LT", "+370", 8),
                Country("Luxembourg", "LU", "+352", 9),
                Country("Macao", "MO", "+853", 8),
                Country("Madagascar", "MG", "+261", 9),
                Country("Malawi", "MW", "+265", 9),
                Country("Malaysia", "MY", "+60", 10),
                Country("Maldives", "MV", "+960", 7),
                Country("Mali", "ML", "+223", 8),
                Country("Malta", "MT", "+356", 8),
                Country("Mauritania", "MR", "+222", 8),
                Country("Mauritius", "MU", "+230", 8),
                Country("Mexico", "MX", "+52", 10),
                Country("Moldova", "MD", "+373", 8),
                Country("Monaco", "MC", "+377", 8),
                Country("Mongolia", "MN", "+976", 8),
                Country("Montenegro", "ME", "+382", 8),
                Country("Morocco", "MA", "+212", 9),
                Country("Mozambique", "MZ", "+258", 9),
                Country("Myanmar", "MM", "+95", 10),
                Country("Namibia", "NA", "+264", 9),
                Country("Nepal", "NP", "+977", 10),
                Country("Netherlands", "NL", "+31", 9),
                Country("New Zealand", "NZ", "+64", 9),
                Country("Nicaragua", "NI", "+505", 8),
                Country("Niger", "NE", "+227", 8),
                Country("Nigeria", "NG", "+234", 10),
                Country("North Korea", "KP", "+850", 10),
                Country("North Macedonia", "MK", "+389", 8),
                Country("Norway", "NO", "+47", 8),
                Country("Oman", "OM", "+968", 8),
                Country("Pakistan", "PK", "+92", 10),
                Country("Palestine", "PS", "+970", 9),
                Country("Panama", "PA", "+507", 8),
                Country("Papua New Guinea", "PG", "+675", 8),
                Country("Paraguay", "PY", "+595", 9),
                Country("Peru", "PE", "+51", 9),
                Country("Philippines", "PH", "+63", 10),
                Country("Poland", "PL", "+48", 9),
                Country("Portugal", "PT", "+351", 9),
                Country("Qatar", "QA", "+974", 8),
                Country("Romania", "RO", "+40", 10),
                Country("Russia", "RU", "+7", 10),
                Country("Rwanda", "RW", "+250", 9),
                Country("Saudi Arabia", "SA", "+966", 9),
                Country("Senegal", "SN", "+221", 9),
                Country("Serbia", "RS", "+381", 9),
                Country("Singapore", "SG", "+65", 8),
                Country("Slovakia", "SK", "+421", 9),
                Country("Slovenia", "SI", "+386", 8),
                Country("Somalia", "SO", "+252", 8),
                Country("South Africa", "ZA", "+27", 9),
                Country("South Korea", "KR", "+82", 10),
                Country("South Sudan", "SS", "+211", 9),
                Country("Spain", "ES", "+34", 9),
                Country("Sri Lanka", "LK", "+94", 9),
                Country("Sudan", "SD", "+249", 9),
                Country("Suriname", "SR", "+597", 7),
                Country("Sweden", "SE", "+46", 10),
                Country("Switzerland", "CH", "+41", 9),
                Country("Syria", "SY", "+963", 9),
                Country("Taiwan", "TW", "+886", 9),
                Country("Tajikistan", "TJ", "+992", 9),
                Country("Tanzania", "TZ", "+255", 9),
                Country("Thailand", "TH", "+66", 9),
                Country("Togo", "TG", "+228", 8),
                Country("Trinidad and Tobago", "TT", "+1", 10),
                Country("Tunisia", "TN", "+216", 8),
                Country("Turkey", "TR", "+90", 10),
                Country("Turkmenistan", "TM", "+993", 8),
                Country("Uganda", "UG", "+256", 9),
                Country("Ukraine", "UA", "+380", 9),
                Country("United Arab Emirates", "AE", "+971", 9),
                Country("United Kingdom", "GB", "+44", 10),
                Country("United States", "US", "+1", 10),
                Country("Uruguay", "UY", "+598", 8),
                Country("Uzbekistan", "UZ", "+998", 9),
                Country("Venezuela", "VE", "+58", 10),
                Country("Vietnam", "VN", "+84", 10),
                Country("Yemen", "YE", "+967", 9),
                Country("Zambia", "ZM", "+260", 9),
                Country("Zimbabwe", "ZW", "+263", 9)
        )

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
        val context = LocalContext.current
        val activity = context as? Activity
        val firebaseAuth = remember { activity?.let { FirebasePhoneAuth(it) } }

        var phoneNumber by remember { mutableStateOf("") }
        var otpCode by remember { mutableStateOf("") }
        var showOtpInput by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var selectedCountry by remember {
                mutableStateOf(allCountries.find { it.code == "LK" } ?: allCountries.first())
        }
        var showCountryPicker by remember { mutableStateOf(false) }
        var fullPhoneNumber by remember { mutableStateOf("") }

        Box(
                modifier =
                        Modifier.fillMaxSize()
                                .background(OneMessageColors.Background)
                                .imePadding() // This handles keyboard padding
        ) {
                // Ambient orb effect (simplified)
                Box(
                        modifier =
                                Modifier.size(300.dp)
                                        .offset((-100).dp, (-100).dp)
                                        .background(
                                                brush =
                                                        Brush.radialGradient(
                                                                colors =
                                                                        listOf(
                                                                                Color.White.copy(
                                                                                        alpha =
                                                                                                0.03f
                                                                                ),
                                                                                Color.Transparent
                                                                        )
                                                        )
                                        )
                )

                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Spacer(modifier = Modifier.height(80.dp))

                        // Logo - Using Material Icon instead of emoji
                        Box(
                                modifier =
                                        Modifier.size(80.dp)
                                                .background(
                                                        brush =
                                                                Brush.linearGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        OneMessageColors
                                                                                                .White,
                                                                                        OneMessageColors
                                                                                                .AccentDark
                                                                                )
                                                                ),
                                                        shape = CircleShape
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "One Message",
                                        modifier = Modifier.size(40.dp),
                                        tint = OneMessageColors.Black
                                )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // App name
                        Text(
                                text = "One Message",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = OneMessageColors.TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tagline
                        Text(
                                text = "Secure. Private. Yours.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = OneMessageColors.TextMuted
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Phone number or OTP input
                        AnimatedVisibility(
                                visible = !showOtpInput,
                                enter = fadeIn(),
                                exit = fadeOut()
                        ) {
                                PhoneNumberInput(
                                        phoneNumber = phoneNumber,
                                        onPhoneNumberChange = {
                                                phoneNumber = it
                                                errorMessage = null
                                        },
                                        selectedCountry = selectedCountry,
                                        onCountryClick = { showCountryPicker = true },
                                        onRequestOtp = {
                                                if (phoneNumber.length ==
                                                                selectedCountry.phoneLength
                                                ) {
                                                        isLoading = true
                                                        // TODO: Call backend to send OTP
                                                        // For now, simulate
                                                        showOtpInput = true
                                                        isLoading = false
                                                } else {
                                                        errorMessage =
                                                                "Phone number must be ${selectedCountry.phoneLength} digits for ${selectedCountry.name}"
                                                }
                                        },
                                        isLoading = isLoading
                                )
                        }

                        AnimatedVisibility(
                                visible = showOtpInput,
                                enter = fadeIn(),
                                exit = fadeOut()
                        ) {
                                OtpInput(
                                        otpCode = otpCode,
                                        onOtpChange = {
                                                otpCode = it
                                                errorMessage = null
                                        },
                                        onVerifyOtp = {
                                                if (otpCode.length == 4) {
                                                        isLoading = true
                                                        // TODO: Call backend to verify OTP
                                                        // For now, simulate success
                                                        onLoginSuccess()
                                                } else {
                                                        errorMessage = "Please enter a 4-digit code"
                                                }
                                        },
                                        onResendOtp = {
                                                // TODO: Resend OTP
                                        },
                                        phoneNumber = "${selectedCountry.dialCode} $phoneNumber",
                                        isLoading = isLoading,
                                        onBack = {
                                                showOtpInput = false
                                                otpCode = ""
                                        }
                                )
                        }

                        // Error message
                        AnimatedVisibility(visible = errorMessage != null) {
                                Text(
                                        text = errorMessage ?: "",
                                        color = OneMessageColors.Error,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 16.dp)
                                )
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        // Terms
                        Text(
                                text =
                                        "By continuing, you agree to our Terms of Service\nand Privacy Policy",
                                style = MaterialTheme.typography.bodySmall,
                                color = OneMessageColors.TextMuted,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                }
        }

        // Country Picker Dialog
        if (showCountryPicker) {
                CountryPickerDialog(
                        countries = allCountries,
                        selectedCountry = selectedCountry,
                        onCountrySelected = { country ->
                                selectedCountry = country
                                showCountryPicker = false
                        },
                        onDismiss = { showCountryPicker = false }
                )
        }
}

@Composable
private fun CountryPickerDialog(
        countries: List<Country>,
        selectedCountry: Country,
        onCountrySelected: (Country) -> Unit,
        onDismiss: () -> Unit
) {
        var searchQuery by remember { mutableStateOf("") }

        val filteredCountries =
                remember(searchQuery) {
                        if (searchQuery.isEmpty()) {
                                countries
                        } else {
                                countries.filter {
                                        it.name.contains(searchQuery, ignoreCase = true) ||
                                                it.dialCode.contains(searchQuery) ||
                                                it.code.contains(searchQuery, ignoreCase = true)
                                }
                        }
                }

        Dialog(onDismissRequest = onDismiss) {
                Surface(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                        shape = RoundedCornerShape(24.dp),
                        color = OneMessageColors.Surface
                ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                                // Header
                                Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Text(
                                                text = "Select Country",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = OneMessageColors.TextPrimary
                                        )
                                        IconButton(onClick = onDismiss) {
                                                Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Close",
                                                        tint = OneMessageColors.TextSecondary
                                                )
                                        }
                                }

                                // Search
                                TextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        placeholder = {
                                                Text(
                                                        "Search country or code",
                                                        color = OneMessageColors.TextMuted
                                                )
                                        },
                                        leadingIcon = {
                                                Icon(
                                                        imageVector = Icons.Default.Search,
                                                        contentDescription = null,
                                                        tint = OneMessageColors.TextMuted
                                                )
                                        },
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .padding(horizontal = 16.dp)
                                                        .clip(RoundedCornerShape(12.dp)),
                                        colors =
                                                TextFieldDefaults.colors(
                                                        focusedContainerColor =
                                                                OneMessageColors.GlassBg,
                                                        unfocusedContainerColor =
                                                                OneMessageColors.GlassBg,
                                                        focusedIndicatorColor = Color.Transparent,
                                                        unfocusedIndicatorColor = Color.Transparent,
                                                        cursorColor = OneMessageColors.White,
                                                        focusedTextColor =
                                                                OneMessageColors.TextPrimary,
                                                        unfocusedTextColor =
                                                                OneMessageColors.TextPrimary
                                                ),
                                        singleLine = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Country list
                                LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding =
                                                PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                        items(filteredCountries) { country ->
                                                CountryItem(
                                                        country = country,
                                                        isSelected =
                                                                country.code ==
                                                                        selectedCountry.code,
                                                        onClick = { onCountrySelected(country) }
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
private fun CountryItem(country: Country, isSelected: Boolean, onClick: () -> Unit) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = onClick)
                                .background(
                                        if (isSelected) OneMessageColors.GlassBgHover
                                        else Color.Transparent
                                )
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                // Country code as badge instead of flag emoji
                Box(
                        modifier =
                                Modifier.size(40.dp)
                                        .background(
                                                OneMessageColors.GlassBg,
                                                RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                                1.dp,
                                                OneMessageColors.GlassBorder,
                                                RoundedCornerShape(8.dp)
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                        Text(
                                text = country.code,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = OneMessageColors.TextPrimary
                        )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                        Text(
                                text = country.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = OneMessageColors.TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                        )
                }

                Text(
                        text = country.dialCode,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = OneMessageColors.TextSecondary
                )

                if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = OneMessageColors.Online,
                                modifier = Modifier.size(20.dp)
                        )
                }
        }
}

@Composable
private fun PhoneNumberInput(
        phoneNumber: String,
        onPhoneNumberChange: (String) -> Unit,
        selectedCountry: Country,
        onCountryClick: () -> Unit,
        onRequestOtp: () -> Unit,
        isLoading: Boolean
) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                        text = "Enter your phone number",
                        style = MaterialTheme.typography.titleMedium,
                        color = OneMessageColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Phone input with country code picker
                Row(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .background(
                                                OneMessageColors.GlassBg,
                                                RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                                1.dp,
                                                OneMessageColors.GlassBorder,
                                                RoundedCornerShape(16.dp)
                                        )
                                        .padding(horizontal = 4.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        // Country code selector
                        Row(
                                modifier =
                                        Modifier.clip(RoundedCornerShape(12.dp))
                                                .clickable(onClick = onCountryClick)
                                                .background(OneMessageColors.SurfaceVariant)
                                                .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                // Country code badge
                                Box(
                                        modifier =
                                                Modifier.size(28.dp)
                                                        .background(
                                                                OneMessageColors.GlassBg,
                                                                RoundedCornerShape(6.dp)
                                                        ),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                text = selectedCountry.code,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = OneMessageColors.TextPrimary
                                        )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                        text = selectedCountry.dialCode,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = OneMessageColors.TextPrimary
                                )

                                Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Select country",
                                        tint = OneMessageColors.TextMuted,
                                        modifier = Modifier.size(20.dp)
                                )
                        }

                        // Phone number input
                        BasicTextField(
                                value = phoneNumber,
                                onValueChange = {
                                        if (it.length <= selectedCountry.phoneLength &&
                                                        it.all { c -> c.isDigit() }
                                        )
                                                onPhoneNumberChange(it)
                                },
                                textStyle =
                                        MaterialTheme.typography.titleMedium.copy(
                                                color = OneMessageColors.TextPrimary
                                        ),
                                cursorBrush = SolidColor(OneMessageColors.White),
                                keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier =
                                        Modifier.weight(1f)
                                                .padding(horizontal = 12.dp, vertical = 12.dp),
                                decorationBox = { innerTextField ->
                                        Box {
                                                if (phoneNumber.isEmpty()) {
                                                        Text(
                                                                text =
                                                                        "${selectedCountry.phoneLength} digits",
                                                                color = OneMessageColors.TextMuted,
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .titleMedium
                                                        )
                                                }
                                                innerTextField()
                                        }
                                }
                        )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Continue button
                Button(
                        onClick = onRequestOtp,
                        enabled = phoneNumber.length == selectedCountry.phoneLength && !isLoading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = OneMessageColors.White,
                                        contentColor = OneMessageColors.Black,
                                        disabledContainerColor = OneMessageColors.GlassBg,
                                        disabledContentColor = OneMessageColors.TextMuted
                                )
                ) {
                        if (isLoading) {
                                CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = OneMessageColors.Black,
                                        strokeWidth = 2.dp
                                )
                        } else {
                                Text(
                                        text = "Continue",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                )
                        }
                }
        }
}

@Composable
private fun OtpInput(
        otpCode: String,
        onOtpChange: (String) -> Unit,
        onVerifyOtp: () -> Unit,
        onResendOtp: () -> Unit,
        phoneNumber: String,
        isLoading: Boolean,
        onBack: () -> Unit
) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                        text = "Enter verification code",
                        style = MaterialTheme.typography.titleMedium,
                        color = OneMessageColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = "Sent to $phoneNumber",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OneMessageColors.TextMuted
                )

                Spacer(modifier = Modifier.height(32.dp))

                // OTP boxes (4 digits)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        repeat(4) { index ->
                                val char = otpCode.getOrNull(index)?.toString() ?: ""
                                Box(
                                        modifier =
                                                Modifier.size(56.dp)
                                                        .background(
                                                                if (char.isNotEmpty())
                                                                        OneMessageColors
                                                                                .GlassBgHover
                                                                else OneMessageColors.GlassBg,
                                                                RoundedCornerShape(14.dp)
                                                        )
                                                        .border(
                                                                1.dp,
                                                                if (index == otpCode.length)
                                                                        OneMessageColors.White
                                                                else OneMessageColors.GlassBorder,
                                                                RoundedCornerShape(14.dp)
                                                        ),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                text = char,
                                                style = MaterialTheme.typography.headlineLarge,
                                                color = OneMessageColors.TextPrimary
                                        )
                                }
                        }
                }

                // Hidden text field for OTP input (4 digits)
                BasicTextField(
                        value = otpCode,
                        onValueChange = {
                                if (it.length <= 4 && it.all { c -> c.isDigit() }) onOtpChange(it)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.size(1.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Resend link
                TextButton(onClick = onResendOtp) {
                        Text(
                                text = "Resend code",
                                color = OneMessageColors.TextMuted,
                                style = MaterialTheme.typography.bodyMedium
                        )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Change number link
                TextButton(onClick = onBack) {
                        Text(
                                text = "Change number",
                                color = OneMessageColors.TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                        )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Verify button
                Button(
                        onClick = onVerifyOtp,
                        enabled = otpCode.length == 4 && !isLoading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = OneMessageColors.White,
                                        contentColor = OneMessageColors.Black,
                                        disabledContainerColor = OneMessageColors.GlassBg,
                                        disabledContentColor = OneMessageColors.TextMuted
                                )
                ) {
                        if (isLoading) {
                                CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = OneMessageColors.Black,
                                        strokeWidth = 2.dp
                                )
                        } else {
                                Text(
                                        text = "Verify",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                )
                        }
                }
        }
}
