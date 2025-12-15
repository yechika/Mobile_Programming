# Firebase Realtime Database Setup Guide

## 🔥 Setup Firebase untuk Proyek Anda

### Langkah 1: Buat Proyek Firebase
1. Kunjungi [Firebase Console](https://console.firebase.google.com/)
2. Klik **"Add project"** atau **"Tambahkan proyek"**
3. Beri nama proyek (misalnya: "Movie App")
4. Ikuti wizard setup hingga selesai

### Langkah 2: Tambahkan Android App
1. Di Firebase Console, klik ikon Android (</>) untuk menambahkan app
2. Isi **Package name**: `com.example.myapplication`
3. Isi **App nickname**: "Movie App" (opsional)
4. Download file `google-services.json`
5. **Penting**: Ganti file `app/google-services.json` yang sudah ada dengan file yang baru Anda download

### Langkah 3: Enable Realtime Database
1. Di Firebase Console, buka **Build** → **Realtime Database**
2. Klik **"Create Database"**
3. Pilih lokasi database (misalnya: asia-southeast1)
4. Pilih mode **"Start in test mode"** untuk development
   ```
   {
     "rules": {
       ".read": true,
       ".write": true
     }
   }
   ```
   ⚠️ **Catatan**: Untuk production, ubah rules menjadi lebih ketat!

### Langkah 4: Sync Gradle
1. Setelah mengganti `google-services.json`, sync project
2. Klik **"Sync Now"** atau tekan `Ctrl+Shift+O`
3. Tunggu hingga build selesai

### Langkah 5: Initialize Database dengan Data
Pada first run, aplikasi akan otomatis populate database dengan dummy data.

Atau Anda bisa manual import data dengan struktur berikut di Firebase Console:

```json
{
  "popular_movies": {
    "1": {
      "id": 1,
      "title": "The Shawshank Redemption",
      "overview": "Framed in the 1940s for double murder...",
      "posterPath": "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
      "backdropPath": "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
      "releaseDate": "1994-09-23",
      "voteAverage": 8.7,
      "voteCount": 25847
    },
    "2": {
      "id": 2,
      "title": "The Godfather",
      "overview": "Spanning the years 1945 to 1955...",
      "posterPath": "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
      "backdropPath": "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
      "releaseDate": "1972-03-14",
      "voteAverage": 8.7,
      "voteCount": 19845
    }
  }
}
```

## 📱 Fitur Firebase yang Diimplementasikan

### ✅ FirebaseManager.java
Singleton class untuk mengelola semua operasi Firebase:

1. **getPopularMovies()** - Load semua film populer
2. **searchMovies(query)** - Cari film by title
3. **getMovieById(id)** - Get detail film specific
4. **saveMovie(movie)** - Simpan/update film
5. **initializeWithDummyData()** - Populate database pertama kali
6. **isDatabaseEmpty()** - Check apakah database kosong

### 🎯 Features
- ✅ Offline persistence enabled (data tersimpan di cache)
- ✅ Real-time updates
- ✅ Error handling yang robust
- ✅ Callback pattern untuk async operations
- ✅ Search functionality
- ✅ Auto-initialize dengan dummy data

## 🔧 Penggunaan di Activity

### MainActivity
```java
FirebaseManager.getInstance().getPopularMovies(new FirebaseManager.FirebaseCallback<List<Movie>>() {
    @Override
    public void onSuccess(List<Movie> movies) {
        // Update UI dengan data
        movieAdapter.setMovies(movies);
    }
    
    @Override
    public void onError(Exception e) {
        // Handle error
        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
});
```

### SearchActivity
```java
FirebaseManager.getInstance().searchMovies(query, new FirebaseManager.FirebaseCallback<List<Movie>>() {
    @Override
    public void onSuccess(List<Movie> results) {
        // Tampilkan hasil search
        movieAdapter.setMovies(results);
    }
    
    @Override
    public void onError(Exception e) {
        // Handle error
    }
});
```

## 🔒 Security Rules (Production)

Untuk production, ubah rules menjadi:

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

## 📊 Database Structure

```
movie-app-database/
├── popular_movies/
│   ├── 1/
│   │   ├── id: 1
│   │   ├── title: "..."
│   │   ├── overview: "..."
│   │   ├── posterPath: "..."
│   │   ├── backdropPath: "..."
│   │   ├── releaseDate: "..."
│   │   ├── voteAverage: 8.7
│   │   └── voteCount: 25847
│   ├── 2/
│   └── ...
└── search_index/ (future feature)
```

## 🚀 Performance Tips

1. **Offline Persistence**: Data di-cache otomatis
2. **Single Value Event**: Gunakan untuk one-time reads
3. **Value Event Listener**: Gunakan untuk real-time updates
4. **Indexing**: Tambahkan indexes di Firebase Console untuk queries yang complex

## 🐛 Troubleshooting

### Error: "google-services.json not found"
- Pastikan file ada di folder `app/`
- Sync gradle lagi

### Error: "Database connection failed"
- Check internet connection
- Pastikan Realtime Database sudah enabled di Firebase Console
- Check Firebase rules

### Data tidak muncul
- Check Firebase Console apakah data sudah ada
- Check Logcat untuk error messages
- Pastikan rules allow read access

## 📝 Next Steps

1. ✅ Replace dummy data di MainActivity dengan Firebase
2. ✅ Replace dummy data di SearchActivity dengan Firebase
3. ✅ Add loading states
4. ✅ Add error handling
5. ✅ Test offline functionality
6. ⏳ Add pagination untuk large datasets
7. ⏳ Add real-time updates
8. ⏳ Implement proper authentication
9. ⏳ Optimize queries dengan indexing

---

**Status**: Firebase setup lengkap, siap digunakan!
**Mode**: Development (test mode rules)
**Database**: Realtime Database dengan offline persistence
