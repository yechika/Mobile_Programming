# Movie App - Android Application

Aplikasi Android untuk mencari dan menyimpan film favorit menggunakan The Movie Database (TMDB) API.

## Fitur Aplikasi

### 1. **SplashActivity** (Aktivitas 1)
- Tampilan awal saat aplikasi dibuka
- Menampilkan logo aplikasi selama 2.5 detik
- Otomatis berpindah ke MainActivity
- **Teknologi**: Handler, Intent

### 2. **MainActivity** (Aktivitas 2)
- Halaman utama yang menampilkan daftar film populer
- Menggunakan RecyclerView untuk menampilkan daftar film
- Setiap film ditampilkan dalam CardView
- **Teknologi**: RecyclerView, CardView, Retrofit

### 3. **MovieDetailActivity** (Aktivitas 3)
- Tampil saat pengguna mengklik film di MainActivity
- Menampilkan detail lengkap film (poster, backdrop, sinopsis, rating, dll)
- Tombol favorit untuk menambah/menghapus film dari favorit
- **Teknologi**: Intent, ImageView, TextView, ScrollView, Room Database

### 4. **SearchActivity** (Aktivitas 4)
- Halaman untuk mencari film
- SearchView di bagian atas
- Hasil pencarian ditampilkan dalam RecyclerView
- **Teknologi**: SearchView, RecyclerView, Retrofit

### 5. **FavoritesActivity** (Aktivitas 5)
- Menampilkan daftar film favorit
- Data disimpan di database lokal menggunakan Room
- **Teknologi**: Room Database, RecyclerView, SQLite

## Langkah Setup

### 1. Dapatkan API Key dari TMDB
1. Kunjungi https://www.themoviedb.org/
2. Buat akun gratis
3. Pergi ke Settings → API
4. Salin API Key (v3 auth)

### 2. Update API Key di Aplikasi
Buka file: `app/src/main/java/com/example/myapplication/api/TMDBApi.java`

Ganti baris berikut dengan API Key Anda:
```java
String API_KEY = "YOUR_TMDB_API_KEY_HERE"; // Ganti dengan API key Anda
```

### 3. Sync Project
1. Buka Android Studio
2. Klik "Sync Project with Gradle Files"
3. Tunggu hingga semua dependencies terdownload

### 4. Build dan Run
1. Sambungkan perangkat Android atau gunakan emulator
2. Klik tombol "Run" di Android Studio
3. Aplikasi akan terinstall dan berjalan

## Struktur Proyek

```
app/src/main/java/com/example/myapplication/
├── adapters/
│   ├── MovieAdapter.java              # Adapter untuk RecyclerView film
│   └── FavoriteMovieAdapter.java      # Adapter untuk RecyclerView favorit
├── api/
│   ├── TMDBApi.java                   # Interface API TMDB
│   └── RetrofitClient.java            # Client Retrofit
├── database/
│   ├── AppDatabase.java               # Room Database
│   ├── FavoriteMovie.java             # Entity untuk favorit
│   └── FavoriteMovieDao.java          # DAO untuk operasi database
├── models/
│   ├── Movie.java                     # Model data Film
│   └── MovieResponse.java             # Model response API
├── SplashActivity.java                # Aktivitas Splash Screen
├── MainActivity.java                  # Aktivitas Utama
├── MovieDetailActivity.java           # Aktivitas Detail Film
├── SearchActivity.java                # Aktivitas Pencarian
└── FavoritesActivity.java             # Aktivitas Favorit
```

## Dependencies yang Digunakan

- **AndroidX**: AppCompat, Material Design
- **RecyclerView**: Untuk menampilkan daftar
- **CardView**: Untuk tampilan kartu film
- **Retrofit**: HTTP client untuk API calls
- **Gson**: JSON converter
- **Glide**: Image loading library (optimized)
- **Room**: Database lokal
- **OkHttp**: Network optimization

## ⚡ Optimasi Performa

Aplikasi telah dioptimasi untuk performa yang lebih ringan:

### Optimasi yang Diterapkan:
- ✅ **ProGuard/R8**: Mengurangi ukuran APK ~40%
- ✅ **Resource Shrinking**: Hapus resource tidak terpakai
- ✅ **Image Format RGB_565**: Hemat memori 50%
- ✅ **RecyclerView Caching**: Scroll lebih smooth
- ✅ **Glide Disk Cache**: Loading lebih cepat
- ✅ **Memory Management**: Auto cleanup saat low memory
- ✅ **Network Timeout**: Optimasi koneksi


## API Credits

Data film menggunakan API dari [The Movie Database (TMDB)](https://www.themoviedb.org/).
