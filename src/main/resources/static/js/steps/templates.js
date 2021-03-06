class Templates {

    static templateStep(data) {
        return `
        <div class="step-container">
            <h2 class="step-title is-size-2 has-text-weight-bold">
            <i class="fas fa-utensils fa-2x"></i>
                Step ${data.sequence}
            </h2>
            ${this.templateStepBox(data)}
        </div>
        <div class="step-container">
            <button class="btn-step-add" data-step-id="${data.id}">
                <i class="fas fa-plus-circle fa-3x"></i> <div>Step 추가 하기</div> 
            </button>
        </div>
        `;
    }

    static templateStepBox(data) {
        return `
        <article class="box" data-step-id="${data.id}">
            ${Templates.templateStepBoxInner(data)}
            <div class="shadow-wrapper">
                <div class="contributors-shadow"></div>
                <div class="contributors">
                    <div class="contributor-container">
                        <div class="contributor main-contributor contributor-selected" data-step-id="${data.id}">
                            <div class="approved fas fa-medal fa-3x"></div>
                            <i class="fas fa-user-circle fa-3x"></i>
                            <div>${data.writer.name}</div>
                        </div>
                    </div>
                </div>
            </div>
        </article>
        `;
    }

    static templateStepBoxInner(data) {
        return `
            <div class="step-inner-container" data-step-id="${data.id}">
                <p class="subtitle">${data.name}</p>
                <div class="columns">
                    <div class="column is-one-third">
                        <img src="${data.imgUrl || "/img/recipe-default.png"}" class="step-img" alt="${data.name}">
                    </div>
                    <div class="column step-article-container">
                        <div class="step-contents-container">
                            <ol class="step-contents">
                                ${data.content.map((e) => (`<li><p>${e}</p></li>`)).join('\n')}
                            </ol>
                        </div>
                    </div>
                    <div class="column is-one-fifth">
                        <div class="is-clearfix">
                            <div class="btn-step-modify icon is-pulled-right ${ data.hasOwnProperty('offerType') ? 'hidden' : ''}">
                                <i class="fas fa-edit fa-2x"></i>
                            </div>
                        </div>
                        <div class="ingredient"></div>
                    </div>
                </div>
            </div>
        `;
    }

    static templateStepOffer(data) {
        return `
            <div class="step-offer" data-step-id=${data.id}>
                <a class="hero is-info step-offer-title-bar">
                    <h3 class="title">${data.name}
                        <span class="step-offer-open is-pulled-right"><i class="fas fa-angle-down"></i></span></span>
                    </h3>
                </a>
                <article class="box hidden">            
                    <div class="step-inner-container" data-step-id="${data.id}">
                        <div class="columns" data-step-id="${data.id}">
                            <div class="column is-one-third">
                                <img src="${data.imgUrl || "/img/recipe-default.png"}" class="step-img" alt="${data.name}">
                            </div>
                            <div class="column step-article-container">
                                <div class="step-contents-container">
                                    <ol class="step-contents">
                                        ${data.content.map((e) => (`<li><p>${e}</p></li>`)).join('\n')}
                                    </ol>
                                </div>
                            </div>
                        </div>
                        <div class="step-offer-writer"><i class="fas fa-user"></i> ${data.writer.name}</div>
                    </div>
                </article>
            </div>
        `;
    }

    static templateStepOfferContainer(data) {
        const stepId = data.target && data.target.id;
        return `
        <div class="step-offers" data-step-id="${stepId}">
            <h2 class="step-offer-title is-size-2"><i class="fas fa-bong"></i> Step Offers</h2>
        </div>
        `
    }

    static templateStepForm(targetStepId, type) {
        return `

        <article class="box step-form" id="step-form-${targetStepId}" data-step-id=${targetStepId}>
        <input class="input subtitle-input" type="text" placeholder="스텝 제목">
            <div class="columns">
                <div class="column is-one-third">
                    <input type="file" accept="image/*" style="min-height:400px;" name="img-upload" id="img-upload-${targetStepId}" class="img-upload"></input>
                    <label for="img-upload-${targetStepId}" class="has-text-centered">이미지를 업로드하려면 클릭하세요</label>
                </div>
                <div class="column step-article-container">
                    <div class="step-contents-container">
                        <ol class="step-contents">
                            ${this.templateStepContentInput()}
                        </ol>
                    </div>
                </div>
            </div>
            <div class="buttons is-right">
                <button class="btn-${type}-confirm button is-info">${type === 'add' ? '추가' : '수정'}</button>
                <button class="btn-${type}-cancel button is-danger">취소</button>
            </div>
        </article>
        `;
    }

    static templateStepContentListItem(content) {
        return `
        <li class="step-item">
            <div class="columns is-vcentered step-item-container">
                <p class="column is-11 step-item-contents" contenteditable="true">${content}</p><button class="btn-minus is-1"><i class="fas fa-minus fa-3x"></i></button>
            </div>
        </li>
        `;
    }

    static templateStepContentInput() {
        return `
        <li class="step-item">
            <div class="columns is-vcentered step-item-input-container">
                <input type="text" class="column is-11 is-large step-item-input input" maxlength="45" placeholder="생선을 잘라 10분간 물에 담가둔다."><button class="btn-plus is-2"><i class="fas fa-plus fa-3x"></i></button>
            </div>
        </li>
        `;
    }

    static templateStepContributor(content) {
        return `<div class="contributor-container">
            <div class="contributor contributor-selected" data-step-id="${content.id}">
                <i class="fas fa-user-circle fa-3x"></i>
                <div>${content.writer.name}</div>
            </div>
        </div>`
    }
}