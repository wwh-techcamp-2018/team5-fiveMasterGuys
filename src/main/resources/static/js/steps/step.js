import {Templates} from './templates.js';

class StepManager {
    constructor(imageUploader) {
        this.recipe = $('.recipe');
        this.imageUploader = imageUploader;
        this.clickEventDelegationMapping = {
            'btn-step-add': this.showAddStepForm,
            'btn-plus': this.appendStepItem,
            'btn-minus': this.removeStepItem,
            'btn-add-confirm': this.handleAddFormConfirmButtonClick,
            'btn-add-cancel': this.handleAddFormCancelButtonClick,
            'btn-modify-confirm': this.handleModifyFormConfirmButtonClick,
            'btn-modify-cancel': this.handleModifyFormCancelButtonClick,
            'btn-modify-offer-confirm': this.handleOfferModifyFormConfirmButtonClick,
            'btn-modify-offer-cancel': this.handleModifyFormCancelButtonClick,
            'btn-step-modify': this.showModifyStepForm,
            'btn-approve': this.handleApproveButtonClick,
            'step-offer-title-bar': this.toggleStepOfferContent,
            'btn-recipe-complete': this.completeRecipe,
            'offer': this.showModifyOfferStepForm,
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
        });
        this.recipe.addEventListener('mouseover', (e) => {
            this.handleMouseOverEvent(e);
        });
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
        target.insertAdjacentHTML('afterend', Templates.templateStepForm(target.getAttribute('data-step-id'), 'add'));
        toggleHidden(target);
    }

    showModifyStepForm(target) {
        checkLoginOrRedirect();
        let stepBox = target.closest('.box');
        const stepBoxInner = stepBox.querySelector('.columns:not(.hidden)');
        stepBox.insertAdjacentHTML('beforebegin', Templates.templateStepForm(stepBox.getAttribute('data-step-id'), 'modify'));

        const stepForm = stepBox.previousElementSibling;
        this.copyValuesToForm(stepBoxInner, stepForm);
        toggleHidden(stepBox);
    }

    showModifyOfferStepForm(target) {
        checkLoginOrRedirect();
        let stepBox = target.closest('.box');
        const stepBoxInner = stepBox.querySelector('.columns:not(.hidden)');
        stepBox.insertAdjacentHTML('beforebegin', Templates.templateStepForm(stepBox.getAttribute('data-step-id'), 'modify-offer'));

        const stepForm = stepBox.previousElementSibling;
        this.copyValuesToForm(stepBoxInner, stepForm);
        toggleHidden(stepBox);
    }

    copyValuesToForm(stepBox, stepForm) {
        const stepItemList = stepForm.querySelector('ol.step-contents');
        const stepItems = this.getStepItemTexts(stepBox.querySelectorAll('ol.step-contents li p'));
        stepForm.querySelector('.subtitle-input').value = stepBox.querySelector('.subtitle').innerText;

        removeElement(stepItemList.firstElementChild);
        stepItems.forEach((item) => {
            stepItemList.insertAdjacentHTML('beforeend', Templates.templateStepContentListItem(item));
        })
        stepItemList.insertAdjacentHTML('beforeend', Templates.templateStepContentInput());
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

    removeSelectedContributor(boxId) {
        let selected = $(`.box[data-step-id="${boxId}"] .contributor-selected`);
        (selected && selected.classList.remove('contributor-selected'));
    }

    handleMouseOverEvent({target}) {
        if (target.classList.contains('contributor')) {

            let boxId = target.closest('.box').getAttribute('data-step-id');
            this.removeSelectedContributor(boxId);
            target.classList.toggle('contributor-selected');

            if (target.getAttribute("data-step-id") === null) {
                return;
            }

            let exposedBoxesInner = $All(`.box[data-step-id="${boxId}"] > .columns`);
            exposedBoxesInner.forEach(t => t.classList.add('hidden'));

            let stepId = target.getAttribute('data-step-id');
            let overedBoxInner = $(`.columns[data-step-id="${stepId}"]`);
            overedBoxInner.classList.toggle(`hidden`);
        }
    }

    handleAddFormConfirmButtonClick(target) {
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

    handleAddFormCancelButtonClick(target) {
        const stepForm = target.closest('.box');
        this.closeAddForm(stepForm);
    }

    handleModifyFormConfirmButtonClick(target) {
        const stepForm = target.closest('.box');
        const requestBody = this.makeRequestBody(stepForm);
        this.requestStepModification(requestBody)
            .then((data) => {
                const stepBox = stepForm.nextElementSibling;
                stepBox.insertAdjacentHTML('beforebegin', Templates.templateStepBox(data));
                removeElement(stepBox);
                removeElement(stepForm);
            })
            .catch((status) => {
                if (status === 401) {
                    location.href = '/users/login';
                }
            });
    }

    handleOfferModifyFormConfirmButtonClick(target) {
        const stepForm = target.closest('.box');
        const requestBody = this.makeRequestBody(stepForm);
        this.requestStepModification(requestBody)
            .then((data) => {
                this.removeSelectedContributor(requestBody.targetStepId);
                const contributorsBox = $(`.box[data-step-id="${requestBody.targetStepId}"] .contributors`);
                contributorsBox.insertAdjacentHTML('beforeend', Templates.templateStepContributor(data));


                const stepBox = stepForm.nextElementSibling;
                const target = stepBox.querySelector('.shadow-wrapper');
                this.renderStepInnerBefore(target, data);
                toggleHidden(stepBox);
                toggleHidden(stepBox.querySelector('.columns:not(.hidden)'));
                removeElement(stepForm);
            })
            .catch((status) => {
                if (status === 401) {
                    location.href = '/users/login';
                }
            });
    }

    handleModifyFormCancelButtonClick(target) {
        const stepForm = target.closest('.box');
        const stepBox = stepForm.nextElementSibling;
        toggleHidden(stepBox);
        removeElement(stepForm);
    }

    handleApproveButtonClick(target) {
            const stepOffer = target.closest('.step-offer');
            this.requestStepApproval(stepOffer)
                .then((data) => {
                   location.reload();
                })
                .catch((status) => {
                    if (typeof status === 'undefined') {
                        alert('네트워크 오류 발생함');
                        return;
                    }
                    if (status === 401) {
                        location.href = '/users/login';
                        return;
                    }
                    console.error(status);
                });
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
            });
        });
    }

    requestStep(stepId) {
        return new Promise((resolve, reject) => {
            fetchManager({
                url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}/steps/${stepId}`,
                headers: {"Content-Type": "application/json"},
                method: 'GET',
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

    renderStep(stepForm, data) {
        if (data.offerType === 'APPEND') {
            const targetStepId = stepForm.getAttribute('data-step-id');
            this.createStepOfferContainer(targetStepId, data);
            this.createStepOffer(targetStepId, data);
        } else {
            this.addStepWithOwner(stepForm, data);
        }
    }

    requestStepModification(requestBody) {
        return new Promise((resolve, reject) => {
            fetchManager({
                url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}/steps/${requestBody.targetStepId}`,
                headers: {"Content-Type": "application/json"},
                method: 'PUT',
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

    requestStepApproval(stepOffer) {
        const recipeId = this.recipe.getAttribute('data-recipe-id');
        const offerId = stepOffer.getAttribute('data-offer-id');

        return new Promise((resolve, reject) => {
            fetchManager({
                url: `/api/recipes/${recipeId}/steps/${offerId}/approve`,
                headers: {"Content-Type": "application/json"},
                method: 'GET',
                onSuccess: ({json}) => {
                    resolve(json.data);
                },
                onFailed: ({status}) => {
                    reject(status);
                },
                onError: () => {
                    reject();
                }
            });
        });
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
        stepForm.closest('.step-container').insertAdjacentHTML('afterend', Templates.templateStep(data));
        $All('h2.step-title').forEach((e, i) => e.innerHTML = `<i class="fas fa-utensils fa-2x"></i>Step ${i + 1}`);
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
            content: this.getStepItemTexts(stepForm.querySelectorAll('.step-item-contents')),
            targetStepId: stepId,
            imgUrl: this.findImageUrl(stepId)
        }
    }

    findImageUrl(stepId) {
        const label = $(`label[for=img-upload-${stepId}]`);
        const backgroundImageUrl = label.style.backgroundImage;
        return backgroundImageUrl === "" ? null : backgroundImageUrl.match(/url\("(.*)"\)$/)[1];
    }

    getStepItemTexts(itemElements) {
        return [...itemElements].map(contentElement => contentElement.innerText);
    }

    completeRecipe(target) {
        fetchManager({
            url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}/complete`,
            method: 'POST',
            onSuccess: () => {
                location.reload();
            }
        })
    }

    renderStepInnerBefore(target, data) {
        target.insertAdjacentHTML('beforebegin', Templates.templateStepBoxInner(data));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new StepManager(new ImageUploader());
});