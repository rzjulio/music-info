package org.expeditors.mexicoapps.onlinemusicinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ResponseArtist {
    public Artist artist;
    public List<TrackTransform> tracks;
}
