package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.excel.ImportError;
import com.vlsu.inventory.dto.excel.ImportExcelResponse;
import com.vlsu.inventory.model.Equipment;
import com.vlsu.inventory.model.Placement;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.model.Subcategory;
import com.vlsu.inventory.repository.EquipmentRepository;
import com.vlsu.inventory.repository.PlacementRepository;
import com.vlsu.inventory.repository.ResponsibleRepository;
import com.vlsu.inventory.repository.SubcategoryRepository;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ImportService {
    ResponsibleRepository responsibleRepository;
    PlacementRepository placementRepository;
    SubcategoryRepository subcategoryRepository;
    EquipmentRepository equipmentRepository;
    public ImportExcelResponse fromExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        ImportExcelResponse response = new ImportExcelResponse();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Equipment equipment;
            try {
                equipment = getEquipmentFromRow(sheet.getRow(i));
            } catch (DataFormatException | ResourceNotFoundException e) {
                response.getErrors().add(new ImportError(i + 1, e.getMessage()));
                continue;
            }

            try {
                equipmentRepository.save(equipment);
                response.successSave();
            } catch (Exception e) {
                response.getErrors().add(new ImportError(i + 1, e.getMessage()));
            }
        }
        return response;
    }

    private Equipment getEquipmentFromRow(Row row) throws DataFormatException, ResourceNotFoundException {
        String inventoryNumber = getRequiredStringCellValue(row, 0, "Инвентарный номер");
        String name = getRequiredStringCellValue(row, 1, "Наименование");

        BigDecimal initialCost;
        try {
            initialCost = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
        } catch (NullPointerException e) {
            throw new DataFormatException("Поле 'Начальная стоимость' должно быть заполнено");
        } catch (IllegalStateException e) {
            throw new DataFormatException("Поле 'Начальная стоимость' имеет неверный формат данных");
        }

        LocalDate commissioningDate = getRequiredLocalDateCellValue(row, 3, "Дата ввода в эксплуатацию");
        String commissioningActNumber = getRequiredStringCellValue(row, 4, "Номер акта о вводе в эксплуатацию");

        LocalDate decommissioningDate = null;
        String decommissioningActNumber = null;

        // TODO Проверить правильности получения данных из ячеек 5 и 6, если обе заполнены
        try {
            decommissioningDate = getRequiredLocalDateCellValue(row, 5, "Дата вывода из эксплуатации");
        } catch (Exception ignored) { }

        try {
            decommissioningActNumber = getRequiredStringCellValue(row, 6, "Номер акта о выводе из эксплуатации");
        } catch (Exception ignored) { }

        if (decommissioningActNumber == null && decommissioningDate != null) {
            throw new DataFormatException("Если указана дата вывода из эксплуатации, то необходимо указать номер акта об этом");
        } else if (decommissioningDate == null && decommissioningActNumber != null) {
            throw new DataFormatException("Если указан номер акта о выводе из эксплуатации, то необходимо указать дату вывода из эксплуатации");
        }

        String responsibleFullName = getRequiredStringCellValue(row, 7, "Ответственный");
        String placementName = getRequiredStringCellValue(row, 8, "Помещение");
        String subcategoryName = getRequiredStringCellValue(row, 9, "Подкатегория");

        return Equipment.builder()
                .inventoryNumber(inventoryNumber)
                .name(name)
                .initialCost(initialCost)
                .commissioningDate(commissioningDate)
                .commissioningActNumber(commissioningActNumber)
                .decommissioningDate(decommissioningDate)
                .decommissioningActNumber(decommissioningActNumber)
                .responsible(getResponsibleByFullName(responsibleFullName))
                .placement(getPlacementByName(placementName))
                .subcategory(getSubcategoryByName(subcategoryName))
                .build();
    }

    private LocalDate getRequiredLocalDateCellValue(Row row, int index, String fieldName) throws DataFormatException {
        LocalDate value;
        try {
            value = row.getCell(index).getLocalDateTimeCellValue().toLocalDate();
        } catch (IllegalStateException e) {
            throw new DataFormatException("Поле '" + fieldName + "' имеет неверный формат данных");
        } catch (NullPointerException e) {
            throw new DataFormatException("Поле '" + fieldName + "' должно быть заполнено");
        }
        return value;
    }

    private String getRequiredStringCellValue(Row row, int index, String fieldName) throws DataFormatException {
        String value;
        try {
            value = row.getCell(index).getStringCellValue();
            if (value.isEmpty()) {
                throw new DataFormatException("Поле '" + fieldName + "' должно быть заполнено");
            }
        } catch (IllegalStateException e) {
            throw new DataFormatException("Поле '" + fieldName + "' имеет неверный формат данных");
        }
        return value;
    }

    private Responsible getResponsibleByFullName(String fullName) throws DataFormatException, ResourceNotFoundException {
        String[] fullNameParts = fullName.trim().split(" ");
        if (fullNameParts.length < 2) throw new DataFormatException("Имя или фамилия ответственного не найдены");
        String lastName = fullNameParts[0];
        String firstName = fullNameParts[1];
        String patronymic = (fullNameParts.length > 2) ? fullNameParts[2] : null;

        List<Responsible> responsibleList;

        if (patronymic != null) {
            responsibleList = responsibleRepository.findByLastNameAndFirstNameAndPatronymic(lastName, firstName, patronymic);
        } else {
            responsibleList = responsibleRepository.findByLastNameAndFirstNameAndPatronymicIsNull(lastName, firstName);
        }

        if (responsibleList.isEmpty())
            throw new ResourceNotFoundException("Ни одного ответственного с ФИО: " +
                    lastName + " " +
                    firstName + " " +
                    (patronymic == null ? "(без отчества)" : patronymic) +
                    " не найдено");

        return responsibleList.get(0);
    }

    private Placement getPlacementByName(String placementName) throws ResourceNotFoundException {
        return placementRepository
                .findByName(placementName.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ни одно помещение с названием: " + placementName + " не найдено"));
    }

    private Subcategory getSubcategoryByName(String subcategoryName) throws ResourceNotFoundException {
        return subcategoryRepository
                .findByName(subcategoryName.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ни одна подкатегория с названием: " + subcategoryName + " не найдена"));
    }
}
