package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SyriaTelBrandLogo
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.SyriatelDarkRed
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelRed
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun SplashScreen(
    isArabic: Boolean,
    onSplashCompleted: () -> Unit
) {
    var startAnim by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.82f,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
        label = "splash_scale"
    )
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 850),
        label = "splash_alpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    LaunchedEffect(Unit) {
        startAnim = true
        delay(2200)
        onSplashCompleted()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFFDF5F6),
                        Color(0xFFF6F7F9)
                    )
                )
            )
            .clickable { onSplashCompleted() }
            .testTag("splash_screen")
    ) {
        // Subtle abstract communication waves, network nodes, and geometric rings
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Subtle upper right red communication rings
            drawCircle(
                color = SyriatelRed.copy(alpha = 0.05f),
                radius = w * 0.48f,
                center = Offset(w * 0.9f, h * 0.12f),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = SyriatelRed.copy(alpha = 0.08f),
                radius = w * 0.32f,
                center = Offset(w * 0.9f, h * 0.12f),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Subtle lower left green & charcoal geometric rings
            drawCircle(
                color = SyriatelGreen.copy(alpha = 0.06f),
                radius = w * 0.42f,
                center = Offset(w * 0.1f, h * 0.88f),
                style = Stroke(width = 2.dp.toPx())
            )

            // Abstract communication wave paths
            val wavePath1 = Path().apply {
                moveTo(0f, h * 0.72f)
                val step = w / 24f
                var x = 0f
                while (x <= w + step) {
                    val y = h * 0.72f + sin((x / w) * 5f + wavePhase) * 24.dp.toPx()
                    lineTo(x, y)
                    x += step
                }
            }
            drawPath(
                path = wavePath1,
                color = SyriatelRed.copy(alpha = 0.12f),
                style = Stroke(width = 2.dp.toPx())
            )

            val wavePath2 = Path().apply {
                moveTo(0f, h * 0.76f)
                val step = w / 24f
                var x = 0f
                while (x <= w + step) {
                    val y = h * 0.76f + sin((x / w) * 4f - wavePhase) * 18.dp.toPx()
                    lineTo(x, y)
                    x += step
                }
            }
            drawPath(
                path = wavePath2,
                color = SyriatelGreen.copy(alpha = 0.10f),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Subtle network nodes / digital particles
            val nodes = listOf(
                Offset(w * 0.18f, h * 0.22f),
                Offset(w * 0.34f, h * 0.16f),
                Offset(w * 0.82f, h * 0.28f),
                Offset(w * 0.76f, h * 0.68f),
                Offset(w * 0.24f, h * 0.78f)
            )
            nodes.forEachIndexed { idx, pt ->
                drawCircle(
                    color = if (idx % 2 == 0) SyriatelRed.copy(alpha = 0.18f) else SyriatelGreen.copy(alpha = 0.18f),
                    radius = 4.dp.toPx(),
                    center = pt
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .scale(scaleAnim)
                .alpha(alphaAnim)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 12.dp,
                border = BorderStroke(1.dp, Color(0xFFE9EAED)),
                modifier = Modifier.padding(bottom = 28.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_syriatel_logo),
                    contentDescription = "SyriaTel Official Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(horizontal = 36.dp, vertical = 24.dp)
                        .width(210.dp)
                        .height(78.dp)
                )
            }

            Text(
                text = "سيريتل",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = SyriatelRed
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "كل ما يهمك في مكان واحد",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = CharcoalGray,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                color = SyriatelRed,
                trackColor = SyriatelRed.copy(alpha = 0.15f),
                modifier = Modifier
                    .width(140.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun LoginScreen(
    isArabic: Boolean,
    isDarkMode: Boolean,
    initialPhone: String,
    onLoginSuccess: (String, Boolean) -> Unit,
    onNavigateToRegister: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    var phone by remember { mutableStateOf(initialPhone) }
    var password by remember { mutableStateOf("123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = SyriatelRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = if (isArabic) "نسخة تجريبية" else "Demo Mode",
                        style = MaterialTheme.typography.labelMedium,
                        color = SyriatelRed,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                TextButton(
                    onClick = onToggleLanguage,
                    modifier = Modifier.testTag("auth_language_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = null,
                        tint = SyriatelRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "English" else "العربية",
                        color = SyriatelRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SyriaTelBrandLogo(
                height = 56.dp,
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isArabic) "تسجيل الدخول" else "Sign In",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isArabic) "أهلاً بك مجدداً في تطبيق سيريتل الذكي" else "Welcome back to the smart SyriaTel app",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isArabic) "رقم الهاتف" else "Phone Number",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                                phone = it
                                errorText = null
                            }
                        },
                        placeholder = { Text("09xxxxxxxx") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.PhoneAndroid,
                                contentDescription = null,
                                tint = SyriatelRed
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_phone_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isArabic) "كلمة المرور" else "Password",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorText = null
                        },
                        placeholder = { Text(if (isArabic) "أدخل كلمة المرور" else "Enter password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = SyriatelRed
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (isArabic) "إظهار/إخفاء كلمة المرور" else "Toggle password visibility"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { rememberMe = !rememberMe }
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = SyriatelRed)
                        )
                        Text(
                            text = if (isArabic) "تذكرني" else "Remember me",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    AnimatedVisibility(visible = errorText != null) {
                        Surface(
                            color = SyriatelRed.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ErrorOutline,
                                    contentDescription = null,
                                    tint = SyriatelRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorText.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SyriatelRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val trimmedPhone = phone.trim()
                            when {
                                trimmedPhone.isEmpty() || password.isBlank() -> {
                                    errorText = if (isArabic) {
                                        "يرجى تعبئة جميع الحقول المطلوبة"
                                    } else {
                                        "Please fill in all required fields"
                                    }
                                }
                                !trimmedPhone.matches(Regex("^09\\d{8}$")) -> {
                                    errorText = if (isArabic) {
                                        "يرجى إدخال رقم موبايل سوري صحيح يبدأ بـ 09 ويتكون من 10 أرقام"
                                    } else {
                                        "Please enter a valid 10-digit Syrian mobile number starting with 09"
                                    }
                                }
                                password.length < 6 -> {
                                    errorText = if (isArabic) {
                                        "يجب أن تتكون كلمة المرور من 6 محارف على الأقل"
                                    } else {
                                        "Password must be at least 6 characters long"
                                    }
                                }
                                else -> {
                                    onLoginSuccess(trimmedPhone, rememberMe)
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_submit_button")
                    ) {
                        Text(
                            text = if (isArabic) "تسجيل الدخول" else "Sign In",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.testTag("navigate_to_register_button")
            ) {
                Text(
                    text = if (isArabic) "إنشاء حساب جديد" else "Create a New Account",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = SyriatelRed,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isArabic) {
                    "تنويه: هذه الواجهة تجريبية (Prototype) ولا تتصل بمخدمات سيريتل الفعلية."
                } else {
                    "Note: This is a simulated authentication flow and does not connect to live SyriaTel servers."
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RegisterScreen(
    isArabic: Boolean,
    isDarkMode: Boolean,
    onRegisterSuccess: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var generatedCode by remember { mutableStateOf<String?>(null) }
    var countdownSeconds by remember { mutableIntStateOf(0) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var codeFeedback by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(countdownSeconds) {
        if (countdownSeconds > 0) {
            delay(1000L)
            countdownSeconds -= 1
        }
    }

    val scrollState = rememberScrollState()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            SyriaTelBrandLogo(
                height = 48.dp,
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isArabic) "إنشاء حساب جديد" else "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isArabic) "أدخل بياناتك لإنشاء حساب سيريتل تجريبي" else "Fill in your details to register a demo SyriaTel account",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "الاسم الأول" else "First Name",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = {
                                    firstName = it
                                    errorText = null
                                },
                                placeholder = { Text("الاسم") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_first_name_input")
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "الكنية" else "Last Name",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = {
                                    lastName = it
                                    errorText = null
                                },
                                placeholder = { Text("الكنية") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("register_last_name_input")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isArabic) "رقم الهاتف" else "Phone Number",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                                phone = it
                                errorText = null
                            }
                        },
                        placeholder = { Text("09xxxxxxxx") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.PhoneAndroid,
                                contentDescription = null,
                                tint = SyriatelRed
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("register_phone_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isArabic) "كلمة المرور" else "Password",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorText = null
                        },
                        placeholder = { Text(if (isArabic) "6 محارف على الأقل" else "At least 6 characters") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = SyriatelRed
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (isArabic) "إظهار/إخفاء كلمة المرور" else "Toggle password visibility"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("register_password_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isArabic) "رمز التحقق التجريبي" else "Verification Code",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = verificationCode,
                            onValueChange = {
                                if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                                    verificationCode = it
                                    errorText = null
                                }
                            },
                            placeholder = { Text("1234") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("register_code_input")
                        )

                        Button(
                            onClick = {
                                val demoCode = "4829"
                                generatedCode = demoCode
                                verificationCode = demoCode
                                countdownSeconds = 60
                                codeFeedback = if (isArabic) {
                                    "رمز التحقق التجريبي: $demoCode (تمت تعبئته تلقائياً)"
                                } else {
                                    "Demo verification code: $demoCode (auto-filled)"
                                }
                            },
                            enabled = countdownSeconds == 0,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SyriatelRed.copy(alpha = 0.12f),
                                contentColor = SyriatelRed,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier
                                .height(54.dp)
                                .testTag("send_verification_code_button")
                        ) {
                            Text(
                                text = if (countdownSeconds > 0) {
                                    if (isArabic) "إعادة (${countdownSeconds}ث)" else "Resend (${countdownSeconds}s)"
                                } else {
                                    if (isArabic) "إرسال الرمز" else "Send Code"
                                },
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    AnimatedVisibility(visible = codeFeedback != null) {
                        Surface(
                            color = SyriatelGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = SyriatelGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = codeFeedback.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SyriatelGreen
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = errorText != null) {
                        Surface(
                            color = SyriatelRed.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ErrorOutline,
                                    contentDescription = null,
                                    tint = SyriatelRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorText.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SyriatelRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val fName = firstName.trim()
                            val lName = lastName.trim()
                            val trimmedPhone = phone.trim()
                            when {
                                fName.isEmpty() || lName.isEmpty() || trimmedPhone.isEmpty() || password.isBlank() || verificationCode.isBlank() -> {
                                    errorText = if (isArabic) {
                                        "جميع الحقول مطلوبة لإتمام التسجيل"
                                    } else {
                                        "All fields are required to complete registration"
                                    }
                                }
                                !trimmedPhone.matches(Regex("^09\\d{8}$")) -> {
                                    errorText = if (isArabic) {
                                        "يرجى إدخال رقم موبايل سوري صحيح يبدأ بـ 09 ويتكون من 10 أرقام"
                                    } else {
                                        "Please enter a valid 10-digit Syrian mobile number starting with 09"
                                    }
                                }
                                password.length < 6 -> {
                                    errorText = if (isArabic) {
                                        "يجب أن تتكون كلمة المرور من 6 محارف على الأقل"
                                    } else {
                                        "Password must be at least 6 characters long"
                                    }
                                }
                                verificationCode.length < 4 -> {
                                    errorText = if (isArabic) {
                                        "يرجى إدخال رمز التحقق المرسل"
                                    } else {
                                        "Please enter a valid verification code"
                                    }
                                }
                                else -> {
                                    onRegisterSuccess(fName, lName, trimmedPhone)
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("register_submit_button")
                    ) {
                        Text(
                            text = if (isArabic) "التسجيل" else "Register",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.testTag("navigate_to_login_button")
            ) {
                Text(
                    text = if (isArabic) "لديك حساب بالفعل؟ تسجيل الدخول" else "Already have an account? Sign In",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = SyriatelRed,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
