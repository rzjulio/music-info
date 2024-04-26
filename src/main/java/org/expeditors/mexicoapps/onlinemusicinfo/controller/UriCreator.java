package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
@Setter
@Component
public class UriCreator {
	private String path = "/{id}";
	public URI getURI(int id) {

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(path)
                .buildAndExpand(id)
                .toUri();
		
        return newResource;
	}

   public URI getURI() {

      URI newResource = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .build()
            .toUri();

      return newResource;
   }

   public ProblemDetail getProblemDetail(HttpStatus status, String detail) {
      var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            detail);
      var instance = getURI();
      problemDetail.setInstance(instance);

      return problemDetail;
   }
}
