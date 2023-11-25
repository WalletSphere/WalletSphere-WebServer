package com.khomishchak.ws.controllers;

import com.khomishchak.ws.model.Feedback;
import com.khomishchak.ws.model.requests.FeedbackRequest;
import com.khomishchak.ws.model.response.CreateFeedbackResp;
import com.khomishchak.ws.services.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<CreateFeedbackResp> createFeedback(@RequestBody FeedbackRequest feedbackRequest,
                                                             @RequestAttribute long userId) {
        return new ResponseEntity<>(feedbackService.saveFeedback(feedbackRequest, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return new ResponseEntity<>(feedbackService.getAllFeedbacks(), HttpStatus.OK);
    }
}
