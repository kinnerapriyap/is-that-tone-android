package com.kinnerapriyap.sugar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kinnerapriyap.sugar.ui.theme.IsThatToneTheme

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var auth: FirebaseAuth

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            IsThatToneTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MyApp(viewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser?.apply { viewModel.onUidChanged(uid) }
        if (currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        viewModel.onUidChanged(auth.currentUser?.uid)
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.auth_unsuccessful),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.finishGame()
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IsThatToneTheme {
        MyApp()
    }
}