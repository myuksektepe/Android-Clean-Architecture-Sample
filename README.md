Clean Architecture (temiz mimari) ile Android uygulama geliştirmeyi öğreneceğimiz bu projede yapacağımız ilk işlem projemizin klasör yapısını şekillendirmek olacak.
(Bknz: Clean Arhitecture)

<p>
Aşağıda gördüğünüz üzere proje anadizinimizin altına şu klasörleri oluşturalım.

- presentation
    - view
        - activity
- domain
- data
- util

MainActivity'i presentation -> view -> activity'nin altına taşıyın.

Şimdi bu klasörleri neden oluşturduğumuzu, klasörlerin amaçlarını anlatarak açıklayalım. Aslında bunları klasör olarak değil birer katman (layer) olarak düşünmeliyiz. Her katmanın kendine ait görevleri vardır. Temiz mimarinin temel amaçlarından biri; birbirinden farklı görevlerdeki işlemlerin içiçe/karışık yapıda olmasını engellemektir. (Bknz: Spagetti Kod)

Sırasıyla bu katmanların hangi görevleri üstlendiklerine bakalım;
</p>

## 1. Presentation (Sunum) Katmanı

<p>
Bu katman kullanıcı ile etkileşimde olan, kullacının uygulama üzerindeki işlemlerini yapabildiği katmandır. Örneğin; Kitapları listeleme, kitap detaylarını görüntüleme, kitabı silme ve kitabı güncelleme gibi.

Dolasıyla içinde sadece bu işlemler için oluşturulan dosyaları barındırır.

Alt klasör yapısı;

- view
    - activity
    - fragment
- viewmodel
- adapter

</p>

## 2. Domain (Aracı) Katman

<p>
Bu katman Data katmanından aldığı verileri Presentation katmanına sunacak logic işlemlerinin yapıldığı katmandır. Presentation ile Data arasındaki aracı katmandır diyebiliriz. Bu katman içerisinde oluşturacağımız her dosya sadece bu amaca hizmet edecektir.

Alt klasör yapısı;

- model / entity
- mapper
- repository
- use_case

</p>

## 3. Data (Veri) Katmanı

<p>
Bu katmanda veri kaynakları için yazacağımız dosyalar yer alacaktır. Bu dosyalar remote (uzak) veri kaynakları yani API (Application Programming Interface)'lar ve local (yerel) database (veri tabanı)'lere bağlantı kurmamızı ve bu kaynaklar üzerinde CRUD (Create, Read, Update, Delete) işlemleri yapabilmemizi sağlar.

Alt klasör yapısı;

- data_source
- repository

Özetle; Prensentation katmanında kullanıcı işlemleri sonrası veri gönderme ya da alma işlemlerini Domain katmanı üzerinden Data katmanında ki veri kaynaklarına bildireceğiz.

Aynı şekilde Data katmanından aldığımız verileri Domain katmanı üzerinden Presentation katmanına göndereceğiz.

Yani katmanlar arası işlemler gidiş-geliş şeklinde çift yönlü olacak.
</p>

## 4. Util

<p>
Buna aslında bir katman değildir. Clean Architecture içinde önemli bir görevi yoktur. Uygulamamızı geliştirken plan dışında bazı dosyalara ihtiyacımız olacaktır. Onları düzenli tutmak için bu klasörü oluşturuyoruz.
</p>

----------------------
----------------------
----------------------


<b>1. Adım</b>

# Entitiy

<p>
Domain katmanında bulunan, veri tabanında oluşturulacak olan her bir tabloyu temsilen hazırlanan veri sınıflarıdır.

- Tablo içindeki alanların PrimaryKey, Nullable, Non-nullable vb gibi özellikleri burada tanımlanır.
- androidx.room.entity (@Entity) ile annotate edilir.
- Annotation içerisine tablo adı, indeks değerleri vb gibi değerler girilir.


1. Domain katmanımızın altına "entity" isminde bir alt klasör oluşturalım.
2. Veri tabanında oluşturacağımız kitaplar tablosu için [Feature]Entity isiminde data class (veri sınıfı) oluşturalım.

</p>

```kotlin
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    indices = [Index(value = ["title", "author"], unique = true)]
)
data class BookEntity(

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @Nullable
    @ColumnInfo(name = "page_count")
    val page_count: Int?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    )
```

----------------------
----------------------
----------------------

<b>2. Adım</b>

# Dao (Database Access Object)

