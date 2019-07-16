package com.deanna.mvrx.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.deanna.mvrx.R
import com.squareup.picasso.Picasso

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Profile @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val profileNameView: TextView
    private val userIdView: TextView
    private val repView: TextView
    private val websiteView: TextView
    private val imageView: ImageView

    init {
        inflate(context, R.layout.profile, this)
        profileNameView = findViewById(R.id.profileName)
        userIdView = findViewById(R.id.userId)
        repView = findViewById(R.id.rep)
        websiteView = findViewById(R.id.website)
        imageView = findViewById(R.id.user_profile_image)
    }

    @TextProp
    fun setProfileName(profileName: CharSequence) {
        profileNameView.text = profileName
    }

    @TextProp
    fun setUserId(userId: CharSequence?) {
        userIdView.visibility = if (userId.isNullOrBlank()) View.GONE else View.VISIBLE
        userIdView.text = userId
    }

    @TextProp
    fun setRep(rep: CharSequence?) {
        repView.visibility = if (rep.isNullOrBlank()) View.GONE else View.VISIBLE
        repView.text = rep
    }

    @TextProp
    fun setWebsite(website: CharSequence?) {
        websiteView.visibility = if (website.isNullOrBlank()) View.GONE else View.VISIBLE
        websiteView.text = website
    }

    @CallbackProp
    fun setImage(url: CharSequence?) {
        if (!url.isNullOrEmpty()) Picasso.get().load(url.toString()).into(imageView)
    }


    @CallbackProp
    fun setClickListener(clickListener: OnClickListener?) {
        setOnClickListener(clickListener)
    }
}