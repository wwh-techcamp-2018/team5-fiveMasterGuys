INSERT INTO user (email, password, name) VALUES ('test@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'owner');
INSERT INTO user (email, password, name) VALUES ('test1@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'contributor1');
INSERT INTO user (email, password, name) VALUES ('test2@er.com', '$2a$10$6dbJkT999eWA/pALX9pVFO30j/cnMcG61NUcdyvEXp/Va3fJhnlRO', 'contributor2');
INSERT INTO recipe (owner_id, name, completed, img_url, created_at, updated_at) VALUES (1, '로즈마리 후추 버거', true, 'https://cdn.bmf.kr/_data/product/I21A3/a5ae10184ec276667e0a35e6f3012f20.jpg', now(), now());
INSERT INTO recipe_step (type, content, img_url, name, closed, sequence, rejected, recipe_id, writer_id) VALUES ('Step', '[''빵을 얹는다'', ''고기를 올린다'']', 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', '버거 모양 만들기', false, 1, false, 1, 1);
INSERT INTO recipe_step (type, content, img_url, name, closed, sequence, rejected, recipe_id, writer_id) VALUES ('Step', '[''빵을 얹는다'']', 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', '버거 모양 만들기2', false, 2, false, 1, 2);
INSERT INTO recipe_step (type, content, img_url, name, closed, sequence, rejected, recipe_id, writer_id) VALUES ('Step', '[''빵을 얹는다'']', 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', '버거 모양 만들기3', false, 3, false, 1, 3);


INSERT INTO recipe (owner_id, name, completed, img_url, created_at, updated_at) VALUES (1, '로즈마리 후추 버거', false, 'https://cdn.bmf.kr/_data/product/I21A3/a5ae10184ec276667e0a35e6f3012f20.jpg', now(), now());
INSERT INTO recipe_step (type, content, img_url, name, closed, sequence, rejected, recipe_id, writer_id) VALUES ('Step', '[''빵을 얹는다'', ''고기를 올린다'']', 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', '버거 모양 만들기', false, 1, false, 2, 1);
INSERT INTO recipe_step (type, content, img_url, name, closed, sequence, rejected, recipe_id, writer_id) VALUES ('Step', '[''빵을 얹는다'']', 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', '버거 모양 만들기2', false, 2, false, 2, 2);
INSERT INTO recipe_step (type, content, img_url, name, closed, sequence, rejected, recipe_id, writer_id) VALUES ('Step', '[''빵을 얹는다'']', 'https://cdn.bmf.kr/_data/product/IF088/2dee3415155804a5e9dc7c24fd21d96c.jpg', '버거 모양 만들기3', false, 3, false, 2, 3);