package org.expeditors.mexicoapps.onlinemusicinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;

import java.time.LocalDate;

@Getter
@Setter
public class TrackTransform extends Track {
    private double price;

    public TrackTransform(int id, String title, String album, LocalDate issueDate, double duration, MediaType mediaType,double price) {
        super(title, album, issueDate, duration, mediaType);
        this.setId(id);
        this.price = price;
    }
}
