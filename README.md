# Movie App - Android Application

Aplikasi Android untuk mencari dan menyimpan film favorit menggunakan Firebase Realtime Database dengan Google Authentication.

## 🎯 Fitur Aplikasi

### 1. **Google Authentication** 🔐
- Login dengan akun Google menggunakan Firebase Auth
- Secure authentication flow dengan OAuth 2.0
- Auto-redirect berdasarkan status login
- **Teknologi**: Firebase Auth, Google Sign-In SDK

### 2. **SplashActivity** (Aktivitas 1)
- Tampilan awal saat aplikasi dibuka
- Menampilkan logo aplikasi selama 2.5 detik
- Auto-check status login pengguna
- Navigate ke LoginActivity atau MainActivity
- **Teknologi**: Handler, Intent, Firebase Auth

### 3. **LoginActivity** (Aktivitas 2)
- Halaman login dengan Google Sign-In button
- Loading indicator dan error handling
- Auto-navigate ke MainActivity setelah login berhasil
- **Teknologi**: Google Sign-In, Firebase Auth, ActivityResultLauncher

### 4. **MainActivity** (Aktivitas 3)
- Halaman utama yang menampilkan daftar film populer
- Data real-time dari Firebase Realtime Database
- Auto-initialize database dengan dummy data jika kosong
- Menu logout dengan confirmation dialog
- **Teknologi**: RecyclerView, CardView, Firebase Realtime Database

### 5. **MovieDetailActivity** (Aktivitas 4)
- Tampil saat pengguna mengklik film di MainActivity
- Menampilkan detail lengkap film (poster, backdrop, sinopsis, rating, dll)
- Tombol favorit untuk menambah/menghapus film dari favorit
- **Favorit tersimpan per akun Google di Firebase**
- **Teknologi**: Intent, ImageView, TextView, ScrollView, Firebase Realtime Database

### 6. **SearchActivity** (Aktivitas 5)
- Halaman untuk mencari film
- SearchView di bagian atas dengan debouncing
- Pencarian real-time dari Firebase
- Hasil pencarian ditampilkan dalam RecyclerView
- **Teknologi**: SearchView, RecyclerView, Firebase Realtime Database

### 7. **FavoritesActivity** (Aktivitas 6)
- Menampilkan daftar film favorit **per akun Google**
- Data tersimpan di Firebase Realtime Database
- Real-time updates saat favorit berubah
- Sync otomatis antar device dengan akun yang sama
- **Teknologi**: Firebase Realtime Database, RecyclerView, Real-time Listener

## 🚀 Langkah Setup

### 1. Setup Firebase Project

