package it.brendon.easycalc

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import it.brendon.easycalc.mathexp.*
import it.brendon.easycalc.ui.theme.EasyCalcTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyCalcTheme {
                Main()
            }
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
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerHeader()
            DrawerBody()
        },
        gesturesEnabled = drawerState.isOpen,
        content = {
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
                expr = evaluateExpression(x, expr, ::mult)
                expr = expr.derivative(x)

                AndroidView(
                    factory = {
                        WebView(it).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            loadData(getHtmlEquation(expr.latexString, colors), null, "UTF-8")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    )


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EasyCalcTheme {
        Main()
    }
}