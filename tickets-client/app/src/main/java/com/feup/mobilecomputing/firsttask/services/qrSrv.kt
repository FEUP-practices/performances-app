package com.feup.mobilecomputing.firsttask.services

import android.graphics.Bitmap
import android.graphics.Color
import com.feup.mobilecomputing.firsttask.models.QRInfoType
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class qrSrv {
    fun createQR(qrInfo: QRInfoType): Bitmap? {
        val bitMatrix = encodeQR(qrInfo) ?: return null
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    private fun encodeQR(qrInfo: QRInfoType): BitMatrix? {
        try {
            val qrCodeWriter = QRCodeWriter()
            return qrCodeWriter.encode(Gson().toJson(qrInfo), BarcodeFormat.QR_CODE, 250, 250)
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }
}