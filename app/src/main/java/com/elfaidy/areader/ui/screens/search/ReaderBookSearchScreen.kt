@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.elfaidy.areader.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.elfaidy.areader.R
import com.elfaidy.areader.components.InputTextField
import com.elfaidy.areader.components.ReaderAppBar
import com.elfaidy.areader.model.Item
import com.elfaidy.areader.navigation.ReaderScreens

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: BookSearchViewModel
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search books",
                navIcon = R.drawable.baseline_arrow_back_24,
                onNavIconClick = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        SearchContent(
            paddingValues = it,
            viewModel = viewModel,
            onCardClick = {
                navController.navigate(
                    ReaderScreens.DetailsScreen.name + "/$it"
                )
            }
        ){ query ->
            viewModel.searchBooks(query)
        }
    }
}

@Composable
fun SearchContent(
    paddingValues: PaddingValues,
    viewModel: BookSearchViewModel,
    onCardClick: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        InputTextField(
            modifier = Modifier.padding(16.dp),
            value = viewModel.search.value,
            labelId = "Search",
            enabled = true,
            isSingleLine = true,
            onAction = KeyboardActions{
                if (viewModel.search.value.isEmpty()) return@KeyboardActions
                onSearch.invoke(viewModel.search.value.trim())
                viewModel.search.value = ""
                keyboardController?.hide()
            },
            onTextChange = { viewModel.search.value = it }
        )

        Divider(Modifier.padding(horizontal = 10.dp))

        if (viewModel.isLoading){
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ){
                items(viewModel.books){book ->
                    SearchBookItem(book = book){
                            onCardClick.invoke(it)
                    }
                }
            }
        }


    }
}


@Composable
fun SearchBookItem(
    book: Item,
    onCardClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onCardClick.invoke(book.id) },
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val imageUri = if (book.volumeInfo.imageLinks != null){
                book.volumeInfo.imageLinks.smallThumbnail
            }else{ "" }

            Image(
                painter = rememberAsyncImagePainter(
                    imageUri
                ),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp)
                    .padding(5.dp),
                contentScale = ContentScale.Fit
            )

            BookInfo(book)
        }
    }
}

@Composable
fun BookInfo(book: Item) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
    ){
        Text(
            text = book.volumeInfo.title,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "${book.volumeInfo.authors}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Date: ${book.volumeInfo.publishedDate}",
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = "${book.volumeInfo.categories}",
            style = MaterialTheme.typography.labelSmall
        )

    }
}
