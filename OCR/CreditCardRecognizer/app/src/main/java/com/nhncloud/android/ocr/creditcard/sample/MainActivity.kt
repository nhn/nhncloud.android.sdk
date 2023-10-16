package com.nhncloud.android.ocr.creditcard.sample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nhncloud.android.ocr.NhnCloudOcr
import com.nhncloud.android.ocr.creditcard.CreditCardRecognitionService
import com.nhncloud.android.ocr.creditcard.sample.ui.CreditCardRecognizerActivity
import com.nhncloud.android.ocr.security.SecureString

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.getString(R.string.app_key) == DEFAULT_APP_KEY ||
            resources.getString(R.string.secret_key) == DEFAULT_SECRET_KEY) {
            error("Use your app key and secret key in config.xml.")
        }

        findViewById<Button>(R.id.recognize_using_default_ui).setOnClickListener {
            if (!CreditCardRecognitionService.isAvailable(this)) {
                alertMessage("Credit card recognition service is not available.")
                return@setOnClickListener
            }

            val nhnCloudOcr = NhnCloudOcr.newBuilder(this)
                .appKey(getString(R.string.app_key))
                .secretKey(getString(R.string.secret_key))
                .build()

            val creditCardRecognizer = nhnCloudOcr.createCreditCardRecognizer()
            creditCardRecognizer.launch(this) { result, data ->
                if (result.isSuccess) {
                    // Conversion to String object is not recommended.
                    val cardFullNumber = data!!.fullNumber.asString()
                    alertMessage("""
                        Card full number: $cardFullNumber
                    """.trimIndent())
                } else {
                    alertMessage(result.message!!)
                }
            }
        }

        findViewById<Button>(R.id.recognize_using_custom_ui).setOnClickListener {
            if (!CreditCardRecognitionService.isAvailable(this)) {
                alertMessage("Credit card recognition service is not available.")
                return@setOnClickListener
            }
            val intent = Intent(this, CreditCardRecognizerActivity::class.java)
            startActivity(intent)
        }

        if (!allRuntimePermissionsGranted()) {
            requestRuntimePermissions()
        }
    }

    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            if (!isPermissionGranted(this, permission)) {
                return false
            }
        }
        return true
    }

    private fun requestRuntimePermissions() {
        val permissionToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            if (!isPermissionGranted(this, permission)) {
                permissionToRequest.add(permission)
            }
        }

        if (permissionToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun alertMessage(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    companion object {
        private const val DEFAULT_APP_KEY = "your_app_key"
        private const val DEFAULT_SECRET_KEY = "your_secret_key"
        private const val PERMISSION_REQUEST_CODE = 100

        private val REQUIRED_RUNTIME_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        private fun isPermissionGranted(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        // Conversion to String object is not recommended.
        private fun SecureString.asString(): String {
            val chars = CharArray(length)
            for (i in chars.indices) {
                chars[i] = get(i)
            }
            return String(chars)
        }
    }
}