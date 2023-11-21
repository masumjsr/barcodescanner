package com.haikalzain.inventorypro.common

import android.util.Log
import com.haikalzain.inventorypro.common.FieldType.Companion.getFieldTypeFromString
import com.haikalzain.inventorypro.common.conditions.Condition
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory.getDefaultValue
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory.getObjectForFieldType
import jxl.Workbook
import jxl.read.biff.BiffException
import jxl.write.Label
import jxl.write.WriteException
import jxl.write.biff.RowsExceededException
import java.io.File
import java.io.IOException
import java.io.Serializable

/**
 * Created by haikalzain on 7/01/15.
 * Barcodes must be unique.
 */
class Spreadsheet(header: SpreadsheetHeader) : Serializable {
    private val items: MutableList<Item>
    val spreadsheetHeader: SpreadsheetHeader
    var fieldSortBy: FieldHeader
    var sortIsAscending = true
        private set
    var filterConditions: MutableList<Condition>
    var filterItems: MutableList<String>

    init {
        fieldSortBy = FieldHeader.NULL
        this.spreadsheetHeader = SpreadsheetHeader(header)
        filterConditions = ArrayList()
        filterItems = ArrayList()
        for (h in header) {
            filterConditions.add(Condition.NULL)
            filterItems.add(getDefaultValue(h.type))
        }
        items = ArrayList()
    }

    fun addItem(itemData: List<String?>) {
        require(itemData.size == spreadsheetHeader.getFieldHeaderCount()) { "Data row incorrect size" }
        val fields: MutableList<Field> = ArrayList()
        for (i in itemData.indices) {
            val field = Field(spreadsheetHeader.getFieldHeader(i), itemData[i]!!)
            fields.add(field)
        }
        items.add(Item(fields))
    }

    val sortByOptions: List<FieldHeader>
        get() {
            val list: MutableList<FieldHeader> = ArrayList()
            list.add(FieldHeader.NULL)
            list.addAll(spreadsheetHeader.getFields())
            return list
        }

    fun getSortBy(): FieldHeader {
        return fieldSortBy
    }

    val itemCount: Int
        get() = items.size

    fun getItem(row: Int): Item {
        return items[row]
    }

    fun getItem(barcode: String): Item? {
        if (barcode == "") { // This means object has no barcode
            return null
        }
        for (item in items) {
            if (item.getField("Barcode")!!.value == barcode) {
                return item
            }
        }
        return null
    }

    val sortedFilteredItemList: List<Item>
        get() {
            val filtered: MutableList<Item> = ArrayList()
            for (item in items) {
                var accept = true
                for (i in 0 until item.fieldCount) {
                    val condition = filterConditions[i]
                    if (condition.toString() == "None") {
                        continue
                    }
                    val currentField = item.getField(i)
                    val compareItem = filterItems[i]
                    if (!condition.evaluate(
                            getObjectForFieldType(
                                currentField.type,
                                currentField.value
                            ),
                            getObjectForFieldType(
                                currentField.type,
                                compareItem
                            )
                        )
                    ) {
                        accept = false
                        break
                    }
                }
                if (accept) {
                    filtered.add(item)
                }
            }
            if (fieldSortBy.name == FieldHeader.NULL.name) {
                Log.v(TAG, "SortBy is null")
                return filtered
            }
           if (sortIsAscending){
               filtered.sortBy {
                   it.getField(fieldSortBy.name)!!.value
               }
           }
            else{
               filtered.sortByDescending {
                   it.getField(fieldSortBy.name)!!.value
               }
           }
            return filtered
        }

    //use null to unset sort
    fun setSortBy(fieldHeader: FieldHeader, isAscending: Boolean) {
        fieldSortBy = fieldHeader
        sortIsAscending = isAscending
    }


    fun setFilters(filterConditions: List<Condition>?, filterItems: List<String>?) {
        this.filterConditions = ArrayList(filterConditions)
        this.filterItems = ArrayList(filterItems)
    }






    fun deleteItem(item: Item) {
        items.remove(item)
    }

    @Throws(IOException::class)
    fun exportExcelToFile(excelFile: File?) {
        val workbook = Workbook.createWorkbook(excelFile)
        val sheet = workbook.createSheet(SHEET_NAME, 0)
        for (i in 0 until spreadsheetHeader.getFieldHeaderCount()) {
            val label = Label(i, 0, encodeField(spreadsheetHeader.getFieldHeader(i)))
            try {
                sheet.addCell(label)
            } catch (e: WriteException) {
                e.printStackTrace()
                throw IOException(e)
            }
        }
        for (i in items.indices) {
            val item = items[i]
            for (j in 0 until item.fieldCount) {
                val label = Label(j, i + 1, item.getField(j).value)
                try {
                    sheet.addCell(label)
                } catch (e: RowsExceededException) {
                    e.printStackTrace()
                    throw IOException(e)
                } catch (e: WriteException) {
                    e.printStackTrace()
                    throw IOException(e)
                }
            }
        }
        workbook.write()
        try {
            workbook.close()
            Log.i("123321", "write complete")
        } catch (e: WriteException) {
            e.printStackTrace()
            throw IOException(e)
        }
    }

    val fieldsCount: Int
        get() = spreadsheetHeader.getFieldHeaderCount()

    fun getHeader(): SpreadsheetHeader {
        return SpreadsheetHeader(spreadsheetHeader)
    }

    companion object {
        private const val TAG = "Spreadsheet"
        private const val SHEET_NAME = "Sheet 1"
        @Throws(IOException::class)
        fun createFromExcelFile(inputFile: File): Spreadsheet {
            Log.v(TAG, "Opening file: " + inputFile.name)
            val workbook: Workbook
            workbook = try {
                Workbook.getWorkbook(inputFile)
            } catch (e: BiffException) {
                e.printStackTrace()
                throw IOException(e)
            }
            val sheet = workbook.getSheet(0)
            val fieldsBuilder = SpreadsheetHeader()
            for (i in 3 until sheet.columns) { //!sheet.getCell(i, 0).getContents().isEmpty()
                val contents = sheet.getCell(i, 0).contents
                val fieldHeader = decodeField(contents)
                fieldsBuilder.addFieldHeader(fieldHeader)
            }
            val spreadsheet = Spreadsheet(fieldsBuilder)
            for (r in 1 until sheet.rows) { //!sheet.getCell(0,r).getContents().isEmpty()
                val item: MutableList<String?> = ArrayList()
                for (c in 0 until spreadsheet.fieldsCount) {
                    item.add(sheet.getCell(c, r).contents)
                }
                spreadsheet.addItem(item)
            }
            return spreadsheet
        }

        private fun encodeField(fieldHeader: FieldHeader): String {
            return fieldHeader.name + " (" + fieldHeader.type.fieldName + ")"
        }

        private fun decodeField(encodedField: String): FieldHeader {
            val strings =
                encodedField.split("[()]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var name = strings[0]
            name = name.trim { it <= ' ' }
            val type = getFieldTypeFromString(strings[1])
            return FieldHeader(type!!, name)
        }
    }
}