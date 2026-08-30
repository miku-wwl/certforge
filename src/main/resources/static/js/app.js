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

    document.querySelectorAll('[data-reset-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!window.confirm(form.getAttribute('data-reset-confirm'))) {
                event.preventDefault();
            }
        });
    });

    document.querySelectorAll('[data-import-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!window.confirm(form.getAttribute('data-import-confirm'))) {
                event.preventDefault();
            }
        });
    });

    function isTypingTarget(target) {
        return target && (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA' || target.tagName === 'SELECT' || target.isContentEditable);
    }

    function legacyCopy(text) {
        const textarea = document.createElement('textarea');
        textarea.value = text;
        textarea.setAttribute('readonly', '');
        textarea.style.position = 'fixed';
        textarea.style.top = '-9999px';
        textarea.style.opacity = '0';
        document.body.appendChild(textarea);
        textarea.select();
        textarea.setSelectionRange(0, textarea.value.length);
        let copied = false;
        try {
            copied = document.execCommand('copy');
        } finally {
            textarea.remove();
        }
        return copied;
    }

    async function copyText(text) {
        if (navigator.clipboard && window.isSecureContext) {
            try {
                await navigator.clipboard.writeText(text);
                return true;
            } catch (error) {
                // Fall back for browsers that expose Clipboard API but deny this call.
            }
        }
        return legacyCopy(text);
    }

    function buildQuestionText(card) {
        const question = card && card.querySelector('.question-text');
        const options = card ? Array.from(card.querySelectorAll('.option-list .option-row')) : [];
        if (!question || !options.length) return '';
        const optionLines = options.map(function (option) {
            const label = option.querySelector('.option-label');
            const copy = option.querySelector('.option-copy');
            return (label ? label.textContent.trim() : '') + '. ' + (copy ? copy.textContent.trim() : '');
        });
        return question.textContent.trim() + '\n\n' + optionLines.join('\n');
    }

    function buildQuestionAnswerText(card) {
        const questionText = buildQuestionText(card);
        const answer = card && (card.querySelector('[data-copy-answer-source] [data-copy-answer]')
            || card.querySelector('[data-copy-answer]'));
        const answerSource = answer && answer.closest('[data-copy-answer-source]');
        const answerLabel = answerSource
            ? answerSource.querySelector('.revealed-answer span')
            : null;
        const explanation = card && (card.querySelector('[data-copy-answer-source] [data-copy-explanation]')
            || card.querySelector('[data-copy-explanation]'));
        const explanationSource = explanation && explanation.closest('[data-copy-answer-source]');
        const explanationLabel = explanationSource
            ? explanationSource.querySelector('.deep-explanation .inline-explanation-heading strong')
            : null;
        if (!questionText || !answer || !explanation) return '';

        return [
            questionText,
            (answerLabel ? answerLabel.textContent.trim() : 'Correct answer') + ': ' + answer.textContent.trim(),
            (explanationLabel ? explanationLabel.textContent.trim() : 'Explanation') + ':\n' + explanation.innerText.trim()
        ].join('\n\n');
    }

    function updateCopyButton(button, succeeded) {
        const status = button.querySelector('[data-copy-status]');
        const originalLabel = button.dataset.copyLabel || 'Copy';
        button.classList.toggle('copied', succeeded);
        button.classList.toggle('copy-failed', !succeeded);
        if (status) {
            status.textContent = succeeded
                ? (button.dataset.copiedLabel || 'Copied')
                : (button.dataset.copyFailedLabel || 'Copy failed');
        }
        window.clearTimeout(button.copyResetTimer);
        button.copyResetTimer = window.setTimeout(function () {
            button.classList.remove('copied', 'copy-failed');
            if (status) status.textContent = originalLabel;
        }, 1800);
    }

    document.querySelectorAll('[data-copy-question], [data-copy-question-answer]').forEach(function (button) {
        button.addEventListener('click', async function () {
            const card = button.closest('.question-card');
            const text = button.hasAttribute('data-copy-question-answer')
                ? buildQuestionAnswerText(card)
                : buildQuestionText(card);
            const succeeded = text ? await copyText(text) : false;
            updateCopyButton(button, succeeded);
        });
    });

    if (body.dataset.page === 'review') {
        const answerReveal = document.querySelector('[data-answer-reveal]');
        if (answerReveal) {
            window.requestAnimationFrame(function () {
                answerReveal.scrollIntoView({ behavior: 'smooth', block: 'start' });
            });
        }
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
