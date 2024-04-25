package org.expeditors.mexicoapps.onlinemusicinfo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Artist {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String nationality;

    @Builder
    public Artist(String name, LocalDate birthDate, String nationality) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }
}
