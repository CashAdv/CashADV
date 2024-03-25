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
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileHeaderMain @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.DefaultProfileHeaderStyle,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var tvUserName: TextView
    private var tvAccountBalance: TextView
    private var ivProfilePic: ImageView

    var userName: String = ""
        set(value) {
            field = value
            tvUserName.text = value
        }

    var accountBalance: String = ""
        set(value) {
            field = value
            tvAccountBalance.text = value
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
        LayoutInflater.from(context).inflate(R.layout.layout_profile_header_main, this, true)

        tvUserName = findViewById(R.id.tv_name)
        tvAccountBalance = findViewById(R.id.tv_account_balance)
        ivProfilePic = findViewById(R.id.iv_profile_pic)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProfileHeaderMain,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {

                val userName = getString(R.styleable.ProfileHeaderMain_userNameText)
                val balance = getString(R.styleable.ProfileHeaderMain_balanceAccountText)
                val profilePic = getDrawable(R.styleable.ProfileHeaderMain_profilePicResId)

                tvUserName.text = userName
                tvAccountBalance.text = balance
                ivProfilePic.setImageDrawable(profilePic)


            } finally {
                recycle()
            }
        }
    }
}