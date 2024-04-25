package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ArtistDaoImpl implements ArtistDao{

    private final Map<Integer, Artist> artistMap = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Artist findByName(String name) {
        return artistMap.values().stream().filter(f -> f.getName().equals(name)).toList().getFirst();
    }

    @Override
    public Artist insert(Artist object) {
        object.setId(nextId.getAndIncrement());
        artistMap.put(object.getId(), object);
        return object;
    }

    @Override
    public boolean update(Artist object) {
        return artistMap.replace(object.getId(), object) != null;
    }

    @Override
    public boolean delete(int id) {
        return artistMap.remove(id) != null;
    }

    @Override
    public Artist findById(int id) {
        return artistMap.get(id);
    }

    @Override
    public List<Artist> findAll() {
        return new ArrayList<>(artistMap.values());
    }

    @Override
    public List<Artist> findArtistsById(List<Integer> idsArtist) {
        return artistMap.values().stream().filter(f -> idsArtist.contains(f.getId())).toList();
    }
}
