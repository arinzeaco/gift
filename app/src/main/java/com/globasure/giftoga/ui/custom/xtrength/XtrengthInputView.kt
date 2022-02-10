package com.globasure.giftoga.ui.custom.xtrength

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.globasure.giftoga.R
import com.globasure.giftoga.ui.custom.xtrength.entity.Xtrength
import com.globasure.giftoga.ui.custom.xtrength.internal.ResolutionUtil
import com.globasure.giftoga.ui.custom.xtrength.internal.XtrengthCheckerInterop
import com.globasure.giftoga.ui.custom.xtrength.internal.XtrengthSingleton


/**
 * Created by longnguyen on 8:02 AM, 4/17/20.
 *
 */
class XtrengthInputView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private var attrs: AttributeSet? = attrs
    private var defaultConfig = Xtrength()
    private lateinit var complexityTextView: TextView
    private lateinit var editText: EditText
    private var xtrengthCheckerInterop: XtrengthCheckerInterop = XtrengthCheckerInterop()

    init {
        initXMLAttributes()
    }


    private fun defaultConfig(init: Xtrength.() -> Unit) {
        defaultConfig.init()
    }

    private fun initXMLAttributes() {
        val typedArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.XtrengthInputView, 0, 0)
        loadAttributes(typedArray)
        typedArray.recycle()

        renderXMLBuilderConfigurations()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun loadAttributes(typedArray: TypedArray) {
        defaultConfig {
            // Load attributes from XML

            // Render configurations
            renderInputView()
        }
    }

    private fun renderXMLBuilderConfigurations() {

    }

    private fun renderInputView() {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        editText = EditText(context)
        editText.layoutParams = layoutParams
        editText.maxLines = 1
        editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        editText.hint = "Password"
        editText.gravity = Gravity.CENTER_VERTICAL
        editText.textSize = 16f
        editText.setHintTextColor(ContextCompat.getColor(context, R.color.hint_text_color_light))
        editText.background = ContextCompat.getDrawable(context, R.drawable.rounded_background_selector)
        val img = ContextCompat.getDrawable(context, R.drawable.ic_lockedpadlock)
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        editText.compoundDrawablePadding = ResolutionUtil.dpToPx(context, defaultConfig.paddingLeft)

        // Initialize font
        if (XtrengthSingleton.typeface == null) {
            val typeface = Typeface.createFromAsset(context.assets, "Heebo-Regular.ttf")
            XtrengthSingleton.typeface = typeface
        }

        // Render font styles
        editText.setTypeface(XtrengthSingleton.typeface, Typeface.NORMAL)

        // Render defaults
        //editText.background.setColorFilter(ContextCompat.getColor(context, R.color.midnightBlue), PorterDuff.Mode.SRC_ATOP)

        editText.setPadding(
            ResolutionUtil.dpToPx(context, defaultConfig.paddingLeft),
            ResolutionUtil.dpToPx(context, defaultConfig.paddingTop),
            ResolutionUtil.dpToPx(context, defaultConfig.paddingRight),
            ResolutionUtil.dpToPx(context, defaultConfig.paddingBottom)
        )

        // Add view to the root view
        addView(editText)

        // Add complexity view to root view
        renderComplexityTextView()

        // Add default input text watcher
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // Additional filter
                p0!!.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                    source.toString().filterNot { it.isWhitespace() }
                })

                // Validate complexity
                if (p0.toString().isNotBlank()) {
                    xtrengthCheckerInterop.validate(p0.toString().replace("\\s".toRegex(), ""))
                    renderComplexityChanges()
                } else {
                    complexityTextView.text = ""
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }
        })
    }

    private fun renderComplexityTextView() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(ResolutionUtil.dpToPx(context, defaultConfig.paddingRight), 0, 140, 0)
        layoutParams.addRule(ALIGN_PARENT_END)
        layoutParams.addRule(CENTER_VERTICAL)

        complexityTextView = TextView(context)
        complexityTextView.layoutParams = layoutParams
        complexityTextView.setTypeface(XtrengthSingleton.typeface, Typeface.NORMAL)
        complexityTextView.setTextColor(Color.WHITE)
        addView(complexityTextView)
    }

    private fun renderComplexityChanges() {
        when {
            getBaseScore() in 86..100 -> renderComplexityViewChanges(4)
            getBaseScore() in 66..85 -> renderComplexityViewChanges(3)
            getBaseScore() in 41..65 -> renderComplexityViewChanges(2)
            getBaseScore() in 21..40 -> renderComplexityViewChanges(1)
            getBaseScore() in 0..20 -> renderComplexityViewChanges(0)
        }
    }

    private fun renderComplexityViewChanges(resId: Int) {
        // Render text
        complexityTextView.text = defaultConfig.complexitySet[resId]

        // Apply colors
        val color = context.resources.getIntArray(R.array.complexityColors)[resId]
        complexityTextView.setTextColor(color)
        //complexityTextView.setTextColor().setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun getInputView(): EditText {
        return editText
    }

    fun getBaseScore(): Int {
        return xtrengthCheckerInterop.getBaseScore()
    }

    fun getCharacterScore(): Int {
        return xtrengthCheckerInterop.getCharacterScore()
    }

    fun getNumberScore(): Int {
        return xtrengthCheckerInterop.getNumberScore()
    }

    fun getSymbolScore(): Int {
        return xtrengthCheckerInterop.getSymbolScore()
    }

    @Suppress("unused")
    fun getMiddleScore(): Int {
        return xtrengthCheckerInterop.getMiddleScore()
    }

    fun getUppercaseScore(): Int {
        return xtrengthCheckerInterop.getUppercaseScore()
    }

    @Suppress("unused")
    fun getLowercaseScore(): Int {
        return xtrengthCheckerInterop.getLowercaseScore()
    }

    @Suppress("unused")
    fun getRequirementScore(): Int {
        return xtrengthCheckerInterop.getRequirementScore()
    }
}