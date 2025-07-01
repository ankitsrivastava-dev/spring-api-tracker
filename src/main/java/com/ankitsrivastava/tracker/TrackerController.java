package com.ankitsrivastava.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api-tracker")
public class TrackerController {

    private final TrackerAspect trackerAspect;
    private final ApiTrackerProperties props;

    public TrackerController(TrackerAspect trackerAspect, ApiTrackerProperties props) {
        this.trackerAspect = trackerAspect;
        this.props = props;
    }

    @GetMapping("/logs")
    public ResponseEntity<?> getLogs() {
        if (!props.isLogEndpoint()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Log viewer is disabled.");
        }
        return ResponseEntity.ok(new ArrayList<>(trackerAspect.logs));
    }
}