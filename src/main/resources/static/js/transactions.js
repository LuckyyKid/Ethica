document.addEventListener('DOMContentLoaded', function () {
    var btn = document.getElementById('showAllBtn');
    if (!btn) return;

    btn.addEventListener('click', function () {
        var rows = document.querySelectorAll('.hidden-row');
        var btnText = document.getElementById('btnText');

        for (var i = 0; i < rows.length; i++) {
            rows[i].classList.toggle('hidden');
        }

        if (btnText.textContent === 'Show all transactions') {
            btnText.textContent = 'Show less';
        } else {
            btnText.textContent = 'Show all transactions';
        }
    });
});
