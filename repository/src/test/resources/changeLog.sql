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
);



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
    PRIMARY KEY (id),
    KEY              certificateNameIndex ( name)
);



DROP TABLE IF EXISTS order_certificate;

CREATE TABLE order_certificate
(
    order_id       bigint NOT NULL,
    certificate_id bigint NOT NULL,
    KEY            FKk7mrxeei77rt3qv19k8gnphvq (certificate_id),
    KEY            FKna5fh9ed972nxlc3fwkfe9sox (order_id),
    CONSTRAINT FKk7mrxeei77rt3qv19k8gnphvq FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id),
    CONSTRAINT FKna5fh9ed972nxlc3fwkfe9sox FOREIGN KEY (order_id) REFERENCES user_order (id)
);


DROP TABLE IF EXISTS tag;
CREATE TABLE tag
(
    id   bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY  tagNameIndex ( name)
);

DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id   bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS user_order;
CREATE TABLE user_order
(
    id            bigint NOT NULL AUTO_INCREMENT,
    order_price   decimal(19, 2) DEFAULT NULL,
    purchase_date datetime(6) DEFAULT NULL,
    user_id       bigint NOT NULL,
    PRIMARY KEY (id),
    KEY           FKj86u1x7csa8yd68ql2y1ibrou (user_id),
    CONSTRAINT FKj86u1x7csa8yd68ql2y1ibrou FOREIGN KEY (user_id) REFERENCES user (id)
);