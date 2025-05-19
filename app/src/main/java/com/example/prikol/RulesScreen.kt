package com.example.prikol

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RulesScreen(
    navigateHome: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp, 0.dp, 15.dp, 15.dp)
        ) {
            Text(
                text = "RULES AND NOTES",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        navigateHome()
                    }
                    .padding(15.dp)
            )
            Text(
                text =  """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi at ante non tortor pretium lobortis. Quisque nulla urna, egestas ac urna et, egestas ultrices nisi. Proin imperdiet fermentum nulla vel fringilla. Quisque consectetur varius bibendum. Etiam efficitur vitae leo quis condimentum. Curabitur sagittis suscipit cursus. Curabitur nec neque sit amet mauris malesuada malesuada. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut tincidunt dui tortor, eget pellentesque elit gravida ornare.
                    Morbi eget neque quis neque posuere viverra eget in lorem. Proin aliquet, justo at interdum maximus, lacus neque pulvinar mauris, ut congue tortor nibh ut eros. Pellentesque ac arcu viverra, varius enim nec, tincidunt leo. Etiam sed iaculis metus. Pellentesque a purus eu tellus efficitur finibus. Etiam finibus velit at dui dignissim, quis feugiat ante egestas. Vestibulum dictum pellentesque erat, et bibendum nisi finibus vitae. Interdum et malesuada fames ac ante ipsum primis in faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ac rhoncus leo. Nullam vitae metus ullamcorper lacus auctor venenatis vitae sed dolor. Quisque maximus arcu lorem, nec venenatis ex tincidunt vel.
                    """,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}