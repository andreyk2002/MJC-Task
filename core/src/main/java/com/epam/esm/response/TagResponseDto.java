package com.epam.esm.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TagResponseDto extends RepresentationModel<TagResponseDto> {

    private long id;

    private String name;

}
