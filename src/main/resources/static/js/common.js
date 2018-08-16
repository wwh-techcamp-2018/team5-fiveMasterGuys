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