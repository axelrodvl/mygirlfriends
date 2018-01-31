package co.axelrod.mygirlfriends.spark;

import co.axelrod.mygirlfriends.Main;
import co.axelrod.mygirlfriends.nsfw.NSFWWrapper;
import co.axelrod.mygirlfriends.util.Sorter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.axelrod.mygirlfriends.util.FileUtil.SORTED_RESULTS_FILE_PATH;
import static co.axelrod.mygirlfriends.util.FileUtil.UNSORTED_RESULTS_FILE_PATH;
import static co.axelrod.mygirlfriends.util.FileUtil.getStringFromFile;
import static co.axelrod.mygirlfriends.vk.VKUtils.getHighResURL;
import static spark.Spark.halt;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 31.01.2018.
 */
public class Routes {
    private String domain;
    private NSFWWrapper nsfwService;

    public Routes(String domain) {
        this.domain = domain;
        setupRoutes();
        nsfwService = new NSFWWrapper(domain);
    }

    private void setupRoutes() {
        final Configuration configuration = new Configuration();
            configuration.setClassForTemplateLoading
                    (Main.class, "/");

            Spark.get("/", new Route() {
            public Object handle(final Request request,
            final Response response) {
                response.redirect("https://oauth.vk.com/authorize?client_id=6348349&display=page&redirect_uri=http://" + domain + ":4567/auth&scope=photos&response_type=code&v=5.71");
                return response;
            }
        });

            Spark.get("/auth", new Route() {
            public Object handle(final Request request,
            final Response response) {

                String code = request.queryParams("code");
                System.out.println("Code retrieved from VK API: " + code);
                nsfwService.invokeScript(code);

                response.redirect("http://" + domain + ":4567/results");

                return response;
            }
        });

            Spark.get("/results", new Route() {
            public Object handle(final Request request,
            final Response response) {
                System.out.println("Opened results page");

                System.out.println("Sorting results");
                Sorter.sortResults(UNSORTED_RESULTS_FILE_PATH, SORTED_RESULTS_FILE_PATH);

                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");

                    Map<String, Object> helloMap = new HashMap<String, Object>();
                    helloMap.put("name", "Freemarker");

                    List<String> results = new ArrayList<>();

                    System.out.println("Started parsing of " + SORTED_RESULTS_FILE_PATH);
                    InputStream is = new FileInputStream(SORTED_RESULTS_FILE_PATH);
                    BufferedReader buf = new BufferedReader(new InputStreamReader(is));

                    String line = buf.readLine();

                    while(line != null) {
                        line = line.substring(6);
                        String userId = line.split("/")[0];

                        String imageId = line.split("/")[1].substring(0, line.split("/")[1].length() - 4)
                                // Removing likes count
                                .split("_")[0];

                        String imagesJSON = getStringFromFile("users/" + userId + ".json");
                        String highResURL = getHighResURL(imagesJSON, imageId);
                        System.out.println(highResURL);
                        if(highResURL != null) {
                            results.add(highResURL);
                        }
                        line = buf.readLine();
                    }

                    System.out.println("Parsing completed, showing page");
                    helloMap.put("results", results);
                    helloTemplate.process(helloMap, writer);
                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }
                return writer;
            }
        });
    }
}
