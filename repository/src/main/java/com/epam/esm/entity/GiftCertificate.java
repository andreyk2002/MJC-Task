package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "gift_certificate", indexes = {
        @Index(name = "certificateNameIndex", columnList = "name"),
        @Index(name = "certificateDescriptionIndex", columnList = "description")
})
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 511)
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private List<GiftTag> tags;


    @Column(name = "duration")
    private Integer duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
        lastUpdateDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdateDate = LocalDateTime.now();
    }


}
