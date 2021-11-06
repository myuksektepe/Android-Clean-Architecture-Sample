package murat.cleanarchitecture.sample.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import murat.cleanarchitecture.sample.data.db.room.note.NoteDao
import murat.cleanarchitecture.sample.data.entity.NoteEntity

/**
 * Room -> AppDatabase
 *
 * RoomDatase'den extend edilmiş (kalıtılmış) bir abstract sınıf olmalıdır.
 *
 * @ Database annotation (dipnot)'u kullanılarak tanımlanmaya başlanır ve parametre olarak
 * - entities: Data katmanında oluşturulan Entity'lerin liste hali,
 * - versionCode: Integer (tam sayı değeri) olarak veritabanının versiyon kodu,
 * - exportSchema: Veritabanını şemasının klasör içerisine çıkartılması için Boolean (doğru/yanlış) tipinde bir değer
 *
 * almalıdır.
 *
 * İçerisine, Data katmanında oluşrutulan her bir DAO (Database Access Object - Veritabanı erişim nesnesi)
 * için dönüş sağlayan Abstract fonksiyonlar tanımlanır.
 *
 */

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}