package com.abdullahstech.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF07111F)
private val Panel = Color(0xFF111F31)
private val Orange = Color(0xFFFF5B16)
private val Soft = Color(0xFF9EB0C7)

data class Product(val name: String, val category: String, val price: Int, val symbol: String, val description: String)

private val products = listOf(
    Product("JBL Charge 5", "Speakers", 1499, "🔊", "Portable Bluetooth speaker"),
    Product("JBL Flip 6", "Speakers", 1199, "🔈", "Compact waterproof speaker"),
    Product("JBL Tune 720BT", "Headphones", 999, "🎧", "Wireless over-ear headphones"),
    Product("JBL Tune Buds", "Earbuds", 899, "🎵", "Wireless earbuds"),
    Product("JBL PartyBox", "Speakers", 3999, "🎶", "Party speaker"),
    Product("Wireless Headphones", "Electronics", 499, "🎧", "Everyday wireless audio"),
    Product("Classic Hoodie", "Clothes", 349, "👕", "Comfortable everyday hoodie"),
    Product("Running Sneakers", "Shoes", 599, "👟", "Casual sports sneakers"),
    Product("Car LED Lights", "Car Accessories", 199, "🚘", "Decorative car accessories"),
    Product("Smart Watch", "Electronics", 449, "⌚", "Smart everyday watch")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = darkColorScheme(
            primary = Orange, background = Navy, surface = Panel, onBackground = Color.White,
            onSurface = Color.White
        )) { ShopApp() } }
    }
}

