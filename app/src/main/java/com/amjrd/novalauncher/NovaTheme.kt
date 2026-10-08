package com.amjrd.novalauncher
import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
@Composable fun NovaTheme(content:@Composable()->Unit){val c=LocalContext.current;val scheme=if(Build.VERSION.SDK_INT>=31)dynamicLightColorScheme(c)else lightColorScheme();MaterialTheme(colorScheme=scheme,typography=Typography(),content=content)}