package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import com.haikalzain.inventorypro.common.FieldType
import com.haikalzain.inventorypro.common.conditions.Condition
import com.haikalzain.inventorypro.utils.ConditionUtils
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

/**
 * Created by haikalzain on 6/01/15.
 */
object FieldViewFactory {
    //only returns user creatable types
    val fieldTypes = Arrays.asList(
        FieldType.TEXT,
        FieldType.LONG_TEXT,
        FieldType.NUMBER,
        FieldType.POSITIVE_NUMBER,
        FieldType.DATE,
        FieldType.TIME,
        FieldType.DECIMAL,
        FieldType.PRICE,
        FieldType.RATING,
        FieldType.YES_NO
    )

    @JvmOverloads
    fun createFieldViewForType(
        context: Context?, type: FieldType?, name: String?, isFilter: Boolean = false
    ): FieldView? {
        when (type) {
            FieldType.TEXT -> return TextFieldView(context, name, isFilter)
            FieldType.LONG_TEXT -> return LongTextFieldView(context, name, isFilter)
            FieldType.DATE -> return DateFieldView(context, name, isFilter)
            FieldType.TIME -> return TimeFieldView(context, name, isFilter)
            FieldType.DAY -> {}
            FieldType.NUMBER -> return NumberFieldView(context, name, isFilter)
            FieldType.POSITIVE_NUMBER -> return PositiveNumberFieldView(context, name, isFilter)
            FieldType.DECIMAL -> return DecimalFieldView(context, name, isFilter)
            FieldType.PRICE -> return PriceFieldView(context, name, isFilter)
            FieldType.RATING -> return RatingFieldView(context, name, isFilter)
            FieldType.YES_NO -> return YesNoFieldView(context, name, isFilter)
            else -> {}
        }
        return null
    }

    @JvmStatic
    fun getObjectForFieldType(type: FieldType?, dataString: String): Any {
        val data: Array<String>
        when (type) {
            FieldType.DATE -> {
                data = dataString.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = data[0].toInt()
                val month = data[1].toInt()
                val year = data[2].toInt()
                return year * 10000 + month * 100 + day
            }

            FieldType.DAY -> {}
            FieldType.TIME -> {
                data = dataString.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return 100 * data[0].toInt() + data[1].toInt()
            }

            FieldType.NUMBER, FieldType.POSITIVE_NUMBER -> return dataString.toInt()
            FieldType.DECIMAL, FieldType.PRICE, FieldType.RATING -> return dataString.toDouble()
            else -> return dataString
        }
        return dataString
    }

    @JvmStatic
    fun getDefaultValue(type: FieldType?): String {
        when (type) {
            FieldType.DATE -> {
                val c = Calendar.getInstance()
                val year = c[Calendar.YEAR]
                val month = c[Calendar.MONTH] + 1
                val day = c[Calendar.DAY_OF_MONTH]
                return "$day/$month/$year"
            }

            FieldType.DAY -> {}
            FieldType.TIME -> {
                val d = Calendar.getInstance()
                val hour = d[Calendar.HOUR_OF_DAY]
                val minute = d[Calendar.MINUTE]
                return String.format(Locale.ENGLISH, "%02d:%02d", hour, minute)
            }

            FieldType.NUMBER, FieldType.POSITIVE_NUMBER -> return "0"
            FieldType.DECIMAL -> return "0.000"
            FieldType.PRICE -> return "0.00"
            FieldType.RATING -> return "5"
            FieldType.YES_NO -> return "No"
            else -> {}
        }
        return ""
    }

    fun getFiltersForFieldType(type: FieldType?): List<Condition> {
        return when (type) {
            FieldType.DATE, FieldType.DAY, FieldType.NUMBER, FieldType.POSITIVE_NUMBER, FieldType.DECIMAL, FieldType.PRICE, FieldType.TIME, FieldType.RATING, FieldType.YES_NO -> ConditionUtils.GENERAL_FILTER_CONDITIONS
            else -> ConditionUtils.STRING_FILTER_CONDITIONS
        }
    }
}