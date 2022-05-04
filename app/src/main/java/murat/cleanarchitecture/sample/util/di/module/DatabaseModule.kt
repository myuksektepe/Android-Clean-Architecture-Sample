package murat.cleanarchitecture.sample.util.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import murat.cleanarchitecture.sample.feature_note.data.data_source.NoteDao
import murat.cleanarchitecture.sample.feature_note.data.data_source.NoteDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): NoteDatabase {
        return Room
            .databaseBuilder(context, NoteDatabase::class.java, "app_database.data_source")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }
}