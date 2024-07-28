package com.mealmate.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealmate.entity.ItemSales;
import com.mealmate.service.DashBoardService;
import com.mealmate.service.OrderService;
import com.mealmate.service.ReportService;
import com.mealmate.service.UserService;
import com.mealmate.vo.BusinessDataVO;
import com.mealmate.vo.OrderReportVO;
import com.mealmate.vo.SalesTop10ReportVO;
import com.mealmate.vo.TurnoverReportVO;
import com.mealmate.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private DashBoardService dashboardService;
    private final String excelTemplatePath = "backend/meal-mate/server/src/main/resources/template/Report Template.xlsx";

    private String getDateString(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        LocalDate curr = startDate;

        while (!curr.equals(endDate)) {
            sb.append(curr).append(",");
            curr = curr.plusDays(1);
        }
        sb.append(endDate);

        return sb.toString();
    }

    @Override
    public TurnoverReportVO getTurnoverReport(LocalDate startDate, LocalDate endDate) {
        List<BigDecimal> turnoverList = orderService.getTurnoverList(startDate, endDate);
        String turnoverListString = turnoverList.stream()
                .map(BigDecimal::toString)
                .collect(Collectors.joining(","));

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(getDateString(startDate, endDate))
                .turnoverList(turnoverListString)
                .build();

        return turnoverReportVO;
    }

    @Override
    public UserReportVO getUserReport(LocalDate startDate, LocalDate endDate) {
        List<Integer> userCountList = userService.getUserCountList(startDate.minusDays(1), endDate);

        StringBuilder sbNewUser = new StringBuilder();
        StringBuilder sbTotalUser = new StringBuilder();
        for (int i = 1; i < userCountList.size(); i++) {
            sbTotalUser.append(userCountList.get(i));
            sbNewUser.append(userCountList.get(i) - userCountList.get(i - 1));
            if (i < userCountList.size() - 1) {
                sbTotalUser.append(",");
                sbNewUser.append(",");
            }
        }

        UserReportVO userReportVO;
        userReportVO = UserReportVO.builder()
                .dateList(getDateString(startDate, endDate))
                .totalUserList(sbTotalUser.toString())
                .newUserList(sbNewUser.toString())
                .build();

        return userReportVO;
    }

    @Override
    public OrderReportVO getOrderReport(LocalDate startDate, LocalDate endDate) {
        List<Integer> orderCountList = orderService.getOrderCountList(startDate, endDate);
        List<Integer> validOrderCountList = orderService.getValidOrderCountList(startDate, endDate);

        String orderCountListString = orderCountList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        String validOrderCountListString = validOrderCountList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        Integer totalOrderCount = orderCountList.stream().mapToInt(Integer::intValue).sum();
        Integer totalValidOrderCount = validOrderCountList.stream().mapToInt(Integer::intValue).sum();

        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(getDateString(startDate, endDate))
                .orderCountList(orderCountListString)
                .validOrderCountList(validOrderCountListString)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(totalOrderCount == 0 ? 0 : (double) totalValidOrderCount / totalOrderCount)
                .build();

        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO getSalesTop10Report(LocalDate startDate, LocalDate endDate) {
        List<ItemSales> itemSalesList = orderService.getTop10Sales(startDate, endDate);

        StringBuilder sbName = new StringBuilder();
        StringBuilder sbQuantity = new StringBuilder();

        for (int i = 0; i < itemSalesList.size(); i++) {
            sbName.append(itemSalesList.get(i).getName());
            sbQuantity.append(itemSalesList.get(i).getQuantity());
            if (i < itemSalesList.size() - 1) {
                sbName.append(",");
                sbQuantity.append(",");
            }
        }

        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(sbName.toString())
                .numberList(sbQuantity.toString())
                .build();

        return salesTop10ReportVO;
    }

    @Override
    public XSSFWorkbook exportExcelFile() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(30);
        BusinessDataVO businessDataVO = dashboardService.getBusinessData(startDate, today);

        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        try {
            fis = new FileInputStream(excelTemplatePath);
            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue("Time: " + startDate.toString() + " to " + today.toString());

            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            for (int i = 0; i < 30; i++) {
                LocalDate date = startDate.plusDays(i);
                BusinessDataVO businessData = dashboardService.getBusinessData(date, date);
                row = sheet.getRow(7 + i);

                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", excelTemplatePath);
        } catch (IOException e) {
            log.error("IO exception occurred");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("Failed to close FileInputStream");
                }
            }
        }

        return workbook;
    }
}
