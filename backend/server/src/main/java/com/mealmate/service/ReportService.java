package com.mealmate.service;

import java.time.LocalDate;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mealmate.vo.OrderReportVO;
import com.mealmate.vo.SalesTop10ReportVO;
import com.mealmate.vo.TurnoverReportVO;
import com.mealmate.vo.UserReportVO;

public interface ReportService {
    
    /**
     * get turnover report
     * @param startDate
     * @param endDate
     * @return
     */
    TurnoverReportVO getTurnoverReport(LocalDate startDate, LocalDate endDate);

    /**
     * get user report
     * @param startDate
     * @param endDate
     * @return
     */
    UserReportVO getUserReport(LocalDate startDate, LocalDate endDate);

    /**
     * get order report
     * @param startDate
     * @param endDate
     * @return
     */
    OrderReportVO getOrderReport(LocalDate startDate, LocalDate endDate);

    /**
     * get sales top 10 report
     * @param startDate
     * @param endDate
     * @return
     */
    SalesTop10ReportVO getSalesTop10Report(LocalDate startDate, LocalDate endDate);

    /**
     * export excel file
     * @return
     */
    XSSFWorkbook exportExcelFile();
}
