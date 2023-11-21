package com.haikalzain.inventorypro.common.conditions

/**
 * Created by haikalzain on 10/01/15.
 */
class GreaterEqualCondition : Condition() {
    override fun evaluate(a: Any?, b: Any?): Boolean {

        return  when (a) {
            is Int -> a.compareTo(b as Int) >= 0
            is Double -> a.compareTo(b as Double) >= 0
            else -> a.toString().compareTo(b.toString()) >= 0
        }
    }

    override fun toString(): String {
        return "Greater than or Equal to"
    }
}