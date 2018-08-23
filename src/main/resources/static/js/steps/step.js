class StepManager {
    constructor(imageUploader) {
        this.recipe = $('.recipe');
        this.imageUploader = imageUploader;
        this.clickEventDelegationMapping = {
            'btn-step-add': this.showAddStepForm,
            'btn-plus': this.appendStepItem,
            'btn-minus': this.removeStepItem,
            'btn-confirm': this.handleConfirmButtonClick,
            'btn-cancel': this.handleCancelButtonClick,
            'step-offer-title-bar': this.toggleStepOfferContent,
        };
        this.registerEvents();
    }

    registerEvents() {
        this.recipe.addEventListener('click', (e) => {
            this.handleClickEvent(e);
        });
        this.recipe.addEventListener('keyup', (e) => {
            this.handleKeyUpEvent(e);
        });
        this.recipe.addEventListener('change', (e) => {
            this.handleChangeEvent(e);
        })
    }

    handleChangeEvent({target}) {
        if (target.classList.contains('img-upload')) {
            this.imageUploader.upload(this.getFile(target))
            .then((data) => {
                const label = $(`label[for=${target.id}]`);
                label.style.backgroundImage = `url(${data})`;
                label.innerText = '';
            })
            .catch();
            return;
        }
    }

    getFile(target) {
        return target.files[0];
    }

    handleKeyUpEvent(e) {
        if (e.target.classList.contains('step-item-input') && e.keyCode === 13) {
            if($('.step-item-input').value !== ""){
                this.appendStepItem(e.target);
                $('.step-item-input').focus();
            }
            return;
        }
    }

    showAddStepForm(target) {
        target.insertAdjacentHTML('afterend', this.templateStepForm(target.getAttribute('data-step-id')));
        toggleHidden(target);
    }

    appendStepItem(target) {
        const stepItem = target.closest('.step-item');
        const value = stepItem.querySelector('input').value;
        const stepContainer = target.closest('ol.step-contents');
        removeElement(stepItem);
        stepContainer.insertAdjacentHTML('beforeend', this.templateStepContentListItem(value));
        stepContainer.insertAdjacentHTML('beforeend', this.templateStepContentInput());
    }

    removeStepItem(target) {
        removeElement(target.closest('.step-item'));
    }

    handleClickEvent(e) {
        let path = e.path || (event.composedPath && event.composedPath());
        path.forEach(dom => {
            if (dom.classList) {
                const handlerName = [...dom.classList].find((cls) => this.clickEventDelegationMapping.hasOwnProperty(cls));
                if (handlerName) {
                    return (this.clickEventDelegationMapping[handlerName]).call(this, dom);
                }
            }
        });
    }

    handleConfirmButtonClick(target) {
        const stepForm = target.closest('.box');
        const requestBody = this.makeRequestBody(stepForm);
        this.requestStepAddition(requestBody)
            .then((data) => {
                this.renderStep(stepForm, data);
                this.closeAddForm(stepForm);
            }).catch((status) => {
            if (typeof status === 'undefined') {
                alert('네트워크 오류 발생함');
                return;
            }
            if (status === 401) {
                location.href = '/users/login';
                return;
            }
            console.error(status);
        })
    }

    handleCancelButtonClick(target) {
        const stepForm = target.closest('.box');
        this.closeAddForm(stepForm);
    }

    toggleStepOfferContent(target) {
        toggleHidden(target.nextElementSibling);
    }

    requestStepAddition(requestBody) {
        return new Promise((resolve, reject) => {
            fetchManager({
                url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}/steps`,
                headers: {"Content-Type": "application/json"},
                method: 'POST',
                body: JSON.stringify(requestBody),
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

    templateStepOfferContainer(data) {
        const stepId = data.target && data.target.id;
        return`
        <div class="step-offers" data-step-id="${stepId}">
            <div class="step-offer-title is-size-2"><i class="fas fa-bong"></i> Step Offers</div>
        </div>
               
        `
    }

    renderStep(stepForm, data) {
        if (data.offerType === 'APPEND') {
            const targetStepId = stepForm.getAttribute('data-step-id');
            this.createStepOfferContainer(targetStepId, data);
            this.createStepOffer(targetStepId, data);
        } else {
            this.addStepWithOwner(stepForm, data);
        }
    }

    addStepWithOwner(stepForm, data) {
        stepForm.closest('.step-container').insertAdjacentHTML('afterend', this.templateStep(data));
        $All('h1.step-title').forEach((e, i) => e.innerHTML = `<i class="fas fa-utensils fa-2x"></i>Step ${i + 1}`);
    }

    createStepOffer(targetStepId, data) {
        $(`.step-offers[data-step-id="${targetStepId}"]`).insertAdjacentHTML('beforeend', this.templateStepOffer(data))
    }

    createStepOfferContainer(targetStepId, data) {
        let stepOfferContainer = $(`.step-offers[data-step-id="${targetStepId}"]`);
        if (!stepOfferContainer) {
            (targetStepId === 'null') ? this.renderNullStepOfferContainer(data) : this.renderStepOfferContainer(targetStepId, data);
        }
    }

    renderStepOfferContainer(targetStepId, data) {
        const el = $(`article[data-step-id="${targetStepId}"]`);
        el.insertAdjacentHTML('afterend', this.templateStepOfferContainer(data));
    }

    renderNullStepOfferContainer(data) {
        const el = $(`button[data-step-id="null"]`).parentElement;
        el.insertAdjacentHTML('afterbegin', this.templateStepOfferContainer(data));
    }

    closeAddForm(stepForm) {
        toggleHidden(this.findStepAddBtn(stepForm.getAttribute('data-step-id')));
        removeElement(stepForm);
    }

    findStepAddBtn(stepId) {
        return $(`button[data-step-id="${stepId}"]`)
    }

    makeRequestBody(stepForm) {
        const stepId = stepForm.getAttribute('data-step-id') !== "null" ? stepForm.getAttribute('data-step-id') : null;
        return {
            name: stepForm.querySelector('.subtitle').value,
            content: this.getStepItemTexts(stepForm),
            previousStepId: stepId,
            imgUrl: this.findImageUrl(stepId)
        }
    }

    findImageUrl(stepId) {
        const label = $(`label[for=img-upload-${stepId}]`);
        const backgroundImageUrl = label.style.backgroundImage;
        return backgroundImageUrl === "" ? null : backgroundImageUrl.match(/url\("(.*)"\)$/)[1];
    }

    getStepItemTexts(steps) {
        return [...steps.querySelectorAll('.step-item-contents')].map(contentElement => contentElement.innerText);
    }

    templateStep(data) {
        return `
        <div class="step-container">
            <h1 class="step-title is-size-2 has-text-weight-bold">
            <i class="fas fa-utensils fa-2x"></i>
                Step ${data.sequence}
            </h1>
            <article class="box">
                <div class="columns">
                    <img src="${data.imgUrl}" alt="${data.name}" class="column is-one-third">
                    <div class="column">
                        <div class="subtitle">${data.name}</div>
                        <div>
                            <ol class="step-contents">
                                ${data.content.map((e) => (`<li>${e}</li>`)).join('\n')}
                            </ol>
                        </div>
                    </div>
                    <div class="column is-one-fifth ingredient"></div>
                </div>
                <div>
                    <div class="speech-bubble-triangle"></div>
                    <div></div>
                    <span>${data.writer.name}</span>
                </div>
            </article>
            <button class="btn-step-add" data-step-id="${data.id}">
                <i class="fas fa-plus-circle fa-4x"> Step 추가 제안하기</i>
            </button>
        </div>
        `;
    }

    templateStepOffer(data) {
        return `
            <a class="hero is-info step-offer-title-bar">
                <h1 class="title">${data.name}
                    <span class="step-offer-open is-pulled-right"><i class="fas fa-angle-down"></i></span></span>
                </h1>
            </a>
            <article class="box step-offer-content hidden">
                <div class="columns">
                    <img src="${data.imgUrl}" alt="${data.name}" class="column is-one-third">
                    <div class="column"> 
                        <div class="subtitle">${data.name}</div>
                        <div>
                            <ol class="step-contents">
                                ${data.content.map((e) => (`<li>${e}</li>`)).join('\n')}
                            </ol>
                        </div>
                    </div>
                    <div class="column is-one-fifth ingredient"></div>
                </div>
                <div>
                    <div class="speech-bubble-triangle"></div>
                    <div></div>
                    <span>${data.writer.name}</span>
                </div>
            </article>
        `;
    }

    templateStepForm(targetStepId) {
        return `
        <article class="box" data-step-id=${targetStepId}>
            <div class="columns">
                <div class="column is-one-third">
                    <input type="file" accept="image/*" style="min-height:400px;" name="img-upload" id="img-upload-${targetStepId}" class="img-upload"></input>
                    <label for="img-upload-${targetStepId}" class="has-text-centered">이미지를 업로드하려면 클릭하세요</label>
                </div>
                <div class="column">
                    <input class="input subtitle-input" type="text" placeholder="스텝 제목">
                    <div class="step-contents-container">
                        <ol class="step-contents">
                            ${this.templateStepContentInput()}
                        </ol>
                    </div>
                </div>
                <div class="column is-one-fifth ingredient"></div>
            </div>
            <div class="buttons is-right">
                <button class="btn-confirm button is-primary">추가</button>
                <button class="btn-cancel button is-danger">취소</button>
            </div>
        </article>
        `;
    }

    templateStepContentListItem(content) {
        return `<li class="step-item"><div class="columns is-vcentered step-item-container"><div class="column is-11 step-item-contents">${content}</div><button class="btn-minus is-1"><i class="fas fa-minus fa-3x"></i></button></div></li>`;
    }

    templateStepContentInput() {
        return `<li class="step-item"><div class="columns is-vcentered step-item-input-container"><input type="text" class="column is-11 is-large step-item-input input" placeholder="hello"><button class="btn-plus is-2"><i class="far fa-plus-square fa-3x"></i></button></div></li>`
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new StepManager(new ImageUploader());
});