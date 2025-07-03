package com.example.frontend.ui.screen.admin

import com.example.frontend.presentation.viewmodel.admin.RevenueViewModel

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.ui.components.ScreenFrame
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.ReemKufifunFontFamily
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.frontend.ui.components.MonthYearPicker
import com.example.frontend.ui.components.RevenueTable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueManagementScreen(viewModel: RevenueViewModel = hiltViewModel())
{
    val context = LocalContext.current
    val revenueData by viewModel.revenueData.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
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
                    text = "Revenue Report",
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
        },
    )
    {
        Text(
            text = "Daily  Earnings",
            color = Color.White,
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        MonthYearPicker(
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            onMonthChange = { viewModel.onUpdateMonth(it) },
            onYearChange = { viewModel.onUpdateYear(it) }
        )
        Button(
            modifier = Modifier.padding(start = 16.dp),
            onClick = {
                viewModel.fetchData()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeRed
            )
        ) {
        Text("Select month", color = DeepSpace, fontFamily = ReemKufifunFontFamily, fontSize = 16.sp, fontWeight = FontWeight.Normal)
    }
        RevenueTable(data = revenueData)
    }
}