package com.skillbridge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.skillbridge.dto.ChatBotRequest;
import com.skillbridge.dto.InterviewTrainingRequest;
import com.skillbridge.dto.JobDescriptionRequest;
import com.skillbridge.service.AiService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/resume-analysis/{resumeId}")
    public String analyzeResumeWithAi(@PathVariable Long resumeId) {
        return aiService.analyzeResumeWithAi(resumeId);
    }

    @PostMapping("/job-match")
    public String compareResumeWithJobUsingAi(@RequestBody JobDescriptionRequest request) {
        return aiService.compareResumeWithJobUsingAi(request);
    }

    @PostMapping("/resume-builder")
    public String generateResumeForJobDescription(@RequestBody JobDescriptionRequest request) {
        return aiService.generateResumeForJobDescription(request);
    }

    @PostMapping("/chat")
    public String chatWithSkillBridgeAi(@RequestBody ChatBotRequest request) {
        return aiService.chatWithSkillBridgeAi(request);
    }

    @PostMapping("/interview-prep/questions")
    public String generateTypedInterviewQuestions(@RequestBody InterviewTrainingRequest request) {
        return aiService.generateTypedInterviewQuestions(request);
    }

    @PostMapping("/interview-prep/job")
    public String generateJobBasedInterviewTraining(@RequestBody InterviewTrainingRequest request) {
        return aiService.generateJobBasedInterviewTraining(request);
    }

    @PostMapping("/interview-prep/best-answer")
    public String generateBestInterviewAnswer(@RequestBody InterviewTrainingRequest request) {
        return aiService.generateBestInterviewAnswer(request);
    }

    @PostMapping("/interview-prep/improve-answer")
    public String improveCandidateInterviewAnswer(@RequestBody InterviewTrainingRequest request) {
        return aiService.improveCandidateInterviewAnswer(request);
    }
}
