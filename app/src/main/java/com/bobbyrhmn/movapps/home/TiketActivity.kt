package com.bobbyrhmn.movapps.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobbyrhmn.movapps.R
import com.bobbyrhmn.movapps.checkout.model.Checkout
import com.bobbyrhmn.movapps.home.model.Film
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_tiket.*
import kotlinx.android.synthetic.main.qrcode_dialog.*
import kotlinx.android.synthetic.main.qrcode_dialog.view.*


class TiketActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)

        btn_qrcode.setOnClickListener{
            val mDialogView:View = LayoutInflater.from(this).inflate(R.layout.qrcode_dialog, null)

            val mBuilder = AlertDialog.Builder(this)

            mBuilder.setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            mDialogView.btn_tutup.setOnClickListener{

                mAlertDialog.dismiss()


            }
//            mBuilder.show()
//                .window.setBackgroundDrawableResource(R.drawable.shape_background_white)

        }

        val data = intent.getParcelableExtra<Film>("data")

        tv_title.text = data.judul
        tv_genre.text = data.genre
        tv_rate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster_image)

        rc_checkout.layoutManager = LinearLayoutManager(this)
        dataList.add(Checkout("C1",""))
        dataList.add(Checkout("C2",""))

        rc_checkout.adapter = TiketAdapter(dataList) {
        }

    }

}
