package com.example.implementbooks

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countryView = findViewById<EditText>(R.id.countryEditText)
        val languageView = findViewById<EditText>(R.id.languageEditText)
        val filterBtn = findViewById<Button>(R.id.filterBtn)

        var resultCountViev = findViewById<TextView>(R.id.resultCountView)
        var listOutputViev = findViewById<TextView>(R.id.listOutputView)

        val myApplication = application as MyBookApp
        val httpApiService = myApplication.httpApiService

        filterBtn.setOnClickListener {
            var country = countryView.text?.toString()
            var language = languageView.text?.toString()

            var maskedCountry = if (country.isNullOrEmpty()) ""
            else country
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            var maskedLang = if (language.isNullOrEmpty()) ""
            else language
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            CoroutineScope(Dispatchers.IO).launch {
                val decodedJsonResult = httpApiService.getBookList()
                val bookList = decodedJsonResult.toList()

                val filterBookList = bookList.filter { it.country == maskedCountry || it.language == maskedLang }

                val booksAsString = StringBuilder("")

                for (item in filterBookList.take(3))
                    booksAsString.append("Result: "+item.title + "\n")

                withContext(Dispatchers.Main) {
                    resultCountViev.text = "Results: ${filterBookList.size}"
                    listOutputViev.text = booksAsString
                }

            }
        }
    }
}
