package com.example.playlistmaker
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imageMusic: ImageView = view.findViewById(R.id.imageMusic)
    private val trackName: TextView = view.findViewById(R.id.trackName)
    private val groupName: TextView = view.findViewById(R.id.groupName)
    private val longTimeMusic: TextView = view.findViewById(R.id.longTimeMusic)

    fun bind(track: Track) {
        trackName.text = track.trackName
        groupName.text = track.artistName
        longTimeMusic.text = track.trackTime

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)


        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .apply(requestOptions)
            .apply(RequestOptions().transform(RoundedCorners(2)))
            .into(imageMusic)
    }
}