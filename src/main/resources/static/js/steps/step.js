class StepManager {
    constructor(imageUploader, errorMessageView) {
        this.recipe = $('.recipe');
        this.errorMessageView = errorMessageView;
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

    handleChangeEvent({ target }) {
        if (target.classList.contains('img-upload')) {
            this.imageUploader.upload(this.getFile(target))
                .then(({data}) => {
                    const label = $(`label[for=${target.id}]`);
                    label.style.backgroundImage = `url(${data})`;
                    label.innerText = '';
                })
                .catch(({status, errors}) => {
                    this.handleAjaxError(status, errors);
                });
            return;
        }
    }

    getFile(target) {
        return target.files[0];
    }

    handleKeyUpEvent(e) {
        if (e.target.classList.contains('step-item-input') && e.keyCode === 13) {
            this.appendStepItem(e.target);
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
        const stepBoxInner = stepBox.querySelector('.step-inner-container:not(.hidden)');
        const targetStepId = stepBox.getAttribute('data-step-id');
        stepBox.insertAdjacentHTML('beforebegin', Templates.templateStepForm(targetStepId, 'modify'));
        location.href=`#step-form-${targetStepId}`

        const stepForm = stepBox.previousElementSibling;
        this.copyValuesToForm(stepBoxInner, stepForm);
        toggleHidden(stepBox);
    }

    showModifyOfferStepForm(target) {
        checkLoginOrRedirect();

        const stepBox = target.closest('.box');
        const stepId = stepBox.getAttribute('data-step-id');
        const stepBoxInner = stepBox.querySelector(`.step-inner-container[data-step-id='${stepId}']`);
        stepBox.insertAdjacentHTML('beforebegin', Templates.templateStepForm(stepId, 'modify-offer'));
        location.href=`#step-form-${stepId}`
        
        const stepForm = stepBox.previousElementSibling;
        this.copyValuesToForm(stepBoxInner, stepForm);
        toggleHidden(stepBox);
    }

    copyValuesToForm(stepBox, stepForm) {
        const imgLabel = stepForm.querySelector('label.has-text-centered');
        imgLabel.style.backgroundImage = `url(${stepBox.querySelector('img.step-img').getAttribute('src')})`;
        imgLabel.innerText = '';

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
        const stepId = target.closest('.box').getAttribute('data-step-id');
        const stepItem = target.closest('.step-item');
        const value = stepItem.querySelector('input').value;
        if (value.trim() === '') {
            return;
        }
        const stepContainer = target.closest('ol.step-contents');
        removeElement(stepItem);
        stepContainer.insertAdjacentHTML('beforeend', Templates.templateStepContentListItem(value));
        stepContainer.insertAdjacentHTML('beforeend', Templates.templateStepContentInput());
        $(`.box[data-step-id="${stepId}"] .step-item-input`).focus();
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

    handleMouseOverEvent({ target }) {
        if (target.classList.contains('contributor')) {

            let boxId = target.closest('.box').getAttribute('data-step-id');
            this.removeSelectedContributor(boxId);
            target.classList.toggle('contributor-selected');

            if (target.getAttribute("data-step-id") === null) {
                return;
            }

            let exposedBoxesInner = $All(`.box[data-step-id="${boxId}"] > .step-inner-container`);
            exposedBoxesInner.forEach(t => t.classList.add('hidden'));

            let stepId = target.getAttribute('data-step-id');
            let overedBoxInner = $(`.step-inner-container[data-step-id="${stepId}"]`);
            overedBoxInner.classList.toggle(`hidden`);
        }
    }

    handleAddFormConfirmButtonClick(target) {
        const stepForm = target.closest('.box');
        const subtitleValue = stepForm.querySelector('.subtitle-input').value;
        if (subtitleValue.trim() === '' || subtitleValue.length > 30) {
            this.errorMessageView.showMessage('스텝 제목은 1 ~ 30자 사이여야 합니다.');
            return;
        }

        const requestBody = this.makeRequestBody(stepForm);
        target.disabled = true;
        this.requestStepAddition(requestBody)
            .then(({data}) => {
                this.renderStep(stepForm, data);
                this.closeAddForm(stepForm);
            }).catch(({status, errors}) => {
                target.disabled = false;
                this.handleAjaxError(status, errors);
            });
    }

    handleAddFormCancelButtonClick(target) {
        const stepForm = target.closest('.box');
        this.closeAddForm(stepForm);
    }

    handleModifyFormConfirmButtonClick(target) {
        
        const stepForm = target.closest('.box');
        const subtitleValue = stepForm.querySelector('.subtitle-input').value;
        if (subtitleValue.trim() === '' || subtitleValue.length > 30) {
            this.errorMessageView.showMessage('스텝 제목은 1 ~ 30자 사이여야 합니다.');
            return;
        }
        
        const requestBody = this.makeRequestBody(stepForm);
        target.disabled = true;
        this.requestStepModification(requestBody)
            .then(({data}) => {
                const stepBox = stepForm.nextElementSibling;
                stepBox.insertAdjacentHTML('beforebegin', Templates.templateStepBox(data));
                removeElement(stepBox);
                removeElement(stepForm);
            })
            .catch(({status, errors}) => {
                target.disabled = false;
                this.handleAjaxError(status, errors);
            });
    }

    handleOfferModifyFormConfirmButtonClick(target) {
        const stepForm = target.closest('.box');
        const subtitleValue = stepForm.querySelector('.subtitle-input').value;
        if (subtitleValue.trim() === '' || subtitleValue.length > 30) {
            this.errorMessageView.showMessage('스텝 제목은 1 ~ 30자 사이여야 합니다.');
            return;
        }
        
        const requestBody = this.makeRequestBody(stepForm);
        target.disabled = true;

        this.requestStepModification(requestBody)
            .then(({data}) => {
                this.removeSelectedContributor(requestBody.targetStepId);
                const contributorsBox = $(`.box[data-step-id="${requestBody.targetStepId}"] .contributors`);
                contributorsBox.insertAdjacentHTML('beforeend', Templates.templateStepContributor(data));


                const stepBox = stepForm.nextElementSibling;
                const target = stepBox.querySelector('.shadow-wrapper');
                this.renderStepInnerBefore(target, data);
                toggleHidden(stepBox);
                toggleHidden(stepBox.querySelector('.step-inner-container:not(.hidden)'));
                removeElement(stepForm);
            })
            .catch(({status, errors}) => {
                target.disabled = false;
                this.handleAjaxError(status, errors);
            });
    }

    handleModifyFormCancelButtonClick(target) {
        const stepForm = target.closest('.box');
        const stepBox = stepForm.nextElementSibling;
        toggleHidden(stepBox);
        removeElement(stepForm);
    }

    handleApproveButtonClick(target) {
        target.disabled = true;

        const stepOffer = target.closest('.step-offer');
        this.requestStepApproval(stepOffer)
            .then(({data}) => {
               location.reload();
            })
            .catch(({status, errors}) => {
                target.disabled = false;
                this.handleAjaxError(status, errors);
            });
    }

    handleAjaxError(status, errors) {
        if (typeof status === 'undefined') {
            this.errorMessageView.showMessage('네트워크 오류 발생함');
            return;
        }
        if (status === 401) {
            location.href = '/users/login';
            return;
        }

        if (errors) {
            this.errorMessageView.showMessage(errors[0].message);
            return;
        }
    }

    toggleStepOfferContent(target) {
        toggleHidden(target.nextElementSibling);
    }


    requestStepAddition(requestBody) {
        return fetchManager({
            url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}/steps`,
            headers: {"Content-Type": "application/json"},
            method: 'POST',
            body: JSON.stringify(requestBody)
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
        return fetchManager({
            url: `/api/recipes/${this.recipe.getAttribute('data-recipe-id')}/steps/${requestBody.targetStepId}`,
            headers: {"Content-Type": "application/json"},
            method: 'PUT',
            body: JSON.stringify(requestBody)
        });
    }


    requestStepApproval(stepOffer) {
        const recipeId = this.recipe.getAttribute('data-recipe-id');
        const offerId = stepOffer.getAttribute('data-step-id');

        return fetchManager({
            url: `/api/recipes/${recipeId}/steps/${offerId}/approve`,
            headers: {"Content-Type": "application/json"},
            method: 'GET'
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
        const el = $(`.btn-step-add[data-step-id="${targetStepId}"]`);
        el.insertAdjacentHTML('beforebegin', Templates.templateStepOfferContainer(data));
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
        const content = this.getStepItemTexts(stepForm.querySelectorAll('.step-item-contents'));
        const input = stepForm.querySelector('.step-item-input');
        if (input !== null && input.value.trim() !== '') {
            content.push(input.value);
        }
        return {
            name: stepForm.querySelector('.subtitle-input').value,
            content,
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
            method: 'POST'
        }).then(() => {
            location.reload();
        });
    }

    renderStepInnerBefore(target, data) {
        target.insertAdjacentHTML('beforebegin', Templates.templateStepBoxInner(data));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const errorMessageView = new ErrorMessageView($('.error-msg-box'));
    new StepManager(new ImageUploader(), errorMessageView);
});