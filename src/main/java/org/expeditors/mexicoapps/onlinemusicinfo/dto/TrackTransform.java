package org.expeditors.mexicoapps.onlinemusicinfo.dto;

import lombok.Getter;
import lombok.Setter;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;

import java.time.LocalDate;

@Getter
@Setter
public class TrackTransform extends Track {
    private double price;

    public TrackTransform(int id, String title, String album, LocalDate issueDate, double duration, MediaFileType mediaFileType, double price) {
        super(title, album, issueDate, duration, mediaFileType);
        this.setId(id);
        this.price = price;
    }
}
