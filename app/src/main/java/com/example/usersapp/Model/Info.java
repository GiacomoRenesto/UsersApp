
package com.example.usersapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Info extends RealmObject {

    @SerializedName("seed")
    @Expose
    private String seed;
    @SerializedName("results")
    @Expose
    private Integer results;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("version")
    @Expose
    private String version;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Info() {
    }

    /**
     * 
     * @param results
     * @param page
     * @param seed
     * @param version
     */
    public Info(String seed, Integer results, Integer page, String version) {
        super();
        this.seed = seed;
        this.results = results;
        this.page = page;
        this.version = version;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
