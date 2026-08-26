(function () {
    const body = document.body;
    const themeButton = document.getElementById('theme-toggle');
    const storedTheme = localStorage.getItem('certforge-theme');
    if (storedTheme === 'dark' || (!storedTheme && window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        body.classList.add('dark');
    }
    if (themeButton) {
        themeButton.addEventListener('click', function () {
            body.classList.toggle('dark');
            localStorage.setItem('certforge-theme', body.classList.contains('dark') ? 'dark' : 'light');
        });
    }

    document.querySelectorAll('[data-language]').forEach(function (button) {
        button.addEventListener('click', function () {
            const url = new URL(window.location.href);
            url.searchParams.set('lang', button.dataset.language);
            window.location.assign(url.toString());
        });
    });

    function isTypingTarget(target) {
        return target && (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA' || target.tagName === 'SELECT' || target.isContentEditable);
    }

    if (body.dataset.page === 'review') {
        document.addEventListener('keydown', function (event) {
            if (isTypingTarget(event.target)) return;
            const key = event.key.toLowerCase();
            if (/^[1-6]$/.test(key)) {
                const input = document.querySelector('#review-form input[data-key="' + String.fromCharCode(64 + Number(key)) + '"]');
                if (input && !input.disabled) { input.click(); event.preventDefault(); }
            } else if (key === 'enter') {
                const button = document.querySelector('.check-answer');
                if (button) { button.click(); event.preventDefault(); }
            } else if (key === 'n' || event.key === 'ArrowRight') {
                const button = document.querySelector('.bottom-actions form:last-of-type button:not([disabled])');
                if (button) { button.click(); event.preventDefault(); }
            } else if (key === 'p' || event.key === 'ArrowLeft') {
                const button = document.querySelector('.bottom-actions form:first-of-type button:not([disabled])');
                if (button) { button.click(); event.preventDefault(); }
            } else if (key === 's') {
                const form = document.querySelector('.question-card-top form[action$="/review/star"]');
                if (form) { form.submit(); event.preventDefault(); }
            }
        });
    }

    const examMain = document.querySelector('[data-started-at]');
    const timer = document.getElementById('exam-timer');
    if (examMain && timer) {
        const started = Number(examMain.dataset.startedAt);
        const tick = function () {
            const elapsed = Math.max(0, Math.floor((Date.now() - started) / 1000));
            const h = String(Math.floor(elapsed / 3600)).padStart(2, '0');
            const m = String(Math.floor((elapsed % 3600) / 60)).padStart(2, '0');
            const s = String(elapsed % 60).padStart(2, '0');
            timer.textContent = h + ':' + m + ':' + s;
        };
        tick();
        window.setInterval(tick, 1000);
    }
})();
