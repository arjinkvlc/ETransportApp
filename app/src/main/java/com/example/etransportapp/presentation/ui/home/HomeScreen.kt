package com.example.etransportapp.presentation.ui.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.etransportapp.R
import com.example.etransportapp.presentation.ui.home.loads.LoadsScreen
import com.example.etransportapp.presentation.ui.home.profile.ProfileScreen
import com.example.etransportapp.presentation.ui.home.trucks.TrucksScreen
import com.example.etransportapp.presentation.ui.loginAndRegister.LoginAndRegisterActivity
import com.example.etransportapp.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(context: Context) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Yük İlanları", "Araç İlanları", "Profil")
    val icons = listOf(
        painterResource(id = R.drawable.icon_tirbul),
        painterResource(id = R.drawable.icon_yukbul),
        painterResource(id = R.drawable.baseline_person_24)
    )

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        items.get(selectedItem),
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic
                        ),
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = LightBlue,
                    titleContentColor = Color.White
                ),
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index

                        },
                        icon = {
                            Image(
                                painter = icons[index],
                                contentDescription = item,
                                colorFilter = ColorFilter.tint(
                                    LightBlue
                                ),
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> LoadsScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            1 -> TrucksScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            2 -> if (!isLoggedIn) {
                startActivity(context, Intent(context, LoginAndRegisterActivity::class.java), null)
            } else {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else ->
                LoadsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
        }
    }
}



