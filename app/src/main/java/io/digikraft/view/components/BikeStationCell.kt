package io.digikraft.view.components

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import io.digikraft.R
import io.digikraft.models.BikeStation
import io.digikraft.utils.checkLocationPermission
import io.digikraft.utils.computeUserDistance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeStationCell(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 0.dp,
    data: BikeStation,
    userLocation: Location?,
    onItemClick: (BikeStation) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 0.5.dp),
        onClick = {
            onItemClick(data)
        },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        val context = LocalContext.current
        Column(Modifier.padding(20.dp)) {
            Text(
                text = "${String.format("%03d", data.index)} ${data.name}",
                style = TextStyle(color = Color.Black.copy(alpha = 0.67F), fontWeight = FontWeight.W700, fontSize = 20.sp)
            )
            Text(text = buildAnnotatedString {
                // Show user
                if (context.checkLocationPermission()) {
                    append(computeUserDistance(userLocation, LatLng(data.coordinates[1], data.coordinates[0])))
                    withStyle(SpanStyle(fontWeight = FontWeight.W700)) {
                        append("  •  ")
                    }
                }
                append("Bike Station")
            }, style = TextStyle(color = Color.DarkGray, fontSize = 13.sp))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.ic_bike), contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Available bikes", style = TextStyle(fontSize = 13.sp))
                    Text(
                        data.bikes,
                        style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color.Green)
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.ic_lock), contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Available places", style = TextStyle(fontSize = 14.sp))
                    Text(
                        data.freeRacks,
                        style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Black)
                    )
                }
            }
        }
    }
}