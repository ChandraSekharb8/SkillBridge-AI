const BASE_URL = "";

const registerForm = document.getElementById("registerForm");
const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const user = {
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        try {
            const response = await fetch(BASE_URL + "/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });

            const data = await response.json();

            message.innerText = data.message;

            if (data.success) {
                localStorage.setItem("userId", data.userId);
                localStorage.setItem("name", data.name);
                localStorage.setItem("email", data.email);

                setTimeout(function () {
                    window.location.href = "index.html";
                }, 1000);
            }

        } catch (error) {
            message.innerText = "Backend connection failed";
            console.log(error);
        }
    });
}

if (loginForm) {
    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const user = {
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        try {
            const response = await fetch(BASE_URL + "/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });

            const data = await response.json();

            message.innerText = data.message;

            if (data.success) {
                localStorage.setItem("userId", data.userId);
                localStorage.setItem("name", data.name);
                localStorage.setItem("email", data.email);

                setTimeout(function () {
                    window.location.href = "index.html";
                }, 1000);
            }

        } catch (error) {
            message.innerText = "Backend connection failed";
            console.log(error);
        }
    });
}