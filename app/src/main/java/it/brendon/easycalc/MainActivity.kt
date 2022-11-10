package it.brendon.easycalc

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import it.brendon.easycalc.mathexp.*
import it.brendon.easycalc.model.MainActivityViewModel
import it.brendon.easycalc.ui.theme.EasyCalcTheme
import it.brendon.easycalc.ui.theme.Typography
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyCalcTheme {
                Main()
            }
        }
    }

    @Composable
    fun Main() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

        }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerHeader()
                DrawerBody()
            },
            gesturesEnabled = drawerState.isOpen,
            content = {
                ModalContent(drawerState)
            }
        )
    }

    @Composable
    fun DrawerHeader() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Header", style = Typography.h1)
        }
    }

    @Composable
    fun DrawerBody() {

    }

    @Composable
    fun ModalContent(drawerState: DrawerState) {
        val scope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("EasyCalc") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                },
            )

            val colors = MaterialTheme.colors

            val x = Value("x")
            var expr = evaluateExpression(x, Const(1.0), ::sum)
            expr = evaluateExpression(expr, x, ::mult)
            expr = evaluateExpression(expr, Const(2.0), ::exp)
            expr = expr.derivative(x)
            expr = expr.eval()

            AndroidView(
                factory = {
                    WebView(it).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadData(getHtmlEquation(expr.latexString, colors), null, Charsets.UTF_8.toString())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        EasyCalcTheme {
            Main()
        }
    }
}



