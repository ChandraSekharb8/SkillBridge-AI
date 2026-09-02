package com.skillbridge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skillbridge.model.Resume;
import com.skillbridge.model.User;
import com.skillbridge.repository.ResumeRepository;
import com.skillbridge.repository.UserRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfService pdfService;

    public String uploadResume(Long userId, MultipartFile file) {

        try {
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                return "User not found";
            }

            if (file.isEmpty()) {
                return "Please upload a valid PDF file";
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return "Only PDF files are allowed";
            }

            String extractedText = pdfService.extractTextFromPdf(file);

            Resume resume = new Resume();
            resume.setFileName(file.getOriginalFilename());
            resume.setExtractedText(extractedText);
            resume.setUploadedAt(LocalDateTime.now());
            resume.setUser(optionalUser.get());

            resumeRepository.save(resume);

            return "Resume uploaded and text extracted successfully";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while uploading resume: " + e.getMessage();
        }
    }

    public List<Resume> getResumesByUser(Long userId) {
        return resumeRepository.findByUser_IdOrderByUploadedAtDesc(userId);
    }
}