package com.github.loadingbutton

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView


class LoadingButton @JvmOverloads constructor(
    ctx: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(ctx, attr, defStyle) {
    /**
     * loading layout
     */
    private var loadingLayout: View? = null
    /**
     * button text
     */
    private var buttonText: String? = null
    /**
     * button loading text
     */
    private var loadingText: String? = null
    /**
     * button loading textColor
     */
    private var loadingTextColor: Int? = null
    /**
     * button textColor
     */
    private var buttonTextColor: Int? = null
    /**
     * press alpha
     */
    private var pressAlpha = 0.7f
    /**
     * button textView
     */
    private val innerTextView by lazy {
        TextView(context).apply {
            id = generateViewId()
        }
    }

    var state: STATE =
        STATE.NORMAL
        set(value) {
            field = value
            when (value) {
                STATE.NORMAL -> {
                    resetState()
                }
                STATE.LOADING -> {
                    loadingLayout?.visibility = VISIBLE
                    alpha = pressAlpha
                    innerTextView.alpha = pressAlpha
                    isEnabled = false
                    if (loadingText != null) {
                        innerTextView.text = loadingText
                        loadingTextColor?.let { color ->
                            innerTextView.setTextColor(color)
                        }
                    } else {
                        innerTextView.visibility = GONE
                    }
                }
            }
        }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        context.obtainStyledAttributes(attr, R.styleable.LoadingButton).apply {
            buttonText = getString(R.styleable.LoadingButton_text)
            buttonTextColor = getColor(
                R.styleable.LoadingButton_textColor,
                Color.parseColor("#666666")
            )
            loadingText = getString(R.styleable.LoadingButton_loadingText)
            loadingTextColor =
                getColor(R.styleable.LoadingButton_loadingTextColor, Color.parseColor("#666666"))
            pressAlpha = getFloat(R.styleable.LoadingButton_pressAlpha, 0.7f)
            val loadingLayoutId =
                getResourceId(
                    R.styleable.LoadingButton_loadingLayout,
                    R.layout.layout_loading
                )
            loadingLayout =
                LayoutInflater.from(context).inflate(loadingLayoutId, this@LoadingButton, false)
            loadingLayout?.id = generateViewId()
            innerTextView.apply {
                textSize = getFloat(R.styleable.LoadingButton_textSize, 14f)
                buttonTextColor?.let { color ->
                    setTextColor(color)
                }
                text = buttonText
            }
            recycle()
        }

        loadingLayout?.let { layout ->
            addView(layout)
        }

        addView(innerTextView)
    }

    fun setButtonClickListener(action: (v: View) -> Unit) {
        setOnClickListener { v ->
            state = STATE.LOADING
            action(v)
        }
    }

    enum class STATE {
        LOADING,
        NORMAL
    }

    /**
     * reset inner state
     */
    private fun resetState() {
        alpha = 1f
        innerTextView.alpha = 1f
        innerTextView.visibility = VISIBLE
        buttonTextColor?.let { color ->
            innerTextView.setTextColor(color)
        }
        loadingLayout?.visibility = GONE
        isEnabled = true
        innerTextView.text = buttonText
    }

}
