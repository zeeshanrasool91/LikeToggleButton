package com.zeeshan.likeanimation


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import android.widget.ToggleButton


class LikeToggleButton : ToggleButton, CompoundButton.OnCheckedChangeListener {

    private var favoriteBackground = -1
    private var unFavoriteBackground = -1
    private var likeColor = -1
    private var unlikeColor = -1

    private var likeToggleListener: LikeToggleListener? = null

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
                likeColor = attrArray.getResourceId(R.styleable.LikeToggleButton_like_color, likeColor)
                unlikeColor = attrArray.getResourceId(R.styleable.LikeToggleButton_unlike_color, unlikeColor)
            } finally {
                //attrArray.recycle()
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
        setToggleIcons()
        setOnCheckedChangeListener(this)

    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        buttonView.startAnimation(initAnimations())
        if (likeToggleListener != null) {
            likeToggleListener!!.liked(isChecked)
        }
    }
    private fun setToggleIcons(){
        val states = arrayOf(intArrayOf(android.R.attr.state_checked), // checked
                intArrayOf(-android.R.attr.state_checked) // unchecked
        )
        val drawables = arrayListOf(ContextCompat.getDrawable(context, if (favoriteBackground == -1) {
            R.drawable.ic_favorite
        } else {
            favoriteBackground
        }),
                ContextCompat.getDrawable(context, if (unFavoriteBackground == -1) {
                    R.drawable.ic_favorite_border
                } else {
                    unFavoriteBackground
                }))
        val toggleIconStates = StateListDrawable()
        toggleIconStates.addState(states[0], drawables[0])
        toggleIconStates.addState(states[1], drawables[1])
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(toggleIconStates)
        } else {
            background = toggleIconStates
        }
        setToggleIconColors(drawables)
    }

    private fun setToggleIconColors(drawables: ArrayList<Drawable?>){
        if (likeColor == -1) {
            drawables[0]!!.setColorFilter(ContextCompat.getColor(context, getThemeAccentColor(context)), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            drawables[0]!!.setColorFilter(ContextCompat.getColor(context, likeColor), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        if (unlikeColor == -1) {
            drawables[1]!!.setColorFilter(ContextCompat.getColor(context, getThemeAccentColor(context)), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            drawables[1]!!.setColorFilter(ContextCompat.getColor(context, unlikeColor), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    interface LikeToggleListener {
        fun liked(isLike: Boolean)
    }


    private fun getThemeAccentColor(context: Context): Int {
        val colorAttr: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = context.resources.getIdentifier("colorAccent", "attr", context.packageName)
        }
        val outValue = TypedValue()
        context.theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }


}
