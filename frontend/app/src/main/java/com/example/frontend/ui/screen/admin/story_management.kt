package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.admin.StoryMgmtViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.ScreenFrame
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.text.isDigitsOnly
import com.example.frontend.ui.components.ConfirmationDialog
import com.example.frontend.ui.components.SelectChip
import com.example.frontend.ui.components.StoryCardCard
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepBlue

@Composable
fun StoryManagementScreen(viewModel: StoryMgmtViewModel = hiltViewModel())
{
    val context = LocalContext.current
    val stories by viewModel.displayedStories.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val tbSearchValue by viewModel.tbSearchValue.collectAsState()
    val selectedStory by viewModel.selectedStory.collectAsState()
    val selectedSearchType by viewModel.selectedSearchType.collectAsState()
    val selectedStates by viewModel.selectedStates.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isShowDialog by viewModel.isShowDialog.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val showApproveDialog = remember { mutableStateOf(false) }
    val showRejectDialog = remember { mutableStateOf(false) }
    val tbAgeRange = remember { mutableStateOf("") }
    val toast by viewModel.toast.collectAsState()
    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
    ConfirmationDialog(
        showDialog = isShowDialog,
        title="Confirm Deletion",
        text = "Are you sure you want to delete this story?",
        onConfirm = {
            viewModel.deleteSelectedStory()
            viewModel.setShowDialogState(false)
        },
        onDismiss = {
            viewModel.setShowDialogState(false)
        }
    )
    ScreenFrame(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    onClick = {viewModel.onGoBack()},
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(25.dp)
                        .weight(0.33f)
                        .wrapContentWidth(Alignment.Start)
                ) {
                    Text(
                        text = "< Back",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                }

                Text(
                    text = "Story",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(0.33f))
            }
        }
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 30.dp)
        )
        {
            Text(//title
                text = "Find stories",
                color = Color.White,
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.setting_ic),
                contentDescription = "Settings",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clickable { showDialog.value = true }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column ( // find by username
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Search by: ",
                    color = Color.LightGray,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                )
                SelectChip(
                    name = "Name",
                    isSelected = "Name" == selectedSearchType,
                    onClick = { viewModel.onSelectSearchType("Name") }
                )
                SelectChip(
                    name = "Author",
                    isSelected = "Author" == selectedSearchType,
                    onClick = { viewModel.onSelectSearchType("Author") }
                )
            }
            TextField(//textfield
                value = tbSearchValue,
                onValueChange = { viewModel.onSearch(it) },
                singleLine = true,
                placeholder = {
                    Text("Enter here", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                },
                textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 53.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = BurntCoral,
                    unfocusedIndicatorColor = Color.White
                )
            )
        }
        //action buttons
        Column(modifier = Modifier) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp)
            )
            {
                Button(
                    onClick = { showApproveDialog.value = true },
                    enabled = selectedStory != null && selectedStory!!.status == "pending",
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    ),
                    modifier = Modifier.weight(1f)
                )
                {
                    Text(
                        text = "Approve",
                        color = if (selectedStory != null && selectedStory!!.status == "pending") DeepBlue else Color.Gray,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { showRejectDialog.value = true },
                    enabled = selectedStory != null && selectedStory!!.status == "pending",
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow
                    ),
                    modifier = Modifier.weight(1f)
                )
                {
                    Text(
                        text = "Reject",
                        color = if (selectedStory != null && selectedStory!!.status == "pending") DeepBlue else Color.Gray,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { viewModel.setShowDialogState(true) },
                    enabled = selectedStory != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.weight(1f)
                )
                {
                    Text(
                        text = "Delete",
                        color = if (selectedStory != null) DeepBlue else Color.Gray,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    )
                }
            }
            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(8.dp)
            ){
                items(stories) { story ->
                    StoryCardCard(story = story, isSelected = story == selectedStory , onClick = { viewModel.onSelectStory(story) }, onClick2 = { viewModel.onGoToStoryScreen(story.id) })
                }
            }
        }
        if(showDialog.value)
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),
                onDismissRequest = { showDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.loadDisplayedStories()
                        showDialog.value = false
                    }) {
                        Text("Apply", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Select filters", color = Color.White)
                },
                text = {
                    Column {
//                        Text("Date:", color = Color.LightGray)
//
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Checkbox(
//                                colors = CheckboxDefaults.colors(
//                                    checkedColor = BurntCoral,
//                                    checkmarkColor = Color(0xFF1C1C1C)
//                                ),
//                                checked = true,
//                                onCheckedChange = {}
//                            )
//                            Text("Newest", color = Color.White)
//                            Spacer(modifier = Modifier.width(10.dp))
//                            Checkbox(
//                                colors = CheckboxDefaults.colors(
//                                    checkedColor = BurntCoral,
//                                    checkmarkColor = Color(0xFF1C1C1C)
//                                ),
//                                checked = false,
//                                onCheckedChange = {}
//                            )
//                            Text("Oldest", color = Color.White)
//                        }

                        Text("State:", color = Color.LightGray)
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow {
                            SelectChip(
                                name = "update",
                                isSelected = "update" in selectedStates,
                                onClick = { viewModel.onSelectState("update") }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            SelectChip(
                                name = "rejected",
                                isSelected = "rejected" in selectedStates,
                                onClick = { viewModel.onSelectState("rejected") }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            SelectChip(
                                name = "pending",
                                isSelected = "pending" in selectedStates,
                                onClick = { viewModel.onSelectState("pending") }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            SelectChip(
                                name = "full",
                                isSelected = "full" in selectedStates,
                                onClick = { viewModel.onSelectState("full") }
                            )
                        }

                        Text("Category:", color = Color.LightGray)
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            else
                            {
                                categories.forEach { category ->
                                    SelectChip(
                                        name = category.name?:"",
                                        isSelected = category.name in selectedCategories,
                                        onClick = { viewModel.onSelectCategory(category.name?:"") }
                                    )
                                }
                            }
                        }
                    }
                },
                containerColor = Color.Black
            )
        }
        if(showApproveDialog.value)
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),
                onDismissRequest = { showApproveDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(tbAgeRange.value.isEmpty() || !tbAgeRange.value.isDigitsOnly() || tbAgeRange.value.toInt() < 0)
                            {
                                Toast.makeText(context, "Please enter an appropriate age range", Toast.LENGTH_SHORT).show()
                                return@TextButton
                            }
                            viewModel.approveSelectedStory(tbAgeRange.value)
                            showApproveDialog.value = false
                        }
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showApproveDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Approve Story", color = Color.White)
                },
                text = {
                    Column {
                        Text("Choose an age range: ", color = Color.LightGray)
                        TextField(
                            value = tbAgeRange.value,
                            onValueChange = { tbAgeRange.value = it },
                            singleLine = true,
                            placeholder = {
                                Text("Enter here", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                            },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 53.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.LightGray,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = BurntCoral,
                                unfocusedIndicatorColor = Color.White
                            )
                        )
                    }
                },
                containerColor = Color.Black
            )
        }
        if(showRejectDialog.value)
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),
                onDismissRequest = { showRejectDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(tbAgeRange.value.isEmpty() || !tbAgeRange.value.isDigitsOnly() || tbAgeRange.value.toInt() < 0)
                            {
                                Toast.makeText(context, "Please enter an appropriate age range", Toast.LENGTH_SHORT).show()
                                return@TextButton
                            }
                            viewModel.rejectSelectedStory(tbAgeRange.value)
                            showRejectDialog.value = false
                        }
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRejectDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Reject Story", color = Color.White)
                },
                text = {
                    Column {
                        Text("Choose an age range: ", color = Color.LightGray)
                        TextField(
                            value = tbAgeRange.value,
                            onValueChange = { tbAgeRange.value = it },
                            singleLine = true,
                            placeholder = {
                                Text("Enter here", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                            },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 53.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.LightGray,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = BurntCoral,
                                unfocusedIndicatorColor = Color.White
                            )
                        )
                    }
                },
                containerColor = Color.Black
            )
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun PreviewStoryManagementScreen()
//{
//    val fakeViewModel = StoryMgmtViewModel(NavigationManager())
//    StoryManagementScreen(fakeViewModel)
//}