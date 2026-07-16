package com.example.notepad.domain.model

import com.example.notepad.proto.SortNotesMode

fun SortNotesMode.toDomainEnum() = when (this) {
    SortNotesMode.Default -> SortNotesModeName.Default
    SortNotesMode.ByCreationTime -> SortNotesModeName.ByCreationTime
    SortNotesMode.ByContentSize -> SortNotesModeName.ByContentSize
    SortNotesMode.ByNameLength -> SortNotesModeName.ByNameLength
    SortNotesMode.ByLastEditTime -> SortNotesModeName.ByLastEditTime
    SortNotesMode.UNRECOGNIZED -> SortNotesModeName.Default
}

fun SortNotesModeName.toProtoEnum() = when (this) {
    SortNotesModeName.Default -> SortNotesMode.Default
    SortNotesModeName.ByCreationTime -> SortNotesMode.ByCreationTime
    SortNotesModeName.ByContentSize -> SortNotesMode.ByContentSize
    SortNotesModeName.ByNameLength -> SortNotesMode.ByNameLength
    SortNotesModeName.ByLastEditTime -> SortNotesMode.ByLastEditTime
}