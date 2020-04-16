package API.Data;

public class DataCatalogs {

    private String name, link;
    private Long id;

    public DataCatalogs(Long id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
