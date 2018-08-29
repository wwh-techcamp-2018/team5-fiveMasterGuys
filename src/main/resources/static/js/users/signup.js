import { validateEmailValue, validatePasswordValue, validateNameValue } from './utils.js';

document.addEventListener('DOMContentLoaded', () => {
    new Signup(new ErrorMessageView($('#error-msg-box')));
})

class Signup {
    constructor(errorMessageView) {
        this.emailField = $('#email-field');
        this.passwordField = $('#password-field');
        this.passwordCheckField = $('#password-check-field');
        this.nameField = $('#name-field');
        this.signupBtn = $('#signup-btn');
        this.errorMessageView = errorMessageView;

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
                })
            }).then(() => {
                location.href = document.referrer;
            }).catch(({errors}) => {
                if (errors) {
                    this.errorMessageView.showMessage(errors[0].message, false);
                    return;
                }
                this.errorMessageView.showMessage('요청 중 문제가 발생하였습네다. 다시 요청해주세요.', false);
            });
        })
    }

    checkSignupForm() {
        return validateEmailValue(this.emailField.value, this.errorMessageView)
                && validatePasswordValue(this.passwordField.value, this.errorMessageView)
                && validatePasswordValue(this.passwordCheckField.value, this.errorMessageView)
                && validateNameValue(this.nameField.value, this.errorMessageView);
    }
}
