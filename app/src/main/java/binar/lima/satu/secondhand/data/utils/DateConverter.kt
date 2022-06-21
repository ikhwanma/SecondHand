package binar.lima.satu.secondhand.data.utils

object DateConverter {
    fun convertDate(date: String?): String {
        var tempDate = ""
        var tempTime = ""
        for (i in 0..date!!.length) {
            if (i <= 9) {
                tempDate += date[i]
            } else if (i in 11..18) {
                tempTime += date[i]
            }
        }

        val arrDate = tempDate.split("-").toTypedArray()
        val day = arrDate[2]
        val month = convertMonth(arrDate[1])

        val arrTime = tempTime.split(":").toTypedArray()
        val h = arrTime[0]
        val m = arrTime[1]

        return "$day $month, $h:$m"
    }

    private fun convertMonth(month : String): String{
        return when(month){
            "01" -> "Jan"
            "02" -> "Feb"
            "03" -> "Mar"
            "04" -> "Apr"
            "05" -> "May"
            "06" -> "Jun"
            "07" -> "Jul"
            "08" -> "Aug"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            else -> "Dec"
        }
    }
}