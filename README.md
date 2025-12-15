# Movie App - Android Application

Aplikasi Android untuk mencari dan menyimpan film favorit menggunakan Firebase Realtime Database.

## Fitur Aplikasi

### 1. **SplashActivity** (Aktivitas 1)
- Tampilan awal saat aplikasi dibuka
- Menampilkan logo aplikasi selama 2.5 detik
- Otomatis berpindah ke MainActivity
- **Teknologi**: Handler, Intent

### 2. **MainActivity** (Aktivitas 2)
- Halaman utama yang menampilkan daftar film populer
- Data real-time dari Firebase Realtime Database
- Auto-initialize database dengan dummy data jika kosong
- Menggunakan RecyclerView untuk menampilkan daftar film
- Setiap film ditampilkan dalam CardView
- **Teknologi**: RecyclerView, CardView, Firebase Realtime Database

### 3. **MovieDetailActivity** (Aktivitas 3)
- Tampil saat pengguna mengklik film di MainActivity
- Menampilkan detail lengkap film (poster, backdrop, sinopsis, rating, dll)
- Tombol favorit untuk menambah/menghapus film dari favorit
- **Teknologi**: Intent, ImageView, TextView, ScrollView, Room Database

### 4. **SearchActivity** (Aktivitas 4)
- Halaman untuk mencari film
- SearchView di bagian atas dengan debouncing
- Pencarian real-time dari Firebase
- Hasil pencarian ditampilkan dalam RecyclerView
- **Teknologi**: SearchView, RecyclerView, Firebase Realtime Database

### 5. **FavoritesActivity** (Aktivitas 5)
- Menampilkan daftar film favorit
- Data disimpan di database lokal menggunakan Room
- **Teknologi**: Room Database, RecyclerView, SQLite

## Langkah Setup

### 1. Setup Firebase Realtime Database

#### A. Buat Proyek Firebase
1. Kunjungi [Firebase Console](https://console.firebase.google.com/)
2. Klik **"Add project"** atau **"Tambahkan proyek"**
3. Beri nama proyek (misalnya: "Movie App")
4. Ikuti wizard setup hingga selesai

#### B. Tambahkan Android App
1. Di Firebase Console, klik ikon Android (</>) untuk menambahkan app
2. Isi **Package name**: `com.example.myapplication`
3. Isi **App nickname**: "Movie App" (opsional)
4. Download file `google-services.json`
5. **PENTING**: Ganti file `app/google-services.json` dengan file yang baru Anda download

#### C. Enable Realtime Database
1. Di Firebase Console, buka **Build** → **Realtime Database**
2. Klik **"Create Database"**
3. Pilih lokasi database (misalnya: asia-southeast1)
4. Pilih mode **"Start in test mode"** untuk development
   ```json
   {
     "rules": {
       ".read": true,
       ".write": true
     }
   }
   ```
   ⚠️ **Catatan**: Untuk production, ubah rules menjadi lebih ketat!

### 2. Sync Project
1. Buka Android Studio
2. Klik "Sync Project with Gradle Files"
3. Tunggu hingga semua dependencies terdownload

### 3. Build dan Run
1. Sambungkan perangkat Android atau gunakan emulator
2. Klik tombol "Run" di Android Studio
3. Aplikasi akan terinstall dan berjalan
4. **Otomatis**: Database akan ter-populate dengan data dummy pada first run

### 4. (Opsional) Gunakan TMDB API
Jika ingin menggunakan TMDB API instead of Firebase:

1. Dapatkan API Key dari [TMDB](https://www.themoviedb.org/)
2. Buka file: `app/src/main/java/com/example/myapplication/api/TMDBApi.java`
3. Ganti: `String API_KEY = "YOUR_TMDB_API_KEY_HERE";`
4. Di MainActivity.java dan SearchActivity.java, ubah:
   ```java
   private static final boolean USE_FIREBASE = false; // false = TMDB API
   ```

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
- **Firebase**: Realtime Database untuk data storage
  - Firebase BOM 32.7.0
  - Firebase Realtime Database
  - Firebase Auth
- **Retrofit**: HTTP client untuk API calls (opsional)
- **Gson**: JSON converter
- **Glide**: Image loading library (optimized)
- **Room**: Database lokal untuk favorites
- **OkHttp**: Network optimization

## 🔥 Firebase Features

### Realtime Database
- **Auto-initialization**: Database otomatis ter-populate dengan dummy data pada first run
- **Offline persistence**: Data di-cache untuk akses offline
- **Real-time sync**: Data ter-update otomatis saat ada perubahan
- **Search functionality**: Pencarian by title dengan Firebase queries

### FirebaseManager.java
Singleton class untuk mengelola semua operasi Firebase:
- `getPopularMovies()` - Load semua film populer
- `searchMovies(query)` - Cari film by title
- `getMovieById(id)` - Get detail film specific
- `saveMovie(movie)` - Simpan/update film
- `initializeWithDummyData()` - Populate database pertama kali
- `isDatabaseEmpty()` - Check apakah database kosong

## 📊 Database Structure (Firebase)

```
movie-app-database/
├── popular_movies/
│   ├── 1/
│   │   ├── id: 1
│   │   ├── title: "The Shawshank Redemption"
│   │   ├── overview: "..."
│   │   ├── posterPath: "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg"
│   │   ├── backdropPath: "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg"
│   │   ├── releaseDate: "1994-09-23"
│   │   ├── voteAverage: 8.7
│   │   └── voteCount: 25847
│   ├── 2/
│   └── ...
```

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
- ✅ **Firebase Offline Persistence**: Data tersedia offline
- ✅ **Lazy Loading**: Data dimuat saat dibutuhkan

## 🔒 Security (Production)

Untuk production deployment, ubah Firebase rules:

```json
{
  "rules": {
    "popular_movies": {
      ".read": true,
      ".write": "auth != null"
    }
  }
}
```

Ini membolehkan semua orang read, tapi hanya authenticated users yang bisa write.

## 🐛 Troubleshooting

### Firebase Connection Failed
- Check internet connection
- Pastikan Realtime Database sudah enabled di Firebase Console
- Check Firebase rules (pastikan allow read access)

### google-services.json Error
- Pastikan file ada di folder `app/`
- File harus valid dari Firebase Console
- Sync gradle setelah mengganti file

### Data tidak muncul
- Check Firebase Console apakah data sudah ada
- Check Logcat untuk error messages
- Coba clear app data dan restart

## 📱 Mode Toggle

Aplikasi mendukung 3 mode data source:

1. **Firebase Mode** (Default)
   ```java
   private static final boolean USE_FIREBASE = true;
   ```

2. **TMDB API Mode**
   ```java
   private static final boolean USE_FIREBASE = false;
   ```

3. **Dummy Data Mode** (Legacy)
   - Masih tersedia di `DummyData.java`
   - Digunakan untuk auto-initialize Firebase

## 📚 Dokumentasi Tambahan

- `FIREBASE_SETUP.md` - Panduan lengkap setup Firebase
- `PRODUCTION_COMPONENTS.md` - Dokumentasi komponen production-ready

## API Credits

- Firebase Realtime Database by Google
- Data film default menggunakan struktur dari [The Movie Database (TMDB)](https://www.themoviedb.org/)
