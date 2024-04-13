package com.elfaidy.areader.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.elfaidy.areader.R
import com.elfaidy.areader.components.BookCardItem
import com.elfaidy.areader.components.ReaderAppBar
import com.elfaidy.areader.model.MBook
import com.elfaidy.areader.navigation.ReaderScreens
import com.elfaidy.areader.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Home(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    Scaffold(
        topBar = {
                 ReaderAppBar(
                     title = stringResource(id = R.string.app_name),
                     navIcon = R.drawable.ic_launcher_foreground,
                     actionIcon = Icons.Filled.Logout,
                     onNavIconClick = { /*TODO*/ }
                 ) {
                     // action icon event
                     FirebaseAuth.getInstance().signOut()
                         .run {
                             navController.navigate(route = ReaderScreens.LoginScreen.name)
                         }
                 }
        },
        floatingActionButton = {
            FABContent{
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }
    ) {
        HomeContent(
            paddingValues = it,
            viewModel = viewModel,
            onCardClick = { bookItemId ->
                navController.navigate(ReaderScreens.UpdateScreen.name + "/$bookItemId")
            }
            ){
            // when click in profile image
            navController.navigate(route = ReaderScreens.StatsScreen.name)
        }
    }
}


@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
    onCardClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {

    val listOfBooks = viewModel.getBooksForCurrentUser()
    val readingNowBooks = listOfBooks.filter { book ->
        book.startedReading != null && book.finishedReading == null
    }
    val readingBooksList = listOfBooks.filter { book ->
        book.startedReading == null && book.finishedReading == null
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        val (readingNowRef, bookListRef,profileTitleRef, dividerRef) = createRefs()

        ProfileTitleInfo(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .constrainAs(profileTitleRef) {
                    top.linkTo(parent.top)
                },
            userName = viewModel.firstName.value,
           onProfileClick = onProfileClick
        )

        Divider(
            Modifier
                .constrainAs(dividerRef) {
                    top.linkTo(profileTitleRef.bottom)
                }
                .padding(horizontal = 10.dp)
        )

        ReadingRightNowArea(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(readingNowRef) {
                    top.linkTo(dividerRef.bottom)
                    bottom.linkTo(bookListRef.top)
                },
            viewModel = viewModel,
            books = readingNowBooks
        ) { onCardClick.invoke(it) }

        BookListArea(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 5.dp,
                    top = 30.dp,
                )
                .constrainAs(bookListRef) {
                    top.linkTo(readingNowRef.bottom)
                    bottom.linkTo(parent.bottom)
                },
            viewModel = viewModel,
            books = readingBooksList
        ) { onCardClick.invoke(it) }
    }

}

@Composable
fun ProfileTitleInfo(
    modifier: Modifier,
    userName: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TitleSection(
            label = """
                Your reading
                activity right now..
            """.trimIndent()
        )

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier
                    .clickable { onProfileClick() }
                    .size(45.dp),
            )

            Text(
                text = userName,
                maxLines = 1,
            )
        }
    }
}


@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Left
        )
    }
}


@Composable
fun ReadingRightNowArea(
    modifier: Modifier,
    books: List<MBook>,
    viewModel: MainViewModel,
    onCardClick: (String) -> Unit
) {
    Column(
        modifier = modifier
    ){
        if (viewModel.books.value.loading == true){
            Loading()
        }else{
            if (books.isNotEmpty()) {
                LazyRow {
                    items(books) { book ->
                        BookCardItem(book) { googelId ->
                            onCardClick.invoke(googelId)
                        }
                    }
                }
            } else {
                EmptyBooksCompose("No Reading Books yet")
            }
        }
    }
}

@Composable
fun BookListArea(
    modifier: Modifier,
    books: List<MBook>,
    viewModel: MainViewModel,
    onCardClicked: (String) -> Unit
) {

    Column(
        modifier = modifier
    ) {

        TitleSection(label = "Reading list")

        if (viewModel.books.value.loading == true){
            Spacer(modifier = Modifier.height(90.dp))
            Loading()
        }else{
            if (books.isNotEmpty()) {
                LazyRow{
                    items(books){ book ->
                        BookCardItem (book){ googelId ->
                            onCardClicked.invoke(googelId)
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(90.dp))
                EmptyBooksCompose("No Books yet, add a book")
            }
        }
    }
}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = onTap,
        shape = CircleShape,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add new book"
        )
    }
}


@Composable
fun EmptyBooksCompose(message: String) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = message)

    }
}


@Composable
fun Loading() {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LinearProgressIndicator()
    }
}


