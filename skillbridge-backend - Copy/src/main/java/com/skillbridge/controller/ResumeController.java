package com.skillbridge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.skillbridge.model.Resume;
import com.skillbridge.service.ResumeService;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload/{userId}")
    public String uploadResume(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        return resumeService.uploadResume(userId, file);
    }

    @GetMapping("/user/{userId}")
    public List<Resume> getUserResumes(@PathVariable Long userId) {
        return resumeService.getResumesByUser(userId);
    }
}