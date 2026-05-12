package ru.gureva.yadro.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.gureva.yadro.R

val regular = FontFamily(
    Font(R.font.wix_made_for_text_regular, FontWeight.Normal, FontStyle.Normal)
)

val medium = FontFamily(
    Font(R.font.wix_made_for_text_medium, FontWeight.Normal, FontStyle.Normal)
)

val semiBold = FontFamily(
    Font(R.font.wix_made_for_text_semi_bold, FontWeight.Normal, FontStyle.Normal)
)

val baseline = Typography()
val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = semiBold),
    displayMedium = baseline.displayMedium.copy(fontFamily = semiBold),
    displaySmall = baseline.displaySmall.copy(fontFamily = semiBold),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = semiBold),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = semiBold, fontSize = 24.sp),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = semiBold),
    titleLarge = baseline.titleLarge.copy(fontFamily = medium, fontSize = 22.sp),
    titleMedium = baseline.titleMedium.copy(fontFamily = medium, fontSize = 18.sp),
    titleSmall = baseline.titleSmall.copy(fontFamily = medium),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = regular),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = regular, fontSize = 18.sp),
    bodySmall = baseline.bodySmall.copy(fontFamily = regular, fontSize = 14.sp),
    labelLarge = baseline.labelLarge.copy(fontFamily = regular),
    labelMedium = baseline.labelMedium.copy(fontFamily = regular),
    labelSmall = baseline.labelSmall.copy(fontFamily = regular),
)
