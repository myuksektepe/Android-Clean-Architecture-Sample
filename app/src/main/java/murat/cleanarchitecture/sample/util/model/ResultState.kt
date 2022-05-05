package murat.cleanarchitecture.sample.util.model

/**
 * TODO ResultState?
 */

sealed class ResultState<out T> {
    data class SUCCESS<out T>(val data: T? = null) : ResultState<T>()

    data class LOADING(
        val loadingResourceId: Int? = null,
        val loadingMessage: String? = null,
        val progress: Int? = 0,
    ) : ResultState<Nothing>()

    data class FAIL(
        val exception: Exception? = null,
        val errorTitle: String? = null,
        val errorMessage: String? = null,
        val buttonTitle: String? = null,
        val callback: () -> Unit? = { },
    ) : ResultState<Nothing>()

    fun toData(): T? = if (this is SUCCESS) this.data else null
}
