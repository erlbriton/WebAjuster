package org.example.project.utils

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.browser.window
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.example.project.utils.DeviceInfo // Убедись, что путь к DeviceInfo верный
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


external interface JsFile : JsAny {
    val name: String
}

// --- 1. Мостики для вызова методов и свойств ---

@JsFun("(obj) => obj.name")
private external fun getJsName(obj: JsAny): String

@JsFun("(obj) => obj.kind")
private external fun getJsKind(obj: JsAny): String

@JsFun("(obj) => obj.done")
private external fun getJsDone(obj: JsAny): Boolean

@JsFun("(obj) => obj.value")
private external fun getJsValue(obj: JsAny): JsAny

@JsFun("(arr, index) => arr[index]")
private external fun getJsElement(arr: JsAny, index: Int): JsAny

@JsFun("(handle) => handle.entries()")
private external fun getEntries(handle: JsAny): JsAny

@JsFun("(iterator) => iterator.next()")
private external fun callNext(iterator: JsAny): JsAny

@JsFun("(handle) => handle.getFile()")
private external fun callGetFile(handle: JsAny): JsAny

@JsFun("(file) => file.text()")
private external fun callText(file: JsAny): JsAny

// --- 2. Мостики для работы с Promise и Callback ---

@JsFun("(promise, callback) => { promise.then(callback); }")
private external fun subscribeToPromise(promise: JsAny, callback: (JsAny) -> Unit)

@JsFun("""
    (callback) => {
        window.showDirectoryPicker()
            .then(callback)
            .catch(() => callback(null));
    }
""")
private external fun showPickerNative(callback: (JsAny?) -> Unit)

@JsFun("""
    (callback) => {
        window.showOpenFilePicker({
            types: [{
                description: 'Config',
                accept: {'text/plain': ['.ini', '.txt']}
            }],
            multiple: false
        }).then(h => callback(h[0])).catch(() => callback(null));
    }
""")
private external fun showFilePickerNative(callback: (JsAny?) -> Unit)

// --- 3. Вспомогательная функция ожидания ---

suspend fun awaitPromise(promise: JsAny): JsAny = suspendCoroutine { cont ->
    subscribeToPromise(promise) { res -> cont.resume(res) }
}

// --- 4. Основная логика ---

actual suspend fun pickDirectory(): DeviceInfo? {
    val handle = suspendCoroutine<JsAny?> { cont ->
        showPickerNative { res -> cont.resume(res) }
    } ?: return null

    return scanForFirstIni(handle)
}

actual suspend fun pickSingleFile(): DeviceInfo? {
    val handle = suspendCoroutine<JsAny?> { cont ->
        showFilePickerNative { res -> cont.resume(res) }
    } ?: return null

    return parseIniFile(handle)
}

private suspend fun scanForFirstIni(dirHandle: JsAny): DeviceInfo? {
    val iterator = getEntries(dirHandle)
    val subFolders = mutableListOf<JsAny>()

    while (true) {
        val nextPromise = callNext(iterator)
        val result = awaitPromise(nextPromise)
        if (getJsDone(result)) break

        val value = getJsValue(result)
        val entry = getJsElement(value, 1)
        val kind = getJsKind(entry)
        val name = getJsName(entry)

        if (kind == "file" && name.endsWith(".ini")) {
            return parseIniFile(entry)
        } else if (kind == "directory") {
            subFolders.add(entry)
        }
    }

    for (folder in subFolders) {
        val found = findIniInDirectory(folder)
        if (found != null) return found
    }
    return null
}

private suspend fun findIniInDirectory(dirHandle: JsAny): DeviceInfo? {
    val iterator = getEntries(dirHandle)
    while (true) {
        val nextPromise = callNext(iterator)
        val result = awaitPromise(nextPromise)
        if (getJsDone(result)) break

        val value = getJsValue(result)
        val entry = getJsElement(value, 1)
        val kind = getJsKind(entry)
        val name = getJsName(entry)

        if (kind == "file" && name.endsWith(".ini")) {
            return parseIniFile(entry)
        }
    }
    return null
}

// Предполагаем, что handle — это объект файла из JS (File или Blob)
private fun parseIniFile(handle: JsAny?): DeviceInfo? {
    if (handle == null) return null

    // В Wasm приведение к external interface через 'as'
    // работает для объектов, пришедших из JS
    val file = handle as JsFile
    val fileName = file.name

    println("DEBUG: Прочитано имя файла: $fileName")

    return DeviceInfo(
        id = fileName,
        location = ""
    )
}