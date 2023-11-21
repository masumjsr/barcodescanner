package com.haikalzain.inventorypro.utils

import com.haikalzain.inventorypro.common.conditions.Condition
import com.haikalzain.inventorypro.common.conditions.ContainsCondition
import com.haikalzain.inventorypro.common.conditions.EndsWithCondition
import com.haikalzain.inventorypro.common.conditions.EqualsCondition
import com.haikalzain.inventorypro.common.conditions.GreaterEqualCondition
import com.haikalzain.inventorypro.common.conditions.GreaterThanCondition
import com.haikalzain.inventorypro.common.conditions.LessEqualCondition
import com.haikalzain.inventorypro.common.conditions.LessThanCondition
import com.haikalzain.inventorypro.common.conditions.StartsWithCondition

object ConditionUtils {
    val STRING_FILTER_CONDITIONS = listOf(
        Condition.NULL,
        EqualsCondition(),
        GreaterThanCondition(),
        GreaterEqualCondition(),
        LessThanCondition(),
        LessEqualCondition(),
        StartsWithCondition(),
        EndsWithCondition(),
        ContainsCondition()
    )
    val GENERAL_FILTER_CONDITIONS = listOf(
        Condition.NULL,
        EqualsCondition(),
        GreaterThanCondition(),
        GreaterEqualCondition(),
        LessThanCondition(),
        LessEqualCondition()
    )
}