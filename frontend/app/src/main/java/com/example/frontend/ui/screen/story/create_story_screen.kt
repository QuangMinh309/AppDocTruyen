package com.example.frontend.ui.screen.story

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.story.CreateStoryViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CreateStoryScreen(viewModel: CreateStoryViewModel = hiltViewModel()) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isFabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2 || listState.firstVisibleItemScrollOffset > 300
        }
    }

    val storyName by viewModel.storyName.collectAsState()
    val description by viewModel.description.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val pricePerChapter by viewModel.pricePerChapter.collectAsState()
    val coverImage by viewModel.coverImage.collectAsState()
    val availableCategories by viewModel.availableCategories.collectAsState()
    val isLoading by viewModel.isLoading
    val toastMessage by viewModel.toast.collectAsState()
    val context = LocalContext.current

    // Image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {
            val file = File(context.cacheDir, "cover_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            viewModel.updateCoverImage(file)
        }
    }

    // Hiển thị Toast
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Create Story",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { /* TODO: Handle settings */ }
            )
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clickable { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    contentAlignment = Alignment.Center
                ) {
                    if (coverImage != null) {
                        AsyncImage(
                            model = coverImage,
                            contentDescription = "Selected cover image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_add_img),
                                contentDescription = "Add img",
                                colorFilter = ColorFilter.tint(OrangeRed),
                                modifier = Modifier.size(47.dp)
                            )
                            Text(
                                text = "+ Thêm ảnh bìa",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 19.sp,
                                fontFamily = FontFamily(Font(R.font.reemkufifun_wght)),
                                modifier = Modifier.padding(top = 7.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(95.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        OrangeRed.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }

                // Title of story
                Box(
                    modifier = Modifier
                        .offset(y = (-25).dp)
                        .padding(start = 16.dp)
                        .fillMaxWidth(0.6f)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = storyName,
                        onValueChange = { viewModel.updateStoryName(it) },
                        cursorBrush = SolidColor(Color.White),
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (storyName.isEmpty()) {
                                Text(
                                    text = "+ Title",
                                    color =  Color.White.copy(alpha = 0.6f),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            item { Spacer(Modifier.height(7.dp)) }
            item {
                Button(
                    onClick = { viewModel.createStory() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(43.dp),
                    enabled = !isLoading && storyName.isNotEmpty(),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(OrangeRed, Color(0xFFDF4258)),
                                    endX = 200f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = "Create Story",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(19.dp)) }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(29.dp))
                    // Description
                    BasicTextField(
                        value = description ?: "",
                        onValueChange = { viewModel.updateDescription(it) },
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 130.dp),
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        decorationBox = { innerTextField ->
                            if (description.isNullOrEmpty()) {
                                Text(
                                    text = "Thêm mô tả...",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category
                    Text(
                        text = "Category",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableCategories.size) { index ->
                            val category = availableCategories[index]
                            FilterChip(
                                selected = categories.contains(category.id),
                                onClick = {
                                    viewModel.updateCategories(
                                        if (categories.contains(category.id)) {
                                            categories - category.id
                                        } else {
                                            categories + category.id
                                        }
                                    )
                                },
                                label = { Text(category.name.toString()) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Price per chapter
                    Text(
                        text = "Price per Chapter",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = pricePerChapter,
                        onValueChange = {
                            viewModel.updatePricePerChapter(it)
                        },
                        cursorBrush = SolidColor(Color.White),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray, shape = RoundedCornerShape(30.dp))
                            .padding(11.dp),
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        decorationBox = { innerTextField ->
                            if (pricePerChapter.isBlank()) {
                                Text(
                                    text = "Enter price per chapter (set as 0 if free)...",
                                    color = Color.LightGray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(17.dp))
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }

        if (isFabVisible) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = OrangeRed,
                modifier = Modifier.padding(15.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Scroll to top",
                    tint = Color.White
                )
            }
        }
    }
}