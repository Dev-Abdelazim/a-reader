package com.elfaidy.areader.ui.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.elfaidy.areader.R
import com.elfaidy.areader.components.LoadingProgress
import com.elfaidy.areader.components.ReaderAppBar
import com.elfaidy.areader.data.Resource
import com.elfaidy.areader.model.Item
import com.elfaidy.areader.model.MBook
import com.elfaidy.areader.model.VolumeInfo
import com.elfaidy.areader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String?,
    viewModel: BookDetailsViewModel
) {
    viewModel.getABookById(bookId!!)

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                navIcon = R.drawable.baseline_arrow_back_24 ,
                onNavIconClick = {

                    navController.popBackStack()
                }
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
            )
        ){
            item{
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    BookDetailsContent(viewModel.book){
                        // when click in save button
                        val book = viewModel.book?.data?.volumeInfo
                        if (book != null){
                            val newBook = MBook(
                                title = book.title,
                                authors =book.authors.toString(),
                                notes = "",
                                imageUrl = book.imageLinks?.thumbnail,
                                categories = "${book.categories}",
                                publishedDate = book.publishedDate,
                                rating = 0.0,
                                description = book.description,
                                pageCount = book.pageCount.toString(),
                                userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                googleBookId = bookId
                            )

                            viewModel.addABook(newBook){
                                // when save book success
                                navController.navigate(ReaderScreens.HomeScreen.name)
                            }
                        }
                    }
                    if (viewModel.loading){
                        LoadingProgress("Saving..")
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetailsContent(
    book: Resource<Item>?,
    onSaveButtonClick: () -> Unit
) {
    Column(
       modifier = Modifier
           .fillMaxSize(),
       horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val mBook = book?.data?.volumeInfo
        Card(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            shape = CircleShape
        ) {
            if (mBook?.imageLinks != null){
                Image(
                    painter = rememberAsyncImagePainter(
                        mBook.imageLinks.smallThumbnail
                    ),
                    contentDescription = "book image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        BookInfo(mBook)

        BookDescription(mBook)

        BookDetailsButton("Save", onSaveButtonClick)

    }
}

@Composable
private fun BookInfo(book: VolumeInfo?) {
    if (book != null){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Author: " + book.authors,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Page Count: " + book.pageCount,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Categories: " + book.categories,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Published: " + book.publishedDate,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun BookDescription(book: VolumeInfo? = null) {
    if (book != null){
        Surface(
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)

        ) {

            val clearDescription = HtmlCompat.fromHtml(
                book.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            LazyColumn(
                Modifier.padding(10.dp)
            ){
                item {
                    Text(
                        text =  clearDescription,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}


@Composable
fun BookDetailsButton(
    text: String ,
    onButtonClick: () -> Unit
) {
    Button(
        onClick = onButtonClick,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            bottomEnd = 20.dp,
            topStart = 20.dp,
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp)
        )
    }
}