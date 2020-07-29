package com.rifki.kotlin.bmi

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_calculate.setOnClickListener(this)

        tv_result.visibility = View.INVISIBLE
        category.visibility = View.INVISIBLE

        if(savedInstanceState != null){
            val resultIbm = savedInstanceState.getString(STATE_RESULT) as String
            tv_result.text = resultIbm
        }
    }

    override fun onSaveInstanceState(outState : Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, tv_result.text.toString())
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View) {
        val height = edt_height.text.toString().trim()
        val weight = edt_weight.text.toString().trim()

        var isEmptyFields = false

        if(v.id == R.id.btn_calculate){
            when{
                height.isEmpty() -> {
                    isEmptyFields = true
                    edt_height.error = getString(R.string.empty)
                }
                weight.isEmpty() -> {
                    isEmptyFields = true
                    edt_weight.error = getString(R.string.empty)
                }
            }
            if (!isEmptyFields) {
                val h = (edt_height.text).toString().toFloat() /100
                val w = edt_weight.text.toString().toFloat()
                if (h > 3.00) {
                    edt_height.error = getString(R.string.not_human)
                }
                if (w > 500) {
                    edt_weight.error = getString(R.string.not_human)
                }
                val resultIbm = w/(h*h)
                tv_result.text = "%.4f".format(resultIbm)

                when{
                    (resultIbm < 18.5) -> {
                        category.text = getString(R.string.under_weight)
                        category.setTextColor(Color.rgb(68, 165, 231))
                    }
                    (resultIbm in 18.5..22.9) -> {
                        category.text = getString(R.string.normal_weight)
                        category.setTextColor(Color.rgb(74, 227, 64))
                    }
                    (resultIbm > 22.9 && resultIbm <= 29.9) -> {
                        category.text = getString(R.string.over_weight)
                        category.setTextColor(Color.rgb(232, 209, 15))
                    }
                    (resultIbm > 29.9 && resultIbm <= 34.9) -> {
                        category.text = getString(R.string.obese)
                        category.setTextColor(Color.rgb(240, 115, 5))
                    }
                    (resultIbm > 34.9) -> {
                        category.text = getString(R.string.extreme_obese)
                        category.setTextColor(Color.rgb(249, 27, 20))
                    }
                }
            }

            tv_result.visibility = View.VISIBLE
            category.visibility = View.VISIBLE

            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(btn_calculate.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.info -> {
                infoItems()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun infoItems() {
        val items = arrayOf(getString(R.string.info_under_weight),
            getString(R.string.info_normal_weight),
            getString(R.string.info_over_weight),
            getString(R.string.info_obese),
            getString(R.string.info_extreme_obese))
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(getString(R.string.app_name))
            setItems(items) { _, _ ->
                onResume()
            }

            setPositiveButton("OK") { _, _ ->
                onResume()
            }
            show()
        }
    }

}