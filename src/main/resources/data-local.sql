INSERT INTO user (email, password, name) VALUES ('test@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'owner');

-- contributors
INSERT INTO user (email, password, name) VALUES ('test1@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'contributor1');
INSERT INTO user (email, password, name) VALUES ('test2@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'contributor2');

-- Completed recipe
INSERT INTO recipe (owner_id, name, completed, img_url, created_at, updated_at) VALUES (1, '로즈마리 후추 버거', true, 'https://cdn.bmf.kr/_data/product/I21A3/a5ae10184ec276667e0a35e6f3012f20.jpg', now(), now());
INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '버거 모양 만들기1', '["빵을 얹는다", "고기를 올린다"]', 1, false, false, 1, 1, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '버거 모양 만들기2', '["소스를 뿌린다"]', 2, false, false, 1, 2, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '버거 모양 만들기3', '["냉동실에 넣는다", "5분 후 오븐에 굽는다", "빵을 덮는다"]', 3, false, false, 1, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
-- Closed step (You can't see this in recipe detail page)
INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '이건 안보여야됨', '["빵을 얹는다", "고기를 올린다"]', 4, true, false, 1, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

-- Incompleted recipe
INSERT INTO recipe (owner_id, name, completed, img_url, created_at, updated_at) VALUES (1, '김치 피자', false, 'https://cdn.bmf.kr/_data/product/I21A3/a5ae10184ec276667e0a35e6f3012f20.jpg', now(), now());

INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '피자 모양 만들기1', '["빵을 얹는다", "고기를 올린다"]', 1, false, false, 2, 1, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '피자 모양 만들기2', '["치즈를 올린다", "고기를 올린다"]', 2, false, false, 2, 2, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '피자 모양 만들기3', '["치즈를 또 얹는다", "오븐에 굽는다"]', 3, false, false, 2, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');
-- Closed step (You CAN see in recipe detail page because recipe is incompleted)
INSERT INTO recipe_step (type, name, content, sequence, closed, rejected, recipe_id, writer_id, img_url) VALUES ('Step', '이건 보여야됨', '["빵을 얹는다"]', 4, true, false, 2, 3, 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg');

