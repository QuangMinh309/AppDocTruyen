## 🏗️ Kiến trúc
# Clean Architecture kết hợp với MVVM + Jetpack Compose + Hilt

---

## 🔧 Công nghệ sử dụng

-  Kotlin
-  Jetpack Compose
-  MVVM
-  Clean Architecture
-  Hilt (Dependency Injection)
-  Retrofit (API)
-  Coroutines
...
  
---

## 🧭 Sơ đồ kiến trúc

```
          UI (Jetpack Compose)
                 │
       ViewModel (State holder)
                 │
     Repository (data abstraction)
                 │
    ┌────────────┴───────────────┐
    │ API │
    │ (Retrofit) │
    └────────────────────────────┘
```  

---

## 🧱 Cấu trúc thư mục

```
app/
├── java/
│   └── com.example.appname/
│       ├── activity/                        # Chứa MainActivity hoặc các Activity khác
│       |   └──  MainActivity.kt
|       ├── data/                           # Tầng data: dữ liệu, network, repository
|       |   ├── model/                      # Các data class (dữ liệu từ API, DB)
|       |   ├── repository/                 # Repository triển khai lấy dữ liệu
|       |   └── api/                        # định nghĩa api giao tiếp với backend      
|       |        └──APISrvice.kt      
|       ├── util/                           # định nghĩa các hàm/tác vụ nhỏ, thường dùng lại nhiều nơi trong project.
|       |   └──UserPreferences.kt    
|       ├── di/                             # Dependency Injection (các module cung cấp đối tượng)
|       |   └──NetworkModule.kt          # Cung cấp Retrofit, OkHttp, ApiService, v.v.
|       ├── presentation/                   # Tầng trình bày: ViewModel và Navigation
│       |  └── viewmodel/                  # ViewModel quản lý trạng thái UI
|       ├── navigation/                 # Điều hướng các màn hình (Jetpack Navigation)
|       └── ui/                             # Giao diện người dùng (Compose)
|       │   ├── components/                 # Các thành phần UI nhỏ tái sử dụng (Button, TextField...)
|       │   ├── screen/                     # Các màn hình chính (HomeScreen, LoginScreen...)
|       │   └── theme/                      # Cấu hình màu sắc, typography, theme app
|       └── MyApplication.kt               # Lớp Application custom, dùng khởi tạo Hilt, cấu hình toàn cục
├── res/
├── AndroidManifest.xml
```

