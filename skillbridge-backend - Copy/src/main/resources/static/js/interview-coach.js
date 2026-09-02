const BASE_URL = "";

const userId = localStorage.getItem("userId");
const userName = localStorage.getItem("name");

let activeResumeId = "";

if (!userId || !userName) {
    window.location.href = "login.html";
}

document.addEventListener("DOMContentLoaded", function () {
    loadResumes();
});

const resumeUploadForm = document.getElementById("resumeUploadForm");

if (resumeUploadForm) {
    resumeUploadForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const fileInput = document.getElementById("resumeFile");
        const uploadMessage = document.getElementById("uploadMessage");

        if (!fileInput.files[0]) {
            uploadMessage.innerText = "Please select a PDF file.";
            return;
        }

        const formData = new FormData();
        formData.append("file", fileInput.files[0]);

        uploadMessage.innerText = "Uploading resume...";

        try {
            const response = await fetch(BASE_URL + "/api/resumes/upload/" + userId, {
                method: "POST",
                body: formData
            });

            const data = await response.text();

            uploadMessage.innerText = data;
            fileInput.value = "";

            await loadResumes();

        } catch (error) {
            uploadMessage.innerText = "Resume upload failed. Check backend.";
            console.log(error);
        }
    });
}

async function loadResumes() {
    try {
        const response = await fetch(BASE_URL + "/api/resumes/user/" + userId);
        const resumes = await response.json();

        if (resumes.length === 0) {
            activeResumeId = "";
            return;
        }

        let latestResume = resumes[0];

        resumes.forEach(function (resume) {
            if (resume.id > latestResume.id) {
                latestResume = resume;
            }
        });

        activeResumeId = latestResume.id;

    } catch (error) {
        console.log("Unable to load resumes", error);
    }
}

function hideAllCoachBoxes() {
    const boxes = document.querySelectorAll(".coach-dynamic-box");

    boxes.forEach(function (box) {
        box.style.display = "none";
    });
}

function showBox(boxId) {
    hideAllCoachBoxes();

    const box = document.getElementById(boxId);

    if (box) {
        box.style.display = "block";
    }
}

function showTrainOnResume() {
    showBox("trainResumeBox");
}

function showTrainOnJobDescription() {
    showBox("trainJdBox");
}

function showPracticeAnswers() {
    showBox("practiceAnswerBox");
}

function showBestAnswer() {
    showBox("bestAnswerBox");
}

function checkResume(resultBox) {
    if (!activeResumeId) {
        resultBox.innerText = "Please upload a resume first in Train on Resume section.";
        return false;
    }

    return true;
}

