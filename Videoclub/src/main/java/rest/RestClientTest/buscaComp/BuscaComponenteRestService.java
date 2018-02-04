
package rest.RestClientTest.buscaComp;

import java.util.List;


import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import retrofit.http.Query;

public interface BuscaComponenteRestService {
	
 @GET("/buscaComponenteRest")
 List<BuscaComponenteRest> getBuscaComponenteRest();
 
 
 
 @GET("/search")
 BuscaComponenteRest getBuscaComponenteRest(@Query("q") String string);
 
 @POST("/buscaComponenteRest")
 boolean addBuscaComponenteRest(@Body BuscaComponenteRest buscaComponenteRest);
} 