class RecipeManager {
    constructor() {
        this.recipe = $('.recipe');
        this.titleInput = $('.recipe-title-input');
        this.registerEvents();
    }

    registerEvents() {
        this.recipe.addEventListener('click', (e) => {
            this.handleClickEvent(e);
        });

        this.titleInput.addEventListener('keydown', (e) => {
            if (e.keyCode === 13) {
                e.preventDefault();
                return;
            }
            autoGrow(e.target);
        });
        autoGrow(this.titleInput);
    }

    handleClickEvent({target}) {
        if (target.classList.contains('btn-recipe-image-modify')) {
            this.requestRecipeModification().then(() => { location.reload() });
            return;
        }
    }

    requestRecipeModification() {
        const image = this.findImageUrl();
        return new Promise((resolve, reject) => {
            fetchManager({
                url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}`,
                headers: {"Content-Type": "application/json"},
                method: 'PUT',
                body: JSON.stringify({
                    imgUrl: image,
                    name: this.titleInput.value
                }),
                onSuccess: ({json}) => {
                    resolve(json.data);
                },
                onFailed: ({status}) => {
                    reject(status);
                },
                onError: () => {
                    reject();
                }
            })
        });
    }

    findImageUrl() {
        const label = $(`label[for="recipe-img-upload"]`);
        const backgroundImageUrl = label.style.backgroundImage;
        return backgroundImageUrl === "" ? null : backgroundImageUrl.match(/url\("(.*)"\)$/)[1];
    }

    handleChangeEvent({target}) {
        this.imageUploader.upload(this.getFile(target))
            .then((data) => {
                const label = $(`label[for=${target.id}]`);
                label.style.backgroundImage = `url(${data})`;
                label.innerText = '';
            })
            .catch();
    }
}


document.addEventListener('DOMContentLoaded', () => {
    new RecipeManager();
});