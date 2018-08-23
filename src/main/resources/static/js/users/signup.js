import { validateEmailValue, validatePasswordValue, validateNotEmptyValue } from './utils.js';

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
        this.errorBox = $('#error-msg-box');
        this.box = $('.box');

        this.registerEvents();
    }

    registerEvents() {
        this.clickHandlerEvent();
    }

    clickHandlerEvent() {
        this.signupBtn.addEventListener('click', (e) => {
            const target = e.target;

            if (!this.isValidaSignupForm()) {
                return;
            }

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
                    this.errorBox.innerText = '입력 값을 다시 확인해주세요.';
                },
                onError: () => {
                    this.errorBox.innerText = '요청 중 문제가 발생하였습네다. 다시 요청해주세요.';
                }
            })
        })
    }

    isValidaSignupForm() {
        return validateEmailValue(this.errorBox, this.emailField.value)
                && validatePasswordValue(this.errorBox, this.passwordField.value)
                && validatePasswordValue(this.errorBox, this.passwordCheckField.value)
                && validateNotEmptyValue(this.errorBox, this.nameField.value);
    }
}
