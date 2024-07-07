package com.pratik.major.dto;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private double weight;
    private String description;
    private String imageName;
    private Integer categoryId;
}
