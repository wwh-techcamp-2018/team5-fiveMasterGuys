import { validateEmailValue, validatePasswordValue, validateNameValue } from './utils.js';

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
        this.clickHandlerEvent();
    }

    clickHandlerEvent() {
        this.signupBtn.addEventListener('click', (e) => {
            const target = e.target;

            if (!this.checkSignupForm()) {
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

    checkSignupForm() {
        return validateEmailValue(this.emailField.value, this.errorBox)
                && validatePasswordValue(this.passwordField.value, this.errorBox)
                && validatePasswordValue(this.passwordCheckField.value, this.errorBox)
                && validateNameValue(this.nameField.value, this.errorBox);
    }
}
