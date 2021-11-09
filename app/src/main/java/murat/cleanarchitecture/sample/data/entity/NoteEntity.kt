package murat.cleanarchitecture.sample.data.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Room -> Entity
 *
 * Room Db'de yaratılacak olan her bir tabloyu temsilen oluşturulan veri sınıflarıdır.
 *
 *@ Entity annotation (dipnot)'u ile tanımlanır ve tableName parametresiyle tablonun ismi belirlenmiş olur.
 *
 * Tıpkı Domain katmanındaki oluşturulan modeller gibi consturactor (yapı) kısmında değişkenler oluşturulur ve bu değişkenler @ColumnInfo ile Room'a tanıtılır.
 *
 * Ayrıca;
 *
 * * @ PrimaryKey: Birincil anahtar alan değeri. autoGenerate parametresi ile otomatik ardışık artan değer olarak tanımlanır.
 *
 * * @ NonNull: Boş bırakılamaz değer.
 *
 * * @ Nullable: Boş bırakılabilir değer.
 *
 *
 * vb. gibi annotation (dipnot)'ları kullanılabilir.
 *
 *
 *
 * Not: Veri sınıfları olduğu için constructor (yapı) içerisinde tanımlanan değişkenler veri tutabilir.
 *
 * Örneğin create_at değişkeni için Date().time ile o an ki zamanın Long tipindeki değeri otomatik tanımlanmış olur.
 **/

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @NonNull
    @ColumnInfo(name = "title")
    val title: String,

    @NonNull
    @ColumnInfo(name = "content")
    val content: String,

    @Nullable
    @ColumnInfo(name = "color")
    val color: String?,

    @NonNull
    @ColumnInfo(name = "create_at")
    val create_at: Long = Date().time,

    @Nullable
    @ColumnInfo(name = "update_at")
    val update_at: Long?
)