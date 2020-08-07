package com.thoughtworks.rslist.utils;

import org.springframework.http.ResponseEntity;


import java.util.List;

public class ListOperation {
    public static ResponseEntity<List> getListByIndex(Integer formIndex, Integer toIndex, List list) {
        if (formIndex == null && toIndex == null) {
            return ResponseEntity.ok(list);
        } else if (formIndex == null) {
            Integer endIndex = toIndex > list.size() ? list.size() : toIndex;
            return ResponseEntity.ok(list.subList(0, endIndex));
        } else if (toIndex == null) {
            Integer startIndex = formIndex < 1 ? 0 : formIndex - 1;
            return ResponseEntity.ok(list.subList(startIndex, list.size()));
        }
        Integer startIndex = formIndex < 1 ? 0 : formIndex - 1;
        Integer endIndex = toIndex > list.size() ? list.size() : toIndex;
        return ResponseEntity.ok(list.subList(startIndex, endIndex));

    }

    //检验输入index参数合法性
    public static boolean isLegalIndex(Integer start, Integer end, List list) {
        if (start == null && end == null)
            return false;
        if (end > list.size() || start - 1 < 0 || start > list.size() || end - 1 < 0)
            return false;
        return true;
    }

}
