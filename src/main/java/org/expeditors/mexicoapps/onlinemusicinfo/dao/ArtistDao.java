package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;

import java.util.List;

public interface ArtistDao extends BaseDao<Artist> {
    Artist findByName(String name);
    List<Artist> findArtistsById(List<Integer> idsArtist);
}
