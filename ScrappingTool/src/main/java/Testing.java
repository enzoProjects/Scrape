import com.google.gson.Gson;
import org.jsoup.nodes.Document;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by springfield on 6/27/17.
 */
public class Testing {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Document doc = ScrappingTool.getInstance().fromURLtoDocument("https://www.just-eat.ca/area/m5v-toronto/?lat=43.6463603&long=-79.396254");
        List<Restaurant> result = ScrappingTool.getInstance().scrap(doc);
        String json = new Gson().toJson(result);
        PrintWriter writer = new PrintWriter("jsonresult.json", "UTF-8");
        writer.println(json);
    }
}
