package com.zeeshan.likeanimation

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import android.widget.ToggleButton

class LikeToggleButton : ToggleButton, CompoundButton.OnCheckedChangeListener {

    private var favoriteBackground = 0
    private var unFavoriteBackground = 0

    internal var likeToggleListener: LikeToggleListener? = null

    fun setLikeToggleListener(likeToggleListener: LikeToggleListener) {
        this.likeToggleListener = likeToggleListener
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // Set Attirbutes here
        if (attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.LikeToggleButton, 0, 0)
            try {
                //likeToggleListener = (LikeToggleListener) this;
                favoriteBackground = attrArray.getResourceId(R.styleable.LikeToggleButton_like_icon, favoriteBackground)
                unFavoriteBackground = attrArray.getResourceId(R.styleable.LikeToggleButton_unlike_icon, unFavoriteBackground)
            } finally {
                attrArray.recycle()
            }
        }
    }

    private fun initAnimations(): Animation {
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator
        return scaleAnimation
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        renderView()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        renderView()
    }

    private fun renderView() {
        //update View Status Here
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_selector))
        setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        buttonView.startAnimation(initAnimations())
        if (likeToggleListener != null) {
            likeToggleListener!!.liked(isChecked)
        }
    }

    interface LikeToggleListener {
        fun liked(isLike: Boolean)
    }
}
