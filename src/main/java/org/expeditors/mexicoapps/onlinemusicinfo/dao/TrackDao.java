package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;

import java.util.List;

public interface TrackDao extends BaseDao<Track>{
    List<Track> findTracksByAnyField(String value, String field);
    List<Track> findTracksById(List<Integer> idsTrack);
}
