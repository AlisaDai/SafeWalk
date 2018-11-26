package ca.bcit.project.safewalk;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {
    private String sourceName;
    private String siteUrl;
    private String author;
    private String title;
    private String url;
    private Date publicshedAt;
    private String content;

    public News(String sourceName, String siteUrl, String author, String title, String url, Date publicshedAt, String content) {
        this.sourceName = sourceName;
        this.siteUrl = siteUrl;
        this.author = author;
        this.title = title;
        this.url = url;
        this.publicshedAt = publicshedAt;
        this.content = content;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getPublicshedAt() {
        return publicshedAt;
    }

    public void setPublicshedAt(Date publicshedAt) {
        this.publicshedAt = publicshedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
