package com.bobbyrhmn.movapps.checkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobbyrhmn.movapps.R
import com.bobbyrhmn.movapps.checkout.adapter.CheckoutAdapter
import com.bobbyrhmn.movapps.checkout.model.Checkout
import com.bobbyrhmn.movapps.home.TiketActivity
import com.bobbyrhmn.movapps.home.model.Film
import com.bobbyrhmn.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total: Int = 0

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>
        val data = intent.getParcelableExtra<Film>("datas")


        for (a in dataList.indices) {
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        btn_tiket.setOnClickListener {
            val intent = Intent(
                this@CheckoutActivity,
                CheckoutSuccessActivity::class.java
            )
            startActivity(intent)

            showNotif(data)
        }
        btn_home.setOnClickListener {
            finish()
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckoutAdapter(dataList) {
        }

        if (preferences.getValues("saldo")!!.isNotEmpty()) {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
            btn_tiket.visibility = View.VISIBLE
            textView3.visibility = View.INVISIBLE
        } else {
            tv_saldo.setText("Rp 0")
            btn_tiket.visibility = View.INVISIBLE
            textView3.visibility = View.VISIBLE
            textView3.text = "Saldo pada E-Wallet anda tidak mencukupi untuk melakukan transaksi"
        }
    }

    private fun showNotif(datas: Film) {
        val NOTIFICATION_CHANNEL_ID = "channel_bwa_notif"
        val context = this.applicationContext
        var notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "MOV Apps Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel= NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName,importance)
            notificationManager.createNotificationChannel(mChannel)
        }

//        val mIntent = Intent (this, CheckoutSuccessActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("id", "id_film")
//        mIntent.putExtras(bundle)

        val mIntent = Intent (this, TiketActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("data", datas)
        mIntent.putExtras(bundle)

        val pendingIntent =
            PendingIntent.getActivity(this, 0,mIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setTicker("Notif MOV Apps Starting")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Pembayaran Berhasil")
            .setContentText("Tiket" +datas.judul+ "berhasil terbeli. Enjoy the movue!")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115, builder.build())
    }
}
