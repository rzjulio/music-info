package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import org.expeditors.mexicoapps.onlinemusicinfo.dao.AssociateRecordsImpl;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.expeditors.mexicoapps.onlinemusicinfo.service.ArtistService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/track")
public class TrackController {
    @Autowired
    private AssociateRecordsService associateRecordsService;
    @Autowired
    private TrackService trackService;

    @GetMapping
    public ResponseEntity<?> getAllTracks(){
        List<ResponseTracks> tracks = trackService.getAllTracks();

        if(tracks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tracks found");
        }
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrackById(@PathVariable("id") int id){
        ResponseTracks track = trackService.getTrack(id);
        if (track == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No track with id: "+id);
        }
        return ResponseEntity.ok(track);
    }

    @PostMapping
    public ResponseEntity<?> addTrack(@RequestBody Track track) {
        ResponseTracks newTrack = trackService.createTrack(track);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTrack.getTrack().getId())
                .toUri();

        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrack(@PathVariable("id") int id) {
        boolean result = trackService.deleteTrack(id);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No track with id: "+id);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateTrack(@RequestBody Track track) {
        boolean result = trackService.updateTrack(track);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No adopter with id: "+track.getId());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byMediaType/{mediaType}")
    public ResponseEntity<?> getTracksByMediaType(@PathVariable("mediaType") MediaType mediaType){
        List<ResponseTracks> tracks = trackService.getTracksByMediaType(mediaType);
        if (tracks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tracks with media type: "+mediaType);
        }
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/byYear/{year}")
    public ResponseEntity<?> getTracksByYear(@PathVariable("year") int year){
        List<ResponseTracks> tracks = trackService.getTracksByYear(String.valueOf(year));
        if (tracks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tracks with year: "+year);
        }
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/byDuration/{selectBy}/{duration}")
    public ResponseEntity<?> getTracksByDuration(@PathVariable("selectBy") String selectBy, @PathVariable("duration") String duration){
        List<ResponseTracks> tracks = trackService.getTracksByDuration(selectBy, Double.parseDouble(duration));
        if (tracks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tracks with duration: "+duration);
        }
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/associateArtist")
    public ResponseEntity<?> associateArtist(@RequestParam int idTrack, @RequestParam List<Integer> idsArtist) {
        int idTrackAssociate = associateRecordsService.createAssociateArtistsToTrack(idTrack,idsArtist);

        if (idTrackAssociate > 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't create associate with the artist");
        }

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/getArtistsByIdTrack/{id}")
                .buildAndExpand(idTrackAssociate)
                .toUri();

        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/disAssociateArtist")
    public ResponseEntity<?> disAssociateArtist(@RequestParam int idTrack, @RequestParam List<Integer> idsArtist) {
        boolean result = associateRecordsService.deleteAssociateArtistsFromTrack(idTrack, idsArtist);

        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No track with id: " + idTrack);
        }
        return ResponseEntity.noContent().build();
    }
}
