class StepManager {
    constructor() {
        this.recipe = $('.recipe');  
        this.delegateMapping = {
            'btn-step-add': this.showAddStepForm,
            'btn-plus': this.appendStepItem,
            'btn-minus': this.removeStepItem,
            'btn-confirm': this.handleConfirmButtonClick,
            'btn-cancel': this.handleCancelButtonClick
        }

        this.registerEvents();    
    }

    registerEvents() {
        this.recipe.addEventListener('click', (e) => { this.handleClickEvent(e) });
        this.recipe.addEventListener('keyup', (e) => { this.handleKeyUpEvent(e) });
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

    handleClickEvent({target}) {
        const handlerName = [...target.classList].find((cls) => this.delegateMapping.hasOwnProperty(cls));
        if (!handlerName) {
            return;
        }
        this.delegateMapping[handlerName].call(this, target);
    }

    handleConfirmButtonClick(target) {
        const stepContents = target.closest('.box');
        const requestBody = this.makeRequestBody(stepContents);
        this.requestStepAddition(requestBody)
            .then((data) => {
                this.addStep(data, stepContents);
                this.closeAddForm(stepContents);
            }).catch((status) => {
                if (typeof status  === 'undefined') {
                    alert('네트워크 오류 발생함');
                    return;
                }
                if (status === 401) {
                    location.href = '/users/login';
                    return;
                }
                alert('똑바로 하그라');
            })
    }

    handleCancelButtonClick(target) {
        const stepContents = target.closest('.box');
        this.closeAddForm(stepContents);
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

    addStep(data, stepContents) {
        stepContents.insertAdjacentHTML('afterend', this.templateStep(data));
    }

    closeAddForm(stepContents) {
        toggleHidden(this.findStepAddBtn(stepContents.getAttribute('data-target-step-id')));
        removeElement(stepContents);
    }

    findStepAddBtn(stepId) {
        return $(`button[data-step-id="${stepId}"]`)
    }

    makeRequestBody(stepContents) {
        const stepId = stepContents.getAttribute('data-target-step-id') !== "null" ? stepContents.getAttribute('data-target-step-id') : null;
        return {
            name: stepContents.querySelector('.subtitle').value,
            content: this.getStepItemTexts(stepContents),
            previousStepId: stepId
        }
    }

    getStepItemTexts(stepContents) {
        return [...stepContents.querySelectorAll('.step-item-contents')].map(contentElement => contentElement.innerText);
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

    templateStepForm(targetStepId) {
        return `
        <article class="box" data-target-step-id=${targetStepId}>
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