@file:Suppress("unused")

package com.globasure.giftoga.utils

import org.json.JSONException
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by longnguyen on 10:31 AM, 4/12/20.
 *
 */
object CalendarUtils {
    private const val EEE_dd_MM_yyyy = "EEE, dd/MM/yyyy"
    private const val DD_MM_YYYY = "dd/MM/yyyy"
    private const val DD_MMMM_YYYY = "dd MMMM, yyyy"
    private const val YYYY_DD_MM = "yyyy-MM-dd"
    private var suffixes = arrayOf(
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
        "th", "st"
    )

    fun parseTransactionDate(date: String): Date {
        val sdf = SimpleDateFormat(YYYY_DD_MM, Locale.getDefault())
        var result = Date()
        try {
            result = sdf.parse(date)!!
        } catch (e: ParseException) {
            Timber.e(e)
        } catch (e: JSONException) {
            Timber.e(e)
        }
        return result
    }

    fun parseStringToDate(date: String): Date {
        val sdf = SimpleDateFormat(DD_MM_YYYY, Locale.getDefault())
        var result = Date()
        try {
            result = sdf.parse(date)!!
        } catch (e: ParseException) {
            Timber.e(e)
        } catch (e: JSONException) {
            Timber.e(e)
        }
        return result
    }

    fun formattedDate(date: Date?): String {
        val sdf = SimpleDateFormat(DD_MMMM_YYYY, Locale.getDefault())
        return if (date != null) sdf.format(date) else sdf.format(Date())
    }

    fun setMidnight(cal: Calendar) {
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
    }

    fun getDayOfWeek(cal: Calendar): String {
        val weekDay: String
        val dayFormat = SimpleDateFormat("E", Locale.getDefault())
        weekDay = dayFormat.format(cal.time)
        return weekDay
    }

    fun getDayOfWeekShort(cal: Calendar): String {
        val weekDay: String
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        weekDay = dayFormat.format(cal.time)
        return weekDay
    }

    fun getDateAndMonth(cal: Calendar): String {
        val dateMonth: String
        val dayFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        dateMonth = dayFormat.format(cal.time)
        return dateMonth
    }

