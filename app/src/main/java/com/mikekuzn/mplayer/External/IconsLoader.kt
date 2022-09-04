package com.mikekuzn.mplayer.External

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import com.mikekuzn.mplayer.Entities.Songs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IconsLoader(private var appContext: Context, private var songs: Songs) {

    private var first = true
    private var length = -1
    private var ready = 0
    val pi = null

    fun execute(): Boolean {
        if (first) {
            first = false
            CoroutineScope(Dispatchers.Default).launch { loadIcons() }
        }
        if (ready == length)Log.i("MikeKuzn", "End for $ready")
        return ready == length
    }
    suspend fun loadIcons() {
        length = songs.fullSize() - 1;
        Log.i("MikeKuzn", "Start")
        for(i in 0..length) {
            val path = songs.getPath(i)
            path?.let {
                CoroutineScope(Dispatchers.Default).launch { loadIcon(path, i) }
            }
//            if (path == null) Log.i("MikeKuzn", "path null for $i")
        }
    }
    suspend fun loadIcon(path: String, num: Int) {
        var bitmap: Bitmap? = null
        val mmr = MediaMetadataRetriever()
        try {
            mmr.setDataSource(path)
            var data = mmr.embeddedPicture
            // convert the byte array to a bitmap
            data?.let {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                bitmap?.let {
                    songs.setBitmap(num, bitmap)
                }
//                if (bitmap == null) Log.i("MikeKuzn", "bitmap null for $num")
            }
//            if (data == null) Log.i("MikeKuzn", "picture null for $num")
        } catch (e: IllegalArgumentException) {
            Log.i("MikeKuzn", "decode icon error")
        }
        ready++;
    }
}