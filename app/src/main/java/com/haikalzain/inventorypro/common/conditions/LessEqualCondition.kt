package com.haikalzain.inventorypro.common.conditions

/**
 * Created by haikalzain on 10/01/15.
 */
class LessEqualCondition : Condition() {
    override fun evaluate(a: Any?, b: Any?): Boolean {

        return  when (b) {
            is Int -> b.compareTo(a as Int) >= 0
            is Double -> b.compareTo(a as Double) >= 0
            else -> b.toString().compareTo(a.toString()) >= 0
        }

    }

    override fun toString(): String {
        return "Less than or Equal to"
    }
}