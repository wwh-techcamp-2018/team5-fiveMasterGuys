document.addEventListener('DOMContentLoaded', () => {
    new SearchNav();

});

class SearchNav {
    constructor() {
        this.searchInput = $('#input-search');
        this.searchButton = $('#btn-search');

        this.registerEvents();
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

        location.href = `/search?q=${keyword}`;
    }
}