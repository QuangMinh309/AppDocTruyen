package com.example.frontend.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ui.components.InputDialog
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.presentation.viewmodel.AddStoryToNameListViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun AddStoryToNameListScreen(viewModel: AddStoryToNameListViewModel = hiltViewModel()) {
    val readingLists by viewModel.readingLists.collectAsState()
    val isLoading by viewModel.isLoading
    val showCreateDialog by viewModel.showCreateDialog
    val toastMessage by viewModel.toast.collectAsState()
    val context = LocalContext.current

    // Hiển thị Toast
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.setToast("") // Xóa toast sau khi hiển thị
        }
    }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Add to Reading List",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.showCreateDialog() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(readingLists) { readList ->
                        ReadListItem(
                            item = readList,
                            onClick = { viewModel.addStoryToNameList(readList.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Hiển thị InputDialog để tạo reading list
        InputDialog(
            showDialog = showCreateDialog,
            title = "Create New Reading List",
            onConfirm = { readListName, description ->
                viewModel.createNameList(readListName, description)
                viewModel.hideCreateDialog()
            },
            onDismiss = { viewModel.hideCreateDialog() }
        )
    }
}