@Composable
fun ShopApp() {
    var tab by remember { mutableStateOf("Home") }
    var category by remember { mutableStateOf("All") }
    var search by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<Product?>(null) }
    var cart by remember { mutableStateOf(mapOf<String, Int>()) }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var checkout by remember { mutableStateOf(false) }

    val totalItems = cart.values.sum()
    val total = products.sumOf { (cart[it.name] ?: 0) * it.price }

    Scaffold(
        containerColor = Navy,
        topBar = {
            Column(Modifier.fillMaxWidth().background(Navy).padding(horizontal = 18.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Orange, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(42.dp)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.ShoppingBag, null, tint = Color.White) }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Abdullahs Tech", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("PREMIUM AUDIO. BETTER VIBES.", fontSize = 9.sp, color = Soft, letterSpacing = 1.sp)
                    }
                    IconButton(onClick = { tab = "Cart" }) {
                        BadgedBox(badge = { if (totalItems > 0) Badge { Text("$totalItems") } }) {
                            Icon(Icons.Default.ShoppingCart, "Cart", tint = Color.White)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = search, onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    placeholder = { Text("Search products...", color = Soft) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Soft) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange, unfocusedBorderColor = Color(0xFF293A50),
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White
                    ), shape = RoundedCornerShape(16.dp)
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0C1929), contentColor = Color.White) {
                listOf("Home", "Categories", "Favorites", "Cart").forEach { label ->
                    val icon = when (label) {
                        "Home" -> Icons.Default.Home
                        "Categories" -> Icons.Default.GridView
                        "Favorites" -> Icons.Default.Favorite
                        else -> Icons.Default.ShoppingCart
                    }
                    NavigationBarItem(
                        selected = tab == label, onClick = { tab = label; selected = null; checkout = false },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(if (label == "Cart" && totalItems > 0) "$label ($totalItems)" else label) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Orange, indicatorColor = Color(0xFF26364A), selectedTextColor = Orange)
                    )
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            when {
                checkout -> {
                    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Checkout", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Text("Order total", color = Soft)
                        Text("${total} kr", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Orange)
                        Text("Choose payment method", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text("Klarna", modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).clickable { }.padding(18.dp))
                        Text("Debit or credit card", modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).clickable { }.padding(18.dp))
                        Text("Payments are demo only. No real payment is taken.", color = Soft, fontSize = 13.sp)
                        Button(onClick = { checkout = false; tab = "Cart" }, colors = ButtonDefaults.buttonColors(containerColor = Orange)) {
                            Text("Back to cart")
                        }
                    }
                }
                selected != null -> {
                    val p = selected!!
                    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("‹ Back", modifier = Modifier.clickable { selected = null }, color = Soft)
                        Box(Modifier.fillMaxWidth().height(190.dp).background(Panel, RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) {
                            Text(p.symbol, fontSize = 76.sp)
                        }
                        Text(p.category.uppercase(), color = Orange, fontSize = 12.sp)
                        Text(p.name, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                        Text("${p.price} kr", fontSize = 24.sp, color = Orange, fontWeight = FontWeight.Bold)
                        Text(p.description, color = Soft)
                        Button(onClick = { cart = cart + (p.name to ((cart[p.name] ?: 0) + 1)); selected = null },
                            modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Orange)) {
                            Icon(Icons.Default.AddShoppingCart, null); Spacer(Modifier.width(8.dp)); Text("Add to cart")
                        }
                        OutlinedButton(onClick = { favorites = if (p.name in favorites) favorites - p.name else favorites + p.name }, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Favorite, null); Spacer(Modifier.width(8.dp)); Text(if (p.name in favorites) "Remove favorite" else "Add to favorites")
                        }
                    }
                }
                tab == "Cart" -> {
                    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Your cart", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        if (cart.isEmpty()) Text("Your cart is empty. Add some products first.", color = Soft)
                        products.filter { (cart[it.name] ?: 0) > 0 }.forEach { p ->
                            Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(p.symbol, fontSize = 30.sp)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(p.name, fontWeight = FontWeight.SemiBold)
                                    Text("${p.price} kr · Qty ${cart[p.name]}", color = Soft, fontSize = 13.sp)
                                }
                                IconButton(onClick = { val q = (cart[p.name] ?: 1) - 1; cart = if (q <= 0) cart - p.name else cart + (p.name to q) }) {
                                    Icon(Icons.Default.RemoveCircleOutline, "Remove one", tint = Orange)
                                }
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        Text("Total: $total kr", fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Button(onClick = { if (cart.isNotEmpty()) checkout = true }, enabled = cart.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Orange)) { Text("Continue to checkout") }
                    }
                }
                else -> {
                    val visible = products.filter {
                        (search.isBlank() || it.name.contains(search, true) || it.category.contains(search, true)) &&
                        when (tab) {
                            "Categories" -> category == "All" || it.category == category
                            "Favorites" -> it.name in favorites
                            else -> true
                        }
                    }
                    Column(Modifier.fillMaxSize().padding(horizontal = 14.dp)) {
                        if (tab == "Home") {
                            Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(20.dp)).padding(18.dp)) {
                                Text("GOOD SOUND. GOOD DAYS.", color = Orange, fontSize = 11.sp, letterSpacing = 1.sp)
                                Text("Find your next favorite", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text("JBL audio and everyday essentials", color = Soft, fontSize = 13.sp)
                            }
                        } else {
                            Text(tab, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))
                        }
                        if (tab == "Categories") {
                            Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.padding(vertical = 10.dp)) {
                                listOf("All", "Speakers", "Headphones", "Earbuds", "Clothes", "Shoes").forEach { c ->
                                    FilterChip(selected = category == c, onClick = { category = c }, label = { Text(c) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Orange, selectedLabelColor = Color.White))
                                }
                            }
                        }
                        Text(if (tab == "Favorites") "Saved products" else "Popular products", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 10.dp))
                        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(bottom = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(visible) { p ->
                                Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(18.dp)).clickable { selected = p }.padding(12.dp)) {
                                    Box(Modifier.fillMaxWidth().height(96.dp).background(Color(0xFF1A2A3D), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                        Text(p.symbol, fontSize = 43.sp)
                                        IconButton(onClick = { favorites = if (p.name in favorites) favorites - p.name else favorites + p.name }, modifier = Modifier.align(Alignment.TopEnd).size(32.dp)) {
                                            Icon(Icons.Default.Favorite, null, tint = if (p.name in favorites) Orange else Soft)
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(p.category.uppercase(), color = Soft, fontSize = 9.sp)
                                    Text(p.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 2, minLines = 2)
                                    Text("${p.price} kr", color = Orange, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                    Button(onClick = { cart = cart + (p.name to ((cart[p.name] ?: 0) + 1)) },
                                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp), contentPadding = PaddingValues(4.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Orange)) {
                                        Icon(Icons.Default.AddShoppingCart, null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp)); Text("Add", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
