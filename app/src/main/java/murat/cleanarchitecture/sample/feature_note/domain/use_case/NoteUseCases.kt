package murat.cleanarchitecture.sample.feature_note.domain.use_case

data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val insertNoteUseCase: InsertNoteUseCase,
)