package com.olehbilykh.virustracker.controller;

import com.olehbilykh.virustracker.models.Location;
import com.olehbilykh.virustracker.services.VirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    @Autowired
    VirusDataService virusDataService;

    @GetMapping("/")
    public String home(Model model){

        List<Location> allStats = virusDataService.getAllStats();

        int totalCasesWorldWide = allStats.stream().flatMapToInt(stat -> IntStream.of(stat.getLatestTotalCases())).sum();
        int totalNewCases = allStats.stream().flatMapToInt(stat -> IntStream.of(stat.getLatestTotalCases())).sum();

        model.addAttribute("locationStatistics", allStats);
        model.addAttribute("totalCasesWorldWide", totalCasesWorldWide);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}
