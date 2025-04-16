package com.example.etransportapp.presentation.ui.loginAndRegister.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etransportapp.R
import com.example.etransportapp.ui.theme.LightBlue

@Composable
fun RegisterPart2Screen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToNext: () -> Unit
) {
    val context = LocalContext.current
    val roleOptions = listOf("Tır Sahibiyim/Şoförüm", "Tır Arıyorum")
    var selectedRole by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White,
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(50.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.height(50.dp),
                    contentScale = ContentScale.FillHeight
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.text_etasimacilik),
                    contentDescription = "App text",
                    modifier = Modifier.height(50.dp),
                    contentScale = ContentScale.FillHeight
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Senin İçin Uygun Olanı Seç",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Aşağıdaki seçeneklerden bir tanesini seç",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(60.dp))

            roleOptions.forEach { role ->
                RoleSelectionButton(
                    text = role,
                    isSelected = selectedRole == role,
                    onClick = { selectedRole = role }
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.registerUser(context, /*onNavigateToNext*/)
                    },
                    modifier = Modifier
                        .width(90.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("İlerle", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun RoleSelectionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(if (isSelected) LightBlue else Color.Black)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White else Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}


@Preview
@Composable
fun PreviewRegisterPart2Screen() {
    RegisterPart2Screen(onNavigateToNext = {})
}
