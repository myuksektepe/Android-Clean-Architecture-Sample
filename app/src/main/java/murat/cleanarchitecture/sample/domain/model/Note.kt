package murat.cleanarchitecture.sample.domain.model

import java.io.Serializable

/**
 *  Model
 *
 *  Local (yerel) ya da Remote (uzak) veri kaynağından gelen verileri serileştirmek için kullanılan veri sınıflarıdır.
 *
 *  Bir model objesinden baz alınarak oluşturulmuş listelerde döngüye sokulduklarında sırası gelen item (eleman)'ın
 *  içindeki bir veriye ulaşmak kolaylaşır.
 *
 *  Bu sınıfların constructor (yapı)'ları içerisinde oluşturulan değişkenler veritabanındaki ilgili tabloda bulunan
 *  her bir sutünü veya API'dan gelen verilerde ki her bir anahtarı temsil eder.
 */

data class Note(
    var id: Int,
    var title: String,
    var content: String,
    var color: String?,
    var create_at: Long,
    var update_at: Long?,
) : Serializable