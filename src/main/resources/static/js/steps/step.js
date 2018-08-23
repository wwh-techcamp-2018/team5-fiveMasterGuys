class StepManager {
    constructor() {
        this.recipe = $('.recipe');

        this.delegateMapping = {
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
            this.handleClickEvent(e)
        });
        this.recipe.addEventListener('keyup', (e) => {
            this.handleKeyUpEvent(e)
        });
    }

    handleKeyUpEvent(e) {
        if (e.target.classList.contains('step-item-input') && e.keyCode === 13) {
            this.appendStepItem(e.target);
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
                const handlerName = [...dom.classList].find((cls) => this.delegateMapping.hasOwnProperty(cls));
                if (handlerName) {
                    return (this.delegateMapping[handlerName]).call(this, dom);
                }
            }
        });
    }

    handleConfirmButtonClick(target) {
        const stepForm = target.closest('.box');
        const requestBody = this.makeRequestBody(stepForm);
        this.requestStepAddition(requestBody)
            .then((data) => {
                this.addStep(stepForm, data);
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
            <div class="title is-info">추가 제안</div>
        </div>
               
        `
    }

    addStep(stepForm, data) {
        if (data.offerType === 'APPEND') {
            const targetStepId = stepForm.getAttribute('data-step-id');
            this.createStepOfferContainer(targetStepId, data);
            this.createStepOffer(targetStepId, data);
        } else {
            stepForm.insertAdjacentHTML('afterend', this.templateStep(data));

        }
    }

    createStepOffer(targetStepId, data) {
        $(`.step-offers[data-step-id="${targetStepId}"]`).insertAdjacentHTML('beforeend', this.templateStepOffer(data))
    }

    createStepOfferContainer(targetStepId, data) {
        let stepOfferContainer = $(`.step-offers[data-step-id="${targetStepId}"]`);

        if (!stepOfferContainer) {
            const step = (targetStepId === 'null')
                ? $(`button[data-step-id="null"]`).previousElementSibling
                : $(`article[data-step-id="${targetStepId}"]`);

            step.insertAdjacentHTML('afterend', this.templateStepOfferContainer(data));
        }
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
            previousStepId: stepId
        }
    }

    getStepItemTexts(steps) {
        return [...steps.querySelectorAll('.step-item-contents')].map(contentElement => contentElement.innerText);
    }

    templateStep(data) {
        return `
        <h1 class="step-title is-size-3 has-text-weight-bold">Step ${data.sequence}</h1>
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
            </div>
            <div>
                <div class="speech-bubble-triangle"></div>
                <div></div>
                <span>${data.writer.name}</span>
            </div>
        </article>
        <button class="btn-step-add" data-step-id=${data.id}></button>
        `;
    }

    templateStepOffer(data) {
        return `
            <a class="hero is-info step-offer-title-bar">
                <h1 class="title">Step 추가 제안 : ${data.name}
                    <span class="step-offer-open is-pulled-right">-</span>
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
                <div style="background-color: yellow; min-height:400px;" class="img-upload column is-one-third"></div>
                <div class="column">
                    <input class="input subtitle" type="text" placeholder="스텝 제목">
                    <div>
                        <ol class="step-contents">
                            ${this.templateStepContentInput()}
                        </ol>

                    </div>
                </div>
            </div>
            <div class="buttons is-right">
                <button class="btn-confirm button is-primary">추가</button>
                <button class="btn-cancel button is-danger">취소</button>
            </div>
        </article>
        `;
    }

    templateStepContentListItem(content) {
        return `<li class="step-item"><div class="columns is-vcentered"><div class="column is-11 step-item-contents">${content}</div><button class="column btn-minus button is-small">-</button></div></li>`;
    }

    templateStepContentInput() {
        return `<li class="step-item"><div class="columns is-vcentered"><input type="text" class="step-item-input input column is-11" placeholder="hello"><button class="btn-plus column button is-small">+</button></div></li>`
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new StepManager();
});