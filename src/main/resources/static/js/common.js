function $(selector) {
    return document.querySelector(selector);
}

function $All(selector) {
    return document.querySelectorAll(selector);
}


function fetchManager({url, method, body, headers}) {
    return new Promise((resolve, reject) => {
        fetch(url, {method, body, headers, credentials: "same-origin"})
            .then((response) => {
                const status = response.status;
                const callback = (json) => {
                    if (status >= 400) {
                        const errors = json ? json.errors : undefined;
                        reject({status, errors});
                        return;
                    }

                    const data = json ? json.data : undefined;
                    resolve({status, data});
                }

                response.json().then(callback).catch(callback);
            }).catch(() => reject());
    });
}


function validate(regex, value) {
    return RegExp(regex).test(value);
}

function toggleHidden(target) {
    target.classList.toggle('hidden');
}

function removeElement(element) {
    element.parentNode.removeChild(element);
}

function getCookie(cookieName) {
    const name = cookieName + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodedCookie.split(';');

    for (let cookie of cookieArray) {
        cookie = cookie.trim();

        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}


function checkLoginOrRedirect() {
    if (getCookie('isLoggedIn') !== 'true') {
        window.location.href = "/users/login"
    }
}

class ImageUploader {
    constructor() {

    }

    upload(file) {
        const data = new FormData();
        data.append('file', file);
        return fetchManager({
                url: '/images',
                method: 'POST',
                body: data,
        });
    }
}

function autoGrow(element) {
    element.style.height = "5px";
    element.style.height = (element.scrollHeight)+"px";
}

class ErrorMessageView {
    constructor(errorMessageBox) {
        this.errorMessageBox = errorMessageBox;
        this.timeout = 1750;
    }

    showMessage(msg, willHide=true) {
        this.errorMessageBox.innerText = msg;
        this.errorMessageBox.style.zIndex = "100";
        this.errorMessageBox.classList.remove('invisible');
        if (willHide) {
            this.reserveHide();
        }
    }

    reserveHide() {
        if (this.reserved) {
            clearTimeout(this.reserved);
        }
        this.reserved = setTimeout(() => this.hide(), this.timeout);
    }

    hide() {
        this.errorMessageBox.classList.add('invisible');
        this.errorMessageBox.style.zIndex = "-1";
    }
}