async function generateTypedInterviewQuestions(questionType) {
    const resultBox = document.getElementById("interviewTrainingResult");

    if (!checkResume(resultBox)) {
        return;
    }

    if (questionType === "HR") {
        resultBox.innerHTML = '<div class="ai-loading">AI is generating HR questions from your resume...</div>';
    } else if (questionType === "PROJECT") {
        resultBox.innerHTML = '<div class="ai-loading">AI is generating project questions from your resume...</div>';
    } else {
        resultBox.innerHTML = '<div class="ai-loading">AI is generating resume-based questions...</div>';
    }

    const requestBody = {
        resumeId: Number(activeResumeId),
        questionType: questionType
    };

    try {
        const response = await fetch(BASE_URL + "/api/ai/interview-prep/questions", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.text();
        showPremiumResult(resultBox, data);

        resultBox.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    } catch (error) {
        resultBox.innerText = "Interview question generation failed. Check backend or Gemini API key.";
        console.log(error);
    }
}

async function generateJobInterviewTraining() {
    const resultBox = document.getElementById("interviewTrainingResult");
    const jobDescription = document.getElementById("interviewJobDescription").value;

    if (!checkResume(resultBox)) {
        return;
    }

    if (!jobDescription.trim()) {
        resultBox.innerText = "Please paste a job description first.";
        return;
    }

    resultBox.innerHTML = '<div class="ai-loading">AI is training you for this job description...</div>';

    const requestBody = {
        resumeId: Number(activeResumeId),
        jobDescription: jobDescription
    };

    try {
        const response = await fetch(BASE_URL + "/api/ai/interview-prep/job", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.text();
        showPremiumResult(resultBox, data);

        resultBox.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    } catch (error) {
        resultBox.innerText = "Job-based interview training failed. Check backend or Gemini API key.";
        console.log(error);
    }
}

async function generateBestAnswer() {
    const resultBox = document.getElementById("interviewTrainingResult");
    const question = document.getElementById("interviewQuestion").value;

    if (!checkResume(resultBox)) {
        return;
    }

    if (!question.trim()) {
        resultBox.innerText = "Please paste an interview question first.";
        return;
    }

    resultBox.innerHTML = '<div class="ai-loading">AI is preparing the best answer...</div>';

    const requestBody = {
        resumeId: Number(activeResumeId),
        question: question
    };

    try {
        const response = await fetch(BASE_URL + "/api/ai/interview-prep/best-answer", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.text();
        showPremiumResult(resultBox, data);

        resultBox.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    } catch (error) {
        resultBox.innerText = "Best answer generation failed. Check backend or Gemini API key.";
        console.log(error);
    }
}

async function improveMyAnswer() {
    const resultBox = document.getElementById("interviewTrainingResult");
    const question = document.getElementById("improveInterviewQuestion").value;
    const answer = document.getElementById("candidateAnswer").value;

    if (!checkResume(resultBox)) {
        return;
    }

    if (!question.trim()) {
        resultBox.innerText = "Please paste an interview question first.";
        return;
    }

    if (!answer.trim()) {
        resultBox.innerText = "Please type your answer first.";
        return;
    }

    resultBox.innerHTML = '<div class="ai-loading">AI is improving your answer...</div>';

    const requestBody = {
        resumeId: Number(activeResumeId),
        question: question,
        answer: answer
    };

    try {
        const response = await fetch(BASE_URL + "/api/ai/interview-prep/improve-answer", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.text();
        showPremiumResult(resultBox, data);

        resultBox.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    } catch (error) {
        resultBox.innerText = "Answer improvement failed. Check backend or Gemini API key.";
        console.log(error);
    }
}

function escapeHtml(text) {
    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

function showPremiumResult(resultBox, text) {
    resultBox.innerHTML = formatAiResponse(text);
}

function formatAiResponse(text) {
    if (!text || text.trim() === "") {
        return '<p class="ai-paragraph">No result found.</p>';
    }

    let safeText = escapeHtml(text.trim());

    safeText = safeText.replace(/\r\n/g, "\n");
    safeText = safeText.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");
    safeText = safeText.replace(/^#+\s*/gm, "");

    const lines = safeText.split("\n");
    let html = '<div class="ai-output">';
    let inList = false;

    for (let i = 0; i < lines.length; i++) {
        let line = lines[i].trim();

        if (line === "") {
            if (inList) {
                html += "</ul>";
                inList = false;
            }
            continue;
        }

        if (isMainTitle(line)) {
            if (inList) {
                html += "</ul>";
                inList = false;
            }

            html += '<h3 class="ai-main-title">' + line + '</h3>';
            continue;
        }

        const numberedHeading = line.match(/^([0-9]+)\.\s*(.+)$/);

        if (numberedHeading) {
            if (inList) {
                html += "</ul>";
                inList = false;
            }

            html += '<div class="ai-section-heading">';
            html += '<span class="ai-number-badge">' + numberedHeading[1] + '</span>';
            html += '<span>' + numberedHeading[2] + '</span>';
            html += '</div>';
            continue;
        }

        const bulletLine = line.match(/^[-•*]\s+(.+)$/);

        if (bulletLine) {
            if (!inList) {
                html += '<ul class="ai-list">';
                inList = true;
            }

            html += '<li>' + bulletLine[1] + '</li>';
            continue;
        }

        const labelLine = line.match(/^([A-Za-z0-9 /&()+\-]{2,55}):\s*(.*)$/);

        if (labelLine) {
            if (inList) {
                html += "</ul>";
                inList = false;
            }

            if (labelLine[2].trim() === "") {
                html += '<p class="ai-label-only">' + labelLine[1] + ':</p>';
            } else {
                html += '<p class="ai-paragraph"><strong>' + labelLine[1] + ':</strong> ' + labelLine[2] + '</p>';
            }

            continue;
        }

        if (inList) {
            html += "</ul>";
            inList = false;
        }

        html += '<p class="ai-paragraph">' + line + '</p>';
    }

    if (inList) {
        html += "</ul>";
    }

    html += "</div>";

    return html;
}

function isMainTitle(line) {
    const titles = [
        "AI RESUME ANALYSIS",
        "JOB MATCH ANALYSIS",
        "TAILORED RESUME DRAFT",
        "TAILORED RESUME FOR JOB DESCRIPTION",
        "JOB DESCRIPTION INTERVIEW TRAINING",
        "BEST INTERVIEW ANSWER",
        "INTERVIEW ANSWER IMPROVEMENT",
        "HR INTERVIEW TRAINING",
        "PROJECT INTERVIEW TRAINING",
        "RESUME INTERVIEW TRAINING",
        "HR INTERVIEW QUESTIONS",
        "PROJECT INTERVIEW QUESTIONS",
        "RESUME INTERVIEW QUESTIONS"
    ];

    return titles.includes(line.toUpperCase());
}
