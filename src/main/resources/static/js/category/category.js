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
            location.href = `/search?category=${categoryId}`;
            return;
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const errorMessageView = new ErrorMessageView($('.error-msg-box'));
    new CategoryManager();
});
