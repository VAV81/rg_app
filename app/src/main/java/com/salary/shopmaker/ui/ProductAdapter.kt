package com.salary.shopmaker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salary.shopmaker.R
import com.salary.shopmaker.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.product_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.product_price)
        private val discountTextView: TextView = itemView.findViewById(R.id.product_discount)
        private val imageView: ImageView = itemView.findViewById(R.id.product_image)

        fun bind(product: Product) {
            nameTextView.text = product.name
            priceTextView.text = product.price?.value ?: "Цена отсутствует"
            discountTextView.text = "${product.showcases?.firstOrNull()?.discountPercent ?: 0}% скидка"

            // Используем Picasso для загрузки изображения
            Picasso.get()
                .load("https://api.rivegauche.ru" + (product.listingImage?.url ?: ""))
                .placeholder(R.drawable.ic_placeholder)  // Место для изображения, если оно не загружено
                .error(R.drawable.ic_error)              // Место для изображения, если возникла ошибка
                .into(imageView)
        }
    }
}