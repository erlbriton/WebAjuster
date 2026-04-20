package org.example.project.utils

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@JsFun("""
    (file) => {
        return new Promise((resolve) => {
            var reader = new FileReader();
            reader.onload = function(e) { resolve(e.target.result); };
            reader.readAsText(file, 'windows-1251');
        });
    }
""")
private external fun readFileAsText(file: JsAny): JsAny
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

// --- 3. Вспомогательная функция ожидания ---

suspend fun awaitPromise(promise: JsAny): JsAny = suspendCoroutine { cont ->
    subscribeToPromise(promise) { res -> cont.resume(res) }
}

// --- 4. Основная логика ---

actual suspend fun pickDirectory(): DeviceInfo? {
    println("DEBUG: Запуск showPickerNative...")

    val handle = suspendCoroutine<JsAny?> { cont ->
        showPickerNative { res ->
            println("DEBUG: JS вернул результат: $res")
            cont.resume(res)
        }
    }

    if (handle == null) {
        println("DEBUG: handle равен null (пользователь отменил или ошибка JS)")
        return null
    }

    println("DEBUG: handle получен, запускаем scanForFirstIni...")
    val result = scanForFirstIni(handle)

    if (result == null) {
        println("DEBUG: scanForFirstIni не нашел .ini файл!")
    } else {
        println("DEBUG: Успех! Нашли: ${result.id}")
    }

    return result
}

private suspend fun scanForFirstIni(dirHandle: JsAny): DeviceInfo? {
    val iterator = getEntries(dirHandle)
    val subFolders = mutableListOf<JsAny>()

    // 1. Собираем только подпапки
    while (true) {
        val nextPromise = callNext(iterator)
        val result = awaitPromise(nextPromise)
        if (getJsDone(result)) break

        val value = getJsValue(result)
        val entry = getJsElement(value, 1)
        val kind = getJsKind(entry)

        if (kind == "directory") {
            subFolders.add(entry)
        }
    }

    // 2. Ищем файлы ТОЛЬКО внутри найденных подпапок (рекурсивно)
    for (folder in subFolders) {
        // Заходим в папку и ищем там файлы
        val found = scanInsideFolder(folder)
        if (found != null) return found
    }

    return null
}

private suspend fun scanInsideFolder(folderHandle: JsAny): DeviceInfo? {
    val iterator = getEntries(folderHandle)
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

private suspend fun parseIniFile(fileHandle: JsAny): DeviceInfo {
    val file = awaitPromise(callGetFile(fileHandle))

    // Используем наш новый JS-мостик
    val text = awaitPromise(readFileAsText(file)).toString()

    var location = ""
    var id = ""

    text.lines().forEach { line ->
        if (line.startsWith("Location=")) location = line.substringAfter("=")
        if (line.startsWith("ID=")) id = line.substringAfter("=")
    }

    return DeviceInfo(id, location)
}