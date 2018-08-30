class CategoryManager {
    constructor(errorMessageView) {
        this.categoryList = $('#category-box');

        this.registerEvents();
    }

    registerEvents() {
        this.categoryList.addEventListener('click', (e) => {
            this.handleClickEvent(e);
        });
    }

    handleClickEvent({target}) {
        if (target.classList.contains('category')) {
            const categoryId = target.getAttribute('data-category-id');
            const baseUrl = target.getAttribute('data-url');
            location.href = this.getLocationUrl(baseUrl, categoryId);
            return;
        }
    }

    getLocationUrl(baseUrl, categoryId) {
        if (baseUrl.match(/\/search\?(.+)/)) {
            return baseUrl + `category=${categoryId}`;
        }
        return `/search?category=${categoryId}`;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const errorMessageView = new ErrorMessageView($('.error-msg-box'));
    new CategoryManager();
});
