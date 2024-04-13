
package com.elfaidy.areader.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.elfaidy.areader.components.InputTextField
import com.elfaidy.areader.components.PasswordInputTextField
import com.elfaidy.areader.components.ReaderLogo
import com.elfaidy.areader.navigation.ReaderScreens

@Composable
fun ReaderLoginScreen(
    navController: NavHostController,
    vm: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ReaderLogo()

                Spacer(modifier = Modifier.height(25.dp))

                UserForm(
                    vm = vm,
                    isInSignUpMode = vm.isInSignUpMode.value,
                    onSignUnClicked =  { email, pass ->
                        vm.isInSignUpMode.value = true
                        vm.createUserWithEmailAndPassword(email, pass){
                            navController.navigate(route = ReaderScreens.HomeScreen.name)
                        }
                    },
                    onSingInTextClick =  { vm.isInSignUpMode.value = false },
                    onForgottenPassTextClick =  {},
                ){ email, password ->
                    vm.signInWithEmailAndPassword(email,password){
                        navController.navigate(route = ReaderScreens.HomeScreen.name)
                    }
                }
            }
        }
    }
}

@Composable
fun UserForm(
    vm: LoginScreenViewModel,
    isInSignUpMode: Boolean = false,
    onSignUnClicked: (String, String) -> Unit,
    onSingInTextClick: () -> Unit,
    onForgottenPassTextClick: () -> Unit,
    onSignInDone: (String, String) -> Unit = { email, pass -> }
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ){

        UserNameRow(
            vm = vm
        )

        InputTextField(
            value = vm.email,
            labelId = "Email address",
            enabled = true,
            keyboardType = KeyboardType.Email,
            onTextChange = {vm.email = it},
            onAction = KeyboardActions{
                vm.passwordFocusRequest.requestFocus()
            }
        )

        PasswordInputTextField(
            value = vm.password,
            modifier = Modifier
                .focusRequester(vm.passwordFocusRequest),
            labelId = "Password",
            enabled = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            passwordVisible = vm.passwordVisibility,
            onAction = KeyboardActions{
                if (!vm.validInput) return@KeyboardActions
                onSignInDone(vm.email.trim(), vm.password.trim())
            },
            onTextChange = {vm.password = it}
        )



        if (!vm.isInSignUpMode.value){

            TextButton(onClick = onForgottenPassTextClick) {
                Text(
                    text = "Forgotten password?",
                    modifier = Modifier
                        .padding(
                            bottom = 25.dp
                        )
                )
            }


            Button(
                onClick = { onSignInDone.invoke(vm.email, vm.password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )

            ) {
                Text(
                    text = "Sign in",
                    modifier = Modifier
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Button(
            onClick = { onSignUnClicked.invoke(vm.email, vm.password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 25.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            )

        ) {
            Text(
                text = "Sign up",
                modifier = Modifier
                    .padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        if (isInSignUpMode){
            UserHaseAAccount(onSingInTextClick = onSingInTextClick)
        }
    }
}


@Composable
fun UserNameRow(
    vm: LoginScreenViewModel
) {
    if (vm.isInSignUpMode.value){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            InputTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = vm.firstName,
                labelId = "First name",
                enabled = true,
            ){ vm.firstName = it }

            InputTextField(
                modifier = Modifier
                    .fillMaxWidth(1f),
                value = vm.lastName,
                labelId = "Last name",
                enabled = true,
            ){ vm.lastName = it }
        }
    }
}


@Composable
fun UserHaseAAccount(
    onSingInTextClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account?"
        )

        TextButton(
            onClick = onSingInTextClick,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = "Sign in",
                fontWeight = FontWeight.Bold
            )
        }
    }
}