class CategoryManager {
    constructor(errorMessageView) {
        this.categoryList = $('#category-box');

        this.showCategory();
        this.registerEvents();
    }

    showCategory() {
        fetchManager({
            url: '/api/categories',
            method: 'GET',
        }).then(({data}) => {
            data.forEach((e) => { this.addCategory(e); });
        }).catch(({status, errors}) => {
            this.handleAjaxError(status, errors);
        });
    }

    registerEvents() {
        this.categoryList.addEventListener('click', (e) => {
            this.handleClickEvent(e);
        });
    }

    handleClickEvent({target}) {
        if (target.classList.contains('category')) {
            const categoryId = target.getAttribute('data-category-id');
            location.href = `/search?category=${categoryId}`;
            return;
        }
    }

    addCategory(data) {
        this.categoryList.insertAdjacentHTML('beforeend', this.categoryTemplate(data));
    }

    categoryTemplate(data) {
        return `<p class="category has-text-centered column child-vertical-align" data-category-id=${data.id}>${data.title}</p>`;
    }

    handleAjaxError(status, errors) {
        if (typeof status === 'undefined') {
            this.errorMessageView.showMessage('네트워크 오류 발생함');
            return;
        }

        if (status === 401) {
            location.href = '/users/login';
            return;
        }

        if (errors) {
            this.errorMessageView.showMessage(errors[0].message);
            return;
        }
    }

}

document.addEventListener('DOMContentLoaded', () => {
    const errorMessageView = new ErrorMessageView($('.error-msg-box'));
    new CategoryManager();
});
