package com.example.prikol.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.ACTION_SEND
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.R
import com.example.prikol.data.Term
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File

val TAG = "TEST"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navigateToAdd: () -> Unit = {  },
    navigateToView: (Int) -> Unit = {  },
    navigateToTest: () -> Unit = {  },
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var isInSelectionMode by remember { mutableStateOf(false) }
    val selectedTerms = remember { mutableStateListOf<Int>() }
    var optionsMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var deleteRequested: Boolean by rememberSaveable { mutableStateOf(false) }

    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val writeExternalPermissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val importDatabaseEventLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            if (intent?.data != null) {
                val uri: Uri = intent.data!!
                Log.d(TAG, "file uri is $uri")
                val path = uri.path!!
                Log.d(TAG, "path of this uri is $path")
                Log.d(TAG, "cut path is ${path.split(":")[1]}")
                try {
                    val db = SQLiteDatabase.openDatabase("/storage/emulated/0/" + path.split(":")[1], null, SQLiteDatabase.OPEN_READONLY)
                    val cursor = db.rawQuery("SELECT * FROM terms_table", null)
                    while (cursor.moveToNext()) {
                        with(cursor) {
                            val type = getString(getColumnIndexOrThrow("type"))
                            val name = getString(getColumnIndexOrThrow("name"))
                            val definition = getString(getColumnIndexOrThrow("definition"))
                            coroutineScope.launch {
                                viewModel.add(
                                    Term(
                                        type = type,
                                        name = name,
                                        definition = definition
                                    )
                                )
                            }
                        }
                    }
                    cursor.close()
                } catch (e: SQLiteException) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }

    BackHandler(
        enabled = isInSelectionMode,
        onBack = {
            selectedTerms.clear()
        }
    )

    LaunchedEffect(
        key1 = isInSelectionMode,
        key2 = selectedTerms.size,
    ) {
        if (isInSelectionMode && selectedTerms.isEmpty()) {
            isInSelectionMode = false
        }
    }

    Scaffold(
        floatingActionButton = {
            if (isInSelectionMode) {
                if (selectedTerms.size == 2) FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.swapTerms(selectedTerms[0], selectedTerms[1])
                            selectedTerms.clear()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.SwapTerms_FAB_descr)
                    )
                }
            } else FloatingActionButton(
                    onClick = navigateToAdd
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.GoToAdd_FAB_descr)
                    )
                }
        },
        topBar = {
            TopAppBar(
                title = { Text(if (isInSelectionMode) stringResource(R.string.Edit_Mode_title) else stringResource(R.string.HomeScreen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
                actions =  { if (isInSelectionMode)
                    Row {
                        IconButton(onClick = { deleteRequested = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.Delete_Button_descr)
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedTerms.clear()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = stringResource(R.string.SwitchMode_Button_descr)
                            )
                        }
                    } else Box {
                        IconButton(onClick = { optionsMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.Options_Button_descr)
                            )
                        }
                        DropdownMenu(
                            expanded = optionsMenuExpanded,
                            onDismissRequest = { optionsMenuExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text(text = "Import database") },
                                onClick = {
                                    if (writeExternalPermissionState.status.isGranted) {
                                        val intent = Intent(ACTION_OPEN_DOCUMENT)
                                        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION) // Check is needed
                                        intent.setType("*/*")
                                        importDatabaseEventLauncher.launch(intent)
                                        optionsMenuExpanded = false
                                    } else {
                                        writeExternalPermissionState.launchPermissionRequest()
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Export database") },
                                onClick = {
                                    sendDatabase(context)
                                    optionsMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Clear all") },
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.deleteAll()
                                    }
                                    optionsMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Go to test screen") },
                                onClick = {
                                    navigateToTest()
                                    optionsMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "test append") },
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.testAppend()
                                    }
                                    optionsMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val homeUiState by viewModel.homeUiState.collectAsState()

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = dimensionResource(R.dimen.default_offset) / 2)
        ) {
            items(items = homeUiState.termsList, key = { it.id }) { term ->
                val isSelected = selectedTerms.contains(term.id)

                TermToDisplay(
                    term = term,
                    onClick = {
                        if (isInSelectionMode) {   // Why can't put when to an argument?
                            if (!isSelected) selectedTerms.add(term.id) else selectedTerms.remove(term.id)
                        } else {
                            navigateToView(term.id)
                        }
                    },
                    onLongClick = {
                        if (!isInSelectionMode) {
                            isInSelectionMode = true
                            selectedTerms.add(term.id)
                        }
                    },
                    isSelected = isSelected
                )
            }
        }

        if (deleteRequested) {
            AlertDialog(
                onDismissRequest = { deleteRequested = false },
                title = { Text(text = "Terms deleting") },
                text = { Text("Are u sure?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteRequested = false
                            coroutineScope.launch {
                                viewModel.deleteTerms(selectedTerms)
                                selectedTerms.clear()
                            }
                        }
                    ) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteRequested = false }) {
                        Text(text = "No")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TermToDisplay(
    term: Term,
    onClick: () -> Unit = {  },
    onLongClick: () -> Unit = {  },
    isSelected: Boolean = false
) {
    val defaultOffset = dimensionResource(R.dimen.default_offset)

    Card(
        shape = if (term.type != "Paragraph") RectangleShape else CardDefaults.shape,   // Put Card in "when" construction?
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Purple80 else Color(0xFFFFFBFE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, if (term.type == "Paragraph") Color.Black else Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultOffset, defaultOffset / 2)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(defaultOffset),
            modifier = Modifier
                .padding(defaultOffset)
        ) {
            Text(
                text = when (term.type) {
                    "Theorem" -> "Lw"
                    "Term" -> "Df"
                    "Paragraph" -> "§"
                    else -> ""
                },
                fontSize = if (term.type == "Paragraph") LocalTextStyle.current.fontSize * 1.2 else LocalTextStyle.current.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (term.type == "Paragraph") term.name + ". " + term.definition else term.name,
                fontSize = if (term.type == "Paragraph") LocalTextStyle.current.fontSize * 1.2 else LocalTextStyle.current.fontSize,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

fun sendDatabase(context: Context) {
    val databaseFile: File = context.getDatabasePath(getString(context, R.string.main_database_name))   // Why it also could return a path for .db file when it was passed to a getDatabasePath()?
    val uri = FileProvider.getUriForFile(context, "com.example.prikol.fileprovider", databaseFile)
    Log.d(TAG, "generated uri is $uri")
    val intent = Intent(ACTION_SEND)
    intent.type = "vnd.android.cursor.dir/email"   // What is it? Probably it's all about type. Try "application/x-sqlite3"?
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.putExtra(Intent.EXTRA_SUBJECT, "Kotiki")
//    intent.setData(uri)   // Not working
//    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION)   // Not needed
    context.startActivity(Intent.createChooser(intent, "Choose where to send your database file"))   // Text is not displayed
}
