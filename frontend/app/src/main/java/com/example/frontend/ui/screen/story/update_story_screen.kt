package com.example.frontend.ui.screen.story

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
import com.example.frontend.data.model.Chapter
import com.example.frontend.presentation.viewmodel.story.UpdateStoryViewModel
import com.example.frontend.ui.components.ConfirmationDialog
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateStoryScreen(viewModel: UpdateStoryViewModel = hiltViewModel()) {
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
    val coverImgId by viewModel.coverImgId.collectAsState()
    val coverImgUrl by viewModel.coverImgUrl.collectAsState()
    val availableCategories by viewModel.availableCategories.collectAsState()
    val chapters by viewModel.chapters.collectAsState()
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage.collectAsState()
    val toastMessage by viewModel.toast.collectAsState()
    val context = LocalContext.current

    // Trạng thái cho chế độ chọn và danh sách chapter được chọn
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedChapters by remember { mutableStateOf(setOf<Int>()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Trạng thái cho pull-to-refresh
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    listOf(
                        async { viewModel.loadData() }
                    ).awaitAll()
                } catch (e: Exception) {
                    Log.e("UpdateStoryScreen", "Error refreshing UpdateStoryScreen: ${e.message}")
                    viewModel.showToast("Error refreshing data: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

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

    // Log URL ảnh bìa
    LaunchedEffect(coverImage, coverImgUrl) {
        if (coverImage != null) {
            Log.d("UpdateStoryScreen", "Using local cover image: ${coverImage!!.absolutePath}")
        } else if (coverImgUrl != null) {
            Log.d("UpdateStoryScreen", "Using remote cover image: $coverImgUrl")
        } else {
            Log.d("UpdateStoryScreen", "No cover image available")
        }
    }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Update Story",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { /* TODO: Handle settings */ }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = OrangeRed)
                    }
                }
                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = errorMessage.toString(),
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
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
                                    .clickable {
                                        launcher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    coverImage != null -> {
                                        AsyncImage(
                                            model = coverImage,
                                            contentDescription = "Selected cover image",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(250.dp),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(R.drawable.placeholder_cover),
                                            error = painterResource(R.drawable.placeholder_cover)
                                        )
                                    }
                                    coverImgUrl != null -> {
                                        AsyncImage(
                                            model = coverImgUrl,
                                            contentDescription = "Current cover image",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(250.dp),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(R.drawable.placeholder_cover),
                                            error = painterResource(R.drawable.placeholder_cover)
                                        )
                                    }
                                    else -> {
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
                                                color = Color.White,
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
                                onClick = { viewModel.updateStory() },
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
                                        CircularProgressIndicator(
                                            color = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Save",
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
                                    onValueChange = { viewModel.updatePricePerChapter(it) },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.DarkGray,
                                            shape = RoundedCornerShape(30.dp)
                                        )
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

                                Spacer(modifier = Modifier.height(16.dp))

                                // Chapters
                                Text(
                                    text = "Chapters",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                                )

                                // Add button
                                Button(
                                    onClick = {
                                        viewModel.onGoToWriteScreen(
                                            viewModel.storyId.value ?: 0
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Green),
                                    shape = RoundedCornerShape(30.dp),
                                    modifier = Modifier
                                        .height(35.dp)
                                        .widthIn(min = 95.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.vote_icon),
                                            contentDescription = "Add",
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.Black
                                        )
                                        Text(text = "Add Chapter")
                                        Spacer(modifier = Modifier.width(5.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                // Nút xóa khi có chapter được chọn
                                if (isSelectionMode && selectedChapters.isNotEmpty()) {
                                    Button(
                                        onClick = { showDeleteDialog = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "Xóa ${selectedChapters.size} chương",
                                            color = Color.White
                                        )
                                    }
                                }

                                // Danh sách chương
                                if (chapters.isEmpty()) {
                                    Text(
                                        text = "No chapters available",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                } else {
                                    chapters.forEachIndexed { index, chapter ->
                                        ChapterItemCard(
                                            chapter = chapter,
                                            isSelectionMode = isSelectionMode && viewModel.isAuthor.value,
                                            isSelected = chapter.chapterId in selectedChapters,
                                            onClick = {
                                                viewModel.onGoToChapterScreen(
                                                    chapter.chapterId,
                                                    viewModel.finalChapterId.value ?: chapter.chapterId,
                                                    viewModel.storyId.value ?: 0,
                                                    viewModel.isAuthor.value
                                                )
                                            },
                                            onLongClick = {
                                                isSelectionMode = true
                                                selectedChapters =
                                                    selectedChapters + chapter.chapterId
                                            },
                                            onCheckedChange = { isChecked ->
                                                selectedChapters = if (isChecked) {
                                                    selectedChapters + chapter.chapterId
                                                } else {
                                                    selectedChapters - chapter.chapterId
                                                }
                                                if (selectedChapters.isEmpty()) {
                                                    isSelectionMode = false
                                                }
                                            }
                                        )
                                        if (index < chapters.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                thickness = 1.2.dp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item { Spacer(Modifier.height(80.dp)) }
                    }

                    // Hiển thị ConfirmationDialog
                    ConfirmationDialog(
                        showDialog = showDeleteDialog,
                        title = "Xác nhận xóa",
                        text = "Bạn có chắc muốn xóa ${selectedChapters.size} chương đã chọn?",
                        onConfirm = {
                            scope.launch {
                                viewModel.deleteChapters(selectedChapters.toList())
                                selectedChapters = emptySet()
                                isSelectionMode = false
                                showDeleteDialog = false
                            }
                        },
                        onDismiss = {
                            showDeleteDialog = false
                            selectedChapters = emptySet()
                            isSelectionMode = false
                        }
                    )

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
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = OrangeRed,
                backgroundColor = Color.White
            )
        }
    }
}

@Composable
fun ChapterItemCard(
    chapter: Chapter,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { if (!isSelectionMode) onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelectionMode) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row {
                Text(text = chapter.chapterName, color = Color.White, fontSize = 19.sp)
                Spacer(modifier = Modifier.width(11.dp))
                if (chapter.lockedStatus && !isSelectionMode) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Locked chapter",
                        modifier = Modifier.size(16.dp),
                        tint = OrangeRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(13.dp))

            Row {
                val formattedDateTime = chapter.updatedAtString?.let { dateStr ->
                    try {
                        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        val dateTime = LocalDateTime.parse(dateStr.trim(), formatter)
                        "${dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))} " +
                                "${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    } catch (e: Exception) {
                        "N/A"
                    }
                } ?: "N/A"
                Text(
                    text = formattedDateTime,
                    color = OrangeRed,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                if (!isSelectionMode) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.comment_icon),
                            contentDescription = "Comments",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            chapter.commentNumber.toString(),
                            color = Color.White,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.width(11.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.view_icon),
                            contentDescription = "Views",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(chapter.viewNum.toString(), color = Color.White, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}