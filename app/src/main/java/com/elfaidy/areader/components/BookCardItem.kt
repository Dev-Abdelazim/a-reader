package com.elfaidy.areader.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.elfaidy.areader.model.MBook


@Composable
fun BookCardItem(
    book: MBook,
    onCardClick: (String) -> Unit
) {
    val context = LocalContext.current
    val resource = context.resources
    val displayMetrics = resource.displayMetrics
    val width = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    val isStarted = remember {
        mutableStateOf(book.startedReading != null && book.finishedReading == null)
    }

    Card(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
            )
            .width(202.dp)
            .height(242.dp)
            .clickable { onCardClick(book.googleBookId.toString()) },
        shape = RoundedCornerShape(
            29.dp
        ),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {

        Column(
            modifier = Modifier
                .width(width.dp - (spacing * 2))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Image(
                    painter = rememberAsyncImagePainter(
                        model  = book.imageUrl),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                )

                BookFavoriteAndRate(book)
            }

            BookNameAndAuthor(
                bookTitle = book.title.toString(),
                author = book.authors.toString()
            )
        }

        val label = if (isStarted.value) "Reading" else "Not Started"
        RoundedButton(label) {

        }
    }
}


@Composable
fun BookFavoriteAndRate(book: MBook) {

    Column(
        modifier = Modifier
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Icon(
            imageVector = Icons.Filled.FavoriteBorder,
            contentDescription = "fav icon"
        )

        BookRate(book.rating ?: 0.0)
    }
}


@Composable
fun BookRate(rate: Double) {
    Surface(
        modifier = Modifier.padding(top = 10.dp),
        shape = RoundedCornerShape(56.dp),
        shadowElevation =6.dp
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val icon = if (rate == 0.0) Icons.Filled.StarBorder else Icons.Filled.Star

            Icon(
                imageVector = icon,
                contentDescription = "rate icon",
            )

            Text(
                text = "$rate",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun BookNameAndAuthor(
    bookTitle: String,
    author: String
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = bookTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = author,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}



