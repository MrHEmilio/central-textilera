package mx.com.ctx.Service;

import mx.com.ctx.Config.ConexionDB;
import mx.com.ctx.Model.DataProducts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class ProductsService {
    private static final Logger log = Logger.getLogger(ProductsService.class.getName());

    private final List<DataProducts> listProducts;

    private final Properties properties;

    public ProductsService() {
        this.properties = new Properties();
        this.listProducts = new ArrayList<>();
    }

    public void extractData() {
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("XmlProp.properties")) {
            this.properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String url = this.properties.getProperty("db.url");
        String username = this.properties.getProperty("db.user");
        String password = this.properties.getProperty("db.pwd");
        String query = this.properties.getProperty("query.select");

        String urlProducts = this.properties.getProperty("url.product");
        String urlImages = this.properties.getProperty("url.images");
        String format = this.properties.getProperty("format.images");

        String plusDescription = this.properties.getProperty("plus.description");

        ConexionDB conexionDB = new ConexionDB(url, username, password);

        try {
            conexionDB.ConectarDB();
            try (PreparedStatement pst = conexionDB.getConnection().prepareStatement(query)) {
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        String id = rs.getString("idcloth");
                        String title = rs.getString("name");
                        String description = rs.getString("description");
                        String nameUrl =  rs.getString("nameUrl");
                        BigDecimal price = rs.getBigDecimal("price");
                        boolean available = rs.getBoolean("active");

                        if(available){
                            String stock = "in_stock";
                            boolean exists = this.listProducts.stream().anyMatch(p -> p.getId().equals(id));
                            if(!exists){
                                this.listProducts.add(new DataProducts(
                                        id,title, description+plusDescription,
                                        stock,urlProducts+nameUrl,urlImages+id+format,
                                        price, "no"
                                ));
                            }
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            log.warning("Error al consultar la Base de Datos: " + sqle.getMessage());
        } finally {
            conexionDB.DesconectarDB();
        }
        generateXml(this.listProducts, this.properties.getProperty("path.xml"));
    }

    private void generateXml(List<DataProducts> products, String xmlPath) {

        try {
            log.info("Generando XML de la Base de Datos: " + xmlPath);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz <rss>
            Element rss = doc.createElement("rss");
            rss.setAttribute("xmlns:g", "http://base.google.com/ns/1.0");
            rss.setAttribute("version", "2.0");
            doc.appendChild(rss);

            // Crear el elemento <channel>
            Element channel = doc.createElement("channel");
            rss.appendChild(channel);

            // Agregar elementos de información general al <channel>
            appendChild(doc, channel, "title", "Central Textilera | Expreciencia y Servicio");
            appendChild(doc, channel, "link", "https://centraltextilera.com/");
            appendChild(doc, channel, "description", "Somos la distribuidora textil líder en el mercado nacional," +
                    " contamos con la variedad de telas más extensa y de mayor calidad en México. Más de 20 años de experiencia nos respaldan.");

            // Generar los productos como <item>
            products.forEach(product -> {
                Element item = doc.createElement("item");

                appendChildWithNamespace(doc, item, "id", product.getId());
                appendChildWithNamespace(doc, item, "title", product.getTitle());
                appendChildWithNamespace(doc, item, "description", product.getDescription());
                appendChildWithNamespace(doc, item, "link", product.getLink());
                appendChildWithNamespace(doc, item, "image_link", product.getImage_link());
                appendChildWithNamespace(doc, item, "condition", "new");
                appendChildWithNamespace(doc, item, "availability", product.getAvailability());
                appendChildWithNamespace(doc, item, "price", product.getPrice().toPlainString());

                appendChildWithNamespace(doc, item, "gtin","");
                appendChildWithNamespace(doc, item, "brand", "Central Textilera");

                channel.appendChild(item);
            });

            // Configurar y transformar el XML
            log.info("Transformando XML de la Base de Datos: ");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlPath);

            transformer.transform(source, result);
            log.info("Se generó correctamente el archivo XML: " + xmlPath);

        } catch (Exception e) {
            log.warning("Error al generar el XML: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Método para agregar un elemento con texto
    private void appendChild(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }

    // Método para agregar un elemento con espacio de nombres
    private void appendChildWithNamespace(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement("g" + ":" + tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }
}
