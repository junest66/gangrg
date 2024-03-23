package com.project.gangrg.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Neighborhood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provinceName;

    @Column(nullable = false)
    private String areaName;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "point", name = "coordinates", nullable = false)
    private Point coordinates;

    public Neighborhood(String provinceName, String areaName, String name, Point coordinates) {
        this.provinceName = provinceName;
        this.areaName = areaName;
        this.name = name;
        this.coordinates = coordinates;
    }
}
