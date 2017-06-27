import org.jsoup.nodes.Document;

/**
 * Created by springfield on 6/27/17.
 */
public class RestaurantThread implements Runnable {
    public Restaurant restaurant;

    public RestaurantThread(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void run() {
        Document doc = ScrappingTool.getInstance().fromURLtoDocument(restaurant.restaurantUrl);
        ScrappingTool.getInstance().scrapRestaurantInside(doc, restaurant);
    }
}
