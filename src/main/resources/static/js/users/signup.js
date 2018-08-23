document.addEventListener('DOMContentLoaded', () => {
    new Signup();
})

class Signup {
    constructor() {
        this.emailField = $('#email-field');
        this.passwordField = $('#password-field');
        this.passwordCheckField = $('#password-check-field');
        this.nameField = $('#name-field');
        this.signupBtn = $('#signup-btn');

        this.registerEvents();
    }

    registerEvents() {
        this.signupBtn.addEventListener('click', () => {
            const email = this.emailField.value;
            const password = this.passwordField.value;
            const passwordCheck = this.passwordCheckField.value;
            const name = this.nameField.value;

            fetchManager({
                url: '/api/users/signup',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email,
                    password,
                    passwordCheck,
                    name
                }),
                onSuccess: () => {
                    location.href = document.referrer;
                },
                onFailed: () => {
                    alert('유효하지 않은 값이 입력되었습니다.')
                },
                onError: () => {
                    alert('요청중 문제가 발생하였습니다. 재접속 후 시도해주세요.')
                }
            })
        })
    }
}
