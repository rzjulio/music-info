package org.expeditors.mexicoapps.onlinemusicinfo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Track{
    private int id;
    private String title;
    private String album;
    private LocalDate issueDate;
    private double duration;
    private MediaType mediaType;

    @Builder
    public Track(String title, String album, LocalDate issueDate, double duration, MediaType mediaType) {
        this.title = title;
        this.album = album;
        this.issueDate = issueDate;
        this.duration = duration;
        this.mediaType = mediaType;
    }
}
