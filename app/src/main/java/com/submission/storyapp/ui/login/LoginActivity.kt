package com.submission.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.submission.storyapp.R
import com.submission.storyapp.ui.main.MainActivity
import com.submission.storyapp.ViewModelFactory
import com.submission.storyapp.data.Result
import com.submission.storyapp.data.pref.UserModel
import com.submission.storyapp.databinding.ActivityLoginBinding
import com.submission.storyapp.databinding.ActivityMainBinding
import com.submission.storyapp.viewmodel.AuthenticationViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: AuthenticationViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener { loginAccount() }
    }

    private fun loginAccount() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        viewModel.authLogin(email, password).observe(this) { login ->
            when(login) {
                is Result.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    viewModel.saveSession(
                        UserModel(
                            email,
                            "Bearer ${login.data.loginResult?.token!!}"
                        )
                    )
                    AlertDialog.Builder(this).apply {
                        setTitle("Success!")
                        setMessage("Congratulations! Success to Login")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Whoops!")
                        setMessage("Failed to Login Account!.")
                        setPositiveButton("Back") { _, _ ->
                        }
                        create()
                        show()
                    }
                }
                is Result.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -20f, 20f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatCount = ObjectAnimator.REVERSE
        }.start()

        val titleLogin = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val descTitleLogin = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val edLoginEmail = ObjectAnimator.ofFloat(binding.edtEmailLayout, View.ALPHA, 1f).setDuration(300)
        val tvPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val edLoginPassword = ObjectAnimator.ofFloat(binding.edtPasswordLayout, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                titleLogin,
                descTitleLogin,
                tvEmail,
                edLoginEmail,
                tvPassword,
                edLoginPassword,
                btnLogin
            )
            start()
        }
    }

}