package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.admin.CommunityMgmtViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.CategoryList
import com.example.frontend.ui.components.CommunityCardCard
import com.example.frontend.ui.components.ConfirmationDialog
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DarkDenim
import com.example.frontend.ui.theme.DeepBlue
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun CommunityManagementScreen(viewModel: CommunityMgmtViewModel = hiltViewModel()) {
    val communities by viewModel.displayedCommunities.collectAsState()
    val selectedCommunity by viewModel.selectedCommunity.collectAsState()
    val tbSearchValue by viewModel.tbSearchValue.collectAsState()
    val tbCommNameValue by viewModel.tbCommNameValue.collectAsState()
    val tbCommDescValue by viewModel.tbCommDescValue.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showImagePicker by remember { mutableStateOf(false) }
    val showCreateDialog = remember { mutableStateOf(false) }
    val showUpdateDialog = remember { mutableStateOf(false) }
    val isShowDialog by viewModel.isShowDialog.collectAsState()
    val context = LocalContext.current
    val toast by viewModel.toast.collectAsState()
    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.setAvatarUri(uri)
            }
            showImagePicker = false
        }
    )

    ConfirmationDialog(
        showDialog = isShowDialog,
        title="Confirm Deletion",
        text = "Are you sure you want to delete this community?",
        onConfirm = {
                viewModel.deleteSelectedCommunity()
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
                    text = "Community",
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
        Text(
            text = "Manage community",
            color = Color.White,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(vertical = 30.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Search by name",
            color = Color.LightGray,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.poppins_bold))
            )
        )
        TextField( //textfield
            value = tbSearchValue,
            onValueChange = { viewModel.onTbSearchValueChange(it) },
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
        Row( //buttons
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 10.dp)
        )
        {
            Button(
                onClick = { showCreateDialog.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeRed
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = "Create",
                    color = DeepBlue,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { showUpdateDialog.value = true },
                enabled = selectedCommunity != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = "Update",
                    color = if (selectedCommunity != null) DeepBlue else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { viewModel.setShowDialogState(true) },
                enabled = selectedCommunity != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCommunity != null) BurntCoral else Color(0xAFAF2238)
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = "Delete",
                    color = if (selectedCommunity != null) DeepBlue else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
        }
        FlowColumn( //list
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center
        ) {
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
            else{
                communities.forEach { community ->
                    CommunityCardCard(
                        community,
                        isSelected = community == selectedCommunity,
                        onClick = { viewModel.onSelectCommunity(community) },
                        onClick2 = { viewModel.onGoToCommunityDetailScreen(community.id) }
                    )
                }
            }
        }
        if(showCreateDialog.value) //create dialog
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),
                onDismissRequest = { showCreateDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(tbCommNameValue.isBlank() || selectedCategory == null || viewModel.selectedAvatarUri.value == null)
                            {
                                Toast.makeText(context, "Please fill in all the necessary fields", Toast.LENGTH_SHORT).show()
                            }
                            else
                            {
                                viewModel.createCommunity(context)
                                showCreateDialog.value = false
                            }
                        }
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Create community", color = Color.White)
                },
                text = {
                    Column {
                        Text("Name: ", color = Color.LightGray) //name
                        TextField(
                            value = tbCommNameValue,
                            onValueChange = { viewModel.onTbNameChange(it) },
                            singleLine = true,
                            placeholder = {
                                Text("Enter name", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                            },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular))),
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
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Description(optional): ", color = Color.LightGray) //desc
                        TextField(
                            value = tbCommDescValue,
                            onValueChange = { viewModel.onTbDescChange(it) },
                            singleLine = true,
                            placeholder = {
                                Text("Enter description", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                            },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular))),
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
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) { //avatar
                            Text("Avatar: ", color = Color.LightGray)
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkDenim
                                ),
                                onClick = { showImagePicker = true }
                            ) {
                                Text("Choose", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = viewModel.selectedAvatarUri.value ?: R.drawable.intro_page1_bg
                                ),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Category: ", color = Color.LightGray) //category
                        CategoryList(
                            categories = categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.onSelectCategory(it) }
                        )
                    }
                },
                containerColor = Color.Black
            )
        }
        if(showUpdateDialog.value) //update dialog
        {
            viewModel.updateFields()
            AlertDialog(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, BurntCoral, shape = RoundedCornerShape(25.dp)),
                onDismissRequest = {
                    viewModel.clearFields()
                    showUpdateDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(tbCommNameValue.isBlank() || selectedCategory == null)
                            {
                                Toast.makeText(context, "Please fill in all the necessary fields", Toast.LENGTH_SHORT).show()
                            }
                            else
                            {
                                viewModel.updateCommunity(context)
                                viewModel.clearFields()
                                showUpdateDialog.value = false
                            }
                        }
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.clearFields()
                        showUpdateDialog.value = false })
                    {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Update community", color = Color.White)
                },
                text = {
                    Column {
                        Text("Name: ", color = Color.LightGray) //name
                        TextField(
                            value = tbCommNameValue,
                            onValueChange = { viewModel.onTbNameChange(it) },
                            singleLine = true,
                            placeholder = {
                                Text("Enter name", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                            },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular))),
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
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Description: ", color = Color.LightGray) //desc
                        TextField(
                            value = tbCommDescValue,
                            onValueChange = { viewModel.onTbDescChange(it) },
                            singleLine = true,
                            placeholder = {
                                Text("Enter description", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold))
                            },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular))),
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
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) { //avatar
                            Text("Avatar: ", color = Color.LightGray)
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkDenim
                                ),
                                onClick = { showImagePicker = true }
                            ) {
                                Text("Choose", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = viewModel.selectedAvatarUri.value ?: R.drawable.intro_page1_bg
                                ),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Category: ", color = Color.LightGray) //category
                        CategoryList(
                            categories = categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.onSelectCategory(it) }
                        )
                    }
                },
                containerColor = Color.Black
            )
        }

        if (showImagePicker) {
            launcher.launch("image/*")
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun PreviewScreenContent() {
//    val fakeViewModel = CommunityMgmtViewModel (NavigationManager())
//    CommunityManagementScreen(viewModel = fakeViewModel)
//}