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

    // ════════════════════════════════════════════════
    //  GET /home  —  render the dashboard
    // ════════════════════════════════════════════════
    @GetMapping("/home")
    public String dashboard(
            @RequestParam(name = "range", defaultValue = "7") String range,
            HttpSession session,
            Model model)
    {
        // Redirect to login if no session exists
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        List<Lift> allLifts = liftRepository.findByUserIdOrderByIdDesc(userId);

        // ── Today's stats ────────────────────────────
        LocalDate today = LocalDate.now();
        List<Lift> todayLifts = allLifts.stream()
                .filter(l -> toLocalDate(l.getId()).equals(today))
                .collect(Collectors.toList());

        int    todaySets   = todayLifts.size();
        long   rawVolume   = todayLifts.stream()
                                .mapToLong(l -> (long) l.getWeight() * l.getReps())
                                .sum();
        String todayVolume = rawVolume >= 1000
                ? String.format("%.1fk", rawVolume / 1000.0)
                : String.valueOf(rawVolume);
        int todayPRs = countTodayPRs(allLifts, today);

        // ── Chart bars ───────────────────────────────
        List<ChartBar> chartBars = buildChartBars(allLifts, range);

        // ── Recent entries table (latest 8) ──────────
        Map<String, Integer> prMap = buildPRMap(allLifts);
        DateTimeFormatter tableFmt = DateTimeFormatter.ofPattern("MMM d");

        List<LiftEntry> recentEntries = allLifts.stream()
                .limit(8)
                .map(l -> {
                    String  dateLabel = toLocalDate(l.getId()).format(tableFmt);
                    boolean isPR      = prMap.getOrDefault(l.getName(), 0) == l.getWeight()
                                        && isFirstOccurrenceOfPR(allLifts, l);
                    return new LiftEntry(
                            l.getName(), l.getWeight(), l.getReps(),
                            1, dateLabel, isPR);
                })
                .collect(Collectors.toList());

        // ── Model ─────────────────────────────────────
        model.addAttribute("liftForm",      new LiftForm());
        model.addAttribute("userName",      session.getAttribute("userName"));
        model.addAttribute("todayDate",     today.format(
                DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        model.addAttribute("todaySets",     todaySets);
        model.addAttribute("todayVolume",   todayVolume);
        model.addAttribute("todayPRs",      todayPRs);
        model.addAttribute("chartRange",    range);
        model.addAttribute("chartBars",     chartBars);
        model.addAttribute("recentEntries", recentEntries);

        return "home";
    }

    // ════════════════════════════════════════════════
    //  POST /lifts  —  save a lift, redirect back
    // ════════════════════════════════════════════════
    @PostMapping("/lifts")
    public String logLift(
            @ModelAttribute LiftForm form,
            HttpSession session,
            RedirectAttributes ra)
    {
        // Redirect to login if no session exists
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        if (form.getLiftName() == null || form.getLiftName().isBlank()
                || form.getWeight() <= 0 || form.getReps() <= 0)
        {
            ra.addFlashAttribute("errorMsg", "Please fill in exercise, weight, and reps.");
            return "redirect:/home";
        }

        // Check for PR before saving
        List<Lift> existing = liftRepository.findByUserIdOrderByIdDesc(userId);
        Map<String, Integer> prMap = buildPRMap(existing);
        boolean isPR = form.getWeight() > prMap.getOrDefault(form.getLiftName(), 0);

        // Save — each set is stored as an individual Lift row
        int sets = form.getSets();
        for (int i = 0; i < sets; i++)
        {
            Lift lift = new Lift(form.getWeight(), form.getReps(), form.getLiftName());
            lift.setUserId(userId);
            liftRepository.save(lift);
        }

        if (isPR)
            ra.addFlashAttribute("prMsg",
                    "New PR on " + form.getLiftName() + " — " + form.getWeight() + " lbs!");
        else
            ra.addFlashAttribute("successMsg",
                    form.getLiftName() + " " + form.getWeight() + " lbs × " + form.getReps() + " logged!");

        return "redirect:/home";
    }

    // ════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════

    private Map<String, Integer> buildPRMap(List<Lift> lifts)
    {
        Map<String, Integer> prs = new HashMap<>();
        for (Lift l : lifts)
            prs.merge(l.getName(), l.getWeight(), Math::max);
        return prs;
    }

    private int countTodayPRs(List<Lift> allLifts, LocalDate today)
    {
        Map<String, Integer> prMap = buildPRMap(allLifts);
        return (int) allLifts.stream()
                .filter(l -> toLocalDate(l.getId()).equals(today))
                .filter(l -> l.getWeight() >= prMap.getOrDefault(l.getName(), 0))
                .map(Lift::getName)
                .distinct()
                .count();
    }

    private boolean isFirstOccurrenceOfPR(List<Lift> allLifts, Lift target)
    {
        return allLifts.stream()
                .filter(l -> l.getName().equals(target.getName())
                          && l.getWeight() == target.getWeight())
                .findFirst()
                .map(l -> l.getId().equals(target.getId()))
                .orElse(false);
    }

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