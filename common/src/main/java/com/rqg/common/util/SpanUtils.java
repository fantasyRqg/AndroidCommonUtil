package com.rqg.common.util;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.util.Locale;

/**
 * @author rqg
 * @date 11/6/15.
 */
public class SpanUtils {
    /**
     * result like  35 km/h
     *
     * @param value
     * @param unit
     * @param unitTextSize
     * @param unitTextColor
     * @return
     */
    public static CharSequence getUnitSpan(String value, String unit, int unitTextSize, int unitTextColor) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        ssb.append(value);
        int len = ssb.length();
        ssb.append(unit);
        ssb.setSpan(new AbsoluteSizeSpan(unitTextSize)
                , len, len + unit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb.setSpan(new ForegroundColorSpan(unitTextColor), len, len + unit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        return ssb;
    }

    public static CharSequence getUnitSpan(String value, String unit, int unitTextSize) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        ssb.append(value);
        int len = ssb.length();
        ssb.append(unit);
        ssb.setSpan(new AbsoluteSizeSpan(unitTextSize)
                , len, len + unit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssb;
    }

    /**
     * 12:32:00
     *
     * @param second
     * @return
     */
    public static CharSequence getTimeSpan(long second) {
        int hour, minute;

        int ss = (int) (second % 60);

        second /= 60;

        hour = (int) (second / 60);
        minute = (int) (second % 60);

        if (hour == 0) {
            return String.format(Locale.ENGLISH, "%02d:%02d", minute, ss);
        } else {
            return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hour, minute, ss);
        }


    }

    public static CharSequence getTimeSpan2(long second) {
        int minute;

        int ss = (int) (second % 60);

        minute = (int) (second / 60);


        return String.format(Locale.ENGLISH, "%02d'%02d\"", minute, ss);


    }

    public static CharSequence getTimeSpanWithUnit(Context context, long second, @DimenRes int unitSizeRes, String hourUnit, String minuteUnit) {
        int hour, minute;

        second /= 60;

        hour = (int) (second / 60);
        minute = (int) (second % 60);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int len;
        String t;

        if (hour != 0) {
            t = String.valueOf(hour);
            ssb.append(t);

            t = hourUnit;
            len = ssb.length();
            ssb.append(t);
            ssb.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(unitSizeRes)),
                    len, len + t.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        t = String.valueOf(minute);
        ssb.append(t);

        t = minuteUnit;
        len = ssb.length();
        ssb.append(t);
        ssb.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(unitSizeRes))
                , len, len + t.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssb;
    }

}
