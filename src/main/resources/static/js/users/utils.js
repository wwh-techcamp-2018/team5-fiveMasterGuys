function validateEmailValue(value, errorBox) {
    if(!validate('^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$', value)) {
        errorBox.showMessage('이메일 형식을 확인하세요', false);
        return false;
    }
    return true;
}

function validatePasswordValue(value, errorBox) {
    if(!validate('^.*(?=^.{8,16}$)(?=.*[0-9])(?=.*[a-zA-Z]).*$', value)) {
        errorBox.showMessage('비밀번호 형식을 확인하세요 \n (숫자와 문자 조합 8 ~ 16자리)', false);
        return false;
    }
    return true;
}

function validateNameValue(value, errorBox) {
    if (value.length < 2 || value.length > 40 ) {
        errorBox.showMessage('이름은 2 ~ 40 글자 사이입니다.', false);
        return false;
    }
    return true;
}

export { validateEmailValue, validatePasswordValue, validateNameValue };
