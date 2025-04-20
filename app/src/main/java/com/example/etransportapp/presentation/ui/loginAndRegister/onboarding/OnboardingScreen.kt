package com.example.etransportapp.presentation.ui.loginAndRegister.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.etransportapp.R
import com.example.etransportapp.data.model.onboardingPages
import com.example.etransportapp.presentation.components.DotsIndicator
import com.example.etransportapp.presentation.components.OnboardingPage
import com.example.etransportapp.presentation.navigation.NavRoutes
import com.example.etransportapp.ui.theme.LightBlue
import com.example.etransportapp.util.PreferenceHelper
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.navigate(NavRoutes.INTRO) }) {
                    Text(fontSize = 24.sp,text = "Geç ", color = LightBlue, fontWeight = FontWeight.Medium)
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_right_alt_24), contentDescription ="Skip button icon", tint = LightBlue )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPage(page = onboardingPages[page])
            }

            DotsIndicator(
                totalDots = onboardingPages.size,
                selectedIndex = pagerState.currentPage,
                selectedColor = LightBlue,
                unselectedColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (pagerState.currentPage + 1 < onboardingPages.size) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        PreferenceHelper.setFirstTime(navController.context, false)
                        navController.navigate(NavRoutes.INTRO) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
            ) {
                Text(
                    text = if (pagerState.currentPage == onboardingPages.lastIndex) "Başla" else "İleri",
                    fontSize = 18.sp
                )
            }
        }
    }
}

