package com.mengshujoey.mengshuadminsystem.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * application name:mengshujoeyPatient - StringCapitalizationUtils
 * application describing:
 * copyright:
 * company:
 * time:2023-02-04 10:55:28
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public class StringCapitalizationUtils {
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 汉字转为拼音
     *
     * @param chinese
     * @return
     */
    public static String convertToLowercase(String chinese) {
        reentrantLock.lock();
        try {
            char[] chars = chinese.toCharArray();
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            // 设置大小写
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            // 设置声调表示方法
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            // 设置字母u表示方法
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            String[] s;
            String rs = StringUtils.EMPTY;
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < chars.length; i++) {
                    // 判断是否为汉字字符
                    if (String.valueOf(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {
                        s = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                        if (s != null) {
                            sb.append(s[0]);
                            continue;
                        }
                    }
                    sb.append(String.valueOf(chars[i]));
                }
                rs = sb.substring(0, sb.length() - 1);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            return rs;
        } finally {
            reentrantLock.unlock();
        }

    }
}
