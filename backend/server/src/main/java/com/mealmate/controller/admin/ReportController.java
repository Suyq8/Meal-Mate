package com.mealmate.controller.admin;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.result.Result;
import com.mealmate.service.ReportService;
import com.mealmate.vo.OrderReportVO;
import com.mealmate.vo.SalesTop10ReportVO;
import com.mealmate.vo.TurnoverReportVO;
import com.mealmate.vo.UserReportVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController("AdminReportController")
@RequestMapping("/admin/report")
@Tag(name = "Report Controller (admin)")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @Operation(summary = "Get turnover statistics")
    public Result<TurnoverReportVO> getTurnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        TurnoverReportVO turnoverReportVO = reportService.getTurnoverReport(begin, end);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    @Operation(summary = "Get user statistics")
    public Result<UserReportVO> getUserReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        UserReportVO userReportVO = reportService.getUserReport(begin, end);
        return Result.success(userReportVO);
    }

    @GetMapping("/ordersStatistics")
    @Operation(summary = "Get orders statistics")
    public Result<OrderReportVO> getOrderReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        OrderReportVO orderReportVO = reportService.getOrderReport(begin, end);
        return Result.success(orderReportVO);
    }

    @GetMapping("/top10")
    @Operation(summary = "Get top 10 sales")
    public Result<SalesTop10ReportVO> getTop10Sales(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10Report(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    @GetMapping("/export")
    @Operation(summary = "Export excel report")
    public void exportExcel(HttpServletResponse response) {
        try (XSSFWorkbook excel = reportService.exportExcelFile()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"report.xlsx\"");
            excel.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("Export excel error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
