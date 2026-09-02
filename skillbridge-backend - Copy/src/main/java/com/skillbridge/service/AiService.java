package com.skillbridge.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillbridge.dto.ChatBotRequest;
import com.skillbridge.dto.InterviewTrainingRequest;
import com.skillbridge.dto.JobDescriptionRequest;
import com.skillbridge.model.Resume;
import com.skillbridge.repository.ResumeRepository;

@Service
public class AiService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private GeminiService geminiService;

    public String analyzeResumeWithAi(Long resumeId) {

        Optional<Resume> optionalResume = resumeRepository.findById(resumeId);

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();
        String resumeText = resume.getExtractedText();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        resumeText = limitText(resumeText);

        String prompt =
                "You are SkillBridge AI, an expert ATS resume analyzer and career mentor for Indian students and freshers.\n\n" +

                "Analyze the candidate resume deeply but explain in simple, practical English.\n\n" +

                "Important rules:\n" +
                "- Do not give generic advice.\n" +
                "- Use only details found in the resume.\n" +
                "- Do not invent experience, companies, certifications, or skills.\n" +
                "- Be honest but supportive.\n" +
                "- Focus on fresher/student resume improvement.\n" +
                "- Keep the output clean and easy to read.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "Give output exactly in this format:\n\n" +

                "AI RESUME ANALYSIS\n\n" +

                "1. ATS Score\n" +
                "Give score out of 100 and explain in 2 lines why.\n\n" +

                "2. Quick Summary\n" +
                "Give a short summary of the candidate profile in 3 to 4 lines.\n\n" +

                "3. Strong Points\n" +
                "List strong points from the resume.\n\n" +

                "4. Weak Points\n" +
                "List weak points clearly.\n\n" +

                "5. Missing Skills\n" +
                "List missing or weak skills that can improve job chances.\n\n" +

                "6. Resume Section Improvements\n" +
                "Give section-wise improvements for Summary, Skills, Projects, Education, and Links.\n\n" +

                "7. Better Resume Bullet Points\n" +
                "Rewrite 5 to 7 better resume bullet points based only on existing resume content.\n\n" +

                "8. Project Improvement Suggestions\n" +
                "Give practical suggestions to make existing projects stronger for interviews.\n\n" +

                "9. Interview Questions From This Resume\n" +
                "Give 8 likely interview questions from this resume.\n\n" +

                "10. Final Action Plan\n" +
                "Give a simple 5-step action plan for the candidate.\n\n" +

                "Candidate Resume:\n" +
                resumeText;

        return geminiService.generateContent(prompt);
    }

    public String compareResumeWithJobUsingAi(JobDescriptionRequest request) {

        Optional<Resume> optionalResume = resumeRepository.findById(request.getResumeId());

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();

        String resumeText = resume.getExtractedText();
        String jobDescription = request.getJobDescription();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        if (jobDescription == null || jobDescription.trim().isEmpty()) {
            return "Please paste a job description first.";
        }

        resumeText = limitText(resumeText);
        jobDescription = limitJobDescription(jobDescription);

        String prompt =
                "You are SkillBridge AI, an expert ATS resume matcher and job readiness mentor.\n\n" +

                "Compare the candidate resume with the job description. Give a practical and honest result.\n\n" +

                "Important rules:\n" +
                "- Do not invent skills that are not in the resume.\n" +
                "- Clearly separate matched skills and missing skills.\n" +
                "- Explain job requirements in fresher-friendly language.\n" +
                "- Give direct resume improvement suggestions.\n" +
                "- Keep output clean and useful.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "Give output exactly in this format:\n\n" +

                "JOB MATCH ANALYSIS\n\n" +

                "1. Match Percentage\n" +
                "Give a realistic percentage and explain why.\n\n" +

                "2. Hiring Chance\n" +
                "Write Low, Medium, Good, or Strong and explain in 2 to 3 lines.\n\n" +

                "3. Job Role Summary\n" +
                "Explain what this job expects in simple language.\n\n" +

                "4. Matched Skills\n" +
                "List skills from resume that match the job description.\n\n" +

                "5. Missing Skills\n" +
                "List skills required by the job description but missing or weak in resume.\n\n" +

                "6. Missing Keywords\n" +
                "List important ATS keywords missing from resume.\n\n" +

                "7. Resume Changes Needed\n" +
                "Give exact changes the candidate should make in resume.\n\n" +

                "8. Project Points To Highlight\n" +
                "Tell which project points should be highlighted for this job.\n\n" +

                "9. Skills To Learn Before Applying\n" +
                "Give a small learning plan based on missing skills.\n\n" +

                "10. Final Recommendation\n" +
                "Tell whether the candidate should apply now or improve first.\n\n" +

                "Candidate Resume:\n" +
                resumeText + "\n\n" +

                "Job Description:\n" +
                jobDescription;

        return geminiService.generateContent(prompt);
    }

    public String generateResumeForJobDescription(JobDescriptionRequest request) {

        Optional<Resume> optionalResume = resumeRepository.findById(request.getResumeId());

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();

        String resumeText = resume.getExtractedText();
        String jobDescription = request.getJobDescription();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        if (jobDescription == null || jobDescription.trim().isEmpty()) {
            return "Please paste a job description first.";
        }

        resumeText = limitText(resumeText);
        jobDescription = limitJobDescription(jobDescription);

        String prompt =
                "You are SkillBridge AI, an expert ATS-friendly resume writer for students and freshers.\n\n" +

                "Create a tailored resume draft using the candidate's uploaded resume and the pasted job description.\n\n" +

                "Very important rules:\n" +
                "- Do not create fake experience.\n" +
                "- Do not invent company names, internships, certificates, awards, percentages, or achievements.\n" +
                "- Use only the candidate's real resume information.\n" +
                "- You may improve wording and reorder content.\n" +
                "- You may align existing projects and skills with the job description.\n" +
                "- If a skill is required in JD but not found in resume, do not add it as an existing skill.\n" +
                "- Put missing skills only in Skills To Learn section.\n" +
                "- Keep the resume suitable for a fresher/student candidate.\n" +
                "- Make it ATS-friendly and professional.\n" +
                "- Do not use tables.\n" +
                "- Do not use markdown symbols like ### or **.\n" +
                "- Output should be ready to copy into a resume document.\n\n" +

                "Give output exactly in this format:\n\n" +

                "TAILORED RESUME DRAFT\n\n" +

                "1. Header\n" +
                "Use name, email, phone, GitHub, LinkedIn, and portfolio only if available in the uploaded resume. If not clear, write: Keep your original contact details here.\n\n" +

                "2. Professional Summary\n" +
                "Write 3 to 4 lines matching the job description.\n\n" +

                "3. Technical Skills\n" +
                "Group skills as:\n" +
                "Programming Languages:\n" +
                "Backend:\n" +
                "Frontend:\n" +
                "Database:\n" +
                "Tools:\n" +
                "AI / GenAI:\n\n" +

                "4. Projects\n" +
                "Rewrite existing projects from the resume in a stronger way.\n" +
                "For each project use:\n" +
                "Project Name:\n" +
                "Tech Stack:\n" +
                "Description:\n" +
                "Key Contributions:\n" +
                "Impact:\n\n" +

                "5. Education\n" +
                "Use only education details found in uploaded resume. If unclear, write: Keep your original education details here.\n\n" +

                "6. Certifications / Achievements\n" +
                "Use only real details found in resume. If not found, write: Add only your real certifications here.\n\n" +

                "7. ATS Keywords Added Naturally\n" +
                "List keywords from JD that match candidate profile and were used naturally.\n\n" +

                "8. Skills To Learn Before Applying\n" +
                "List missing JD skills that candidate should learn. Do not add them as fake existing skills.\n\n" +

                "9. Final Resume Tips\n" +
                "Give short practical tips.\n\n" +

                "Candidate Uploaded Resume:\n" +
                resumeText + "\n\n" +

                "Job Description:\n" +
                jobDescription;

        return geminiService.generateContent(prompt);
    }

    public String chatWithSkillBridgeAi(ChatBotRequest request) {

        String question = request.getQuestion();

        if (question == null || question.trim().isEmpty()) {
            return "Please type your question first.";
        }

        String prompt =
                "You are SkillBridge AI, a friendly human-like career mentor for students and freshers.\n\n" +

                "Your main behavior:\n" +
                "- Reply naturally like a helpful mentor, not like a strict template.\n" +
                "- Understand the user's message and respond according to context.\n" +
                "- Use simple English suitable for Indian students and freshers.\n" +
                "- Be practical, clear, and supportive.\n\n" +

                "Casual message rules:\n" +
                "- If user says hi, hello, hey, thanks, ok, yes, no, good morning, or small casual messages, reply in 1 to 3 short lines only.\n" +
                "- Do not add difficulty level for casual messages.\n" +
                "- Do not add headings for casual messages.\n" +
                "- Do not over-explain.\n\n" +

                "Technical question rules:\n" +
                "- If user asks about Java, Spring Boot, SQL, DSA, frontend, backend, or project doubts, explain step by step.\n" +
                "- Use simple examples.\n" +
                "- If code is needed, give clean copy-paste code.\n" +
                "- Do not give unnecessary long theory.\n\n" +

                "Interview question rules:\n" +
                "- If user asks for interview answer, give a spoken answer they can say in interview.\n" +
                "- Add key points only if useful.\n" +
                "- Keep it confident and fresher-friendly.\n\n" +

                "Resume/career question rules:\n" +
                "- Give practical resume, project, job, and interview suggestions.\n" +
                "- Do not create fake experience.\n" +
                "- Suggest honest improvements.\n\n" +

                "Avoid:\n" +
                "- Do not force headings for every answer.\n" +
                "- Do not always say difficulty level.\n" +
                "- Do not sound robotic.\n" +
                "- Do not write too much for small questions.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "User message:\n" +
                question;

        return geminiService.generateContent(prompt);
    }

    public String generateTypedInterviewQuestions(InterviewTrainingRequest request) {

        Optional<Resume> optionalResume = resumeRepository.findById(request.getResumeId());

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();

        String resumeText = resume.getExtractedText();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        String questionType = request.getQuestionType();

        if (questionType == null || questionType.trim().isEmpty()) {
            questionType = "RESUME";
        }

        resumeText = limitText(resumeText);

        String title;
        String focusArea;

        if (questionType.equalsIgnoreCase("HR")) {

            title = "HR INTERVIEW TRAINING";

            focusArea =
                    "Generate HR interview questions for a fresher/student.\n" +
                    "Focus on introduction, strengths, weakness, goals, teamwork, pressure, relocation, salary, and why should we hire you.\n" +
                    "Use resume details wherever possible.";

        } else if (questionType.equalsIgnoreCase("PROJECT")) {

            title = "PROJECT INTERVIEW TRAINING";

            focusArea =
                    "Generate project-based interview questions from the candidate resume.\n" +
                    "Focus on project workflow, architecture, frontend, backend, database, APIs, AI integration, challenges, deployment, and improvements.";

        } else {

            title = "RESUME INTERVIEW TRAINING";

            focusArea =
                    "Generate resume-based interview questions.\n" +
                    "Focus on skills, projects, education, career objective, resume gaps, tools, technologies, and candidate strengths.";
        }

        String prompt =
                "You are SkillBridge AI, an expert mock interview trainer for students and freshers.\n\n" +

                "Train the candidate based on their uploaded resume.\n\n" +

                "Important rules:\n" +
                "- Generate practical interview questions only from resume context.\n" +
                "- Do not ask unrelated advanced questions unless resume supports it.\n" +
                "- Give answers in first person where possible, so candidate can speak them.\n" +
                "- Keep answers clear and fresher-friendly.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "Give output exactly in this format:\n\n" +

                title + "\n\n" +

                "For each question use this format:\n" +
                "Question:\n" +
                "Best Answer:\n" +
                "Key Points To Remember:\n" +
                "Follow-up Question:\n\n" +

                "Generate 10 important questions.\n\n" +

                "Focus Area:\n" +
                focusArea + "\n\n" +

                "Candidate Resume:\n" +
                resumeText;

        return geminiService.generateContent(prompt);
    }

    public String generateJobBasedInterviewTraining(InterviewTrainingRequest request) {

        Optional<Resume> optionalResume = resumeRepository.findById(request.getResumeId());

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();

        String resumeText = resume.getExtractedText();
        String jobDescription = request.getJobDescription();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        if (jobDescription == null || jobDescription.trim().isEmpty()) {
            return "Please paste a job description first.";
        }

        resumeText = limitText(resumeText);
        jobDescription = limitJobDescription(jobDescription);

        String prompt =
                "You are SkillBridge AI, an expert job description interview trainer.\n\n" +

                "Train the candidate for this job using their resume and the pasted job description.\n\n" +

                "Important rules:\n" +
                "- Explain the job role in simple language.\n" +
                "- Predict likely interview questions from the JD.\n" +
                "- Connect answers with the candidate's resume honestly.\n" +
                "- Do not invent fake experience or fake skills.\n" +
                "- If a skill is missing, tell how to handle it honestly in interview.\n" +
                "- Keep output practical and interview-ready.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "Give output exactly in this format:\n\n" +

                "JOB DESCRIPTION INTERVIEW TRAINING\n\n" +

                "1. Role Understanding\n" +
                "Explain what this job expects in simple words.\n\n" +

                "2. Candidate Fit For This Role\n" +
                "Explain how the candidate profile matches this role.\n\n" +

                "3. Most Expected Technical Questions\n" +
                "Give 8 technical questions with best answers.\n\n" +

                "4. Most Expected Project Questions\n" +
                "Give 5 project questions with best answers using candidate resume.\n\n" +

                "5. HR / Manager Round Questions\n" +
                "Give 5 HR or manager round questions with best answers.\n\n" +

                "6. Missing Skills And Honest Answers\n" +
                "Mention missing skills and how candidate can answer honestly.\n\n" +

                "7. Resume Points To Highlight\n" +
                "Tell which resume/project points should be highlighted for this job.\n\n" +

                "8. Final Interview Strategy\n" +
                "Give a simple step-by-step strategy before attending interview.\n\n" +

                "Candidate Resume:\n" +
                resumeText + "\n\n" +

                "Job Description:\n" +
                jobDescription;

        return geminiService.generateContent(prompt);
    }

    public String generateBestInterviewAnswer(InterviewTrainingRequest request) {

        Optional<Resume> optionalResume = resumeRepository.findById(request.getResumeId());

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();

        String resumeText = resume.getExtractedText();
        String question = request.getQuestion();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        if (question == null || question.trim().isEmpty()) {
            return "Please paste an interview question first.";
        }

        resumeText = limitText(resumeText);

        String prompt =
                "You are SkillBridge AI, an expert interview answer coach.\n\n" +

                "Generate the best answer for the given interview question using the candidate resume.\n\n" +

                "Important rules:\n" +
                "- Answer should sound natural and confident.\n" +
                "- Write in first person, as if the candidate is speaking.\n" +
                "- Use candidate resume details honestly.\n" +
                "- Do not invent fake experience or skills.\n" +
                "- Keep it suitable for a fresher/student.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "Give output exactly in this format:\n\n" +

                "BEST INTERVIEW ANSWER\n\n" +

                "1. Question\n" +
                "Repeat the question.\n\n" +

                "2. What Interviewer Wants To Know\n" +
                "Explain the intention behind the question in simple words.\n\n" +

                "3. Best Spoken Answer\n" +
                "Give a strong answer the candidate can directly speak.\n\n" +

                "4. Short 30-Second Answer\n" +
                "Give a short version.\n\n" +

                "5. Detailed 1-Minute Answer\n" +
                "Give a detailed version.\n\n" +

                "6. Keywords To Include\n" +
                "List important keywords.\n\n" +

                "7. Mistakes To Avoid\n" +
                "Tell what the candidate should not say.\n\n" +

                "Candidate Resume:\n" +
                resumeText + "\n\n" +

                "Interview Question:\n" +
                question;

        return geminiService.generateContent(prompt);
    }

    public String improveCandidateInterviewAnswer(InterviewTrainingRequest request) {

        Optional<Resume> optionalResume = resumeRepository.findById(request.getResumeId());

        if (optionalResume.isEmpty()) {
            return "Resume not found. Please upload your resume again.";
        }

        Resume resume = optionalResume.get();

        String resumeText = resume.getExtractedText();
        String question = request.getQuestion();
        String answer = request.getAnswer();

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return "Resume text is empty. Please upload a clear PDF resume.";
        }

        if (question == null || question.trim().isEmpty()) {
            return "Please paste an interview question first.";
        }

        if (answer == null || answer.trim().isEmpty()) {
            return "Please type your answer first.";
        }

        resumeText = limitText(resumeText);

        String prompt =
                "You are SkillBridge AI, an expert mock interview evaluator and answer improvement coach.\n\n" +

                "Evaluate the candidate's answer and rewrite it in a stronger interview-ready way.\n\n" +

                "Important rules:\n" +
                "- Be honest but supportive.\n" +
                "- Explain what is good and what is missing.\n" +
                "- Improve the answer using candidate resume details.\n" +
                "- Do not invent fake experience or skills.\n" +
                "- Make the improved answer natural and speakable.\n" +
                "- Keep it suitable for a fresher/student.\n" +
                "- Do not use markdown symbols like ### or **.\n\n" +

                "Give output exactly in this format:\n\n" +

                "INTERVIEW ANSWER IMPROVEMENT\n\n" +

                "1. Score\n" +
                "Give score out of 100 with one short reason.\n\n" +

                "2. What Is Good\n" +
                "Mention strengths in the candidate answer.\n\n" +

                "3. What Is Missing\n" +
                "Mention missing technical points, project points, examples, or clarity.\n\n" +

                "4. Mistakes In Current Answer\n" +
                "Mention specific mistakes.\n\n" +

                "5. Improved Spoken Answer\n" +
                "Rewrite the answer in a confident interview-ready way.\n\n" +

                "6. Short 30-Second Answer\n" +
                "Give a short version.\n\n" +

                "7. Strong 1-Minute Answer\n" +
                "Give a stronger detailed version.\n\n" +

                "8. Keywords To Add\n" +
                "Give important words or phrases to include.\n\n" +

                "9. Final Tip\n" +
                "Give one practical interview tip.\n\n" +

                "Candidate Resume:\n" +
                resumeText + "\n\n" +

                "Interview Question:\n" +
                question + "\n\n" +

                "Candidate Answer:\n" +
                answer;

        return geminiService.generateContent(prompt);
    }

    private String limitText(String text) {

        int maxLength = 12000;

        if (text.length() > maxLength) {
            return text.substring(0, maxLength);
        }

        return text;
    }

    private String limitJobDescription(String text) {

        int maxLength = 6000;

        if (text.length() > maxLength) {
            return text.substring(0, maxLength);
        }

        return text;
    }
}
