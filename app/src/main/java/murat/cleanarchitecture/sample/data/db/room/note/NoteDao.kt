package murat.cleanarchitecture.sample.data.db.room.note

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.data.entity.NoteEntity

/**
 * Room -> Dao (Database Access Object)
 *
 * Veritabanında bir tablosu bulunan/bulunacak her bir feature için oluşturulan interface'lerdir.

 * Bu dosyalar ilgili tablo üzerinde yapılacak CRUD işlemlerinin fonksiyonlarını barındırırlar.
 * Bu fonksiyonlardan Read (Okuma, Listeleme) işlemi için olan fonksiyonun bir Query (sorgu) olduğu annotation (dipnot) edilir.
 *
 * Update, Insert ve Delete işlemleri için ise Room DB'in sağladığı hazır annotate'ler kullanılabilir.
 *
 *
 * Not: Insert işlemi sonrası kaydın gerçekleşip gerçekleşmediğini anlayabilmek için fonksiyona Long tipinde bir dönüş yaptırması sağlanabilir.
 * Ayrıca conflict (çatışma, çakışma) olması durumunda yine Room'un sağladığı bir stratejiyi belirlemek için hazır annotate'lerden biri kullanılır.
 *
 * Aynı primery key ile kayıt geldiği zaman;
 *
 * * OnConflictStrategy.REPLACE: Var olan kaydı yenisi ile günceller. İşlem yapıldığı (veritabanında bir satır/satırlar etkilendiği) için asla -1 değeri döndürmez.

 * * OnConflictStrategy.IGNORE: Çakışma işlemini görmezden gelir. Kayıt işlemi gerçekleşmediği (veritabanında hiç bir satır etkilenmediği) için -1 değeri döndürür.

 * * OnConflictStrategy.ABORT: İşlemi iptal eder.
 *
 **/

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY create_at DESC")
    fun getAllNotes(): MutableList<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)
}