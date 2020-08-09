package me.assel.moviedb.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import me.assel.moviedb.BuildConfig
import me.assel.moviedb.R

fun Fragment.showToast(resString: Int) = context?.showToast(resString)
fun Context.showToast(resString: Int) = showToast(getString(resString))
fun Fragment.showToast(message: String) = context?.showToast(message)
fun Context.showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun ImageView.loadImage(url: String?, isRound: Boolean = false) {
    Glide.with(context).load(url).error(R.drawable.ic_image).diskCacheStrategy(DiskCacheStrategy.ALL).apply(if (isRound) RequestOptions.circleCropTransform() else RequestOptions.noTransformation()).into(this)
}

fun ImageView.loadImage(source: Bitmap?, isRound: Boolean = false) {
    Glide.with(context).load(source).diskCacheStrategy(DiskCacheStrategy.ALL).apply(if (isRound) RequestOptions.circleCropTransform() else RequestOptions.noTransformation()).into(this)
}

fun ImageView.loadImage(uri: Uri, isRound: Boolean = false) {
    Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).apply(if (isRound) RequestOptions.circleCropTransform() else RequestOptions.noTransformation()).into(this)
}

fun ImageView.loadRoundedImage(url: String?, radius: Int) {
    Glide.with(context).load(url).error(R.drawable.ic_image).diskCacheStrategy(DiskCacheStrategy.ALL).transform(RoundedCorners(radius)).into(this)
}

fun ImageView.grayscale(set: Boolean) {
    colorFilter = if (set) ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) }) else null
}


fun Fragment.showSnackBar(message: String, indefinite: Boolean = false, cancelable: Boolean = false) = activity?.showSnackBar(message, indefinite, cancelable)
fun Fragment.showSnackBar(resString: Int, indefinite: Boolean = false, cancelable: Boolean = false) = activity?.showSnackBar(this.getString(resString), indefinite, cancelable)
fun Activity.showSnackBar(message: String, indefinite: Boolean = false, cancelable: Boolean = false): Snackbar {
    val snackbar = Snackbar.make(findViewById(android.R.id.content),
            message, if (indefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG)
    if (cancelable) snackbar.setAction(R.string.ok) { snackbar.dismiss() }
    val sbView = snackbar.view
    val textView = sbView
            .findViewById<TextView>(R.id.snackbar_text)
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    snackbar.show()
    return snackbar
}

fun Activity.createSnackBar(message: String, actionText: String? = null, action: (() -> Unit)? = null): Snackbar {
    return Snackbar.make(findViewById(android.R.id.content), message,
            if (!actionText.isNullOrBlank()) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG).apply {
        if (!actionText.isNullOrBlank()) {
            setAction(actionText) {
                action?.invoke()
                dismiss()
            }
            setActionTextColor(ContextCompat.getColor(this@createSnackBar, android.R.color.white))
        }
    }
}

fun Fragment.hideSoftInput() = activity?.hideSoftInput()
fun Activity.hideSoftInput() {
    var view = currentFocus
    if (view == null) view = View(this)
    val imm = this
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.isPackageInstalled(packagename: String): Boolean {
    return activity?.isPackageInstalled(packagename) ?: false
}

fun Context.isPackageInstalled(packagename: String): Boolean {
    try {
        val packageManager = packageManager
        packageManager?.let {
            packageManager.getPackageInfo(packagename, 0)
            return packageManager.getApplicationInfo(packagename, 0).enabled
        }
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
    return false
}

fun MenuItem.tintMenuItemIcon(color: Int) {
    val drawable = icon
    if (drawable != null) {
        val wrapped = DrawableCompat.wrap(drawable)
        drawable.mutate()
        DrawableCompat.setTint(wrapped, color)
        icon = drawable
    }
}

fun Activity.openUpdatePage() {
    val appId = BuildConfig.APPLICATION_ID
    try {
//        https://developer.android.com/distribute/marketing-tools/linking-to-google-play
        val intent = Intent(Intent.ACTION_VIEW).apply {
            Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            setPackage("com.android.vending")
        }
        startActivity(intent)
    } catch (anfe: android.content.ActivityNotFoundException) {
        showToast(R.string.something_went_wrong)
    }
}

fun Fragment.openUpdatePage() {
    val appId = BuildConfig.APPLICATION_ID
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appId")))
    } catch (exception: android.content.ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")))

    }
}

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.inflate(res: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(this).inflate(res, parent, false)
}

fun Fragment.showSoftInput() = activity?.showSoftInput()
fun Activity.showSoftInput() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
}