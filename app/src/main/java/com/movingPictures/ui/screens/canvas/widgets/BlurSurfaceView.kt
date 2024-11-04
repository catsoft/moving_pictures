package com.movingPictures.ui.screens.canvas.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

// not my solution, get it from here, cut and modified
// https://proandroiddev.com/creating-dynamic-background-blur-with-jetpack-compose-in-android-c53bef7fb98a
// not the exact right to designs, but it's a good start
@Composable
fun BlurSurface(
    modifier: Modifier,
) {
    val context = LocalContext.current
    val decorView = (context as Activity).window.decorView
    val parent: ViewGroup = decorView.findViewById(android.R.id.content)

    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        AndroidView(
            factory = {
                BlurSurfaceView(parent.context)
            },
            modifier = Modifier
                .fillMaxSize(),
            update = { blurView ->
                blurView.setParent(
                    parent = parent
                )
            }
        )
    }
}

class BlurSurfaceView : View {
    private var canvas: Canvas? = null
    private lateinit var bitmap: Bitmap
    private lateinit var parent: ViewGroup

    private var renderScript: RenderScript? = null
    private lateinit var blurScript: ScriptIntrinsicBlur
    private lateinit var outAllocation: Allocation

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init(measuredWidth, measuredHeight)
    }

    private fun init(measuredWidth: Int, measuredHeight: Int) {
        bitmap = Bitmap.createBitmap(
            measuredWidth,
            measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.restore()
    }

    private fun getBackgroundAndDrawBehind() {
        //Arrays to store the co-ordinates
        val rootLocation = IntArray(2)
        val viewLocation = IntArray(2)

        parent.getLocationOnScreen(rootLocation) //get the parent co-ordinates
        this.getLocationOnScreen(viewLocation) //get view co-ordinates

        //Calculate relative co-ordinates
        val left: Int = viewLocation[0] - rootLocation[0]
        val top: Int = viewLocation[1] - rootLocation[1]

        canvas?.save()
        canvas?.translate(-left.toFloat(), -top.toFloat()) //translates the initial position
        canvas?.let {
            try {
                parent.draw(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        canvas?.restore()
        blurWithRenderScript()
    }

    private fun blurWithRenderScript() {
        renderScript = RenderScript.create(context)
        blurScript = ScriptIntrinsicBlur.create(renderScript, android.renderscript.Element.U8_4(renderScript))

        if (this::bitmap.isInitialized.not()) return
        val inAllocation = Allocation.createFromBitmap(renderScript, bitmap)
        outAllocation = Allocation.createTyped(renderScript, inAllocation.type)
        blurScript.setRadius(20f)
        blurScript.setInput(inAllocation)

        blurScript.forEach(outAllocation)
        outAllocation.copyTo(bitmap)

        inAllocation.destroy()
    }

    fun setParent(parent: ViewGroup) {
        this.parent = parent
        this.parent.viewTreeObserver.removeOnPreDrawListener(drawListener)
        this.parent.viewTreeObserver.addOnPreDrawListener(drawListener)
    }

    private val drawListener =
        ViewTreeObserver.OnPreDrawListener {
            getBackgroundAndDrawBehind()
            true
        }
}