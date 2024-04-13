@file:OptIn(ExperimentalComposeUiApi::class)

package com.elfaidy.areader.ui.screens.update

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.elfaidy.areader.R
import com.elfaidy.areader.components.InputTextField
import com.elfaidy.areader.components.ReaderAppBar
import com.elfaidy.areader.model.MBook
import com.elfaidy.areader.navigation.ReaderScreens
import com.elfaidy.areader.viewmodel.MainViewModel
import com.elfaidy.areader.utils.Constants

@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    bookId: String
) {

    val book = viewModel.getBooksForCurrentUser().firstOrNull { bookId == it.googleBookId }

    if (book != null){
        book.rating.let { viewModel.ratingState.value = it!!.toInt() }
        viewModel.note.value = book.notes
    }

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                navIcon = R.drawable.baseline_arrow_back_24,
                onNavIconClick = {
                    navController.popBackStack()
                }
            )
        }
    ){
        UpdateScreenContent(
            paddingValues = it,
            note = viewModel.note,
            book = book,
            vm = viewModel,
            navController = navController
        )
    }

}

@Composable
fun UpdateScreenContent(
    paddingValues: PaddingValues,
    note: MutableState<String?>,
    book: MBook?,
    vm: MainViewModel,
    navController: NavHostController,
) {
   Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       val isOpen = remember { mutableStateOf(false) }

       if (book != null){

           if (isOpen.value){
               DeleteAlertDialog(
                   isOpen = isOpen,
                   title = "Delete Book",
                   message = """
                       Are you sure you want to delete this book,
                       this action is not reversible
                   """.trimIndent()
               ) {
                   // delete book
                   vm.deleteBook(book.id!!)
                   navController.navigate(ReaderScreens.HomeScreen.name)
                   isOpen.value = false
               }
           }



           BookCardInfo(book)

           AddNoteField(
               note = note.value ?: "No Thoughts available :("
           ){
               note.value = it
           }


           StartAndFinishButtons(
               isStartReading = vm.isStartReading.value,
               isFinished = vm.isFinish.value,
               book = book,
               onStartClick = { vm.isStartReading.value = true},
               onFinishClick = {vm.isFinish.value = true}
           )

           RatingBar(vm = vm){
               vm.rating.value = it
           }

           Row(
               Modifier
                   .fillMaxWidth()
                   .padding(20.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceEvenly
           ){
               RoundedButton(label = "Update") {
                   if (vm.checkUpdateBook(book)){
                       vm.updateBook(book)
                       navController.navigate(ReaderScreens.HomeScreen.name)
                   }
               }
               RoundedButton(label = "Delete") {
                   isOpen.value = true
               }
           }
       }
    }
}

@Composable
fun BookCardInfo(book: MBook){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = book.imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(130.dp)
                    .height(150.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(topStart = 120.dp, topEnd = 20.dp)
                    )
            )

            Column {

                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = book.authors.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                )

                Text(
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                )
            }
        }
    }
}


@Composable
fun AddNoteField(
    note: String,
    onNoteChange: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    InputTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            ),
        value = note,
        labelId = "Enter your thoughts",
        enabled = true,
        isSingleLine = false,
        onTextChange = onNoteChange,
        onAction = KeyboardActions{
            if (note.trim().isEmpty()) return@KeyboardActions
            onNoteChange.invoke(note.trim())
            keyboardController?.hide()
        }
    )
}


@Composable
fun StartAndFinishButtons(
    isStartReading: Boolean,
    isFinished: Boolean,
    book: MBook,
    onStartClick: () -> Unit,
    onFinishClick: () -> Unit,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        TextButton(
            onClick = onStartClick,
            enabled = book.startedReading == null
        ) {
            if (book.startedReading == null){
                Text(
                    text = if (!isStartReading) {
                        "Start Reading"
                    } else {
                        "Started Reading!"
                    },
                    color = if (isStartReading) MaterialTheme.colorScheme.primary.copy(0.5f) else MaterialTheme.colorScheme.onBackground,
                    modifier = if (isStartReading) Modifier.alpha(0.6f) else Modifier.alpha(1f),
                    fontSize = 18.sp
                )
            }else{
                Text(text = "Started on: ${Constants.formatDate(book.startedReading!!)}")
            }
        }

        TextButton(
            onClick = onFinishClick,
            enabled = book.finishedReading == null
        ) {
            if (book.finishedReading == null){
                Text(
                    text = if (!isFinished) {
                        "Make as Read"
                    } else {
                        "Finished Reading!"
                    },
                    fontSize = 18.sp
                )
            }else{
                Text(text = "Finished on: ${Constants.formatDate(book.finishedReading!!)}")
            }
        }

    }
}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    vm: MainViewModel,
    onPressRating: (Int) -> Unit
) {

    Column(
        Modifier.padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Rating", style = MaterialTheme.typography.titleMedium)

        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            val size = animateDpAsState(
                targetValue = if (vm.isSelected.value) 42.dp else 34.dp,
                spring(Spring.DampingRatioMediumBouncy),
                label = ""
            ).value

            for (i in 1..5){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_rate_24) ,
                    contentDescription = null,
                    modifier = modifier
                        .width(size)
                        .height(size)
                        .pointerInteropFilter {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    vm.isSelected.value = true
                                    onPressRating.invoke(i)
                                    vm.ratingState.value = i
                                }

                                MotionEvent.ACTION_UP -> {
                                    vm.isSelected.value = false
                                }
                            }
                            true
                        },
                    tint = if (i <= vm.ratingState.value) Color.Yellow else Color.LightGray
                )
            }
        }
    }
}


@Composable
fun RoundedButton(
    label: String,
    onPress: () -> Unit
) {
    Button(
        onClick = onPress,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomEnd = 28.dp,
                    topStart = 28.dp,
                )
            )
            .padding(0.dp),
        shape = RectangleShape
    ) {

        Text(
            text = label
        )
    }
}


@Composable
fun DeleteAlertDialog(
    isOpen: MutableState<Boolean>,
    title: String,
    message: String,
    onDeleteClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { isOpen.value = false },
        confirmButton = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = onDeleteClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(text = "Yes")
                }

                Button(
                    onClick = {isOpen.value = false},
                ) {
                    Text(text = "No")
                }
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },

        )

}