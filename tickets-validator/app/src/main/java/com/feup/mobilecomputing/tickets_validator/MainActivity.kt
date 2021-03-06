package com.feup.mobilecomputing.tickets_validator

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.feup.mobilecomputing.tickets_validator.api.CallbackAPI
import com.feup.mobilecomputing.tickets_validator.api.TicketsAPI
import com.feup.mobilecomputing.tickets_validator.models.QRInfoType
import com.google.gson.Gson

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"

class MainActivity : Activity() {
    private val tvMessage by lazy { findViewById<TextView>(R.id.tv_message) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.bt_scan_qr).setOnClickListener { _ -> scan(true) }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putCharSequence("Message", tvMessage.text)
        super.onSaveInstanceState(bundle)
    }

    private fun scan(qrcode: Boolean) {
        try {
            val intent = Intent(ACTION_SCAN)
            intent.putExtra("SCAN_MODE", if (qrcode) "QR_CODE_MODE" else "PRODUCT_MODE")
            startActivityForResult(intent, 0)
        }
        catch (anfe: ActivityNotFoundException) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                val contents = data?.getStringExtra("SCAN_RESULT") ?: ""
                TicketsAPI(applicationContext).validateTicket(Gson().fromJson(contents, QRInfoType::class.java), object: CallbackAPI<Boolean> {
                    override fun onSuccess(response: Boolean) {
                        Toast.makeText(applicationContext, "Validated correctly!", Toast.LENGTH_LONG).show()
                    }
                    override fun onError(errorMsg: String?) {
                        Toast.makeText(applicationContext, errorMsg ?: "Error validating QR", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(applicationContext, "Error scanning QR", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDialog(act: Activity, title: CharSequence, message: CharSequence, buttonYes: CharSequence, buttonNo: CharSequence): AlertDialog {
        val downloadDialog = AlertDialog.Builder(act)
        downloadDialog.setTitle(title)
        downloadDialog.setMessage(message)
        downloadDialog.setPositiveButton(buttonYes) { _, _ ->
            val uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            act.startActivity(intent)
        }
        downloadDialog.setNegativeButton(buttonNo, null)
        return downloadDialog.show()
    }
}
