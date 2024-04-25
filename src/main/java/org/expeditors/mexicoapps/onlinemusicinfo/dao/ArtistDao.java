package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;

import java.util.List;
import java.util.Optional;

public interface ArtistDao extends BaseDao<Artist> {
    Artist findByName(String name);
    List<Artist> findArtistsById(List<Integer> idsArtist);
}
