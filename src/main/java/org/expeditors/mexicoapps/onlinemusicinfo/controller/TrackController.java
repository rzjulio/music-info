package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/track")
public class TrackController {
    @Autowired
    private AssociateRecordsService associateRecordsService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UriCreator uriCreator;

    @GetMapping
    public ResponseEntity<?> getAllTracks(){
        List<ResponseTracks> tracks = trackService.getAllTracks();

        if(tracks.isEmpty()) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No tracks found");
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrackById(@PathVariable("id") int id){
        ResponseTracks track = trackService.getTrack(id);
        if (track == null) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No track with id: "+id);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(track);
    }

    @PostMapping
    public ResponseEntity<?> addTrack(@RequestBody Track track) {
        ResponseTracks newTrack = trackService.createTrack(track);

        URI newResource = uriCreator.getURI(newTrack.getTrack().getId());


        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrack(@PathVariable("id") int id) {
        boolean result = trackService.deleteTrack(id);
        if (!result) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No track with id: "+id);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateTrack(@RequestBody Track track) {
        boolean result = trackService.updateTrack(track);
        if (!result) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No adopter with id: "+track.getId());
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byMediaType/{mediaType}")
    public ResponseEntity<?> getTracksByMediaType(@PathVariable("mediaType") MediaFileType mediaFileType){
        List<ResponseTracks> tracks = trackService.getTracksByMediaType(mediaFileType);
        if (tracks == null) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No tracks with media type: "+ mediaFileType);;
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/byYear/{year}")
    public ResponseEntity<?> getTracksByYear(@PathVariable("year") int year){
        List<ResponseTracks> tracks = trackService.getTracksByYear(String.valueOf(year));
        if (tracks == null) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No tracks with year: "+year);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/byDuration/{selectBy}/{duration}")
    public ResponseEntity<?> getTracksByDuration(@PathVariable("selectBy") String selectBy, @PathVariable("duration") String duration){
        List<ResponseTracks> tracks = trackService.getTracksByDuration(selectBy, Double.parseDouble(duration));
        if (tracks == null) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No tracks with duration: "+duration);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/associateArtist")
    public ResponseEntity<?> associateArtist(@RequestParam int idTrack, @RequestParam List<Integer> idsArtist) {
        int idTrackAssociate = associateRecordsService.createAssociateArtistsToTrack(idTrack,idsArtist);

        if (idTrackAssociate < 1){
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"Can't create associate with the artist");
            return ResponseEntity.badRequest()
                    .body(pd);
        }

        uriCreator.setPath("/getArtistsByIdTrack/{id}");
        URI newResource = uriCreator.getURI(idTrackAssociate);

        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/disAssociateArtist")
    public ResponseEntity<?> disAssociateArtist(@RequestParam int idTrack, @RequestParam List<Integer> idsArtist) {
        boolean result = associateRecordsService.deleteAssociateArtistsFromTrack(idTrack, idsArtist);

        if (!result) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No track with id: " + idTrack);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.noContent().build();
    }
}
