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

class IconsLoader(private var songs: Songs) {

    @Volatile private var starts = 0
    @Volatile private var ready = 0

    fun execute(): Boolean {
        if (starts == 0) {
            loadIcons()
        }
        return ready == starts
    }
    private fun loadIcons() {
        Log.i("MikeKuzn", "Start loading icons")
        for(i in 0 until songs.fullSize()) {
            val path = songs.getPath(i)
            path?.let {
                starts++
                CoroutineScope(Dispatchers.Default).launch {
                    loadIcon(path, i) }
            }
        }
    }
    private fun loadIcon(path: String, num: Int) {
        val mmr = MediaMetadataRetriever()
        try {
            mmr.setDataSource(path)
            val data = mmr.embeddedPicture
            // convert the byte array to a bitmap
            data?.let {
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                bitmap?.let {
                    songs.setBitmap(num, bitmap)
                }
            }
        } catch (e: IllegalArgumentException) {
            Log.i("MikeKuzn", "decode icon error")
        } finally {
            CoroutineScope(Dispatchers.Main).launch {
                ready++;
            }
        }
    }
}