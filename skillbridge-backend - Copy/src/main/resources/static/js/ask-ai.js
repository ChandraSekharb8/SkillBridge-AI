const BASE_URL = "";

document.addEventListener("DOMContentLoaded", function () {
    const questionInput = document.getElementById("chatQuestion");

    if (questionInput) {
        questionInput.addEventListener("keydown", function (event) {
            if (event.key === "Enter" && !event.shiftKey) {
                event.preventDefault();
                askSkillBridgeChat();
            }
        });
    }
});

async function askSkillBridgeChat() {
    const questionInput = document.getElementById("chatQuestion");
    const question = questionInput.value.trim();

    if (!question) {
        appendChatMessage("ai", "Please type your question first.");
        return;
    }

    appendChatMessage("user", question);
    questionInput.value = "";
    autoResizeTextArea(questionInput);

    const aiBubble = appendChatMessage("ai", "Thinking...");

    const requestBody = {
        question: question
    };

    try {
        const response = await fetch(BASE_URL + "/api/ai/chat", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.text();
        aiBubble.innerHTML = formatChatResponse(data);

        scrollChatToBottom();

    } catch (error) {
        aiBubble.innerText = "Chatbot failed. Check backend or Gemini API key.";
        console.log(error);
    }
}

function appendChatMessage(type, message) {
    const chatBox = document.getElementById("chatBox");
    const rowDiv = document.createElement("div");
    const messageDiv = document.createElement("div");

    if (type === "user") {
        rowDiv.className = "chat-row user-row";
        messageDiv.className = "chat-message user-message";
        messageDiv.innerText = message;
    } else {
        rowDiv.className = "chat-row ai-row";
        messageDiv.className = "chat-message ai-message";
        messageDiv.innerHTML = formatChatResponse(message);
    }

    rowDiv.appendChild(messageDiv);
    chatBox.appendChild(rowDiv);
    scrollChatToBottom();

    return messageDiv;
}

function clearSkillBridgeChat() {
    const chatBox = document.getElementById("chatBox");

    chatBox.innerHTML = `
        <div class="chat-row ai-row">
            <div class="chat-message ai-message">
                Hi, I am SkillBridge AI. Ask me anything about coding, projects, interviews, or placements.
            </div>
        </div>
    `;
}

function scrollChatToBottom() {
    const chatBox = document.getElementById("chatBox");
    chatBox.scrollTop = chatBox.scrollHeight;
}

function autoResizeTextArea(textarea) {
    if (!textarea) {
        return;
    }

    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
}

function formatChatResponse(text) {
    if (!text || text.trim() === "") {
        return "No answer found.";
    }

    let safeText = escapeHtml(text.trim());

    safeText = safeText.replace(/\r\n/g, "\n");
    safeText = safeText.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");
    safeText = safeText.replace(/^#+\s*/gm, "");

    const lines = safeText.split("\n");
    const hasStructure = lines.some(function (line) {
        return /^([0-9]+)\.\s+/.test(line.trim()) || /^[-•*]\s+/.test(line.trim());
    });

    if (!hasStructure && lines.length <= 3) {
        return safeText.replace(/\n/g, "<br>");
    }

    return formatChatStructuredResponse(safeText);
}

function formatChatStructuredResponse(safeText) {
    const lines = safeText.split("\n");
    let html = '<div class="chat-ai-output">';
    let inList = false;

    lines.forEach(function (rawLine) {
        const line = rawLine.trim();

        if (line === "") {
            if (inList) {
                html += "</ul>";
                inList = false;
            }
            return;
        }

        const numberedHeading = line.match(/^([0-9]+)\.\s*(.+)$/);

        if (numberedHeading) {
            if (inList) {
                html += "</ul>";
                inList = false;
            }

            html += '<p class="chat-ai-heading">' + numberedHeading[1] + '. ' + numberedHeading[2] + '</p>';
            return;
        }

        const bulletLine = line.match(/^[-•*]\s+(.+)$/);

        if (bulletLine) {
            if (!inList) {
                html += '<ul class="chat-ai-list">';
                inList = true;
            }

            html += '<li>' + bulletLine[1] + '</li>';
            return;
        }

        if (inList) {
            html += "</ul>";
            inList = false;
        }

        html += '<p>' + line + '</p>';
    });

    if (inList) {
        html += "</ul>";
    }

    html += "</div>";
    return html;
}

function escapeHtml(text) {
    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}
