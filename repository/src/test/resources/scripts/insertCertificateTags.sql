DELETE
FROM certificate_tag;
DELETE
FROM tag
WHERE id = 19
   OR id = 22;
DELETE
FROM gift_certificate
WHERE id = 77;

INSERT INTO gift_certificate (id, name, price, duration, create_date, last_update_date)
VALUES (77, 'info', 19, 0, '2002-08-12T11:54:22.619', '2002-08-12T11:54:22.619');

INSERT INTO tag (id, name)
VALUES (19, 'first tag');

INSERT INTO tag (id, name)
VALUES (22, 'second tag');

INSERT INTO certificate_tag (certificate_id, tag_id)
VALUES (77, 19);

INSERT INTO certificate_tag (certificate_id, tag_id)
VALUES (77, 22);