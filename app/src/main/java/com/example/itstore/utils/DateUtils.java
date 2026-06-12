package com.example.itstore.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String formatDateDMY(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";

        String[] patterns = {
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'"
        };

        for (String pattern : patterns) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(pattern, Locale.US);
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = inputFormat.parse(isoDate);

                SimpleDateFormat outputFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
                return outputFormat.format(date);
            } catch (Exception e) {
                // thử pattern tiếp theo
            }
        }

        return isoDate;
    }
}