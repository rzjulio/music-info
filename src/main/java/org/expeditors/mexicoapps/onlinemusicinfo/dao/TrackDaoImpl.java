package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Repository
public class TrackDaoImpl implements TrackDao{

    private final Map<Integer, Track> tracksMap = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);
    @Override
    public Track insert(Track object) {
        if(object == null) {
            return null;
        }

        object.setId(nextId.getAndIncrement());
        tracksMap.put(object.getId(), object);
        return object;
    }

    @Override
    public boolean update(Track object) {
        return tracksMap.replace(object.getId(), object) != null;
    }

    @Override
    public boolean delete(int id) {
        return tracksMap.remove(id) != null;
    }

    @Override
    public Track findById(int id) {
        return tracksMap.get(id);
    }

    @Override
    public List<Track> findAll() {
        return new ArrayList<>(tracksMap.values());
    }

    @Override
    public List<Track> findTracksByAnyField(String value, String field) {
        return new ArrayList<>(tracksMap.values()).stream()
                .filter(f -> predicates(field,f,value)).toList();
    }

    @Override
    public List<Track> findTracksById(List<Integer> idsTrack) {
        return tracksMap.values().stream().filter(f -> idsTrack.contains(f.getId())).toList();
    }

    private static boolean predicates(String field,Track track, String value) {
        switch (field){
            case "mediaType":
                Predicate<Track> mediaType = (trackPredicate) -> track.getMediaFileType().equals(MediaFileType.valueOf(value));
                return mediaType.test(track);
            case "year":
                Predicate<Track> year = (trackPredicate) -> String.valueOf(track.getIssueDate().getYear()).toLowerCase().equals(value);
                return year.test(track);
            case "duration":
                String getBy = value.split(":")[0];
                double durationValue = Double.parseDouble(value.split(":")[1]);
                Predicate<Track> duration = (trackPredicate) -> (getBy.equals("equal") && track.getDuration() == durationValue) ||
                        (getBy.equals("longer") && track.getDuration() > durationValue) ||
                        (getBy.equals("shorter") && track.getDuration() < durationValue);
                return duration.test(track);
            default:
                throw new IllegalStateException("Unexpected value: " + field);
        }
    }
}
