document.addEventListener('DOMContentLoaded', () => {
    new SearchNav();

});

class SearchNav {
    constructor() {
        this.searchInput = $('#input-search');
        this.searchButton = $('#btn-search');
        this.categoryOptions = $('#select-search');

        this.initializeCategories();
        this.registerEvents();
    }

    initializeCategories() {
        fetchManager({
            url: '/api/categories',
            method: 'GET',
            onSuccess: ({
                json
            }) => {
                json.data.forEach(category => {
                    const option = document.createElement('option');
                    option.text = category.title;
                    option.setAttribute('data-category-id', category.id);
                    this.categoryOptions.add(option);
                })
            },
            onFailed: () => {},
            onError: () => {}
        });
    }

    registerEvents() {
        this.searchInput.addEventListener('keyup', ({ keyCode }) => {
            if (keyCode === 13) {
                this.search();
                return;
            }
        })
        this.searchButton.addEventListener('click', this.search.bind(this))
    }

    search() {
        const keyword = this.searchInput.value;
        const categoryId = this.categoryOptions.options[this.categoryOptions.selectedIndex].getAttribute('data-category-id');

        location.href = `/search?q=${keyword}${categoryId !== null ? `&category=${categoryId}` : ''}`;
    }
}