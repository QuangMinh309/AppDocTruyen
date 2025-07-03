package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.admin.CategoryMgmtViewModel
import com.example.frontend.ui.components.ScreenFrame
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.CategoryList
import com.example.frontend.ui.components.ConfirmationDialog
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun CategoryManagementScreen(viewModel: CategoryMgmtViewModel = hiltViewModel())
{
    val context = LocalContext.current
    val tbNameValue by viewModel.name.collectAsState()
    val tbNewName by viewModel.newName.collectAsState()
    val categoryList by viewModel.categories.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()
    val isCategoriesLoading by viewModel.isCategoriesLoading.collectAsState()
    val isShowDialog by viewModel.isShowDialog.collectAsState()
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
        text = "Are you sure you want to delete this category?",
        onConfirm = {
            viewModel.deleteSelectedCategory()
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
                    text = "Category",
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
            text = "Create or update categories",
            color = Color.White,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column( //find category
        )
        {
            Text(
                text = "Find category",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            TextField(
                value = tbNameValue,
                onValueChange = { viewModel.onNameChange(it) },
                singleLine = true,
                placeholder = {
                    Text("Enter name (leave empty if new category)", style = TextStyle(color = Color.Gray))
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
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White,
                    focusedIndicatorColor = BurntCoral,
                    unfocusedIndicatorColor = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column ( //update category
        )
        {
            Text(
                text = "Update",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Row {
                TextField(
                    value = tbNewName,
                    onValueChange = { viewModel.onNewNameChange(it) },
                    singleLine = true,
                    placeholder = {
                        Text("Enter new name", style = TextStyle(color = Color.Gray))
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                    modifier = Modifier
                        .weight(1f)
                        .height(height = 53.dp)
                        .padding(end = 10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        focusedIndicatorColor = BurntCoral,
                        unfocusedIndicatorColor = Color.White
                    )
                )
                Button(
                    onClick = { viewModel.createOrUpdateCategory() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeRed
                    ),
                    modifier = Modifier.width(100.dp)
                )
                {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    )
                }
            }

        }
//        SearchBar()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 30.dp)
        )
        {
            Text(
                text = "All categories",
                color = Color.White,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.setShowDialogState(true) },
                enabled = selectedItem != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedItem != null) BurntCoral else Color(0xAFAF2238)
                ),
                modifier = Modifier.width(100.dp)
            )
            {
                Text(
                    text = "Delete",
                    color = if (selectedItem != null) Color.White else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
        }
        if (isCategoriesLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else {
            CategoryList(
                categories = categoryList,
                selectedCategory = selectedItem,
                onCategorySelected = { viewModel.selectCategory(it) }
            )
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun PreviewScreenContent() {
//    val fakeViewModel = CategoryMgmtViewModel (NavigationManager())
//    CategoryManagementScreen(viewModel = fakeViewModel)
//}