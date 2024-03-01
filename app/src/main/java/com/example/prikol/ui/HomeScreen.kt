package com.example.prikol.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.ACTION_SEND
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.data.Term
import com.example.prikol.data.databaseName
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.HomeViewModel
import kotlinx.coroutines.launch
import java.io.File

internal val defaultOffset = 10.dp
internal val TAG = "TEST"

@Composable
fun HomeScreen(
    navigateToAdd: () -> Unit = {  },
    navigateToView: (Int) -> Unit = {  },
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAdd
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add term"
                )
            }
        },
        topBar = {
            HomeTopAppBar(
                viewModel = viewModel
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = defaultOffset)
        ) {
            items(items = homeUiState.termsList, key = { it.id }) { term ->
                TermToDisplay(
                    term = term,
                    onClick = navigateToView
                )
            }
        }
    }
}

fun sendDatabase(context: Context) {
    val databaseFile: File = context.getDatabasePath(databaseName) // Why it also could return a path for .db file when it was passed to a getDatabasePath()?
    val uri = FileProvider.getUriForFile(context, "com.example.prikol.fileprovider", databaseFile)
    Log.d("ROFL", "generated uri is $uri")
    val intent = Intent(ACTION_SEND)
    intent.type = "vnd.android.cursor.dir/email" // What is it? Probably it's all about type // "application/x-sqlite3"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.putExtra(Intent.EXTRA_SUBJECT, "Kotiki")
//    intent.setData(uri) // This is not working
//    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION) // This is not needed
    context.startActivity(Intent.createChooser(intent, "Choose where to send your database file")) // Text is not displayed
}

@Composable
fun TermToDisplay(
    term: Term,
    onClick: (Int) -> Unit = {  }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(term.id) }
            .padding(defaultOffset)
    ) {
        Text(
            text = term.name,
            modifier = Modifier.padding(defaultOffset)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    viewModel: HomeViewModel
) {
    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val databaseFile: File = context.getDatabasePath(databaseName)
    var menuExpanded: Boolean by remember { mutableStateOf(false) }

//    val pickDatabaseEventLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        if (uri != null) {
//            val databaseFile: File = context.getDatabasePath(databaseName)
//            Log.d(TAG, "Picked uri is $uri")
//            val pickedFile: File = File(uri.path)
//            pickedFile.copyTo(context.filesDir)
//        }
//    }

    val pickDatabaseEventLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            val uri: Uri? = intent?.data
            Log.d(TAG, "file uri is $uri")
//            Log.d(TAG, "path to this uri is ${uri?.path}")
            val contentResolver = context.contentResolver
            val inputStream = uri?.let { contentResolver.openInputStream(it) }
            val byteArray = inputStream?.readBytes()
            inputStream?.close()
            if (byteArray != null) {
                databaseFile.writeBytes(byteArray)
            }
            coroutineScope.launch {
                viewModel.testAppend()
            }
//            val outputStream = contentResolver.openOutputStream(FileProvider.getUriForFile(context, "com.example.prikol.fileprovider", databaseFile))
//            outputStream?.write(byteArray) // No difference?
//            outputStream?.close()
        }
    }

    TopAppBar(
        title = { Text( text = "Terms overview") },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
        actions = {
            Box() {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "More"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Import database") },
                        onClick = {
                            val intent = Intent(ACTION_OPEN_DOCUMENT)
                            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION) // Check is needed
                            intent.setType("*/*")
                            pickDatabaseEventLauncher.launch(intent)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Export database") },
                        onClick = {
                            sendDatabase(context)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Clear all") },
                        onClick = {
                            coroutineScope.launch {
                                viewModel.deleteAll()
                            }
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "test append") },
                        onClick = {
                            coroutineScope.launch {
                                viewModel.testAppend()
                            }
                            menuExpanded = false
                        }
                    )
//                            DropdownMenuItem(
//                                text = { Text(text = "Settings") },
//                                onClick = {
//
//                                    menuExpanded = false
//                                }
//                            )
                }
            }
        }
    )
}

//fun sendWalDatabaseFiles(context: Context) {
//    val databaseFile = context.getDatabasePath(databaseName)
//    val walFile = File(databaseFile.path + "-wal")
//    val shmFile = File(databaseFile.path + "-shm")
//    val databaseFileUri = FileProvider.getUriForFile(context, "com.example.prikol.fileprovider", databaseFile)
//    val walFileUri = FileProvider.getUriForFile(context, "com.example.prikol.fileprovider", walFile)
//    val shmFileUri = FileProvider.getUriForFile(context, "com.example.prikol.fileprovider", shmFile) // To optimize
//    val databaseFilesUris: ArrayList<Uri> = arrayListOf(databaseFileUri, walFileUri, shmFileUri)
//    val intent = Intent(ACTION_SEND_MULTIPLE)
//    intent.type = "vnd.android.cursor.dir/email"
//    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, databaseFilesUris)
//    intent.putExtra(Intent.EXTRA_SUBJECT, "Message text")
//    context.startActivity(Intent.createChooser(intent, "Choose where to send database file"))
//}