<h1 align="center">Notepad</h1>

![Compose BOM](https://img.shields.io/badge/Compose%20Bom-2026.03.00-blue)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple)
![Api](https://img.shields.io/badge/API-26+-green)

**Notepad** a simple and minimalistic android app📱 for notes📃
Based on *Room* and *Jetpack Compose* with **MVVM** architecture.

## 🖼️Gallery
#### Screenshots in ☀️light/🌙dark themes
<table>
    <tr>
        <td>
            <img src="previews/preview (1).jpg">
        </td>
        <td>
            <img src="previews/preview (2).jpg">
        </td>
        <td>
            <img src="previews/preview (4).jpg">
        </td>
        <td>
            <img src="previews/preview (3).jpg">
        </td>
    </tr>
</table>

## 🦾Functionality

#### Base:
* creating a note
* deleting a note
* editing a note
* deleting all notes

#### Additional:
* sharing a note
* searching specific note-s via search string
* editing all notes display settings
* editing note display settings

## 📖How to use?
The app's functionality is simple and convenient. The plus button at the bottom creates a new note. Tapping a note once opens it. Editing and action buttons are located on the right side of the note card, and you can also delete all notes. A search bar is available to help you find the note you need. Settings are also available, allowing you to customize options (such as how notes are displayed) to your liking.

## 🏗️Architecture patterns
* **MVVM**
* **Repository pattern**

## 📚Tech stack
* **Dagger-Hilt** - dependency injection
* **Room** - store all notes in database
* **Ksp** - Room and Dagger-Hilt implementation
* **Viewmodel** - store and update states
* **Data store** - store app settings parameters
* **Google Protobuf** - structuring application settings flags and parameters for store in a datastore

## 🔃Android Versions
Android **8.0** and later