package com.vlsu.inventory.util;

import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationMap<T> {
    private final Page<T> pageList;
    private final List<T> objects;

    public PaginationMap(@NonNull Page<T> pageList, @NonNull List<T> objects) {
        this.pageList = pageList;
        this.objects = objects;
    }

    public Map<String, Object> getPaginatedMap() {
        Map<String, Object> paginatedMap = new HashMap<>();
        paginatedMap.put(objects.get(0).getClass().getCanonicalName(), objects);
        paginatedMap.put("currentPage", pageList.getNumber());
        paginatedMap.put("totalItems", pageList.getTotalElements());
        paginatedMap.put("totalPages", pageList.getTotalPages());
        return paginatedMap;
    }
}
