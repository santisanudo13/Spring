
package rest.RestClientTest.buscaComp;

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
    "names",
    "titles",
    "companies"
})
public class Results {

    @JsonProperty("names")
    private List<Name> names = new ArrayList<Name>();
    @JsonProperty("titles")
    private List<Title> titles = new ArrayList<Title>();
    @JsonProperty("companies")
    private List<Company> companies = new ArrayList<Company>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The names
     */
    @JsonProperty("names")
    public List<Name> getNames() {
        return names;
    }

    /**
     * 
     * @param names
     *     The names
     */
    @JsonProperty("names")
    public void setNames(List<Name> names) {
        this.names = names;
    }

    /**
     * 
     * @return
     *     The titles
     */
    @JsonProperty("titles")
    public List<Title> getTitles() {
        return titles;
    }

    /**
     * 
     * @param titles
     *     The titles
     */
    @JsonProperty("titles")
    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    /**
     * 
     * @return
     *     The companies
     */
    @JsonProperty("companies")
    public List<Company> getCompanies() {
        return companies;
    }

    /**
     * 
     * @param companies
     *     The companies
     */
    @JsonProperty("companies")
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
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
