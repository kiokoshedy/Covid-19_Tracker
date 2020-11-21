/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.kiokoCode.covid19tracker.controller;

import io.kiokoCode.covid19tracker.model.CovidStats;
import io.kiokoCode.covid19tracker.service.CovidtrackerService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author DATA INTEGRATED
 */

@Controller
public class CovidStatsController {
    
    @Autowired
    CovidtrackerService covidtrackerService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<CovidStats> list = covidtrackerService.getAllStats();
        int totalReportedCases = list.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = list.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("covidStats", list);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "home.xhtml";
    }
}
