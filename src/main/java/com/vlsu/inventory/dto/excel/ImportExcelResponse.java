package com.vlsu.inventory.dto.excel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ImportExcelResponse {
    private int savesCount = 0;
    private List<ImportError> errors = new ArrayList<>();

    public void successSave() {
        savesCount++;
    }
}
