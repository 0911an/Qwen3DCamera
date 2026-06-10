package com.example.qwen3dcamera

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Qwen3DCameraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFD9F3FD)
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun Qwen3DCameraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFD9F3FD),
            background = Color(0xFFD9F3FD),
            surface = Color.White
        )
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var resultImageUrl by remember { mutableStateOf<String?>(null) }
    
    // 相机参数
    var azimuth by remember { mutableStateOf(0f) }
    var elevation by remember { mutableStateOf(15f) }
    var distance by remember { mutableStateOf(7f) }
    var fov by remember { mutableStateOf(50f) }
    
    // 图片选择器 - 关键修复！
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9F3FD))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部标题
        Text(
            text = "3D 相机生成器",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        // 上传图片区域 - 使用 Button 确保点击有效
        Button(
            onClick = { 
                // 点击打开图片选择器
                imagePickerLauncher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize().padding(8.dp).clip(RoundedCornerShape(8.dp))
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("📷", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击上传图片",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 2D 轨道控制器 - 确保 4 个滑块都显示
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🎮 2D 轨道控制器",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 方位角滑块
                Text("方位角 (Azimuth): ${"%.1f".format(azimuth)}°", fontSize = 14.sp)
                Slider(
                    value = azimuth,
                    onValueChange = { azimuth = it },
                    valueRange = -180f..180f,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 仰角滑块
                Text("仰角 (Elevation): ${"%.1f".format(elevation)}°", fontSize = 14.sp)
                Slider(
                    value = elevation,
                    onValueChange = { elevation = it },
                    valueRange = -90f..90f,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 距离滑块
                Text("距离 (Distance): ${"%.1f".format(distance)}", fontSize = 14.sp)
                Slider(
                    value = distance,
                    onValueChange = { distance = it },
                    valueRange = 1f..20f,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 视野滑块
                Text("视野 (FOV): ${"%.1f".format(fov)}°", fontSize = 14.sp)
                Slider(
                    value = fov,
                    onValueChange = { fov = it },
                    valueRange = 10f..120f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 生成按钮
        Button(
            onClick = {
                if (selectedImageUri != null) {
                    isGenerating = true
                    // TODO: 调用 API 生成图片
                    // 这里需要实现 API 调用逻辑
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB))
        ) {
            if (isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("🚀 生成新视角图片", fontSize = 18.sp, color = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 结果显示区域
        if (resultImageUrl != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "生成结果",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    AsyncImage(
                        model = resultImageUrl,
                        contentDescription = "Result Image",
                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp))
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { /* TODO: 下载图片 */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
                        ) {
                            Text("💾 下载", color = Color.White)
                        }
                        
                        Button(
                            onClick = { /* TODO: 分享图片 */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9B59B6))
                        ) {
                            Text("📤 分享", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
