<h1 align="center">Notepad</h1>

![Compose BOM](https://img.shields.io/badge/Compose%20Bom-2026.03.00-blue)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple)
![Min Sdk](https://img.shields.io/badge/Min%20Sdk-26-green)

**Notepad** a simple and minimalistic android app📱 for notes📃
Based on *Room* and *Jetpack Compose* with **MVVM** architecture.

## 🖼️Gallery
#### Screenshots in ☀️light/🌙dark themes
<table>
    <tr>
        <td>
            <img src="previews/preview (1).jpg">
            <p align="center">1</p>
        </td>
        <td>
            <img src="previews/preview (2).jpg">
            <p align="center">2</p>
        </td>
        <td>
            <img src="previews/preview (4).jpg">
            <p align="center">3</p>
        </td>
        <td>
            <img src="previews/preview (3).jpg">
            <p align="center">4</p>
        </td>
    </tr>
</table>

## 🔃Android Versions
Android **8.0** and later

## 🦾Possibilities
#### 👉Here you can:
* creating a note
* deleting a note
* editing a note
* deleting all notes
* sharing a note

## 🌟Features
* grid notes list mode
* note last edit time view
* edit note text size
* enable/disable note text wrap mode

## 📚Tech stack
* **Dagger-Hilt** - dependency injection
* **Room** - store all notes in database (*repository pattern* with dependency injection)
* **Ksp** - Room and Dagger-Hilt implementation
* **Viewmodel** - store and update states
* **Data store** - store app settings parameters (*repository pattern* with dependency injection)

## 📖How to use?
App functionality is simple and easy. The plus button at the bottom creates a new note. A single tap on a note will view it, and a long press will bring up a menu with options for editing or deleting it. You can also delete all notes, and there are also mini settings where you can change the notes list display mode to grid.