<p>
Veri tabanında oluşturulacak her bir tablo için yazılan interface (arayüz)'lerdir.

- androidx.room.dao (@Dao) ile annotate edilir.

İçerisine yazacağımız fonksiyonları;

- Tablodan veri getirecek ise @Query,
- Tablodaki bir veriyi değiştirecek yani güncelleyecek ise @Update,
- Tablodaki bir veriyi silecek ise @Delete,
- Tabloya yeni bir veri ekleyecek ise @Insert ile annotate etmemiz gerekir.

<b>Not:</b>
Insert işleminde kayıt etmek istediğimiz verideki "id" değeri veri tabanımızda daha önceden kayıtlı olması durumunda conflict (çakışma) sorunu ile karşılaşırız. Bu sorunu ortadan kaldırmak için @Insert annotation'ın içine onConflict parametresi tanımlanmalı ve değer olarak OnConflictStrategy sınıfı altındaki uygun bir değer atanmalıdır.

<b>Not 2:</b>
Bütün fonksiyonlarımız "suspend" olarak tanımlanması gerekirken, bir liste tipinde dönüş yapacak fonksiyonlar için dönüş tiplerinde Flow yapısı kullanılmalıdır bu yüzden bu fonksiyonların "suspend" olarak tanımlanmasına gerek yoktur.  
Örneğin; tablodaki bütün kitapları getirmek için yazacağımız "getBooks" fonksiyonu.

(Bknz: Suspend Fun)
(Bknz: Kotlin Flow)

1. Data katmanı içerisine "data_source" isimli bir alt klasör oluşturalım.
2. İlgili tablomuzda CRUD işlemlerini yapmak için gerekli fonksiyonları yazacağımız "BookDao" interface'ini oluşturalım.

</p>

```kotlin
import androidx.room.*
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.presentation.model.ResultState
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookByID(id: Int): BookEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(bookEntity: BookEntity): Long

    @Update
    suspend fun updateBook(bookEntity: BookEntity)

    @Delete
    suspend fun deleteBook(bookEntity: BookEntity)
}
```

----------------------
----------------------
----------------------

<b>3. Adım</b>

# Database

<p>
Uygulamamız için veri tabanı oluşturacak abstract class'ımızdır.

- androidx.room.Database (@Database) ile annotate edilir.
- @Database içerisine entities, version, export_schema gibi parametreler için değerler girilmelidir.
- RoomDatabase sınıfından kalıtılır.
- İçerisinde abstract fun olarak Dao dönüş tipinde fonksiyonlar tanımlanır.

(Bknz: Abstract Class)

1. Data katmanı içerisine "data_source" isimli alt klasör içinde "AppDatabase" ya da "[Feature]Database" gibi açıklayıcı bir isme sahip dosya oluşturalım.
2. Database sınıfımızın içini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
import androidx.room.Database
import androidx.room.RoomDatabase
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity

