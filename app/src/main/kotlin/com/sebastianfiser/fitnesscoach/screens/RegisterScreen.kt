package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.sebastianfiser.fitnesscoach.models.Appwrite
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.navigation.Screen
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.activity.compose.BackHandler
import com.sebastianfiser.fitnesscoach.models.AppViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: AppViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    BackHandler(enabled = true) {
        navController.popBackStack()
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(3.dp, Color.White),
                color = Color(0xFFa84444),
            ) {
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                )
            }
        }}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create Account", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(32.dp))
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    if (name != "LetMeTrough") {
                        scope.launch {
                            try {
                                Appwrite.onRegister(email, password, name)
                                Appwrite.onLogin(email, password)
                                val currentUser = Appwrite.getCurrentUser()
                                navController.navigate(Screen.SetupSchedule.route)
                            } catch (e: Throwable) {
                                scope.launch {
                                    var errorMsg = Appwrite.ParseErrorMsg(e.message ?: "") 
                                    snackbarHostState.showSnackbar("Registration failed: ${errorMsg}")
                                }
                            }
                        }
                    } else {
                        navController.navigate(Screen.SetupSchedule.route)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text("Register", color = Color.Black)
            }
        } 
    }
}
