package it.brendon.easycalc

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red


val Color.htmlString: String
    get() {
        val builder = StringBuilder()
        builder.apply {
            append(toArgb().red.toString(16))
            append(toArgb().green.toString(16))
            append(toArgb().blue.toString(16))
        }
        return builder.toString()
    }

fun getHtmlEquation(
    latexEquation: String,
    colors: Colors
): String {
    val htmlPage = """<!DOCTYPE html>
<html>
   <head>
     <script type="text/javascript" id="MathJax-script" async
       src="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js">
     </script>
     <script type="text/x-mathjax-config">
       MathJax.Hub.Config({ TeX: { extensions: ["color.js"] }});
     </script>

  </head>
  <body style="background-color:rgb(${colors.background.toArgb().red},${colors.background.toArgb().green},${colors.background.toArgb().blue});">
    <p>
        ${'$'}${'$'}
        \definecolor{${colors.onBackground.htmlString}}{RGB}{${colors.onBackground.toArgb().red},${colors.onBackground.toArgb().green},${colors.onBackground.toArgb().blue}}
        \color{${colors.onBackground.htmlString}} $latexEquation 
        ${'$'}${'$'}
    </p>
  </body>
</html>""".trimIndent()

    return htmlPage
}