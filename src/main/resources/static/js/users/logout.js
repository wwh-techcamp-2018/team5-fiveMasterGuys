document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = $('#logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', setUserLogout);
    }
});

function setUserLogout() {
    fetchManager({
        url: '/api/users/logout',
        method: 'POST',
        onSuccess: () => {
            location.reload();
        },
        onFailed: () => {},
        onError: () => {}
    });
}
