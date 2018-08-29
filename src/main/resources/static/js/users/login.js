import { validateEmailValue, validatePasswordValue } from './utils.js';

document.addEventListener('DOMContentLoaded', () => {
    new Login(new ErrorMessageView($('#error-msg-box')));
})

class Login {
    constructor(errorMessageView) {
        this.emailField = $('#email-field');
        this.passwordField = $('#password-field');
        this.loginBtn = $('#login-btn');
        this.errorMessageView = errorMessageView;

        this.registerEvents();
    }

    registerEvents() {
        [this.emailField, this.passwordField].forEach(field => {
            field.addEventListener('keyup', ({ keyCode }) => {
                if (keyCode === 13) {
                    this.login();
                }
            })
        })
        this.loginBtn.addEventListener('click', this.login.bind(this))
    }

    login() {
        if (!this.checkLoginFields()) {
            return;
        }

        const email = this.emailField.value;
        const password = this.passwordField.value;

        fetchManager({
            url: '/api/users/login',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email,
                password
            })
        }).then(() => {
            location.href = document.referrer;
        }).catch(({errors}) => {
            if (errors) {
                this.errorMessageView.showMessage('아이디 또는 비밀번호가 일치하지 않습니다.', false);
                return;
            }
            this.errorMessageView.showMessage('요청중 문제가 발생하였습니다. 재접속 후 시도해주세요.', false);
        });
    }

    checkLoginFields() {
        return validateEmailValue(this.emailField.value, this.errorMessageView)
            && validatePasswordValue(this.passwordField.value, this.errorMessageView);
    }
}