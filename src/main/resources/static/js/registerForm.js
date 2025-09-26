const password = document.getElementById("password");
const password2 = document.getElementById("password2");
const registerBtn = document.getElementById("registerBtn");

function checkPasswords() {
    registerBtn.disabled = !(password.value && password.value === password2.value);
}

const email = document.getElementById("email");

function checkMail() {
    const emailValue = email.value;
    const emailPattern = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*$/;
    const errorSpan = document.getElementById("email-error");

    if (!emailPattern.test(emailValue)) {
        email.style.borderColor = "red";
        errorSpan.textContent = "Ungültige E-Mail!";
        email.setCustomValidity("Ungültige E-Mail-Adresse!")
        console.log("Email Adresse ungültig : " + email.value);
    } else {
        email.style.borderColor = "";
        errorSpan.textContent = "";
        email.setCustomValidity("");
        console.log("Email Adresse gültig: " + email.value);
    }

}

// Event Listener für beide Eingabefelder
password.addEventListener("input", checkPasswords);
password2.addEventListener("input", checkPasswords);
email.addEventListener("input", checkMail);