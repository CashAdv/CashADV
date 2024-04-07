package app.cashadvisor.profile.presentation.ui

import android.content.Context
import app.cashadvisor.common.di.UnAuthInterceptorOkHttpClient
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject


@GlideModule
class UnsafeOkHttpGlideModule @Inject constructor(
    @UnAuthInterceptorOkHttpClient private val okHttpClient: OkHttpClient
) : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }
}