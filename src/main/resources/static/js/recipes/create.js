document.addEventListener('DOMContentLoaded', () => {
    function toggleBtn(target) {
        if (target.value.length > 0) {
            $("#submit").classList.remove("is-btn-inactive");
            $("#submit").classList.add("is-danger");
        } else {
            $("#submit").classList.remove("is-danger");
            $("#submit").classList.add("is-btn-inactive");
        }
    }

    $('#submit').addEventListener('click', submit);
    $('input.box').addEventListener('keyup', ({target}) => toggleBtn(target));
    $('input.box').addEventListener('keypress', e => {
        e.keyCode = e.keyCode || e.which;
        if (e.keyCode === 13 && e.target.value.length <= 0) {
            e.preventDefault();
            return false;
        }
    });

    function submit() {
        if ($('input.box').value.length > 0) $('#create-form').submit();
    }

    toggleBtn($('input.box'));

});