@Database(
    version = 1,
    entities = [BookEntity::class],
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
```

----------------------
----------------------
----------------------

<b>4. Adım</b>

# Model

<p>

</p>

```kotlin

```


----------------------
----------------------
----------------------

<b>5. Mapper</b>

# Mapper

<p>
Buraya kadar yaptığımız her işlem local database üzerine olduğu için Entity'i kullanarak ilerledik. 
Bundan sonra ki işlemlerde Data katmanı ile Presentation katmanı arasındaki veri alış-verişi yapılarını kuracağız.
Presentation katmanında Entity'leri kullanamayacağımız için -genellikle- aynı yapıya sahip olan Modelleri oluşturacağız.

Mapper'lara ihtiyacımız tam da burada ortaya çıkıyor. Yani 

- Data katmanından Presentation katmanına veri gönderirken Entity'den Model'e, 
- Presentation katmanından Data katmanına veri gönderirken Model'den Entity'e 
    
çevirme işlemlerini Mapper'lar ile yaparız.


1. Data katmanı içerisine "mapper" isimli alt klasör oluşturalım.
2. Mapper isimli bir dosya oluşturalım ve içini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.presentation.model.BookModel

fun BookEntity.toModel(): BookModel {
    return BookModel(
        id = this.id,
        title = this.title,
        author = this.author,
        page_count = this.page_count
    )
}

fun BookModel.toEntitiy(): BookEntity {
    return BookEntity(
        id = this.id,
        title = this.title,
        author = this.author,
        page_count = this.page_count
    )
}
```


----------------------
----------------------
----------------------

<b>6. Adım</b>

# LocalDataSource

<p>
Şimdiye kadar bahsettiğimiz üzere 2 farklı veri kaynağımız olabilir. Uzak sunucu yani API ve/veya yerel bir veritabanı kullanarak veri alış-veriş işlemlerini yapabiliriz.

(Bu projemizde sadece yerel veritabanı kullanacağımızı hatırlatmak isterim)

Uygulamamızda veri kaynağına ulaşacağımız fonksiyonlar Repository ile sağlanacak. Fakat öncesinde hangi veri kaynağı/kaynakları ile devam edeceğimizi belirlememiz gerekiyor.
Bunun için LocalDataSource ve RemoteDataSource diye isimlendirdiğimiz dosyalara ihtiyacımız var.

Yerel veri tabanı için devam edecek olursak;

1. Data katmanında ki "repository" isimli alt klasörümüze gidelim.
2. LocalDataSource isimli interface'imizi aşağıdaki gibi oluşturalım.
</p>

```kotlin
import com.muratlakodla.aca.feature_book.presentation.model.BookModel

interface BookLocalDataSource {
    suspend fun getBooks(): MutableList<BookModel>
    suspend fun getBookById(id: Int): BookModel
    suspend fun insertBook(bookModel: BookModel): Long
    suspend fun updateBook(bookModel: BookModel)
    suspend fun deleteBook(bookModel: BookModel)
}
```

----------------------
----------------------
----------------------

<b>7. Adım</b>

# LocalDataSourceImpl

<p>

</p>

```kotlin

```

----------------------
----------------------
----------------------

<b>8. Adım</b>

# Repository

<p>
Direkt olarak veri kaynağına ulaşmak amacıyla yazılan interface'lerdir.

- Test işlemini kolaylaştırmak için interface tipindedirler. (İlerde göreceğiz.)
- İçerisinde ilgili Feature'ın veri tabanı üzerinde yapılacak işlemleri için tanımlanan fonksiyonları barındırır.
- Flow tipinde dönüşü olmayan fonksiyonlar için suspend function'lar kullanılmalıdır.
- Interface'ler içinde logic işlemler barındıramadığı ve Hilt'e bildirilemediği için kendisinden kalıtılan Implementation'lara ihtiyaç duyarlar.

<b>Not:</b>
Uygulama içerisinde veri tabanı üzerindeki işlemler Repository'lerden değil her bir veri tabanı işlemi için ayrı ayrı oluşturacağımız UseCase'ler üzerinden sağlanır.

<b>Not 2:</b>
Repository'ler Data yerine Domain katmanında bulunmalıdırlar.

1. Domain katmanı içerisine "repository" isimli bir alt klasör oluşturalım.
2. "[Feature]Repository" isimli, interface tipinde bir dosya oluşuturup içerisini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(): Flow<List<BookEntity>>
    suspend fun getBookByID(id: Int): BookEntity?suspend fun updateBook(bookEntity: BookEntity)
    suspend fun deleteBook(bookEntity: BookEntity)
}

```

----------------------
----------------------
----------------------

<b>9. Adım</b>

# RepositoryImpl

<p>
Bir önceki adımda oluşturduğumuz Repository'lerden kalıtılan sınıflardır. 

- Veri alış-verişinin hangi kaynak üzerinden (API / Local Database) burada karar veririz.
- Eğer veri kaynağı olarak local database (Room) seçilirse consturactor'ları içerisine Dao tanımlanmalıdır.
- İçerisinde yer alacak her bir override fonksiyon içinde ilgili veri kaynağının ilgili fonksiyonu çağırılır.

1. Data katmanı içerisine "repository" isimli bir alt klasör oluşturalım.
2. "[Feature]RepositoryImpl" isimli bir class oluşuturup içerisini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
import com.muratlakodla.aca.feature_book.data.data_source.BookDao
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {
    override fun getBooks(): Flow<List<BookEntity>> {
        return bookDao.getBooks()
    }

    override suspend fun getBookByID(id: Int): BookEntity? {
        return bookDao.getBookByID(id)
    }

    override suspend fun updateBook(bookEntity: BookEntity) {
        bookDao.updateBook(bookEntity)
    }

    override suspend fun deleteBook(bookEntity: BookEntity) {
        bookDao.deleteBook(bookEntity)
    }

}

```

----------------------
----------------------
----------------------

<b>10. Adım</b>

# UseCase

<p>

