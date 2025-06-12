package mx.com.ctx.Model;

import java.math.BigDecimal;

public class DataProducts {
    private final String id;
    private final String title;
    private final String description;
    private final String availability;
    private final String link;
    private final String image_link;
    private final BigDecimal price;
    private final String identifier_exist;
    public DataProducts(String id,
                        String title,
                        String description,
                        String availability,
                        String link,
                        String image_link,
                        BigDecimal price,
                        String identifier_exist) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.availability = availability;
        this.link = link;
        this.image_link = image_link;
        this.price = price;
        this.identifier_exist = identifier_exist;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailability() {
        return availability;
    }

    public String getLink() {
        return link;
    }

    public String getImage_link() {
        return image_link;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getIdentifier_exist() {
        return identifier_exist;
    }
}
