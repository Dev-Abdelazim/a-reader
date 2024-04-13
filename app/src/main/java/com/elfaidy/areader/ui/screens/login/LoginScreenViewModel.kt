package com.elfaidy.areader.ui.screens.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfaidy.areader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth  = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    val isInSignUpMode = mutableStateOf(false)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by  mutableStateOf("")
    val passwordVisibility =  mutableStateOf(false)
    val passwordFocusRequest = FocusRequester.Default
    //val keyboardController = LocalSoftwareKeyboardController.current
    val validInput = email.trim().isNotEmpty() && password.trim().isNotEmpty()


    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            onSuccess()
                        }else{
                            Log.d("FB","signInWithEmailAndPassword: ${task.result}")
                        }
                    }

            }catch (e: Exception){
                Log.d("FB", "signInWithEmailAndPassword: ${e.message}")
            }
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            if (_loading.value == false){
                _loading.value = true
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                createNewUser(firstName, lastName, email)
                                onSuccess()
                            } else {
                                Log.d("FB", "createUserWithEmailAndPassword: ${task.result}")
                            }
                        }
                    _loading.value = false

                } catch (e: Exception) {
                    Log.d("FB", "createUserWithEmailAndPassword: ${e.message}")
                    _loading.value = false
                }
            }
        }
    }

    private fun createNewUser(
        firstName: String,
        lastName: String,
        email: String
    ){
        val userId = auth.currentUser?.uid

        val user = MUser(
            id = null,
            userId = userId.toString(),
            firstName = firstName,
            lastName = lastName,
            email = email,
            avatarUrl = "",
            quote = "Life is greate",
            profession = "Android developer"
        ).toMap()

        val dbUsers = FirebaseFirestore
            .getInstance()
            .collection("users")

        dbUsers
            .add(user)
            .addOnSuccessListener { docRef ->
                val docId = docRef.id
                dbUsers
                    .document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
            }

    }




}