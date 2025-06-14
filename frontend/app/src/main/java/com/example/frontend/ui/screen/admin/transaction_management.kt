package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
import com.example.frontend.presentation.viewmodel.admin.TransactionMgmtViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.theme.BurntCoral
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.frontend.data.model.Transaction
import com.example.frontend.ui.components.SelectChip

@Composable
fun TransactionManagementScreen(viewModel: TransactionMgmtViewModel = hiltViewModel())
{
    val tbUserNameValue by viewModel.userName.collectAsState()
    val tbTransactionIdValue by viewModel.transactionId.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val listTypes by viewModel.listTypes.collectAsState()
    val statusTypes by viewModel.statusTypes.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
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
                    text = "Transaction",
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
            text = "Sort transactions",
            color = Color.White,
            style = TextStyle(
                fontSize = 33.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column ( // find by username
        )
        {
            Text(
                text = "Find by username",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            TextField(
                value = tbUserNameValue,
                onValueChange = { viewModel.onUserNameChange(it) },
                singleLine = true,
                placeholder = {
                    Text("Enter username", style = TextStyle(color = Color.Gray))
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
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column ( // find by id
        )
        {
            Text(
                text = "Find by transaction ID",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            TextField(
                value = tbTransactionIdValue.toString(),
                onValueChange = { viewModel.onTransactionIdChange(it) },
                singleLine = true,
                placeholder = {
                    Text("Enter ID", style = TextStyle(color = Color.Gray))
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
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column ( // sort by type
        )
        {
            Text(
                text = "Sort by type",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            SelectList(
                names = listTypes,
                selectedName = selectedType,
                onNameSelected = { viewModel.onSelectType(it) }
            )

        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column ( // sort by status
        )
        {
            Text(
                text = "Sort by status",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            SelectList(
                names = statusTypes,
                selectedName = selectedStatus,
                onNameSelected = { viewModel.onSelectStatus(it) }
            )
        }
        Text(
            text = "Results",
            color = Color.White,
            style = TextStyle(
                fontSize = 33.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
        {
            TransactionCard(transactions[0])
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    val fakeViewModel = TransactionMgmtViewModel (NavigationManager())
    TransactionManagementScreen(fakeViewModel)
}
@Composable
fun SelectList(
    names: List<String>,
    selectedName: String?,
    onNameSelected: (String) -> Unit
)
{
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
    {
        names.forEach { name ->
            SelectChip(
                name = name,
                isSelected = name == selectedName,
                onClick = { onNameSelected(name) }
            )
        }
    }
}

@Composable
fun TransactionCard(item : Transaction)
{
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.DarkGray, RoundedCornerShape(10.dp)),
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
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Time: " + item.time.toString(),
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(IntrinsicSize.Min)
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.avt_img),
                    contentScale = ContentScale.Crop,
                    contentDescription = "pfp",
                    modifier = Modifier
                        .size(50.dp, 50.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    Row {
                        Text(
                            text = "User ID: " + item.userId.toString(),
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                    }
                    Row {
                        Text(
                            text = "Type: " + item.type,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
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
                    Row {
                        Text(
                            text = "Money: ",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                        Text(
                            text = item.money.toString() + "đ",
                            color = if(item.type == "withdraw") Color.Red else Color.Green,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                    }
                    Row {
                        Text(
                            text = "Status: " + item.status,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                    }
                }
            }
        }
    }
}
