DROP TABLE IF EXISTS tag;
CREATE TABLE tag
(
    id   bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
)


DROP TABLE IF EXISTS gift_certificate;
CREATE TABLE gift_certificate
(
    id               bigint NOT NULL AUTO_INCREMENT,
    name             varchar(255)   DEFAULT NULL,
    description      text,
    price            decimal(10, 0) DEFAULT NULL,
    duration         int            DEFAULT NULL,
    create_date      datetime       DEFAULT NULL,
    last_update_date datetime       DEFAULT NULL,
    PRIMARY KEY (id)
)

DROP TABLE IF EXISTS certificate_tag;

CREATE TABLE certificate_tag
(
    id             bigint NOT NULL AUTO_INCREMENT,
    certificate_id bigint DEFAULT NULL,
    tag_id         bigint DEFAULT NULL,
    PRIMARY KEY (id),
    KEY            tag_id (tag_id),
    KEY            certificate_id (certificate_id),
    CONSTRAINT certificate_tag_ibfk_1 FOREIGN KEY (tag_id) REFERENCES tag (id),
    CONSTRAINT certificate_tag_ibfk_2 FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id)
)