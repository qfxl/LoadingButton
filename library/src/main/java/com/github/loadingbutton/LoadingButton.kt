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
     * button textSize
     */
    private var buttonTextSize: Float? = null
    /**
     * button loading text
     */
    private var loadingText: String? = null
    /**
     * button textColor
     */
    private var buttonTextColor: Int? = null
    /**
     * button textView
     */
    private val innerTextView by lazy {
        TextView(context).apply {
            buttonText?.let { s ->
                text = s
            }

            buttonTextColor?.let { color ->
                setTextColor(color)
            }

            buttonTextSize?.let { size ->
                textSize = size
            }
            id = View.generateViewId()
        }
    }

    var state: STATE =
        STATE.NORMAL
        set(value) {
            field = value
            when (value) {
                STATE.NORMAL -> {
                    alpha = 1f
                    loadingLayout?.visibility = View.GONE
                    isEnabled = true
                    loadingText?.run {
                        innerTextView.text = buttonText
                    }
                }
                STATE.LOADING -> {
                    loadingLayout?.visibility = View.VISIBLE
                    alpha = 0.7f
                    isEnabled = false
                    loadingText?.let { s ->
                        innerTextView.text = s
                    }
                }
            }
        }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        context.obtainStyledAttributes(attr, R.styleable.LoadingButton).apply {
            innerTextView.apply {
                textSize = getFloat(R.styleable.LoadingButton_textSize, 14f)
                setTextColor(
                    getColor(
                        R.styleable.LoadingButton_textColor,
                        Color.parseColor("#666666")
                    )
                )
                buttonText = getString(R.styleable.LoadingButton_text)
                text = buttonText
                loadingText = getString(R.styleable.LoadingButton_loadingText)
            }

            val loadingLayoutId =
                getResourceId(
                    R.styleable.LoadingButton_loadingLayout,
                    R.layout.layout_loading
                )
            loadingLayout =
                LayoutInflater.from(context).inflate(loadingLayoutId, this@LoadingButton, false)
            loadingLayout?.id = View.generateViewId()
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
}
