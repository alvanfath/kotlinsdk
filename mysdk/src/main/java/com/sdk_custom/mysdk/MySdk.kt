package com.sdk_custom.mysdk

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails


class MySdk(private val context: Context) {

    private var referrerClient: InstallReferrerClient? = null
    fun getInstallReferrer(callback: (String?, Long?, Long?, String?) -> Unit) {
        referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient?.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient!!.installReferrer
                        callback(
                            response.installReferrer,
                            response.referrerClickTimestampSeconds,
                            response.installBeginTimestampSeconds,
                            null
                        )
                    }
                    else -> callback(null, null, null, "Install Referrer API failed with code: $responseCode")
                }
                referrerClient?.endConnection()
            }

            override fun onInstallReferrerServiceDisconnected() {
                callback(null, null, null, "Install Referrer Service Disconnected")
            }
        })
    }
}