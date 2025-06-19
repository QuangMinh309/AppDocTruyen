package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.frontend.ui.components.CategoryList
import com.example.frontend.ui.components.SelectChip
import com.example.frontend.ui.components.StoryCard4
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun StoryManagementScreen(viewModel: StoryMgmtViewModel = hiltViewModel())
{
    val stories by viewModel.stories.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val tbSearchValue by viewModel.tbSearchValue.collectAsState()
    val selectedSearchType by viewModel.selectedSearchType.collectAsState()
    val selectedStates by viewModel.selectedStates.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

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
            Text(
                text = "Find stories",
                color = Color.White,
                style = TextStyle(
                    fontSize = 30.sp,
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
                SelectChip(
                    name = "ID",
                    isSelected = "ID" == selectedSearchType,
                    onClick = { viewModel.onSelectSearchType("ID") }
                )
            }
            TextField(
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
        Column(modifier = Modifier) {
            Spacer(Modifier.height(20.dp))
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            // Apply filters here
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
                            Text("Date:", color = Color.LightGray)

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = BurntCoral,
                                        checkmarkColor = Color(0xFF1C1C1C)
                                    ),
                                    checked = true,
                                    onCheckedChange = {}
                                )
                                Text("Newest", color = Color.White)
                                Spacer(modifier = Modifier.width(10.dp))
                                Checkbox(
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = BurntCoral,
                                        checkmarkColor = Color(0xFF1C1C1C)
                                    ),
                                    checked = false,
                                    onCheckedChange = {}
                                )
                                Text("Oldest", color = Color.White)
                            }

                            Text("State:", color = Color.LightGray)
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow {
                                SelectChip(
                                    name = "approved",
                                    isSelected = "approved" in selectedStates,
                                    onClick = { viewModel.onSelectState("approved") }
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
                                    name = "update",
                                    isSelected = "update" in selectedStates,
                                    onClick = { viewModel.onSelectState("update") }
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
                                categories.forEach { category ->
                                    SelectChip(
                                        name = category.name,
                                        isSelected = category.name in selectedCategories,
                                        onClick = { viewModel.onSelectCategory(category.name) }
                                    )
                                }
                            }
                        }
                    },
                    containerColor = Color(0xFF1C1C1C)
                )
            }
            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(8.dp)
            ){
                items(stories) { story ->
                    StoryCard4(story = story, onClick = {  })
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun PreviewStoryManagementScreen()
{
    val fakeViewModel = StoryMgmtViewModel(NavigationManager())
    StoryManagementScreen(fakeViewModel)
}