import spark.Service;

/**
 * The main entry point to the application. This registers all of our public API's endpoints and starts the web
 * server listening on port 8888. Because this is not a "production" implementation, the server will run until the
 * user hits the ENTER key.
 * 
 * The web server is powered by a third party library, Spark. The documentation for the library can be found at
 * http://sparkjava.com/ and it's pretty simple to pick up and use. 
 */
public class ApiMain
{

    /**
     * Sample data in order to only have to focus on UI design at the beginning
     */
    private static String sampleBook =
        "  {\n" +
        "    \"id\" : \"9780641723445\",\n" +
        "    \"cat\" : [\"book\",\"hardcover\"],\n" +
        "    \"name\" : \"The Lightning Thief\",\n" +
        "    \"author\" : \"Rick Riordan\",\n" +
        "    \"series_t\" : \"Percy Jackson and the Olympians\",\n" +
        "    \"sequence_i\" : 1,\n" +
        "    \"genre_s\" : \"fantasy\",\n" +
        "    \"inStock\" : true,\n" +
        "    \"price\" : 12.50,\n" +
        "    \"pages_i\" : 384\n" +
        "  }\n";

    private static String sampleBooks =
        "[\n" +
        "  {\n" +
        "    \"id\" : \"9780641723445\",\n" +
        "    \"cat\" : [\"book\",\"hardcover\"],\n" +
        "    \"name\" : \"The Lightning Thief\",\n" +
        "    \"author\" : \"Rick Riordan\",\n" +
        "    \"series_t\" : \"Percy Jackson and the Olympians\",\n" +
        "    \"sequence_i\" : 1,\n" +
        "    \"genre_s\" : \"fantasy\",\n" +
        "    \"inStock\" : true,\n" +
        "    \"price\" : 12.50,\n" +
        "    \"pages_i\" : 384\n" +
        "  }\n" +
        ",\n" +
        "  {\n" +
        "    \"id\" : \"9781423103349\",\n" +
        "    \"cat\" : [\"book\",\"paperback\"],\n" +
        "    \"name\" : \"The Sea of Monsters\",\n" +
        "    \"author\" : \"Rick Riordan\",\n" +
        "    \"series_t\" : \"Percy Jackson and the Olympians\",\n" +
        "    \"sequence_i\" : 2,\n" +
        "    \"genre_s\" : \"fantasy\",\n" +
        "    \"inStock\" : true,\n" +
        "    \"price\" : 6.49,\n" +
        "    \"pages_i\" : 304\n" +
        "  }\n" +
        ",\n" +
        "  {\n" +
        "    \"id\" : \"9781857995879\",\n" +
        "    \"cat\" : [\"book\",\"paperback\"],\n" +
        "    \"name\" : \"Sophie's World : The Greek Philosophers\",\n" +
        "    \"author\" : \"Jostein Gaarder\",\n" +
        "    \"sequence_i\" : 1,\n" +
        "    \"genre_s\" : \"fantasy\",\n" +
        "    \"inStock\" : true,\n" +
        "    \"price\" : 3.07,\n" +
        "    \"pages_i\" : 64\n" +
        "  }\n" +
        ",\n" +
        "  {\n" +
        "    \"id\" : \"9781933988177\",\n" +
        "    \"cat\" : [\"book\",\"paperback\"],\n" +
        "    \"name\" : \"Lucene in Action, Second Edition\",\n" +
        "    \"author\" : \"Michael McCandless\",\n" +
        "    \"sequence_i\" : 1,\n" +
        "    \"genre_s\" : \"IT\",\n" +
        "    \"inStock\" : true,\n" +
        "    \"price\" : 30.50,\n" +
        "    \"pages_i\" : 475\n" +
        "  }\n" +
        "]";

    public static void main(String[] args) throws Exception
    {
        // The mock database you can connect to
        DataStore db = new DataStore();

        Service webServer = startWebServer(8888);
        webServer.get ("/hello", (request, response) -> "Yes, the web server is running.");
        webServer.post("/hello", (request, response) -> "You posted properly, too!");

        webServer.post("/book", (request, response) -> "Creating new book");
        webServer.get("/book", (request, response) -> sampleBooks);
        webServer.get("/book/:book", (request, response) -> sampleBook);
        webServer.post("/book/:book", (request, response) -> String.format("Updating book %", request.params().get("book")));
        webServer.delete("/book:book", (request, response) -> String.format("Deleting book %", request.params().get("book")));

        System.out.println("[Api] Web server running. Press ENTER to quit.");
        System.in.read();
        System.out.println("[Api] Web server shut down. Bye bye!");
        webServer.stop();
    }

    /**
     * Factory to create/start a new web server instance running on the given port
     * @param port The port to run the server on (choose value above 1024 so you don't have to run as root)
     * @return The now-fired-up web server
     */
    private static Service startWebServer(int port)
    {
        System.out.println("[Api] Starting web server on port port " + port);
        return Service.ignite().port(port);
    }
}
