
package rest.RestClientTest;

import java.util.List;


import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface PeliRestService {
	
 @GET("/peliRest")
 List<PeliRest> getPeliRest();
 
 @GET("/{index}")
PeliRest getPeliRest(@Path("index") String string);
 
 @POST("/peliRest")
 boolean addPeliRest(@Body PeliRest peli);
} 