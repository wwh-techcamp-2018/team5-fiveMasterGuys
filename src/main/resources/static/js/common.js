function $(selector) {
    return document.querySelector(selector);
}

function $All(selector) {
    return document.querySelectorAll(selector);
}

function fetchManager({url, method, body, headers, onSuccess, onFailed, onError}) {
    fetch(url, {method, body, headers, credentials: "same-origin"})
        .then((response) => {
            let callback;
            const status = response.status;
            if (status >= 400) {
                callback = onFailed;
            } else callback = onSuccess;

            console.log(response);

            response.json().then(json => callback({status, json})).catch(() => callback({status}))
        }).catch(onError)
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
        location.href = "/users/login"
    }
}

class ImageUploader {
    constructor() {

    }

    upload(file) {
        const data = new FormData();
        data.append('file', file);
        return new Promise((resolve, reject) => {
            fetchManager({
                url: '/images',
                method: 'POST',
                body: data,
                onSuccess: ({json}) => {
                    resolve(json.data);
                },
                onFailed: () => {
                    reject();
                },
                onError: () => {
                    reject();
                }
            }); 
        })
    }
}

function autoGrow(element) {
    element.style.height = "5px";
    element.style.height = (element.scrollHeight)+"px";
}