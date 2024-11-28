package com.salary.shopmaker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salary.shopmaker.api.ApiService
import com.salary.shopmaker.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    fun loadProducts(pageSize: Int) {
        viewModelScope.launch {
            try {
                val initialResponse = apiService.getProducts(currentPage = 0, pageSize = pageSize)
                val totalPages = initialResponse.pagination.totalPages
                val allProducts = mutableListOf<Product>()

                for (page in 0 until totalPages) {
                    val response = apiService.getProducts(currentPage = page, pageSize = pageSize)
                    allProducts.addAll(response.results)
                }

                _products.value = allProducts.filter { product ->
                    product.showcases?.any { it.discountPercent > 0 } == true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}