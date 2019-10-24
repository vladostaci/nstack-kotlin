package dk.nodes.nstack.kotlin.features.feedback.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dk.nodes.nstack.R
import kotlinx.android.synthetic.main.activity_feedback.*

private const val RC_GALLERY = 111

internal class FeedbackActivity : AppCompatActivity(R.layout.activity_feedback),
    FeedbackValidator.Callback {

    private lateinit var viewModel: FeedbackViewModel

    private val feedbackValidator = FeedbackValidator(this)

    private var feedbackImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[FeedbackViewModel::class.java]
        viewModel.viewState.observe(this, Observer(this::showViewState))

        nameInputView.addTextChangedListener {
            feedbackValidator.name = it.toString()
        }

        emailInputView.addTextChangedListener {
            feedbackValidator.email = it.toString()
        }

        messageInputView.addTextChangedListener {
            feedbackValidator.message = it.toString()
        }

        screenshotCardView.setOnClickListener {
            launchGalleryIntent()
        }

        submitButton.setOnClickListener {
            viewModel.sendFeedback(
                name = feedbackValidator.name,
                email = feedbackValidator.email,
                message = feedbackValidator.message,
                image = feedbackImage
            )
        }

        screenshotClearButton.setOnClickListener {
            updateFeedbackImage(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GALLERY && resultCode == Activity.RESULT_OK) {
            obtainFeedbackImage(data?.data)
        }
    }

    override fun onValidationUpdate(isValid: Boolean) {
        submitButton.isEnabled = isValid
    }

    private fun showViewState(state: FeedbackViewState) {
        loadingView.isVisible = state.isLoading
        contentView.isVisible = !state.isLoading

        if (state.isFeedbackSent == true) {
            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
            finish()
        }

        state.errorMessage?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchGalleryIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, RC_GALLERY)
    }

    private fun obtainFeedbackImage(uri: Uri?) = updateFeedbackImage(
        when (uri) {
            null -> null
            else -> FeedbackImageDecoder(contentResolver).decode(uri)
        }
    )

    private fun updateFeedbackImage(image: Bitmap?) {
        feedbackImage = image
        screenshotImageView.setImageBitmap(feedbackImage)
        screenshotClearButton.visibility = if (image == null) View.GONE else View.VISIBLE
    }
}