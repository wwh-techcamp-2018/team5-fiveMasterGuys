import {Templates} from './templates.js';

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
            if (e.target.value !== "") {
                let stepId = e.target.closest('.box').getAttribute('data-step-id');
                this.appendStepItem(e.target);
                $(`.box[data-step-id="${stepId}"] .step-item-input`).focus();
            }
            return;
        }
    }

    showAddStepForm(target) {
        checkLoginOrRedirect();
        target.insertAdjacentHTML('afterend', Templates.templateStepForm(target.getAttribute('data-step-id')));
        toggleHidden(target);
    }

    appendStepItem(target) {
        const stepItem = target.closest('.step-item');
        const value = stepItem.querySelector('input').value;
        const stepContainer = target.closest('ol.step-contents');
        removeElement(stepItem);
        stepContainer.insertAdjacentHTML('beforeend', Templates.templateStepContentListItem(value));
        stepContainer.insertAdjacentHTML('beforeend', Templates.templateStepContentInput());
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

    addStep(stepForm, data) {
        if (data.offerType === 'APPEND') {
            const targetStepId = stepForm.getAttribute('data-step-id');
            this.createStepOfferContainer(targetStepId, data);
            this.createStepOffer(targetStepId, data);
        } else {
            this.addStepWithOwner(stepForm, data);
        }
    }

    addStepWithOwner(stepForm, data) {
        stepForm.closest('.step-container').insertAdjacentHTML('afterend', Templates.templateStep(data));
        $All('h1.step-title').forEach((e, i) => e.innerHTML = `<i class="fas fa-utensils fa-2x"></i>Step ${i + 1}`);
    }

    createStepOffer(targetStepId, data) {
        $(`.step-offers[data-step-id="${targetStepId}"]`).insertAdjacentHTML('beforeend', Templates.templateStepOffer(data))
    }

    createStepOfferContainer(targetStepId, data) {
        let stepOfferContainer = $(`.step-offers[data-step-id="${targetStepId}"]`);
        if (!stepOfferContainer) {
            (targetStepId === 'null') ? this.renderNullStepOfferContainer(data) : this.renderStepOfferContainer(targetStepId, data);
        }
    }

    renderStepOfferContainer(targetStepId, data) {
        const el = $(`article[data-step-id="${targetStepId}"]`);
        el.insertAdjacentHTML('afterend', Templates.templateStepOfferContainer(data));
    }

    renderNullStepOfferContainer(data) {
        const el = $(`button[data-step-id="null"]`).parentElement;
        el.insertAdjacentHTML('afterbegin', Templates.templateStepOfferContainer(data));
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
            name: stepForm.querySelector('.subtitle-input').value,
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

}

document.addEventListener('DOMContentLoaded', () => {
    new StepManager(new ImageUploader());
});