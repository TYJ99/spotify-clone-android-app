package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyj.spotifycloneandroidapp.domain.model.Song

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistInfo(
    modifier: Modifier = Modifier,
    songImage: Bitmap?,
    song: Song
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerImageItem(
            image = songImage,
            icon = Icons.Default.MusicNote,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            // modifier = Modifier.weight(1f)
        ) {}
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                //overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(),
                maxLines = 1
            )
            //Spacer(modifier = Modifier.size(3.dp))
            Text(
                text = song.subtitle,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
                //overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(),
                maxLines = 1
            )
        }
    }

}
