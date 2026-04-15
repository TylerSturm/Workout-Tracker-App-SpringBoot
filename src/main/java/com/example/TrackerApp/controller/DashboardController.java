package com.example.TrackerApp.controller;

import com.example.TrackerApp.dto.ChartBar;
import com.example.TrackerApp.dto.LiftEntry;
import com.example.TrackerApp.dto.LiftForm;
import com.example.TrackerApp.entity.Lift;
import com.example.TrackerApp.repository.LiftRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController
{
    private final LiftRepository liftRepository;

    public DashboardController(LiftRepository liftRepository)
    {
        this.liftRepository = liftRepository;
    }

    @GetMapping("/home")
    public String dashboard(
            // Define user session
            @RequestParam(name = "range", defaultValue = "7") String range,
            HttpSession session,
            Model model)
    {
        // Redirect to login if no session exists
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        // Get all lifts from current user
        List<Lift> allLifts = liftRepository.findByUserIdOrderByIdDesc(userId);

        // Get current date and assign to lift
        LocalDate today = LocalDate.now();
        List<Lift> todayLifts = allLifts.stream()
                .filter(l -> toLocalDate(l.getId()).equals(today))
                .collect(Collectors.toList());

        int todaySets = todayLifts.size();

        // Calculate volume for current session
        long rawVolume = todayLifts.stream().mapToLong(l -> (long) l.getWeight() * l.getReps()).sum();
        String todayVolume = rawVolume >= 1000
                ? String.format("%.1fk", rawVolume / 1000.0)
                : String.valueOf(rawVolume);

        // Compile all lifts as chart entries
        List<ChartBar> chartBars = buildChartBars(allLifts, range);

        DateTimeFormatter tableFmt = DateTimeFormatter.ofPattern("MMM d");

        // 
        List<LiftEntry> recentEntries = allLifts.stream().limit(8).map(l -> {
                    String  dateLabel = toLocalDate(l.getId()).format(tableFmt);
                    return new LiftEntry(
                            l.getName(), l.getWeight(), l.getReps(),
                            1, dateLabel);
                })
                .collect(Collectors.toList());  

        // Add all created components to dashboard model
        model.addAttribute("liftForm", new LiftForm());
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("todayDate", today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        model.addAttribute("todaySets", todaySets);
        model.addAttribute("todayVolume", todayVolume);
        model.addAttribute("chartRange", range);
        model.addAttribute("chartBars", chartBars);
        model.addAttribute("recentEntries", recentEntries);

        // Direct user to home page with created model
        return "home";
    }

    @PostMapping("/lifts")
    public String logLift(@ModelAttribute LiftForm form, HttpSession session, RedirectAttributes ra)
    {
        // Redirect to login if no session exists
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
        {
            return "redirect:/login";
        }

        if (form.getLiftName() == null || form.getLiftName().isBlank() || form.getWeight() <= 0 || form.getReps() <= 0)
        {
            // redirect user to home page with error message if any sections are left blank
            ra.addFlashAttribute("errorMsg", "Please fill in exercise, weight, and reps.");
            return "redirect:/home";
        }

        // Save each set as an individual Lift row
        int sets = form.getSets();
        for (int i = 0; i < sets; i++)
        {
            Lift lift = new Lift(form.getWeight(), form.getReps(), form.getLiftName());
            lift.setUserId(userId);
            liftRepository.save(lift);
        }

        ra.addFlashAttribute("successMsg", form.getLiftName() + " " + form.getWeight() + " lbs × " + form.getReps() + " logged!");

        return "redirect:/home";
    }

    // Helper functions

    private List<ChartBar> buildChartBars(List<Lift> allLifts, String range)
    {
        LocalDate today = LocalDate.now();
        LocalDate cutoff;
        switch (range)
        {
            case "30":  cutoff = today.minusDays(30); break;
            case "all": cutoff = LocalDate.of(2000, 1, 1); break;
            default:    cutoff = today.minusDays(7);
        }

        DateTimeFormatter barFmt = DateTimeFormatter.ofPattern("MMM d");

        Map<LocalDate, Long> volumeByDate = new LinkedHashMap<>();
        allLifts.stream()
                .filter(l -> !toLocalDate(l.getId()).isBefore(cutoff))
                .sorted(Comparator.comparingLong(Lift::getId))
                .forEach(l -> {
                    LocalDate d = toLocalDate(l.getId());
                    volumeByDate.merge(d, (long) l.getWeight() * l.getReps(), Long::sum);
                });

        if (volumeByDate.isEmpty()) return Collections.emptyList();

        long maxVol = Collections.max(volumeByDate.values());

        return volumeByDate.entrySet().stream()
                .map(e -> new ChartBar(
                        e.getKey().format(barFmt),
                        (int) Math.round((double) e.getValue() / maxVol * 100)))
                .collect(Collectors.toList());
    }

    // Replace this with lift.getCreatedAt() once you add a createdAt field to Lift
    private LocalDate toLocalDate(Long id)
    {
        return LocalDate.now();
    }
}