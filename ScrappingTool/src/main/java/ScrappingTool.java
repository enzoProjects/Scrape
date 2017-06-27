
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by springfield on 6/27/17.
 */
public class ScrappingTool {
    public static ScrappingTool instance = null;

    public static ScrappingTool getInstance() {
        if (instance == null) instance = new ScrappingTool();
        return instance;
    }

    private ScrappingTool() {

    }

    public List<Restaurant> scrap(Document doc) {
        List<Restaurant> result = new ArrayList<Restaurant>();
        Elements restaurants = doc.getElementsByAttributeValue("itemtype", "http://schema.org/Restaurant");
        for (Element restaurant: restaurants) {
            Elements textInfo = restaurant.getElementsByClass("infoText");
            Element header = restaurant.getElementsByTag("header").get(0);
            Restaurant rest = new Restaurant();
            rest.name = header.text();
            rest.type = textInfo.get(0).text();
            rest.address = textInfo.get(1).text();
                try {
                    rest.deliveryFee = Integer.parseInt(textInfo.get(2).text().replaceAll("[^0-9]", ""));
                } catch (Exception ex) {
                    rest.deliveryFee = 0;
                }
                try {
                    rest.minDelivery = Integer.parseInt(textInfo.get(3).text().replaceAll("[^0-9]", ""));
                } catch (Exception ex) {
                    rest.minDelivery = 0;
                }
                if (restaurant.getElementsByAttributeValue("data-test-id","sponsored").size() > 0) {
                    rest.sponsored = true;
                }
                String restaurantUrl = restaurant.childNode(1).attr("href");
                //scrapRestaurantInside(fromURLtoDocument("https://www.just-eat.ca" + restaurantUrl), rest);
                rest.restaurantUrl = "https://www.just-eat.ca" + restaurantUrl;
                Runnable run = new RestaurantThread(rest);
                Thread thread = new Thread(run);
                thread.run();
                result.add(rest);

        }
        return result;

    }

    public void scrapRestaurantInside(Document doc, Restaurant rest) {
        Elements categories = doc.getElementsByClass("menuCard-category");
        Map<String, List> menu = new HashMap<String, List>();
        for(Element category : categories) {
            String title = category.getElementsByClass("menuCard-category-title").get(0).text();
            Elements products = category.getElementsByClass("menu-product");
            List<Product> categoryProducts = new ArrayList<Product>();
            for(Element prod: products) {
                try {
                    Product product = new Product();
                    product.name = prod.getElementsByClass("product-title").get(0).text();
                    product.description = prod.getElementsByClass("product-description").get(0).text();
                    product.price = Integer.parseInt(prod.getElementsByClass("product-price").get(0).text().replaceAll("[^0-9]", ""));
                    categoryProducts.add(product);
                } catch (Exception ex) {
                    continue;
                }

            }
            menu.put(title, categoryProducts);
        }
        rest.menu = menu;

    }

    public Document fromURLtoDocument(String sUrl) {
        try {
            Document doc = Jsoup.connect(sUrl).timeout(0).get();
            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
