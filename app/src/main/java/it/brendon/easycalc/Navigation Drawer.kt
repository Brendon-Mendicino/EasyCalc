package it.brendon.easycalc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.brendon.easycalc.ui.theme.Typography

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 65.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Header", style = Typography.h1)
    }
}

@Composable
fun DrawerBody() {

}