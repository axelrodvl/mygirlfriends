package co.axelrod.webnsfw;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;

import static spark.Spark.halt;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 27.01.2018.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String domain = args[0];
        //String domain = "localhost";

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
                invokeScript(domain, code);

                response.redirect("http://" + domain + ":4567/results");

                return response;
            }
        });

        Spark.get("/results", new Route() {
            public Object handle(final Request request,
                                 final Response response) {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");

                    Map<String, Object> helloMap = new HashMap<String, Object>();
                    helloMap.put("name", "Freemarker");

                    List<String> results = new ArrayList<>();

                    InputStream is = new FileInputStream("sortedResults.txt");
                    BufferedReader buf = new BufferedReader(new InputStreamReader(is));

                    String line = buf.readLine();

                    Integer count = 100;
                    while(line != null || count > 0) {
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
                            count--;
                        }
                        line = buf.readLine();
                    }

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

    public static void invokeScript(String domain, String code) {
        try {
            System.out.println("Domain: " + code);
            System.out.println("Code: " + code);

            String[] cmds = {
                    "/bin/zsh", "/Users/vadim/work/nsfw/open_nsfw/runForFiles.sh", domain, code};

            Process p = Runtime.getRuntime().exec(cmds);

            while(p.isAlive()) {
                // Grab output and print to display
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            System.out.println ("Bash script running is done");
        }
        catch (Exception e) {
            System.out.println ("Err: " + e.getMessage());
        }
    }

    public static String getStringFromFile(String pathToFile) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(pathToFile));
        String outputData = new String(encoded, "UTF-8");
        return outputData;
    }

    private static String getHighResURL(String response, String imageId) throws Exception {
        JsonElement photosJson = new JsonParser().parse(response).getAsJsonObject().get("response");

        JsonArray photosArray = photosJson.getAsJsonObject().get("items").getAsJsonArray();

        for(JsonElement photo : photosArray) {
            if(photo.getAsJsonObject().get("id").getAsString().equals(imageId)) {
                JsonArray sizes = photo.getAsJsonObject().get("sizes").getAsJsonArray();
                for(JsonElement size : sizes) {
                    if(size.getAsJsonObject().get("type").getAsString().equals("z")) {
                        return size.getAsJsonObject().get("src").getAsString();
                    }
                }
            }
        }

        return null;
    }
}
