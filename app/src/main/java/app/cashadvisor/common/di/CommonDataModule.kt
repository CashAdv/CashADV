package app.cashadvisor.common.di

import android.content.Context
import app.cashadvisor.BuildConfig
import app.cashadvisor.common.data.ErrorInterceptor
import app.cashadvisor.common.data.api.NetworkConnectionProvider
import app.cashadvisor.common.data.impl.NetworkConnectionProviderImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.security.cert.CertificateException
import javax.inject.Qualifier
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpClientBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnsafeOkHttpClientBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnAuthInterceptorOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    //TODO: после решения проблем с сертификтом на сервере убрать лишние аннотации

    @UnAuthInterceptorOkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor,
        @UnsafeOkHttpClientBuilder okkHttpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient {
        return okkHttpClientBuilder
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(errorInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @UnAuthInterceptorOkHttpClient okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val endpointBaseUrl = if (BuildConfig.FLAVOR == PROD) {
            ENDPOINT_URL_PROD
        } else ENDPOINT_URL_STAGE
        return Retrofit.Builder()
            .baseUrl(endpointBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @OkHttpClientBuilder
    @Singleton
    @Provides
    fun provideOkhttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }


    //TODO: Временное решение. Так как на данный момент есть проблемы с сертификатом у сервера, используем OkhttpClient без проверки сертификатов, позже заменить на обычный с проверкой
    @UnsafeOkHttpClientBuilder
    @Singleton
    @Provides
    fun provideUnsafeOkhttpClientBuilder(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            // builder.hostnameVerifier { _, _ -> true }
            builder.hostnameVerifier(hostnameVerifier = HostnameVerifier { _, _ -> true })
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * Перехватчик для логирования отправленных и полученных данных
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * Перехватчик для обработки ошибок, приходящих с сервера
     */

    @Provides
    @Singleton
    fun provideNetworkConnectionProvider(@ApplicationContext context: Context): NetworkConnectionProvider {
        return NetworkConnectionProviderImpl(context)
    }

    companion object {
        const val ENDPOINT_URL_PROD = "https://212.233.78.3:8080/v1/"
        const val ENDPOINT_URL_STAGE = "https://212.233.78.3:8080/v1/"
        const val PROD = "prod"
    }

}
