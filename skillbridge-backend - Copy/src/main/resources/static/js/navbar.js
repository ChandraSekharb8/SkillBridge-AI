document.addEventListener("DOMContentLoaded", function () {
    renderNavbar();
    renderHomeAuthButtons();
});

function renderNavbar() {
    const navLinks = document.getElementById("navLinks");

    if (!navLinks) {
        return;
    }

    const userId = localStorage.getItem("userId");
    const userName = localStorage.getItem("name");

    if (userId && userName) {
        navLinks.innerHTML = `
            <a href="index.html">Home</a>
            <a href="index.html#aboutUs">About Us</a>
            <a href="#" onclick="logout()">Logout</a>

            <div class="feature-menu-wrapper">
                <button class="menu-button" onclick="toggleFeatureMenu()">☰ Menu</button>

                <div class="feature-menu" id="featureMenu">
                    <a href="ai-resume-analysis.html">AI Resume Analysis</a>
                    <a href="ask-ai.html">Ask AI</a>
                    <a href="resume-builder.html">Resume Builder</a>
                    <a href="job-match.html">Job Match</a>
                    <a href="interview-coach.html">Interview Coach</a>
                </div>
            </div>
        `;
    } else {
        navLinks.innerHTML = `
            <a href="index.html">Home</a>
            <a href="index.html#aboutUs">About Us</a>
            <a href="login.html">Login</a>

            <div class="feature-menu-wrapper">
                <button class="menu-button" onclick="toggleFeatureMenu()">☰ Menu</button>

                <div class="feature-menu" id="featureMenu">
                    <a href="ai-resume-analysis.html">AI Resume Analysis</a>
                    <a href="ask-ai.html">Ask AI</a>
                    <a href="resume-builder.html">Resume Builder</a>
                    <a href="job-match.html">Job Match</a>
                    <a href="interview-coach.html">Interview Coach</a>
                </div>
            </div>
        `;
    }
}

function toggleFeatureMenu() {
    const featureMenu = document.getElementById("featureMenu");

    if (!featureMenu) {
        return;
    }

    featureMenu.classList.toggle("show-feature-menu");
}

document.addEventListener("click", function (event) {
    const menuWrapper = document.querySelector(".feature-menu-wrapper");
    const featureMenu = document.getElementById("featureMenu");

    if (!menuWrapper || !featureMenu) {
        return;
    }

    if (!menuWrapper.contains(event.target)) {
        featureMenu.classList.remove("show-feature-menu");
    }
});

function renderHomeAuthButtons() {
    const homeAuthButtons = document.getElementById("homeAuthButtons");
    const bottomAuthButtons = document.getElementById("bottomAuthButtons");

    const userId = localStorage.getItem("userId");
    const userName = localStorage.getItem("name");

    const loggedInButtons = `
        <a href="ai-resume-analysis.html" class="hero-btn">Start Resume Analysis</a>
        <a href="interview-coach.html" class="hero-btn secondary">Practice Interview</a>
    `;

    const loggedOutButtons = `
        <a href="register.html" class="hero-btn">Get Started</a>
        <a href="login.html" class="hero-btn secondary">Login</a>
    `;

    if (homeAuthButtons) {
        if (userId && userName) {
            homeAuthButtons.innerHTML = loggedInButtons;
        } else {
            homeAuthButtons.innerHTML = loggedOutButtons;
        }
    }

    if (bottomAuthButtons) {
        if (userId && userName) {
            bottomAuthButtons.innerHTML = loggedInButtons;
        } else {
            bottomAuthButtons.innerHTML = loggedOutButtons;
        }
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}
