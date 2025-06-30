package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.frontend.ui.components.TransactionCard
import com.example.frontend.ui.components.SelectList
import com.example.frontend.ui.theme.DeepBlue

@Composable
fun TransactionManagementScreen(viewModel: TransactionMgmtViewModel = hiltViewModel())
{
    val context = LocalContext.current
    val userId by viewModel.userId.collectAsState()
    val transactionId by viewModel.transactionId.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val selectedTransaction by viewModel.selectedTransaction.collectAsState()
    val listTypes by viewModel.listTypes.collectAsState()
    val statusTypes by viewModel.statusTypes.collectAsState()
    val transactions by viewModel.displayedTransactions.collectAsState()
    val showUpdateDialog = remember { mutableStateOf(false) }
    val showDenyDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    val toast by viewModel.toast.collectAsState()
    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
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
            text = "Find transactions",
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
        Column ( // find by userid
        )
        {
            Text(
                text = "Find by user ID",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                ),
            )
            TextField(
                value = userId,
                onValueChange = { viewModel.onUserNameChange(it) },
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
        Column ( // find by transactionid
        )
        {
            Text(
                text = "Find by transaction ID",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                ),
            )
            TextField(
                value = transactionId,
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
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
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
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                ),
            )
            SelectList(
                names = statusTypes,
                selectedName = selectedStatus,
                onNameSelected = { viewModel.onSelectStatus(it) }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp)
        )
        {//buttons
            Button(
                onClick = { showUpdateDialog.value = true },
                enabled = selectedTransaction != null && selectedTransaction!!.status == "pending",
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTransaction != null) Color.Green else Color.LightGray
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = "Approve",
                    color = if (selectedTransaction != null && selectedTransaction!!.status == "pending") DeepBlue else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { showDenyDialog.value = true },
                enabled = selectedTransaction != null && selectedTransaction!!.status == "pending",
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTransaction != null) Color.LightGray else Color(0xAFAF2238)
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = "Deny",
                    color = if (selectedTransaction != null && selectedTransaction!!.status == "pending") DeepBlue else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { showDeleteDialog.value = true },
                enabled = selectedTransaction != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTransaction != null) BurntCoral else Color(0xAFAF2238)
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = "Delete",
                    color = if (selectedTransaction != null) DeepBlue else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
        }

        if(showUpdateDialog.value)
        {
            AlertDialog(
                onDismissRequest = { showUpdateDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showUpdateDialog.value = false
                            viewModel.approveSelectedTransaction()
                        }
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUpdateDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Warning", color = Color.White)
                },
                text = {
                    Text("Are you sure to approve the transaction?", color = Color.LightGray)
                },
                containerColor = Color(0xFF1C1C1C)
            )
        }

        if(showDenyDialog.value)
        {
            AlertDialog(
                onDismissRequest = { showDenyDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDenyDialog.value = false
                            viewModel.denySelectedTransaction()
                        }
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDenyDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Warning", color = Color.White)
                },
                text = {
                    Text("Are you sure to deny the transaction?", color = Color.LightGray)
                },
                containerColor = Color(0xFF1C1C1C)
            )
        }

        if(showDeleteDialog.value)
        {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                            viewModel.deleteSelectedTransaction()
                        }
                    ) {
                        Text("Yes", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog.value = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                title = {
                    Text("Warning", color = Color.White)
                },
                text = {
                    Text("Are you sure you want to delete this transaction?", color = Color.LightGray)
                },
                containerColor = Color(0xFF1C1C1C)
            )
        }

        FlowColumn (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center
        )
        {
            if(transactions.isEmpty())
            {
                Text(
                    text = "No transactions found",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 20.dp)
                )
            }
            else
            {
                transactions.forEach { transaction ->
                    TransactionCard(
                        item = transaction,
                        isSelected = transaction == selectedTransaction,
                        onClick = {viewModel.onSelectTransaction(transaction)}
                    )
                }
            }
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun PreviewScreenContent() {
//    val fakeViewModel = TransactionMgmtViewModel (NavigationManager())
//    TransactionManagementScreen(fakeViewModel)
//}