package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tag", indexes = @Index(name = "tagNameIndex", columnList = "name"))
public class GiftTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

}
