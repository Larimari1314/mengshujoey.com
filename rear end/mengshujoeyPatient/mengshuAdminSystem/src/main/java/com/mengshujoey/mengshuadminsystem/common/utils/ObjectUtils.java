package com.mengshujoey.mengshuadminsystem.common.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * application name:mengshujoeyPatient - ObjectUtils
 * application describing:
 * copyright:
 * company:
 * time:2023-01-30 08:43:53
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Component
public class ObjectUtils<T> {
    public static ArrayList<String> fraction_name = new ArrayList<>();

    static {
        Collections.addAll(fraction_name,
                "fraction_one",
                "fraction_two",
                "fraction_three",
                "fraction_four",
                "fraction_five",
                "fraction_six",
                "fraction_seven"
        );
    }

    public List<T> fractionBySeven(List<T> list) {
        if (list.size() < 7) {
            int size = list.size();
            for (int i = 0; i < 7 - size; i++) {
                list.add(null);
            }
        } else if (list.size() > 7) {
            //前端存在重复添加
            int size = list.size();
            for (int i = size - 7; i > 0; i--) {
                list.remove(6 + i);
            }
        }
        return list;
    }
}
