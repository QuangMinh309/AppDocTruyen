package com.example.frontend.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.window.layout.WindowMetricsCalculator
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.User
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ReadListItem_
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

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
         //   GenreChip(genre = model.category.name)

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
fun MemberCard(model : User){
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = model.avatarUrl ,
            placeholder = painterResource(id = R.drawable.intro_page1_bg),
            contentDescription = "community avatar",
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
        Button(
            onClick = { model.plusFollowerNum()},
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
                    .background(color = OrangeRed, shape = RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Follow",
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
fun NotificationCard(cardType :String ,
                     transactionContent:String = "",
                     transactionType: String = "",
                     time: String = ""
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ){
        if(cardType == "transactionNotification")
            Icon(
                imageVector = when (transactionType) {
                    "Withdraw" -> Icons.Filled.LocalAtm
                    "Recharge" -> Icons.Filled.AccountBalance
                    "Transfer" -> Icons.Filled.Payments
                    else -> Icons.Filled.QuestionMark
                },
                contentDescription = "transaction icon" ,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .padding(horizontal = 5.dp)
            )
        else{
            Image(
                painter = painterResource(id = R.drawable.intro_page1_bg),
                contentDescription =null,
                modifier = Modifier
                    .size(40.dp)
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
                text = transactionContent,
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Text(
                text = time,
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 12.sp
                )
            )
        }
    }
}

//endregion

@Composable
fun ChapterItemCard(
    chapter: Chapter,
    onClick: () -> Unit={}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Text(text = chapter.chapterName, color = Color.White, fontSize = 19.sp)
                Spacer(modifier = Modifier.width(11.dp))
                if (true) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.write_icon),
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = Color.White
                    )
                } else if (chapter.lockedStatus) {
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
                Text(text = chapter.updateAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), color = OrangeRed, fontSize = 14.sp)
                Text(
                    text = chapter.updateAt.format(DateTimeFormatter.ofPattern("HH:mm")) ,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 7.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

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
                    model = novel.coverImgUrl, // Sử dụng URL từ Story
                    contentDescription = null,
                    modifier = Modifier
                        .height(184.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(11.dp))

                Text(
                    text = novel.name, // Thay "title" bằng "name"
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
                        Text(
                            text = novel.price.toString(), // Sử dụng price từ Story
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.popular_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = OrangeRed
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatViews(novel.voteNum.toLong()), // Sử dụng voteNum từ Story
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryCard4(
    modifier: Modifier = Modifier, story: Story, onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model= story.coverImgUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 160.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            //name
            Text(
                story.name,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text("@${story.author.dName}", color = Color.LightGray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(13.dp))

            //genre tags
            SmallGenreTags(story.categories)
            Spacer(modifier = Modifier.height(27.dp))

            //updated time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Last Updated: ", color = Color.White, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(story.updateAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), color = OrangeRed, fontSize = 11.sp)
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
    }
    Spacer(Modifier.height(11.dp))
}

@Composable
fun StoryCard(
    story: Story,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
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
                        color = if (story.price.compareTo(BigDecimal.ZERO) != 0) Color(0xFFFBBC05) else BrightAquamarine,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (story.price.compareTo(BigDecimal.ZERO) !=0) "PREMIUM" else "FREE",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tiêu đề truyện
        Text(
            text = story.name,
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
    val isPremium = story.price.compareTo(BigDecimal.ZERO) != 0
    Column(
        modifier = modifier
            .width(200.dp)
            .background(Color.Transparent, RoundedCornerShape(5.dp))
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // Story Image (on top)
        AsyncImage(
            model = story.coverImgUrl,
            contentDescription = "Story Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Story Details (below the image)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = story.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
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
                imageVector = Icons.Filled.Favorite,
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
    onClick: () -> Unit = {}
) {
    // Hàng 1: Ảnh bìa + Thông tin cơ bản
    Row(
        modifier = modifier
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                modifier = Modifier.fillMaxSize()
            )


        }

        // Thông tin chi tiết
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tiêu đề
            Text(
                text = story.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ngày cập nhật
            Text(
                buildAnnotatedString {
                    withStyle (
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    ) {
                        append("Last Updated: ")
                    }
                    withStyle (
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 10.sp,
                            color = OrangeRed
                        )
                    ) {
                        append(story.updateAt.toString())
                    }
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Các thể loại (Chips)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                story.categories.forEach { genre ->
                    Chip(text = genre.name)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    painter = painterResource(R.drawable.popular_icon),
                    contentDescription = "Votes",
                    tint = OrangeRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatViews(story.viewNum.toLong()),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }

}

// Định dạng số lượt xem (167800 -> 167.8K)
internal fun formatViews(views: Long): String {
    return when {
        views >= 1000000 -> "${views / 1000000}M"
        views >= 1000 -> "${views / 1000}K"
        else -> views.toString()
    }
}

//endregion

@Composable
fun AuthorInfoCard(model: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = model.avatarUrl, // URL của avatar
            contentDescription = "avatar",
            placeholder = painterResource(id = R.drawable.avt_img),
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // fill mode
        )
        Column {
            Text(
                text = model.name,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 7.dp)
            )
            Text(text = "@${model.dName}", color = Color.White, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = {}) {
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
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Phần avatar xếp chồng (lấy từ 3 truyện đầu tiên)
        Box(modifier = Modifier.size(110.dp,140.dp)) {
            item.stories.take(3).forEachIndexed { index, story ->
                AsyncImage(
                    model = story.coverImgUrl, // Lấy avatar từ coverImage của truyện
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
        }

        // Phần thông tin
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(
                    LocalContext.current)
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
                    modifier = modifier
                        .widthIn(max = halfWidth) // Giới hạn chiều rộng tối đa
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    OrangeRed,
                                    BurntCoral
                                )
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = item.description,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
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
            Image(
                painter = image,
                contentDescription = "select button icon",
                modifier = Modifier
                    .size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                fontSize = 25.sp,
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

