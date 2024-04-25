package org.expeditors.mexicoapps.onlinemusicinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ResponseTracks {
    public TrackTransform track;
    public List<Artist> artists;
}
