package com.jeffreyfhow.yogaflashcards

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by jeffreyfhow on 9/21/17.
 */

private val BTN_TINT_COLOR : Int = Color.argb(150, 155, 155, 155)

class TintImageButton : AppCompatImageButton {

    private var rect: Rect? = null
    lateinit var onTouchCallback: () -> Unit
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun isInRect(eX: Int, eY: Int) = rect?.contains(left + eX , top + eY) ?: false

    override fun onTouchEvent(me: MotionEvent?): Boolean {
        if (me?.action === MotionEvent.ACTION_DOWN) {
            setColorFilter(BTN_TINT_COLOR)
            rect = Rect(left, top, right, bottom)
            return true
        } else if (me?.action === MotionEvent.ACTION_UP) {
            clearColorFilter()
            if( isInRect(me.x.toInt(), me.y.toInt())) {
                onTouchCallback?.invoke()
            }
            return true
        } else if(me?.action == MotionEvent.ACTION_MOVE){
            if( isInRect(me.x.toInt(), me.y.toInt())){
                setColorFilter(BTN_TINT_COLOR)
            } else {
                clearColorFilter()
            }
        }
        return false
    }
}