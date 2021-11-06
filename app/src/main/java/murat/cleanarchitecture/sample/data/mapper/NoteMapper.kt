package murat.cleanarchitecture.sample.data.mapper

import murat.cleanarchitecture.sample.data.entity.NoteEntity
import murat.cleanarchitecture.sample.domain.model.Note

/**
 * Mapper (Feature)
 *
 * Room DB'de her bir tabloyu temsil için oluşturulan Data katmanında ki Entity'ler ile
 * Domain katmanında oluşuturulan Model'leri birbirine maplemek (haritalamak) için oluşturulan,
 * Entity'den Model'e ve Model'den Entity'e çeviri için gerekli fonksiyonları barındıran dosyalardır.
 *
 * Modelde yer alan değişkenler ile Entityde yer alan değişken isimleri farklı olabileceği için
 * burada değişkenlerin birbiriyle doğru şekilde eşleşmiş olması önemlidir.
 * */

fun NoteEntity.toNote(): Note {
    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        color = this.color,
        create_at = this.create_at,
        update_at = this.update_at,
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        color = this.color,
        create_at = this.create_at,
        update_at = this.update_at,
    )
}