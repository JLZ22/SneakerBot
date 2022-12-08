import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SneakerBot {
    public static void main(String[] args) throws IOException, InterruptedException {
        // The URL of the sneaker release page
        URL url = new URL("https://www.nike.com/w/new-mens-3n82yznik1");

        // Check the release page every minute
        while (true) {
            // Open a connection to the release page
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method and timeouts
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // Send the request and get the response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();

            // Check if the sneaker is released
            if (response.contains("Air Jordan 12 Retro") &&
                    response.contains("In Stock")) {
                // The sneaker is released and in stock, so buy it!
                buySneaker();
            }

            // Sleep for one minute before checking again
            Thread.sleep(60000);
        }
    }

    public static void buySneaker() throws IOException {
        // The URL of the sneaker page
        URL url = new URL("https://www.nike.com/w/new-mens-3n82yznik1");

        // Open a connection to the sneaker page
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the request method and timeouts
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        // Send the request and get the response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();

        // Extract the form data from the response
        String formData = extractFormData(response);

        // The URL of the add to cart endpoint
        URL addToCartUrl = new URL("https://www.nike.com/cart/add");

        // Open a connection to the add to cart endpoint
        conn = (HttpURLConnection) addToCartUrl.openConnection();

        // Set the request method, request properties, and timeouts
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(formData.length()));
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        // Send the form data to the add to cart endpoint
        OutputStream out = conn.getOutputStream();
        out.write(formData.getBytes());
        out.flush();
        out.close();

        // Check the response code
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Sneaker added to cart!");
        } else {
            System.out.println("Failed to add sneaker to cart.");
        }
    }

    public static String extractFormData(String html) throws UnsupportedEncodingException {
        // The form data string
        StringBuilder formData = new StringBuilder();

        // Use a regular expression to find the form fields in the HTML
        Pattern pattern = Pattern.compile("<input[^>]*name=\"([^\"]*)\"[^>]*value=\"([^\"]*)\"[^>]*>");
        Matcher matcher = pattern.matcher(html);

        // Iterate over the form fields
        while (matcher.find()) {
            // Get the name and value of the form field
            String name = matcher.group(1);
            String value = matcher.group(2);

            // Append the name and value to the form data string
            formData.append(name);
            formData.append("=");
            formData.append(URLEncoder.encode(value, "UTF-8"));
            formData.append("&");
        }

        // Remove the trailing '&' character from the form data string
        formData.setLength(formData.length() - 1);

        // Return the form data string
        return formData.toString();
    }
}
