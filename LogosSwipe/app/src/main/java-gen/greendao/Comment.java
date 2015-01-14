package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table COMMENT.
 */
public class Comment {

    private Long id;
    private String name;
    private java.util.Date datetime;
    private String content;

    public Comment() {
    }

    public Comment(Long id) {
        this.id = id;
    }

    public Comment(Long id, String name, java.util.Date datetime, String content) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Date getDatetime() {
        return datetime;
    }

    public void setDatetime(java.util.Date datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
