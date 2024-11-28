package com.salary.shopmaker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salary.shopmaker.api.ApiService
import com.salary.shopmaker.model.Product
import com.salary.shopmaker.ui.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: ApiService // Получаем ApiService через Hilt

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = ProductAdapter(products)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Запуск асинхронной загрузки данных
        loadProducts()
    }

    private fun loadProducts() {
        val pageSize = 24
        val batchSize = 5 // Пакет запросов по 5 страниц

        // Используем корутины для асинхронной загрузки
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Первый запрос для получения общего количества страниц
                val initialResponse = apiService.getProducts(currentPage = 0, pageSize = pageSize)
                val totalPages = initialResponse.pagination.totalPages

                for (batchStart in 0 until totalPages step batchSize) {
                    val batchProducts = mutableListOf<Product>()

                    for (page in batchStart until minOf(batchStart + batchSize, totalPages)) {
                        val pageResponse = apiService.getProducts(currentPage = page, pageSize = pageSize)
                        val discountedProducts = extractDiscountedProducts(pageResponse.results)
                        batchProducts.addAll(discountedProducts)
                    }

                    // Обновляем UI на основном потоке
                    withContext(Dispatchers.Main) {
                        updateUI(batchProducts)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductListActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun extractDiscountedProducts(products: List<Product>): List<Product> {
        return products.filter { product ->
            product.showcases?.any { showcase ->
                showcase.discountPercent > 0
            } == true
        }
    }

    private fun updateUI(newProducts: List<Product>) {
        products.addAll(newProducts)
        adapter.notifyDataSetChanged()
    }
}