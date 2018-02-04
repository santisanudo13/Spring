
package rest.RestClientTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "type",
    "title",
    "year",
    "description",
    "certificate",
    "duration",
    "released",
    "cast",
    "genres",
    "directors",
    "writers",
    "image",
    "review"
})
public class Data {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("title")
    private Object title;
    @JsonProperty("year")
    private Object year;
    @JsonProperty("description")
    private String description;
    @JsonProperty("certificate")
    private String certificate;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("released")
    private String released;
    @JsonProperty("cast")
    private List<String> cast = new ArrayList<String>();
    @JsonProperty("genres")
    private List<String> genres = new ArrayList<String>();
    @JsonProperty("directors")
    private List<Object> directors = new ArrayList<Object>();
    @JsonProperty("writers")
    private List<Object> writers = new ArrayList<Object>();
    @JsonProperty("image")
    private String image;
    @JsonProperty("review")
    private Review review;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public Object getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(Object title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The year
     */
    @JsonProperty("year")
    public Object getYear() {
        return year;
    }

    /**
     * 
     * @param year
     *     The year
     */
    @JsonProperty("year")
    public void setYear(Object year) {
        this.year = year;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The certificate
     */
    @JsonProperty("certificate")
    public String getCertificate() {
        return certificate;
    }

    /**
     * 
     * @param certificate
     *     The certificate
     */
    @JsonProperty("certificate")
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    /**
     * 
     * @return
     *     The duration
     */
    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    @JsonProperty("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The released
     */
    @JsonProperty("released")
    public String getReleased() {
        return released;
    }

    /**
     * 
     * @param released
     *     The released
     */
    @JsonProperty("released")
    public void setReleased(String released) {
        this.released = released;
    }

    /**
     * 
     * @return
     *     The cast
     */
    @JsonProperty("cast")
    public List<String> getCast() {
        return cast;
    }

    /**
     * 
     * @param cast
     *     The cast
     */
    @JsonProperty("cast")
    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    /**
     * 
     * @return
     *     The genres
     */
    @JsonProperty("genres")
    public List<String> getGenres() {
        return genres;
    }

    /**
     * 
     * @param genres
     *     The genres
     */
    @JsonProperty("genres")
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    /**
     * 
     * @return
     *     The directors
     */
    @JsonProperty("directors")
    public List<Object> getDirectors() {
        return directors;
    }

    /**
     * 
     * @param directors
     *     The directors
     */
    @JsonProperty("directors")
    public void setDirectors(List<Object> directors) {
        this.directors = directors;
    }

    /**
     * 
     * @return
     *     The writers
     */
    @JsonProperty("writers")
    public List<Object> getWriters() {
        return writers;
    }

    /**
     * 
     * @param writers
     *     The writers
     */
    @JsonProperty("writers")
    public void setWriters(List<Object> writers) {
        this.writers = writers;
    }

    /**
     * 
     * @return
     *     The image
     */
    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The review
     */
    @JsonProperty("review")
    public Review getReview() {
        return review;
    }

    /**
     * 
     * @param review
     *     The review
     */
    @JsonProperty("review")
    public void setReview(Review review) {
        this.review = review;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
