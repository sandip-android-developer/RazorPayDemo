package com.example.rozarpaydemo

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.DecimalFormat


class MainActivity : AppCompatActivity(),PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       Checkout.preload(applicationContext)
        initView()
    }

    private fun initView() {
        btn_payment.setOnClickListener {
            startPayment()
        }
        editAmount.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length!=0)
                {
                    txtPayment.text="Do Payment!  Rs."+editAmount.text.toString().trim().toDouble()*100 +" Paisa"
                    println("Payment--"+decimalFormator(editAmount.text.toString().trim()))
                }

            }

        })
    }

    private fun startPayment() {
        var checkout = Checkout()
        var activity: Activity = this
        checkout.setKeyID("rzp_test_16aDCdOdDuAfHO")
        var amount=editAmount.text.toString().trim().toDouble()*100

        try {
            var options: JSONObject = JSONObject()
            options.put("name", "Sandroid")
            options.put("description", "Course Charges")
            options.put("currency", "INR")
            options.put("amount", (decimalFormator(amount.toString())!!.toDouble()).toInt())
            options.put("theme.color", "#FD8F5F")
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")

            var prefill: JSONObject = JSONObject()
            prefill.put("email", "sksandipkumar588@gmail.com")
            prefill.put("contact", "1234567890")
            prefill.put("method", "card")
            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            println("Error--"+e.message)
            e.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Error---$p1",Toast.LENGTH_LONG)
        println("Error--1--"+p1)
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Success---$p0",Toast.LENGTH_SHORT)
        println("Error--2--"+p0)
    }

    fun decimalFormator(amount: String): String? {
        try {
            val dec = DecimalFormat("0.00")
            return dec.format(amount.toDouble())
        } catch (e: Exception) {
            println("Exception in decimal--"+e.message)
            return amount
        }

    }
}