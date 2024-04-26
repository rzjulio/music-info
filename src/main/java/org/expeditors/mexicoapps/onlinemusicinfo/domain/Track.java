package org.expeditors.mexicoapps.onlinemusicinfo.domain;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Track{
    private int id;
    private String title;
    private String album;
    @Valid
    private LocalDate issueDate;
    private double duration;
    private MediaFileType mediaFileType;

    public Track(){

    }
    @Builder
    public Track(String title, String album, LocalDate issueDate, double duration, MediaFileType mediaFileType) {
        this.title = title;
        this.album = album;
        this.issueDate = issueDate;
        this.duration = duration;
        this.mediaFileType = mediaFileType;
    }
}
