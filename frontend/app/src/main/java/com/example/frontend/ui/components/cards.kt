package com.example.frontend.ui.components

import coil.compose.AsyncImage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AccountBalance

import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bookmarks


import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.IconButton
import androidx.window.layout.WindowMetricsCalculator
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.data.model.Author
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.DayRevenue
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.model.User
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ReadListItem_
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.collections.forEach

//region community Card
@Composable
fun CommunityCard(model: Community, onClick: () -> Unit = {}){

    Column (

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(180.dp)
            .width(150.dp)
            .background(Color.DarkGray.copy(0.4f), RoundedCornerShape(10.dp))
            .padding(vertical = 12.dp)
            .clickable { onClick() }
    ){
        Image(
            painter = painterResource(id = R.drawable.intro_page1_bg),
            contentDescription = "community avatar",
            modifier = Modifier
                .size(70.dp, 70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // Cắt ảnh nếu cần thiết để lấp đầy không gian
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Community name
            Text(
                text =model.name,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding( vertical = 4.dp,horizontal = 10.dp)
            )
            //genre chip
            //GenreChip(genre = model.category)
            model.category?.let {
                GenreChip(genre = it)
            }

            //member number
            Text(
                text = formatViews(model.memberNum.toLong()) + " members",
                color = Color.White,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier
                    .padding( vertical = 4.dp,horizontal = 10.dp)
            )
        }

    }
}

@Composable
fun MemberCard(model : User, onClick: () -> Unit = {}){
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = model.avatarUrls ,
            placeholder = painterResource(id = R.drawable.intro_page1_bg),
            contentDescription = "member avatar",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // fill mode
        )
        Column(
            modifier = Modifier
                .padding(start = 15.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = model.name,
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "@${model.dName}",
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
        //follow button
        Button(
            onClick = { onClick()},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .wrapContentWidth(Alignment.End)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .background(
                        color = if (model.isFollowed)  Color.LightGray else OrangeRed,
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (model.isFollowed) "UnFollow" else "Follow",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun
        NotificationCard(content:String = "",
                         type: String = "notification",
                         time: LocalDateTime
){
    val typeList = listOf("purchase","withdraw","deposit","premium","TRANSACTION_APPROVAL","USER_REPORT","STORY_APPROVAL")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ){
        if(type in typeList){
            Icon(
                imageVector = when (type) {
                    "withdraw" -> Icons.Filled.LocalAtm
                    "deposit"-> Icons.Filled.AccountBalance
                    "purchase" -> Icons.Filled.Payments
                    "premium" -> Icons.Filled.Diamond
                    "TRANSACTION_APPROVAL" -> Icons.Filled.AccountBalanceWallet
                    "USER_REPORT" -> Icons.Filled.DoNotDisturbOn
                    "STORY_APPROVAL" -> Icons.Filled.Bookmarks
                    else -> Icons.Filled.QuestionMark
                },
                contentDescription = "transaction icon" ,
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(horizontal = 5.dp)
            )
        }
        else{
            Image(
                painter = painterResource(R.drawable.avt_img),
                contentDescription =null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop // fill mode
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 15.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){

            Text(
                text = content,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
            //Time
            val formatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
            val daysDifference = ChronoUnit.DAYS.between(time, LocalDateTime.now())
            val dayOfWeek = time.format(formatter)
            Text(
                text =  if (daysDifference>7)
                            "$dayOfWeek ${time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
                        else if(daysDifference>1)
                            "$daysDifference ago."
                        else time.format(DateTimeFormatter.ofPattern("hh:MM a")),
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}

//endregion
@Composable
fun ChapterItemCard(
    chapter: Chapter,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Log.d("ChapterItemCard", "Rendering chapter: ${chapter.chapterName}, updatedAtString: ${chapter.updatedAtString}")
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
                        contentDescription = null,
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
                                dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
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
                            imageVector = ImageVector.vectorResource(id = R.drawable.comment_icon),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(chapter.commentNumber.toString(), color = Color.White, fontSize = 15.sp)

                        Spacer(modifier = Modifier.width(11.dp))

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.view_icon),
                            contentDescription = null,
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

//region story card
@Composable
fun SimilarNovelsCard(novels: List<Story>, viewModel: BaseViewModel) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(17.dp)) {
        items(novels, key = { it.id }) { novel ->
            Column(
                modifier = Modifier
                    .width(128.dp)
                    .clickable { viewModel.onGoToStoryScreen(novel.id) },
                horizontalAlignment = Alignment.Start
            ) {
                AsyncImage(
                    model = novel.coverImgUrl.takeIf { it.isNotEmpty() } ?: R.drawable.placeholder_cover, // Sử dụng URL từ Story
                    contentDescription = null,
                    modifier = Modifier
                        .height(184.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.placeholder_cover),
                    error = painterResource(R.drawable.placeholder_cover)
                )
                Spacer(modifier = Modifier.height(11.dp))

                Text(
                    text = novel.name?:"", // Thay "title" bằng "name"
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = novel.author.name, // Giả định Author có thuộc tính name
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.price_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = OrangeRed
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val formatter = DecimalFormat("#,###"+"đ")

                        Text(
                            text = "${formatter.format(novel.pricePerChapter.toLong())}/Chapter ", // Sử dụng price từ Story
                            color = Color.White,
                            fontSize = 12.sp
                        )

                    }

                    Spacer(modifier = Modifier.weight(1f))


                }
            }
        }
    }
}

@Composable
fun StoryCard4(
    modifier: Modifier = Modifier,
    story: Story,
    onClick: () -> Unit = {},
    onDeleteClick: (() -> Unit)? = null,
    showDeleteButton: Boolean = false // Thêm tham số để kiểm soát hiển thị nút xóa
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = story.coverImgUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 160.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder_cover),
            error = painterResource(R.drawable.placeholder_cover)
        )

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            // name
            Text(
                story.name ?: "",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                "@${story.author.name}",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(13.dp))

            // genre tags

            SmallGenreTags(story.categories ?: emptyList())
            Spacer(modifier = Modifier.height(27.dp))

            // updated time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Last Updated: ",
                    color = Color.White,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    story.updateAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    color = OrangeRed,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(11.dp))

            // other info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.view_icon),
                    contentDescription = "View Icon",
                    tint = OrangeRed,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    "${story.viewNum}",
                    color = Color.White,
                    fontSize = 12.5.sp
                )

                Spacer(modifier = Modifier.width(25.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                    contentDescription = "List Chapter Icon",
                    tint = OrangeRed,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    "${story.chapterNum}",
                    color = Color.White,
                    fontSize = 12.5.sp
                )
            }
        }

        // Nút xóa (chỉ hiển thị nếu showDeleteButton = true và onDeleteClick được cung cấp)
        if (showDeleteButton && onDeleteClick != null) {
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Story",
                    tint = OrangeRed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    // Dialog xác nhận xóa (chỉ hiển thị nếu showDeleteButton = true)
    if (showDeleteButton && showDeleteDialog) {
        ConfirmationDialog(
            showDialog = showDeleteDialog,
            title = "Delete Story",
            text = "Are you sure you want to delete '${story.name}'? This action cannot be undone.",
            onConfirm = {
                onDeleteClick?.invoke()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Spacer(Modifier.height(11.dp))
}

@Composable
fun StoryCard(
    story: Story,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Log.d("StoryCard", "Rendering story: ${story.name}")
    Column(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // Hình ảnh bìa truyện
            AsyncImage(
                model = story.coverImgUrl.takeIf { it.isNotEmpty() } ?: R.drawable.placeholder_cover,
                contentDescription = story.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, end = 5.dp)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(R.drawable.placeholder_cover),
                error = painterResource(R.drawable.placeholder_cover)
            )

            // Nhãn Premium/Free
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = if (story.pricePerChapter?.compareTo(BigDecimal.ZERO) != 0) Color(0xFFFBBC05) else BrightAquamarine,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (story.pricePerChapter?.compareTo(BigDecimal.ZERO) !=0) "PREMIUM" else "FREE",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tiêu đề truyện
        Text(
            text = story.name?:"",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Tác giả
        Text(
            text = "@${story.author.name}",
            color = Color.White,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Mô tả ngắn
        Text(
            text = story.description ?:"No description available...",
            color = Color.LightGray,
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}


@Composable
fun StoryCard2(
    story: Story,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Log.d("StoryCard2", "Rendering story: ${story.name}")
    val isPremium = story.pricePerChapter?.compareTo(BigDecimal.ZERO) != 0
    Column(
        modifier = modifier
            .width(200.dp)
            .background(Color.Transparent, RoundedCornerShape(5.dp))
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // Story Image (on top)
        AsyncImage(
            model = story.coverImgUrl.takeIf { it.isNotEmpty() } ?: R.drawable.placeholder_cover,
            contentDescription = "Story Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder_cover),
            error = painterResource(R.drawable.placeholder_cover)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Story Details (below the image)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = story.name ?: "",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1, // Giới hạn tối đa 1 dòng
                overflow = TextOverflow.Ellipsis // Hiển thị dấu ba chấm khi bị cắt
            )
            if (isPremium) { // Chỉ hiển thị biểu tượng khóa nếu truyện là premium
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock",
                    modifier = Modifier
                        .size(18.dp)
                        .padding(start = 4.dp),
                    tint = OrangeRed
                )
            }
        }
        Text(
            text = "@${story.author.name}",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.popular_icon),
                contentDescription = "Likes",
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp),
                tint = BurntCoral
            )
            Text(
                text = formatViews(story.voteNum.toLong()),
                color = Color.White,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                contentDescription = "Chapters",
                modifier = Modifier.size(18.dp),
                tint = OrangeRed
            )
            Text(
                text = "${story.chapterNum}",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}


@Composable
fun StoryCard3(
    story: Story,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDeleteClick: (() -> Unit)? = null,
    showDeleteButton: Boolean = false // Thêm tham số để kiểm soát hiển thị nút xóa
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Log.d("StoryCard3", "Rendering story: ${story.name}")

    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Ảnh bìa (tỉ lệ 3:4)
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(160.dp)
        ) {
            AsyncImage(
                model = story.coverImgUrl.takeIf { it.isNotEmpty() } ?: R.drawable.placeholder_cover,
                contentDescription = story.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.placeholder_cover),
                error = painterResource(R.drawable.placeholder_cover)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin chi tiết
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tiêu đề
            Text(
                text = story.name ?: "",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ngày cập nhật
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    ) {
                        append("Last Updated: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 10.sp,
                            color = OrangeRed
                        )
                    ) {
                        append(story.updateAt?.toString() ?: "N/A")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Các thể loại (Chips)
            Box(
                modifier = Modifier
                    .height(48.dp) // Giả định: mỗi dòng ~24.dp → 2 dòng là 48.dp
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Transparent)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    story.categories?.forEach { genre ->
                        genre.name?.let { name ->
                            Chip(text = name)
                        }
                    }
                }

                // Optional: Fade khi overflow, để tạo hiệu ứng "..."
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Số lượt xem
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.popular_icon),
                    contentDescription = "Votes",
                    tint = OrangeRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatViews(story.voteNum.toLong()),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // Nút xóa (chỉ hiển thị nếu showDeleteButton = true và onDeleteClick được cung cấp)
        if (showDeleteButton && onDeleteClick != null) {
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Story",
                    tint = OrangeRed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    // Dialog xác nhận xóa (chỉ hiển thị nếu showDeleteButton = true)
    if (showDeleteButton && showDeleteDialog) {
        ConfirmationDialog(
            showDialog = showDeleteDialog,
            title = "Remove Story",
            text = "Are you sure you want to remove '${story.name}' from this list? This action cannot be undone.",
            onConfirm = {
                onDeleteClick?.invoke()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}// Định dạng số lượt xem (167800 -> 167.8K)
internal fun formatViews(views: Long): String {
    return when {
        views >= 1000000 -> "${views / 1000000}M"
        views >= 1000 -> "${views / 1000}K"
        else -> views.toString()
    }
}

//endregion

@Composable
fun AuthorInfoCard(model: Author, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = model.avatarUrl, // URL của avatar
            contentDescription = "avatar",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop ,// fill mode
            placeholder = painterResource(R.drawable.broken_image),
            error = painterResource(R.drawable.broken_image)
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = model.name,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 7.dp)
            )
            Text(text = "@${model.dName}", color = Color.White, fontSize = 13.sp,)
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onClick) {
            Text("Thêm >", color = Color.White)
        }
    }
}

@Preview
@Composable
fun PreviewReadListItem(){
    ReadListItem(ReadListItem_)
}
@Composable
fun ReadListItem(
    item: NameList,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    showMoreOptions: Boolean = false, // Thêm tham số để kiểm soát hiển thị icon 3 chấm
    onUpdateClick: (NameList) -> Unit = {},
    onDeleteClick: (NameList) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Phần avatar xếp chồng (lấy từ 1 đến 3 truyện đầu tiên)
        Box(modifier = Modifier.size(110.dp, 140.dp)) {
            val storiesToShow = item.stories.take(3)
            storiesToShow.forEachIndexed { index, story ->
                AsyncImage(
                    model = story.coverImgUrl,
                    contentDescription = "Story cover ${index + 1}",
                    placeholder = painterResource(R.drawable.broken_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .width(90.dp)
                        .align(Alignment.TopStart)
                        .offset(
                            x = (index * 10).dp,
                            y = (index * 10).dp
                        )
                        .padding(2.dp)
                )
            }
            // Điền khoảng trống nếu ít hơn 3 truyện
            if (storiesToShow.size < 3) {
                for (i in storiesToShow.size until 3) {
                    Spacer(
                        modifier = Modifier
                            .size(90.dp, 120.dp)
                            .align(Alignment.TopStart)
                            .offset(
                                x = (i * 10).dp,
                                y = (i * 10).dp
                            )
                            .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        // Phần thông tin
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(
                    LocalContext.current
                )
                val halfWidth = with(LocalDensity.current) {
                    windowMetrics.bounds.width().toDp() * 0.3f
                }

                Text(
                    text = item.name,
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .widthIn(max = halfWidth)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(OrangeRed, BurntCoral)
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

                Box(
                    modifier = Modifier.size(20.dp)
                )
                {
                    // Hiển thị icon 3 chấm chỉ khi showMoreOptions = true
                    // thằng ngu lol đéo bt sửa bug↑↑↑
                    if (showMoreOptions) {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Color.White
                            )
                        }

                        // Dropdown Menu
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Update Read List") },
                                onClick = {
                                    onUpdateClick(item)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Read List") },
                                onClick = {
                                    onDeleteClick(item)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Text(
                text = item.description,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
@Composable
fun RowSelectItem(
    name: String,
    image: Painter,
    onClick: () -> Unit = {}
)
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clickable { onClick() }
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )
        {
            Icon(
                painter = image,
                contentDescription = "select button icon",
                tint = OrangeRed,
                modifier = Modifier
                    .size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = ">",
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                color = Color.White
            )
        }
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
fun TransactionCard(
    item : Transaction,
    isSelected : Boolean,
    onClick: () -> Unit = {}
)
{
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 10.dp)
            .background(if(isSelected) Color.Gray else Color.DarkGray, RoundedCornerShape(10.dp))
            .clickable{ onClick() },
        contentAlignment = Alignment.CenterStart
    )
    {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )
        {
            Row {
                Text(
                    text = "ID: " + item.transactionId.toString(),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),

                )
                Spacer(modifier = Modifier.width(5.dp))
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .height(17.dp)
                        .width(1.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "wallet: ${item.user?.wallet}",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Time: " + item.time,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            )
            {
                AsyncImage(
                    model = if(!item.user?.avatarUrl.isNullOrEmpty()) item.user?.avatarUrl else R.drawable.intro_page1_bg,
                    contentDescription = "pfp",
                    placeholder = painterResource(R.drawable.broken_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp, 50.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    Text(
                        text = "User ID: " + item.user!!.id,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        ),
                    )
                    Row {
                        Text(
                            text = "Type: ",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            ),
                        )
                        Text(
                            text = item.type,
                            color = if(item.type == "withdraw") Color.Red else Color.Green,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            ),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    Text(
                        text = "Money: " + item.money.toString() + "đ",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        ),
                    )
                    Text(
                        text = "Status: " + item.status,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun UserCard(
    item : User,
    isSelected : Boolean,
    onClick: () -> Unit = {},
    onClick2: () -> Unit = {}
)
{
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 10.dp)
            .background(if(isSelected) Color.Gray else Color.DarkGray, RoundedCornerShape(10.dp))
            .clickable{ onClick() },
        contentAlignment = Alignment.CenterStart
    )
    {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "ID: " + item.id,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Mail: " + item.mail,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = " view >",
                    color = OrangeRed,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                    modifier = Modifier.clickable{ onClick2() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            )
            {
                AsyncImage(
                    model = item.avatarUrl.takeIf { it.isNotEmpty() } ?: R.drawable.intro_page1_bg,
                    contentDescription = "pfp",
                    placeholder = painterResource(R.drawable.intro_page1_bg),
                    error = painterResource(R.drawable.placeholder_cover),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp, 50.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    Text(
                        text = "Username: " + item.name,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        ),
                    )
                    Text(
                        text = "Author: @" + item.dName,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        ),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    FlowRow {
                        Text(
                            text = "Status: ",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            ),
                        )
                        Text(
                            text = item.status.toString(),
                            color = if(item.status == "locked") Color.Red else Color.White,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryCardCard(
    modifier: Modifier = Modifier, story: Story,isSelected: Boolean, onClick: () -> Unit = {}, onClick2: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .background(if(isSelected) Color.DarkGray else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model= story.coverImgUrl.takeIf { it.isNotEmpty() } ?: R.drawable.placeholder_cover,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 160.dp)
                .clickable { onClick() },
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder_cover),
            error = painterResource(R.drawable.placeholder_cover)
        )

        Spacer(modifier = Modifier
            .width(13.dp)
            .clickable { onClick() })

        Column(modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
        )
        {
            //name
            Text(
                story.name?:"",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text("@${story.author.name}", color = Color.LightGray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(13.dp))

            //genre tags
            SmallGenreTags(story.categories?: emptyList())
            Spacer(modifier = Modifier.height(10.dp))

            //updated time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Last Updated: ", color = Color.White, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(story.updateAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), color = OrangeRed, fontSize = 11.sp)
            }

            //status
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Status: ", color = Color.White, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(story.status, color = if(story.status == "pending") Color.Yellow else OrangeRed, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // other info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.view_icon),
                    contentDescription = "View Icon",
                    tint = OrangeRed,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text("${story.viewNum}", color = Color.White, fontSize = 12.5.sp)

                Spacer(modifier = Modifier.width(25.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                    contentDescription = "List Chapter Icon",
                    tint = OrangeRed,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text("${story.chapterNum}", color = Color.White, fontSize = 12.5.sp)
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = ">",
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            color = Color.White,
            modifier = Modifier.clickable{ onClick2() }
        )
    }
}

@Composable
fun CommunityCardCard(
    item : Community,
    isSelected : Boolean,
    onClick: () -> Unit = {},
    onClick2: () -> Unit = {}
)
{
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(vertical = 10.dp)
            .background(if(isSelected) Color.Gray else Color.DarkGray, RoundedCornerShape(10.dp))
            .clickable{ onClick() },
        contentAlignment = Alignment.CenterStart
    )
    {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "ID: " + item.id,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = " visit >",
                    color = OrangeRed,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    ),
                    modifier = Modifier.clickable{ onClick2() }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = if(item.avatarUrl != "")item.avatarUrl else R.drawable.intro_page1_bg
                    ),
                    contentDescription = "pfp",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp, 50.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    Text(
                        text = item.memberNum.toString() + " Members",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    )
                    Text(
                        text = "Category: " + item.category?.name, //dont remove question mark
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                FlowRow {
                    Text(
                        text = "Description: " + item.description,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RevenueTable(data: List<DayRevenue>) {
    val totalIncome = data.sumOf { it.totalIncome }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Date",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Income",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Divider(color = Color.LightGray)

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            // Data Rows
            data.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = item.date,
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${item.totalIncome}đ",//${"%.2f".format(item.totalIncome)}
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // Total Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "Total",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${totalIncome}đ",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthYearPicker(
    selectedMonth: String,
    selectedYear: String,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit
) {
    val months = listOf(
        "01", "02", "03", "04", "05", "06",
        "07", "08", "09", "10", "11", "12"
    )

    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.padding(16.dp)) {
        // Year input
        OutlinedTextField(
            value = selectedYear,
            onValueChange = { onYearChange(it.filter { c -> c.isDigit() }) },
            label = { Text("Year") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(5.dp))

        // Month dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .weight(1f)
        ) {
            OutlinedTextField(
                value = selectedMonth,
                onValueChange = {},
                readOnly = true,
                label = { Text("Month") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .weight(1f)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .heightIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                months.forEach { month ->
                    DropdownMenuItem(
                        text = { Text(month) },
                        onClick = {
                            onMonthChange(month)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}