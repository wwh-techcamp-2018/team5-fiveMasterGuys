class Templates {

    static templateStep(data) {
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

    static templateStepOffer(data) {
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

    static templateStepOfferContainer(data) {
        const stepId = data.target && data.target.id;
        return `
        <div class="step-offers" data-step-id="${stepId}">
            <div class="step-offer-title is-size-2"><i class="fas fa-bong"></i> Step Offers</div>
        </div>
        `
    }

    static templateStepForm(targetStepId) {
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

    static templateStepContentListItem(content) {
        return `<li class="step-item"><div class="columns is-vcentered step-item-container"><div class="column is-11 step-item-contents">${content}</div><button class="btn-minus is-1"><i class="fas fa-minus fa-3x"></i></button></div></li>`;
    }

    static templateStepContentInput() {
        return `<li class="step-item"><div class="columns is-vcentered step-item-input-container"><input type="text" class="column is-11 is-large step-item-input input" placeholder="hello"><button class="btn-plus is-2"><i class="far fa-plus-square fa-3x"></i></button></div></li>`
    }
}

export {Templates};