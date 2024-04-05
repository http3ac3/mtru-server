package com.vlsu.inventory.util;

import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationMap<T> {
    private final Page<T> pageList;
    private final String typeName;

    public PaginationMap(Page<T> pageList, String typeName) {
        this.pageList = pageList;
        this.typeName = typeName;
    }

    public Map<String, Object> getPaginatedMap() {
        Map<String, Object> paginatedMap = new HashMap<>();
        paginatedMap.put(typeName, pageList.getContent());
        paginatedMap.put("currentPage", pageList.getNumber() + 1);
        paginatedMap.put("totalItems", pageList.getTotalElements());
        paginatedMap.put("totalPages", pageList.getTotalPages());
        return paginatedMap;
    }
}
