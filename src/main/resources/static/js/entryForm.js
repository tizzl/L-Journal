document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("entryForm");
    const title = document.getElementById("title");
    const body = document.getElementById("body");
    const titleError = document.getElementById("title-error");
    const bodyError = document.getElementById("body-error");

    form.addEventListener("submit", function (event) {
        let hasError = false;

        // Titel prüfen
        if (!title.value.trim()) {
            title.style.borderColor = "red";
            titleError.textContent = "Titel darf nicht leer sein!";
            hasError = true;
        } else {
            title.style.borderColor = "";
            titleError.textContent = "";
        }

        // Body prüfen
        if (!body.value.trim()) {
            body.style.borderColor = "red";
            bodyError.textContent = "Inhalt darf nicht leer sein!";
            hasError = true;
        } else {
            body.style.borderColor = "";
            bodyError.textContent = "";
        }

        if (hasError) {
            event.preventDefault(); // Submit verhindern
            return false;           // zusätzlich sicherstellen
        }
    });
});

// BFCache-Reset beim Zurück/Weiter
window.addEventListener('pageshow', function (event) {
    if (event.persisted || performance.getEntriesByType("navigation")[0].type === 'back_forward') {
        const form = document.getElementById('entryForm');
        if (form) form.reset();
    }
});