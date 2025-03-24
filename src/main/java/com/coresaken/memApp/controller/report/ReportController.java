package com.coresaken.memApp.controller.report;

import com.coresaken.memApp.data.dto.NewReportDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.report.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    final ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<Response> report(@RequestBody NewReportDto newReportDto, HttpServletRequest request){
        return reportService.report(newReportDto, request);
    }


}
