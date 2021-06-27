package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription=R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription=R.string.not_hazardous_asteroid_image.toString()

    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}


@BindingAdapter("ListData")
fun bindRecyclerView(recyclerView:RecyclerView, list:List<Asteroid>?){
    val adapter=recyclerView.adapter as AsteroidAdapter
    adapter.submitList(list)
}

@BindingAdapter("NasaImage")
fun bindImageOftheDay(imageView: ImageView,pictureOfDay: PictureOfDay?){
    if(pictureOfDay!=null)
    {
        Picasso.with(imageView.context).load(pictureOfDay.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(imageView)}
    else
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
}