document.addEventListener('DOMContentLoaded', () => {
    function toggleBtn(target) {
        if (target.value.trim().length > 0) {
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
        if (e.keyCode === 13){
            e.preventDefault();
            submit();
            return false;
        }
    });

    function submit() {
        checkLoginOrRedirect();
        if ($('input.box').value.trim().length > 0) {
            $('#create-form').submit();
        }
    }

    toggleBtn($('input.box'));

});