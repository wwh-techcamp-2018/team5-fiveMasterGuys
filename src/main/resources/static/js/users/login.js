document.addEventListener('DOMContentLoaded', () => {
    new Login();
})

class Login {
    constructor() {
        this.emailField = $('#email-field');
        this.passwordField = $('#password-field');
        this.loginBtn = $('#login-btn');

        this.registerEvents();
    }

    registerEvents() {
        this.loginBtn.addEventListener('click', () => {
            
            if (!this.validateLoginFields()) {
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
                    alert('아이디 또는 비밀번호가 일치하지 않습니다.')
                },
                onError: () => {
                    alert('요청중 문제가 발생하였습니다. 재접속 후 시도해주세요.')
                }
            })
        })
    }

    validateLoginFields() {
        return this.validateEmailValue() && this.validatePasswordValue();
    }
    
    validateEmailValue() {
        if(!validate('^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$', this.emailField.value)) {
            alert('이메일 형식을 확인하세요');
            return false;
        }
        return true;
    }

    validatePasswordValue() {
        if(!validate('^.*(?=^.{8,16}$)(?=.*[0-9])(?=.*[a-zA-Z]).*$', this.passwordField.value)) {
            alert('비밀번호 형식을 확인하세요');
            return false;
        }
        return true;
    }
}