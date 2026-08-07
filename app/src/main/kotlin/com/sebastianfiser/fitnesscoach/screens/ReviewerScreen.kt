package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.AppViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import java.io.File
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.sp

@Composable
fun ReviewerScreen(navController: NavController, viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.loadPendingSubmissions()
        }

    }

    Scaffold(

    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {

            if (viewModel.pendingSubmissions.isEmpty()) {
                item {
                    showReturnMessage(navController = navController)
                }
            }

            items(viewModel.pendingSubmissions) { submission ->
                val data = submission.data
                val exerciseName = data["exerciseName"] as? String ?: "Unknown"
                val weight = (data["weight"] as? Number)?.toFloat() ?: 0f
                val userId = data["userId"] as? String ?: "Unknown"
                val videoFileId = data["videoFileId"] as? String ?: ""


                SubmissionCard(
                    submissionId = submission.id,
                    exerciseName = exerciseName,
                    weight = weight,
                    userId = userId,
                    videoFileId = videoFileId,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SubmissionCard(submissionId: String, exerciseName: String, weight: Float, userId: String, videoFileId: String, viewModel: AppViewModel) {
    val scopeOne = rememberCoroutineScope()
    val context = LocalContext.current
    var videoBytes by remember { mutableStateOf<ByteArray?>(null)}

    LaunchedEffect(videoFileId) {
        videoBytes = viewModel.getVideoBytes(videoFileId)
    }

    val videoUri = remember(videoBytes) {
        videoBytes?.let { bytes ->
            val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
            tempFile.writeBytes(bytes)
            Uri.fromFile(tempFile)
        }
    }

    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri ?: Uri.EMPTY))
            prepare()
            playWhenReady = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = userId,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = exerciseName,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                    val convWeight = viewModel.convertUnit(weight, viewModel.unit)
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                            text = "$convWeight ${viewModel.unit}",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onSurface)
                ) {
                    if (videoUri != null) {
                        AndroidView(
                            factory = { ctx ->
                                PlayerView(ctx).apply {
                                    player = exoPlayer
                                    useController = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { scopeOne.launch {
                        viewModel.updateSubmissionStatus(submissionId, "approved")
                    } },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Approve", color = MaterialTheme.colorScheme.onPrimary)
                }

                OutlinedButton(
                    onClick = { scopeOne.launch {
                        viewModel.updateSubmissionStatus(submissionId, "rejected")
                    } },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(text = "Reject", color = MaterialTheme.colorScheme.onSurface)
                }

            }

        }
    }
    DisposableEffect(videoUri) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
fun showReturnMessage(navController: NavController) {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card (
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .background(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Wait", color = MaterialTheme.colorScheme.onSurface, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Theres no submissions to review", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Go Back")
                }
            }
        }
    }
}
