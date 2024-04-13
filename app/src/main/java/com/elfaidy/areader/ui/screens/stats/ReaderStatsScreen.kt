package com.elfaidy.areader.ui.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.elfaidy.areader.R
import com.elfaidy.areader.components.ReaderAppBar
import com.elfaidy.areader.model.Item
import com.elfaidy.areader.model.MBook
import com.elfaidy.areader.viewmodel.MainViewModel
import dagger.Provides

@Composable
fun ReaderStatsScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    mainViewModel.readingNowList.value = mainViewModel.getBooksForCurrentUser().filter {
        it.startedReading != null && it.finishedReading == null
    }
    mainViewModel.haveReadList.value = mainViewModel.getBooksForCurrentUser().filter {
        it.startedReading != null && it.finishedReading != null
    }

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
    ){
        StatsScreenContent(
            paddingValue = it,
            viewModel = mainViewModel
        )
    }
}

@Composable
fun StatsScreenContent(paddingValue: PaddingValues, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        UserImageAndName(viewModel.firstName.value)
        UserStatsInfo(
            readingBooks = viewModel.readingNowList,
            finishReadingBooks = viewModel.haveReadList
        )
        
        Text(text = "Read books", modifier = Modifier.padding(top = 20.dp))

        Divider(Modifier.padding(horizontal = 16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ){
            items(viewModel.haveReadList.value){ book ->
                BookStatsItem(book)
            }
        }
    }
}

@Composable
fun UserImageAndName(
    userName: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Sharp.Person,
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Hi, $userName",
            fontWeight = FontWeight.SemiBold
        )
    }
}



@Composable
fun UserStatsInfo(
    readingBooks: MutableState<List<MBook>>,
    finishReadingBooks: MutableState<List<MBook>> ,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(
                text = "Your Stats",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )

            Divider(modifier = Modifier.padding(8.dp))

            Text(
                text = "You're reading: ${readingBooks.value.size} books",
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "You've read: ${finishReadingBooks.value.size} books",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
fun BookStatsItem(
    book: MBook
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val imageUri = if (book.imageUrl!= null){
                book.imageUrl
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

            BookStatsInfo(book)
        }
    }
}

@Composable
fun BookStatsInfo(book: MBook) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
    ){
        Text(
            text = book.title.toString(),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "${book.authors}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Date: ${book.publishedDate}",
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = "${book.categories}",
            style = MaterialTheme.typography.labelSmall
        )

    }
}