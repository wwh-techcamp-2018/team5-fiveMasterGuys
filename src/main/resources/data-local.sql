INSERT INTO user (email, password, name) VALUES ('test@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'owner');

-- contributors
INSERT INTO user (email, password, name) VALUES ('test1@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'contributor1');
INSERT INTO user (email, password, name) VALUES ('test2@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'contributor2');

INSERT INTO category(title) VALUES('한식');
INSERT INTO category(title) VALUES('중식');
INSERT INTO category(title) VALUES('양식');
INSERT INTO category(title) VALUES('대식');
INSERT INTO category(title) VALUES('소식');
INSERT INTO category(title) VALUES('이유식');

-- Completed recipe
INSERT INTO recipe (category_id, owner_id, name, completed, img_url, created_at, updated_at) VALUES (1, 1, '로즈마리 후추 버거', true, '/img/recipe-default.png', now(), now());
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '버거 모양 만들기1', '["빵을 얹는다", "고기를 올린다"]', 1, false, false, 1, 1, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '버거 모양 만들기2', '["소스를 뿌린다"]', 2, false, false, 1, 2, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '버거 모양 만들기3', '["냉동실에 넣는다", "5분 후 오븐에 굽는다", "빵을 덮는다"]', 3, false, false, 1, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
-- Closed step (You can't see this in recipe detail page)
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '이건 안보여야됨', '["빵을 얹는다", "고기를 올린다"]', 4, true, false, 1, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

-- Incompleted recipe
INSERT INTO recipe (category_id, owner_id, name, completed, img_url, created_at, updated_at) VALUES (2, 1, '김치 피자', false, '/img/recipe-default.png', now(), now());

INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '피자 모양 만들기1', '["빵을 얹는다", "고기를 올린다"]', 1, false, false, 2, 1, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '피자 모양 만들기2', '["치즈를 올린다", "고기를 올린다"]', 2, false, false, 2, 2, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '피자 모양 만들기3', '["치즈를 또 얹는다", "오븐에 굽는다"]', 3, false, false, 2, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

-- Step Offer MODIFY
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url, target_id, offer_type) VALUES ('StepOffer', '스텝 추가 제안', '["빵을 사온다"]', 4, false, false, 2, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', 5, 'APPEND');
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url, target_id, offer_type) VALUES ('StepOffer', '스텝 수정 제안', '["빵을 얹는다", "상추를 올린다"]', 4, false, false, 2, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', 5, 'MODIFY');

-- Closed step (You CAN see in recipe detail page because recipe is incompleted)
INSERT INTO step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '이건 보여야됨', '["빵을 얹는다"]', 4, true, false, 2, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

