package app.cashadvisor.customView

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.DefaultProfileHeaderStyle,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var tvUserName: TextView
    private var tvAccountBalance: TextView
    private var ivProfilePic: ImageView
    private var btnPeriod: Button

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

    var period: String = ""
        set(value) {
            field = value
            btnPeriod.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_profile_header, this, true)

        tvUserName = findViewById(R.id.tv_name)
        tvAccountBalance = findViewById(R.id.tv_account_balance)
        ivProfilePic = findViewById(R.id.iv_profile_pic)
        btnPeriod = findViewById(R.id.btn_period)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProfileHeader,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {

                val userName = getString(R.styleable.ProfileHeader_userNameText)
                val balance = getString(R.styleable.ProfileHeader_balanceAccountText)
                val profilePic = getDrawable(R.styleable.ProfileHeader_profilePicResId)
                val btnPeriodText = getString(R.styleable.ProfileHeader_btnPeriodText) ?: ""

                tvUserName.text = userName
                tvAccountBalance.text = balance
                ivProfilePic.setImageDrawable(profilePic)
                btnPeriod.text = btnPeriodText

            } finally {
                recycle()
            }
        }
    }

    fun setBtnPeriodOnClickListener(clickListener: () -> Unit) {
        btnPeriod.setOnClickListener {
            clickListener.invoke()
        }
    }
}