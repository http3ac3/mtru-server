package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.excel.ImportError;
import com.vlsu.inventory.dto.excel.ImportExcelResponse;
import com.vlsu.inventory.dto.excel.ImportRequest;
import com.vlsu.inventory.model.*;
import com.vlsu.inventory.repository.*;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ImportService {
    ResponsibleRepository responsibleRepository;
    PlacementRepository placementRepository;
    SubcategoryRepository subcategoryRepository;
    EquipmentRepository equipmentRepository;
    UserRepository userRepository;


    public ImportExcelResponse fromExcel(ImportRequest request, User principal) throws IOException {
        Workbook workbook = new XSSFWorkbook(request.file().getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormat fmt = workbook.createDataFormat();
        CellStyle textCellStyle = workbook.createCellStyle();
        textCellStyle.setDataFormat(fmt.getFormat("@"));
        sheet.setDefaultColumnStyle(0, textCellStyle);
        ImportExcelResponse response = new ImportExcelResponse();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Equipment equipment;
            try {
                equipment = getEquipmentFromRow(sheet.getRow(i), request);

                Responsible principalResponsible = userRepository.findByUsername(principal.getUsername()).get().getResponsible();
                if (!principal.isAdmin() && !Objects.equals(
                        equipment.getResponsible().getDepartment().getId(),
                        principalResponsible.getDepartment().getId())) {
                    throw new ActionNotAllowedException(
                            "Ответственный " +
                            equipment.getResponsible().getLastName() + " " +
                            equipment.getResponsible().getFirstName() + " " +
                            " не принадлежит к структурному подразделению " +
                            principalResponsible.getDepartment().getName());
                }

            } catch (DataFormatException | ResourceNotFoundException | ActionNotAllowedException e) {
                response.getErrors().add(new ImportError(i + 1, e.getMessage()));
                continue;
            }

            try {
                equipmentRepository.save(equipment);
                response.successSave();
            } catch (Exception e) {
                String message = e.getMessage().substring(
                        e.getMessage().indexOf("Подробности"),
                        e.getMessage().indexOf("]")
                );
                response.getErrors().add(new ImportError(i + 1, message));
            }
        }
        return response;
    }

    private Equipment getEquipmentFromRow(Row row, ImportRequest request) throws DataFormatException, ResourceNotFoundException {
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


        LocalDate commissioningDate =
                request.autoCommissioningDate() == null ?
                getRequiredLocalDateCellValue(row, 3, "Дата ввода в эксплуатацию") : request.autoCommissioningDate();

        String commissioningActNumber =
                request.autoCommissioningActNumber() == null ?
                getRequiredStringCellValue(row, 4, "Номер акта о вводе в эксплуатацию") : request.autoCommissioningActNumber();

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

        Responsible responsible = getResponsible(row, request.autoResponsibleId());
        Placement placement = getPlacement(row, request.autoPlacementId());
        String subcategoryName = getRequiredStringCellValue(row, 9, "Подкатегория");

        return Equipment.builder()
                .inventoryNumber(inventoryNumber)
                .name(name)
                .initialCost(initialCost)
                .commissioningDate(commissioningDate)
                .commissioningActNumber(commissioningActNumber)
                .decommissioningDate(decommissioningDate)
                .decommissioningActNumber(decommissioningActNumber)
                .responsible(responsible)
                .placement(placement)
                .subcategory(getSubcategoryByName(subcategoryName))
                .build();
    }

    private Responsible getResponsible(Row row, Long responsibleId) throws ResourceNotFoundException, DataFormatException {
        if (responsibleId != null) {
            return responsibleRepository.findById(responsibleId).orElseThrow(
                    () -> new ResourceNotFoundException("Ответственный с ID '" + responsibleId + "' не найден"));
        } else {
            return getResponsibleByFullName(getRequiredStringCellValue(row, 7, "Ответственный"));
        }
    }

    private Placement getPlacement(Row row, Long placementId) throws ResourceNotFoundException, DataFormatException {
        if (placementId != null) {
            return placementRepository.findById(placementId).orElseThrow(
                    () -> new ResourceNotFoundException("Помещение с ID '" + placementId + "' не найдено"));
        } else {
            return getPlacementByName(getRequiredStringCellValue(row, 8, "Помещение"));
        }
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
        String value = "";
        try {
            Cell currentCell = row.getCell(index);
            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                value = BigDecimal.valueOf(currentCell.getNumericCellValue()).toPlainString();
            } else {
                value = currentCell.getStringCellValue();
            }
            if (value.isEmpty()) {
                throw new DataFormatException("Поле '" + fieldName + "' должно быть заполнено");
            }
        } catch (IllegalStateException e) {
            throw new DataFormatException("Поле '" + fieldName + "' имеет неверный формат данных");
        } catch (NullPointerException e) {
            throw new DataFormatException("Поле '" + fieldName + "' должно быть заполнено");
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

    private Placement getPlacementByName(String placementName) {
        Optional<Placement> placement = placementRepository
                .findByName(placementName.trim());
        if (placement.isPresent()) {
            return placement.get();
        } else {
            Placement newPlacement = new Placement();
            newPlacement.setName(placementName);
            return placementRepository.save(newPlacement);
        }
    }

    private Subcategory getSubcategoryByName(String subcategoryName) throws ResourceNotFoundException {
        return subcategoryRepository
                .findByName(subcategoryName.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ни одна подкатегория с названием: " + subcategoryName + " не найдена"));
    }
}
