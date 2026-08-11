package com.sebastianfiser.fitnesscoach.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.models.ProfileImageState
import kotlinx.coroutines.launch

@Composable
fun EditScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageState by viewModel.imageState.collectAsState()

    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val currentUser = viewModel.getCurrentUser()
        userName = currentUser?.name ?: ""
        userEmail = currentUser?.email ?: ""
    }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { newUri ->
            coroutineScope.launch {
                val currentUser = viewModel.getCurrentUser()
                val userId = currentUser?.id ?: ""
                if (userId.isNotEmpty()) {
                    viewModel.uploadImage(context, newUri, userId)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Back",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.popBackStack() }
            )

            Text(
                text = "PFP",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (isSaving) "..." else "Save",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = !isSaving) {
                    isSaving = true
                    coroutineScope.launch {
                        viewModel.updateUserData(userName, userEmail) { success ->
                            isSaving = false
                            if (success) navController.popBackStack()
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when (val state = imageState) {
                    is ProfileImageState.Loading -> {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                    is ProfileImageState.Success -> {
                        Image(
                            bitmap = state.bitmap.asImageBitmap(),
                            contentDescription = "PFP",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is ProfileImageState.Error -> {
                        val initial = userName.firstOrNull()?.uppercase() ?: "U"
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initial,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 28.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            OutlinedButton(
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Change",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Personal Info",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Username",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
            Text(
                text = "EDIT",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Email",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
            Text(
                text = "Edit",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
