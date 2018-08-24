import { validateEmailValue, validatePasswordValue } from './utils.js';

document.addEventListener('DOMContentLoaded', () => {
    new Login();
})

class Login {
    constructor() {
        this.emailField = $('#email-field');
        this.passwordField = $('#password-field');
        this.loginBtn = $('#login-btn');
        this.errorBox = $('#error-msg-box');

        this.registerEvents();
    }

    registerEvents() {
        this.loginBtn.addEventListener('click', () => {

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
                }),
                onSuccess: () => {
                    location.href = document.referrer;
                },
                onFailed: () => {
                    this.errorBox.innerText = '아이디 또는 비밀번호가 일치하지 않습니다.';
                },
                onError: () => {
                    this.errorBox.innerText = '요청중 문제가 발생하였습니다. 재접속 후 시도해주세요.';
                }
            })
        })
    }

    checkLoginFields() {
        return validateEmailValue(this.emailField.value, this.errorBox)
            && validatePasswordValue(this.passwordField.value, this.errorBox);
    }
}