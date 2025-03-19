package com.klavs.e_commerceapp.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import kotlin.math.floor

enum class StarRatingSize {
    Small, Medium, Large
}

@Composable
fun StarRating(rating: Float, raters: Int, size: StarRatingSize) {
    val iconSize = when (size) {
        StarRatingSize.Small -> 13.dp
        StarRatingSize.Medium -> 20.dp
        StarRatingSize.Large -> 25.dp
    }
    val textSize = when(size) {
        StarRatingSize.Small -> 9.sp
        StarRatingSize.Medium -> 12.sp
        StarRatingSize.Large -> 15.sp
    }
    var fullStars = floor(rating).toInt()
    if (rating % 1 > 0.80f) fullStars ++
    val halfStar = if (rating - floor(rating) in 0.20f..0.80f) 1 else 0
    val emptyStars = 5 - fullStars - halfStar

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = Color.Yellow.copy(red = 0.7f, green = 0.7f),
                modifier = Modifier.size(iconSize)
            )
        }
        if (halfStar == 1) Icon(
            imageVector = Icons.AutoMirrored.Filled.StarHalf,
            contentDescription = null,
            tint = Color.Yellow.copy(red = 0.7f, green = 0.7f),
            modifier = Modifier.size(iconSize)
        )
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = "",
                tint = Color.Yellow.copy(red = 0.7f, green = 0.7f),
                modifier = Modifier.size(iconSize)
            )
        }
        Text(
           text = buildAnnotatedString {
               withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                   append(rating.toString())
               }
               append(" ($raters)")
           },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = textSize)
        )
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun StarRatingPreview() {
    ECommerceAppTheme {
        Card {
            Box(modifier = Modifier.padding(5.dp)) {
                StarRating(rating = 3.19f, raters = 95, StarRatingSize.Small)
            }
        }
    }
}