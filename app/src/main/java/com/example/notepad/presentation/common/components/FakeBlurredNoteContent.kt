package com.example.notepad.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

/** Displays blurred text of random length.
 * @param modifier composable modifier */
@Composable
fun FakeBlurredNoteContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        // fake note text
        val fakeTextContent = remember {
            """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam fermentum mauris nec mauris pretium interdum. Vivamus ultrices porttitor molestie. Phasellus consectetur sagittis neque in pretium. Vivamus id mi eget diam fringilla gravida. Curabitur interdum sem hendrerit rhoncus aliquet. Nam eleifend vestibulum magna vel faucibus. Donec varius tellus in mi malesuada rhoncus. Suspendisse facilisis pretium tincidunt. Nunc euismod fringilla massa vel convallis. Nullam venenatis nunc sed risus vulputate, vitae ornare nisl lobortis. Nullam magna ante, tincidunt vitae vulputate id, egestas nec nunc.

                Donec eu felis rutrum justo rhoncus ornare. Fusce vestibulum diam ut dolor tristique, sed porttitor nibh convallis. Donec nec libero elit. Fusce iaculis felis vel lectus efficitur imperdiet. Nunc facilisis vestibulum leo. Nulla ultricies lacus eu imperdiet sagittis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus at pretium orci.

                Sed quis orci nunc. Pellentesque a cursus ligula. Phasellus tristique mollis nisl, eu viverra nulla porta ut. Cras tincidunt mollis lectus eu facilisis. Cras varius ligula nunc, sed semper ligula varius ac. Cras urna elit, tincidunt eu lobortis non, tempus eu dui. Donec dapibus molestie metus. Nam quis nisi malesuada sapien accumsan lacinia. Fusce convallis feugiat dictum. Nulla venenatis justo sed mattis feugiat.

                Pellentesque pharetra bibendum erat id bibendum. Donec a arcu sed ante tempus congue. Duis dictum efficitur commodo. Duis at purus eget tortor tempus pretium at at odio. Vestibulum ante neque, efficitur a eros in, laoreet molestie urna. Aenean leo neque, mollis non ante a, efficitur tincidunt sapien. Quisque lobortis turpis sit amet magna placerat ullamcorper. Fusce at diam et lectus tempus convallis. Morbi a tincidunt lectus. Phasellus maximus faucibus ex vitae condimentum. Nullam et augue eget lorem venenatis ultrices a egestas sem. Nunc tristique leo sed lacus porttitor elementum.

                Proin erat augue, gravida faucibus lectus sit amet, venenatis condimentum tortor. Suspendisse porttitor diam at ipsum faucibus facilisis. Aliquam sodales condimentum mauris, vitae lobortis enim blandit in. Praesent vehicula tellus sit amet felis finibus aliquet. Sed consectetur laoreet justo et vulputate. Proin nulla mauris, tempus vitae tincidunt at, gravida a lacus. Sed diam erat, scelerisque eu purus quis, dictum efficitur nisl. Fusce sed enim ut eros commodo condimentum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Quisque eu tristique risus. Nam ornare in justo eget efficitur. Proin porttitor ipsum quis ante dictum, vitae condimentum eros hendrerit. Nunc tortor metus, accumsan in aliquam sed, tincidunt id nunc. Vivamus eu mauris vel neque dapibus pretium. Vivamus aliquet nisi vel enim congue dignissim. Phasellus eleifend nisl justo, ac molestie libero consectetur non.
            """.trimIndent()
        }

        Text(
            text = fakeTextContent.take((100..fakeTextContent.length).random()),
            modifier = Modifier.blur(10.dp)
        )
    }
}