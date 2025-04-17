package com.example.etransportapp.data.model

import androidx.annotation.DrawableRes
import com.example.etransportapp.R

data class OnboardingPageData(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPageData(
        title = "Yük ve Araç Eşleşmesi",
        description = "Yük sahipleri ve araç sahiplerini bir araya getirir.",
        imageRes = R.drawable.ic_onboarding1
    ),
    OnboardingPageData(
        title = "Hemen Deneyin",
        description = "Uygulamamızı hemen deneyin ve onlarca yük ilanına erişin.",
        imageRes = R.drawable.ic_onboarding2
    ),
    OnboardingPageData(
        title = "Hoşgeldiniz",
        description = "Taşımacılık işlerinizi kolayca halledin.",
        imageRes = R.drawable.ic_onboarding3
    )
)

