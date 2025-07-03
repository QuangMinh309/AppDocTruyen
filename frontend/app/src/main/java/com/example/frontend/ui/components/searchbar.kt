package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.OrangeRed

@Preview
@Composable
fun SearchBar(
    value: String = "",
    onValueChange:(String)->Unit = {},
    cancelClick:()->Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){

        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .background(Color(0xA6747373), shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ){

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = OrangeRed,

                )

            val scrollState = rememberScrollState()
            LaunchedEffect(value) {
                scrollState.scrollTo(scrollState.maxValue)
            }
            BasicTextField(
                value =  value,
                onValueChange =  onValueChange,
                textStyle = TextStyle(
                    color = Color.Gray
                ),
                cursorBrush = SolidColor(Color.White),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                    innerTextField()
                }

            )
        }
        //cancel text button
        TextButton(onClick = {cancelClick()}) {
            Text(
                text = "Cancel",
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }

    }
}


@Preview
@Composable
fun SearchBarv2(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isSearching: Boolean = false,
    onSearchClick: () -> Unit = {},
    onCancel: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    // Quản lý focus
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(35.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(if (isSearching) 0.8f else 0.9f) // Thu ngắn khi tìm kiếm
                .fillMaxHeight()
                .background(Color(0xA6747373), shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp)
                .clickable {
                    onSearchClick()
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = OrangeRed
            )

            val scrollState = rememberScrollState()
            LaunchedEffect(value) {
                scrollState.scrollTo(scrollState.maxValue)
            }
            BasicTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    if (!isSearching) onSearchClick()
                },
                cursorBrush = SolidColor(Color.White),
                textStyle = TextStyle(color = Color.Gray),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && !isSearching) {
                            onSearchClick()
                        }
                    },
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                    innerTextField()
                }
            )
        }

        // Chuyển đổi giữa icon setting và nút Cancel
        if (isSearching) {
            Text(
                text = "Cancel",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable {
                        onCancel()
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                    .padding(horizontal = 8.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.setting_config),
                contentDescription = "Custom Icon",
                tint = Color.White,
                modifier = Modifier
                    .weight(0.33f)
                    .size(24.dp)
                    .wrapContentWidth(Alignment.End)
                    .clickable { onSettingClick() }
            )
        }
    }
}