    fun get7DaysFromNow(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until 7) {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            setMidnight(calendar)
            calendar.add(Calendar.DATE, i)
            val day = calendar[Calendar.DAY_OF_MONTH]
            val dayStr: String = day.toString() + suffixes[day]
            val dayOfWeek: String =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())!!
            val monthName =
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
            val result = "$dayOfWeek, $dayStr $monthName"
            list.add(result)
        }
        return list
    }

    fun convertToDateMovie(calendar: Calendar): String {
        setMidnight(calendar)
        val day = calendar[Calendar.DAY_OF_MONTH]
        val dayStr: String = day.toString() + suffixes[day]
        val dayOfWeek: String =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())!!
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return "$dayOfWeek, $dayStr $monthName"
    }

    fun convertTodayToDateMovie(): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        setMidnight(calendar)
        //calendar.add(Calendar.DATE, 1)
        val day = calendar[Calendar.DAY_OF_MONTH]
        val dayStr: String = day.toString() + suffixes[day]
        val dayOfWeek: String =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())!!
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return "$dayOfWeek, $dayStr $monthName"
    }

    fun getTimeFromString(time: String?): Long {
        var timeValue = time
        var result: Long = 0
        if (timeValue != null) {
            try {
                val isAM = timeValue.contains("AM") || timeValue.contains("am")
                val isPM = timeValue.contains("PM") || timeValue.contains("pm")
                if (isAM || isPM) {
                    timeValue = timeValue.substring(0, timeValue.indexOf(" "))
                }
                val data = timeValue.split(":").toTypedArray()
                val hour = if (isPM) {
                    if (data[0].toInt() == 12) data[0].toInt() else data[0].toInt() + 12
                } else if (isAM) {
                    if (data[0].toInt() == 12) 0 else data[0].toInt()
                } else {
                    data[0].toInt()
                }
                val minute = data[1].toInt()
                result = (hour * 60 + minute).toLong()
            } catch (e: Exception) {
                Timber.e(e)
                return 0
            }
        }
        return result
    }

    fun getCurrentTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        return (hour * 60 + minute).toLong()
    }

    fun getCurrentDate(): String {
        val df: DateFormat = SimpleDateFormat(EEE_dd_MM_yyyy, Locale.getDefault())
        return df.format(Calendar.getInstance().time)
    }

    fun getNextDate(): String {
        val df: DateFormat = SimpleDateFormat(EEE_dd_MM_yyyy, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        setMidnight(calendar)
        calendar.add(Calendar.DATE, 1)
        return df.format(calendar.time)
    }

    fun getCurrentDateMilliseconds(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        setMidnight(calendar)
        return calendar.timeInMillis
    }

    fun getNextDayDateMilliseconds(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        setMidnight(calendar)
        calendar.add(Calendar.DATE, 1)
        return calendar.timeInMillis
    }

    fun getSelectedDate(day: Int, month: Int, year: Int): String {
        val myDay = if (day < 10) {
            "0$day"
        } else {
            "$day"
        }
        val myMonth = if (month < 10) {
            "0$month"
        } else {
            "$month"
        }
        val myYear = "$year"
        return "$myDay/$myMonth/$myYear"
    }

    fun getSelectedCalendar(day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        setMidnight(calendar)
        return calendar.timeInMillis
    }

    fun getLastWeek(mCalendar: Calendar): Pair<String, String> {
        val date = Date()
        mCalendar.time = date

        // 1 = Sunday, 2 = Monday, etc.
        val dayOfWeek = mCalendar[Calendar.DAY_OF_WEEK]
        val mondayOffset: Int
        mondayOffset = if (dayOfWeek == 1) {
            13
        } else (dayOfWeek - 2) + 7
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, -mondayOffset)
        val mDateMonday = mCalendar.time

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        val mDateSunday = mCalendar.time

        // Date format
        val sdf = SimpleDateFormat(DD_MM_YYYY, Locale.getDefault())
        val startDate = sdf.format(mDateMonday)
        val endDate = sdf.format(mDateSunday)

        return Pair(startDate, endDate)
    }

    fun getCurrentWeek(mCalendar: Calendar): Pair<String, String> {
        val date = Date()
        mCalendar.time = date

        // 1 = Sunday, 2 = Monday, etc.
        val dayOfWeek = mCalendar[Calendar.DAY_OF_WEEK]
        val mondayOffset: Int
        mondayOffset = if (dayOfWeek == 1) {
            -6
        } else 2 - dayOfWeek
        mCalendar.add(Calendar.DAY_OF_YEAR, mondayOffset)
        val mDateMonday = mCalendar.time

        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        val mDateSunday = mCalendar.time

        //Get format date
        val sdf = SimpleDateFormat(DD_MM_YYYY, Locale.getDefault())
        val startDate = sdf.format(mDateMonday)
        val endDate = sdf.format(mDateSunday)

        return Pair(startDate, endDate)
    }

    fun getThisMonth(mCalendar: Calendar): Pair<String, String> {
        mCalendar[Calendar.DATE] = mCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        val monthFirstDay = mCalendar.time
        mCalendar[Calendar.DATE] = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val monthLastDay = mCalendar.time

        //Get format date
        val sdf = SimpleDateFormat(DD_MM_YYYY, Locale.getDefault())
        val startDate = sdf.format(monthFirstDay)
        val endDate = sdf.format(monthLastDay)

        return Pair(startDate, endDate)
    }

    fun convertTransactionDateTime(transactionDate: String): String {
        if (transactionDate.contains("T")) {
            val dateString = transactionDate.split("T")[0]
            val timeAndZoneString = transactionDate.split("T")[1]
            if (timeAndZoneString.contains("+")) {
                val timeString = timeAndZoneString.split("+")[0]
                val zoneString = timeAndZoneString.split("+")[1]

                val dateFormatted = formattedDate(parseTransactionDate(dateString))
                val timeFormatted = timeString.substring(0, timeString.lastIndexOf(":"))

                return "$dateFormatted - $timeFormatted"
            }
        }

        return transactionDate
    }
}