Kullanıcının (ilgili feature'da) uygulamamız üzerinde yapacağı her işlem için ayrı oluşturacağımız sınıflardır.

- Oluşturuluş amaçlarına göre tek fonksiyon içerirler ve amaca göre isimlendirilirler. Örneğin; "GetBooksUseCase" ya da "GetBooks"
- Veri kaynağına ulaşıp bu kaynak üzerinde işlem yapacağı için constructor (kurucu yapı)'ları içerisinde ilgili Repository tanımlanmalıdır.
- Bussiness logic işlemleri yapıldığı için Domain katmanında yer alırlar.

1. Domain katmanı içerisine "use_case" isimli bir alt klasör oluşturalım.
2. "[Operation][Feature]UseCase" isimli bir class oluşuturup içerisini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<List<BookEntity>> = repository.getBooks()
}
```

```kotlin
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.domain.repository.BookRepository

class GetBookByIdUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(id: Int): BookEntity? = repository.getBookByID(id)
}
```

```kotlin
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.domain.repository.BookRepository

class UpdateBookUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookEntity: BookEntity) = repository.updateBook(bookEntity)
}
```

```kotlin
import com.muratlakodla.aca.feature_book.domain.entity.BookEntity
import com.muratlakodla.aca.feature_book.domain.repository.BookRepository

class DeleteBookUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookEntity: BookEntity) = repository.deleteBook(bookEntity)
}
```

----------------------
----------------------
----------------------

<b>11. Adım</b>

# UseCases

<p>
Oluşturduğumuz Use Case'leri ilerleyen zamanlarda ViewModel'ler içerisinde kullanıcı aksiyonları için kullanacağız.
Aynı ViewModel içinde birden fazla Use Case'i constructor içinde çağırmak yerine tek bir veri sınfında toplayıp bu sınıfı çağırmak
hem kod temizliği açısından hem de Hilt için modül oluşturken bize yardımcı olacaktır.

1. Domain katmanı içerisine "use_case" klasörümüze gidelim.
2. "[Feature]UseCases" isimli bir data class oluşuturup içerisini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
data class BookUseCases(
    val getBooksUseCase: GetBooksUseCase,
    val getBookByIdUseCase: GetBookByIdUseCase,
    val deleteBookUseCase: DeleteBookUseCase,
    val updateBookUseCase: UpdateBookUseCase
)
```

----------------------
----------------------
----------------------

<b>12. Adım</b>

# Application

<p>
Oluşturduğumuz bu dosyalardan sonra artık Hilt işlemlerine geçme vakti gelmiş oldu. Hilt işlemleri için ilk yapmamız gereken uygulamamız için Application sınıfından kalıtılmış bir sınıf oluşturmaktır.
Bu sınıf uygulamamızın en genel işlemlerini yapacağımız yerdir.

- android.app.Application sınıfından kalıtılmalıdır.
- dagger.hilt.android.HiltAndroidApp (@HiltAndroidApp) ile annotate edilmelidir.
- AndroidManifest.xml dosyasında, "application" etiketleri içerisine "android:name" anahtarı kullanılarak tanımlanmalıdır.

1. Projemizin ana dizinine gidelim.
2. "App" ya da "MyApp" gibi kolay anlaşılabilir şekilde isimlendirdiğimiz bir class oluşturalım ve içerisini aşağıdaki örnekteki gibi dolduralım.

</p>

```kotlin
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()
```

```xml

<application android:name="murat.cleanarchitecture.sample.App"......</application>
```

----------------------
----------------------
----------------------

<b>13. Adım</b>

# DI (Dependency Injection) -> Module

<p>
<b>Module Nedir?</b>

Module'ler; Inject edeceğimiz her bir değişken için ara bellekte oluşmasını sağlamak üzere Hilt'e tanıttığımız Object'lerdir.

- İçerisinde oluşturulan fonksiyon isimleri genellikle "provide" ile başlansa da aslında isimler önemli değildir. Önemli olan fonksiyonların dönüş tipleridir.
- Her modül için @InstallIn annotation'ı kullanılmalı ve içerisine Hilt'in sağlamış olduğu uygun bir sınıf tanımlanmalıdır.
- Her modül @Module ile annotate edilmelidir.
- Barındırdığı her fonksiyon ise @Provides ile annotate edilmelidir.

## 13.1. AppModule

AppModule içerisinde veri tabanımız ile alakalı olan bütün sınıflarımızı, @Singleton annotation'ını kullanarak provide etmemiz gerekir.

(Bknz: Singleton)

</p>