#### A. Buat Proyek Firebase
1. Kunjungi [Firebase Console](https://console.firebase.google.com/)
2. Klik **"Add project"** atau **"Tambahkan proyek"**
3. Beri nama proyek (misalnya: "Movie App")
4. Ikuti wizard setup hingga selesai

#### B. Enable Google Authentication
1. Di Firebase Console, buka **Authentication** → **Sign-in method**
2. Klik **Google** dan klik **Enable**
3. Isi **Project support email**
4. Klik **Save**

#### C. Dapatkan Web Client ID
1. Di Firebase Console, klik **⚙️ Settings** (gear icon) → **Project settings**
2. Scroll ke bawah ke section **Your apps**
3. Atau buka [Google Cloud Console](https://console.cloud.google.com/)
4. Pilih project yang sama dengan Firebase
5. Buka **APIs & Services** → **Credentials**
6. Copy **Web Client ID** (format: `xxxxx.apps.googleusercontent.com`)
7. **SUDAH DIKONFIGURASI**: `89467527216-dm26vcok3nvec8u9hmg5ht8pm292kms8.apps.googleusercontent.com`

#### D. Tambahkan SHA-1 Fingerprint
Dapatkan SHA-1 fingerprint dan tambahkan ke Firebase:

```bash
# Windows PowerShell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

Copy **SHA1**, lalu:
- Firebase Console → Project Settings → Your apps
- Scroll ke **SHA certificate fingerprints**
- Klik **Add fingerprint** → Paste SHA-1
- Klik **Save**

#### E. Tambahkan Android App
1. Di Firebase Console, klik ikon Android (</>) untuk menambahkan app
2. Isi **Package name**: `com.example.myapplication`
3. Isi **App nickname**: "Movie App" (opsional)
4. Download file `google-services.json`
5. **PENTING**: Ganti file `app/google-services.json` dengan file yang baru Anda download

#### F. Enable Realtime Database
1. Di Firebase Console, buka **Build** → **Realtime Database**
2. Klik **"Create Database"**
3. Pilih lokasi database (misalnya: asia-southeast1)
4. Pilih mode **"Start in locked mode"**
5. Update rules dengan:
   ```json
   {
     "rules": {
       "users": {
         "$uid": {
           "favorites": {
             ".read": "$uid === auth.uid",
             ".write": "$uid === auth.uid"
           }
         }
       },
       "movies": {
         ".read": "auth != null",
         ".write": "auth != null"
       }
     }
   }
   ```
6. Klik **Publish**

### 2. Sync Project
1. Buka Android Studio
2. Klik "Sync Project with Gradle Files"
3. Tunggu hingga semua dependencies terdownload

### 3. Build dan Run
1. Sambungkan perangkat Android atau gunakan emulator (dengan Google Play Services)
2. Klik tombol "Run" di Android Studio
3. Aplikasi akan terinstall dan berjalan
4. **Login dengan akun Google**
5. **Otomatis**: Database akan ter-populate dengan data dummy pada first run

### 4. Build APK untuk Testing

```powershell
# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug
```

APK akan tersedia di: `app/build/outputs/apk/debug/app-debug.apk`

### 5. (Opsional) Gunakan TMDB API
Jika ingin menggunakan TMDB API instead of Firebase:

1. Dapatkan API Key dari [TMDB](https://www.themoviedb.org/)
2. Buka file: `app/src/main/java/com/example/myapplication/api/TMDBApi.java`
3. Ganti: `String API_KEY = "YOUR_TMDB_API_KEY_HERE";`
4. Di MainActivity.java dan SearchActivity.java, ubah:
   ```java
   private static final boolean USE_FIREBASE = false; // false = TMDB API
   ```

## 📁 Struktur Proyek

```
app/src/main/java/com/example/myapplication/
├── adapters/
│   ├── MovieAdapter.java              # Adapter untuk RecyclerView film
│   └── FavoriteMovieAdapter.java      # Adapter untuk RecyclerView favorit
├── api/
│   ├── TMDBApi.java                   # Interface API TMDB
│   └── RetrofitClient.java            # Client Retrofit
├── database/
│   ├── AppDatabase.java               # Room Database (legacy)
│   ├── FavoriteMovie.java             # Entity untuk favorit
│   └── FavoriteMovieDao.java          # DAO untuk operasi database
├── firebase/
│   ├── AuthManager.java               # 🔐 Google Authentication Manager
│   ├── FavoriteManager.java           # ⭐ Favorites Manager (Firebase)
│   └── FirebaseManager.java           # Firebase Database Manager
├── models/
│   ├── Movie.java                     # Model data Film
│   └── MovieResponse.java             # Model response API
├── utils/
│   ├── ErrorHandler.java              # Error handling utilities
│   ├── ImageLoader.java               # Image loading utilities
│   ├── LoadingHelper.java             # Loading state management
│   └── NetworkUtils.java              # Network utilities
├── LoginActivity.java                 # 🔐 Aktivitas Login
├── SplashActivity.java                # Aktivitas Splash Screen
├── MainActivity.java                  # Aktivitas Utama
├── MovieDetailActivity.java           # Aktivitas Detail Film
├── SearchActivity.java                # Aktivitas Pencarian
└── FavoritesActivity.java             # Aktivitas Favorit
```

## 📦 Dependencies yang Digunakan

- **AndroidX**: AppCompat, Material Design
- **RecyclerView**: Untuk menampilkan daftar
- **CardView**: Untuk tampilan kartu film
- **Firebase**: 
  - Firebase BOM 32.3.1
  - Firebase Realtime Database (data storage & sync)
  - Firebase Auth (Google Authentication)
- **Google Sign-In**: Google Play Services Auth 20.7.0
- **Retrofit**: HTTP client untuk API calls (opsional)
- **Gson**: JSON converter
- **Glide**: Image loading library (optimized) v4.15.1
- **Room**: Database lokal (legacy, sekarang menggunakan Firebase)
- **OkHttp**: Network optimization

## 🔥 Firebase Features

### 1. Google Authentication (AuthManager.java)
Singleton class untuk mengelola Google Sign-In:
- `initializeGoogleSignIn()` - Initialize Google Sign-In client
- `getGoogleSignInIntent()` - Dapatkan intent untuk sign-in
- `handleSignInResult()` - Handle hasil sign-in
- `signOut()` - Logout dari Firebase dan Google
- `isUserSignedIn()` - Cek status login
- `getCurrentUser()` - Dapatkan user saat ini
- `getCurrentUserId()` - Dapatkan user ID
- `getCurrentUserEmail()` - Dapatkan email user

### 2. Favorites Manager (FavoriteManager.java)
Singleton class untuk mengelola favorit di Firebase:
- `addToFavorites()` - Tambah film ke favorit
- `removeFromFavorites()` - Hapus film dari favorit
- `isFavorite()` - Cek apakah film favorit
- `loadFavorites()` - Load semua favorit user
- `addFavoritesListener()` - Real-time listener untuk auto-update

**Struktur Data:**
```
/users/{userId}/favorites/{movieId}/
```

### 3. Realtime Database (FirebaseManager.java)
Singleton class untuk mengelola film database:
- `getPopularMovies()` - Load semua film populer
- `searchMovies(query)` - Cari film by title
- `getMovieById(id)` - Get detail film specific
- `saveMovie(movie)` - Simpan/update film
- `initializeWithDummyData()` - Populate database pertama kali
- `isDatabaseEmpty()` - Check apakah database kosong

## 📊 Database Structure (Firebase)

```
firebase-realtime-database/
├── users/
│   └── {google_user_id}/
│       └── favorites/
│           ├── {movie_id_1}/
│           │   ├── id: 1
│           │   ├── title: "Movie Title"
│           │   ├── overview: "..."
│           │   ├── posterPath: "/path.jpg"
│           │   ├── backdropPath: "/path.jpg"
│           │   ├── releaseDate: "2024-01-01"
│           │   ├── voteAverage: 8.5
│           │   ├── voteCount: 1234
│           │   └── addedAt: 1234567890
│           └── {movie_id_2}/
│               └── ...
└── movies/
    └── popular_movies/
        ├── 1/
        │   ├── id: 1
        │   ├── title: "The Shawshank Redemption"
        │   ├── overview: "..."
        │   ├── posterPath: "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg"
        │   ├── backdropPath: "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg"
        │   ├── releaseDate: "1994-09-23"
        │   ├── voteAverage: 8.7
        │   └── voteCount: 25847
        └── 2/
            └── ...
```

**Keuntungan Struktur Ini:**
- ✅ Favorit tersimpan **per akun Google**
- ✅ Sinkronisasi otomatis **antar device**
- ✅ Data tidak hilang saat **uninstall app**
- ✅ Real-time updates untuk **instant sync**
- ✅ Secure dengan **authentication rules**

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

## 🔒 Security & Firebase Rules

### Production Rules (Direkomendasikan)

```json
{
  "rules": {
    "users": {
      "$uid": {
        "favorites": {
          ".read": "$uid === auth.uid",
          ".write": "$uid === auth.uid",
          "$movieId": {
            ".validate": "newData.hasChildren(['id', 'title'])"
          }
        }
      }
    },
    "movies": {
      ".read": "auth != null",
      ".write": false
    }
  }
}
```

**Penjelasan Rules:**
- 🔐 User hanya bisa read/write **favorit mereka sendiri**
- 🔐 Semua authenticated users bisa **read movies**
- 🔐 Tidak ada yang bisa **write ke movies** (hanya admin via console)
- ✅ Validation untuk memastikan data memiliki field wajib

### User Flow
1. **Splash Screen** → Check login status
2. **Not Logged In** → **Login Activity** → Google Sign-In
3. **Logged In** → **Main Activity** → Browse movies
4. **Add to Favorites** → Saved to `/users/{userId}/favorites/`
5. **View Favorites** → Load from Firebase per user
6. **Logout** → Clear session → Back to Login

## 🐛 Troubleshooting

### Sign-in Cancelled / Sign-in Failed
- **Penyebab**: SHA-1 fingerprint belum ditambahkan ke Firebase
- **Solusi**: 
  1. Generate SHA-1 (lihat langkah setup di atas)
  2. Tambahkan ke Firebase Console
  3. Download ulang `google-services.json`
  4. Rebuild app

### Firebase Permission Denied
- **Penyebab**: Firebase Database Rules belum diupdate
- **Solusi**: 
  1. Buka Firebase Console → Realtime Database → Rules
  2. Update rules sesuai contoh di atas
  3. Klik **Publish**
  4. Pastikan sudah login di app

### Favorit Tidak Muncul
- **Penyebab 1**: User belum login
- **Solusi**: Login dengan Google terlebih dahulu

- **Penyebab 2**: Firebase rules salah
- **Solusi**: Check rules di Firebase Console

- **Penyebab 3**: No-argument constructor missing
- **Solusi**: Sudah diperbaiki di `FavoriteMovie.java`

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
- Pastikan sudah login dengan Google
- Coba clear app data dan restart

### Build Error: Java 17 Required
- **Solusi**: Project sudah dikonfigurasi untuk JDK 11
- Android Gradle Plugin: 7.4.2
- CompileSdk: 33

## 🎨 Fitur Unggulan

### ✨ Real-time Sync
- Favorit tersinkronisasi secara real-time
- Tambah favorit di device A → langsung muncul di device B
- Menggunakan Firebase ValueEventListener

### 🔄 Cross-Device Sync
- Login dengan akun Google yang sama di device berbeda
- Favorit otomatis tersinkronisasi
- Data tidak hilang saat ganti device

### 🔐 Secure Authentication
- OAuth 2.0 via Google Sign-In
- Firebase Authentication integration
- No password management needed

### 💾 Persistent Data
- Data tersimpan di cloud (Firebase)
- Tidak hilang saat uninstall app
- Available offline dengan Firebase caching

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

- `GOOGLE_AUTH_SETUP.md` - Panduan lengkap setup Google Authentication
- `FIREBASE_SETUP.md` - Panduan lengkap setup Firebase (jika ada)

## 🔧 Technical Stack

- **Language**: Java
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 33 (Android 13)
- **Build Tool**: Gradle 8.2
- **Android Gradle Plugin**: 7.4.2
- **JDK**: 11

## 📱 Tested On

- ✅ Android 9.0 (API 28) - Android 13 (API 33)
- ✅ Emulator dengan Google Play Services
- ✅ Physical devices

## 🎯 Future Improvements

- [ ] Profile screen dengan user info
- [ ] Email/password authentication sebagai alternatif
- [ ] Movie recommendations berdasarkan favorit
- [ ] Share favorit ke social media
- [ ] Dark mode support
- [ ] Push notifications untuk movie updates

## 👨‍💻 Development Notes

### Migration dari Room ke Firebase
- ❌ **Old**: Room Database (SQLite lokal)
- ✅ **New**: Firebase Realtime Database (cloud sync)
- ✅ **Benefit**: Cross-device sync, no data loss, real-time updates

### Authentication Flow
1. User buka app → SplashActivity
2. Check `AuthManager.isUserSignedIn()`
3. If signed in → MainActivity
4. If not signed in → LoginActivity
5. Login success → Save to Firebase Auth → MainActivity

## API Credits

- Firebase Realtime Database by Google
- Firebase Authentication by Google
- Google Sign-In SDK by Google
- Data film default menggunakan struktur dari [The Movie Database (TMDB)](https://www.themoviedb.org/)

## 📄 License

This project is for educational purposes.

---

**Made with ❤️ using Firebase & Google Cloud**
