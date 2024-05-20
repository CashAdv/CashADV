package app.cashadvisor.customView

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileHeaderSettings @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.DefaultProfileHeaderSettingsStyle,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var tvUserName: TextView
    private var tvSubscriptionInfo: TextView
    private var ivProfilePic: ImageView

    var userName: String = ""
        set(value) {
            field = value
            tvUserName.text = value
        }

    var subscriptionInfo: String = ""
        set(value) {
            field = value
            tvSubscriptionInfo.text = value
        }

    var profilePicUri: Uri = Uri.EMPTY
        set(value) {
            field = value
            Glide.with(this)
                .load(value)
                .placeholder(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.placeholder_profile_picture
                    )
                )
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfilePic)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_profile_header_settings, this, true)

        this.setPadding(resources.getDimensionPixelSize(R.dimen.header_padding))
        this.background = ContextCompat.getDrawable(context, R.drawable.light_gray_text_mask)

        tvUserName = findViewById(R.id.tv_name)
        tvSubscriptionInfo = findViewById(R.id.tv_subscription_info)
        ivProfilePic = findViewById(R.id.iv_profile_pic)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProfileHeaderSettings,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {

                val userName = getString(R.styleable.ProfileHeaderSettings_userNameTextSettings)
                val subscriptionInfo =
                    getString(R.styleable.ProfileHeaderSettings_subscriptionInfoText)
                val profilePic =
                    getDrawable(R.styleable.ProfileHeaderSettings_profilePicResIdSettings)

                tvUserName.text = userName
                tvSubscriptionInfo.text = subscriptionInfo
                ivProfilePic.setImageDrawable(profilePic)

            } finally {
                recycle()
            }
        }
    }
}