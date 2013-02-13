package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class AmazonTest  extends HttpServlet 
{
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "143NFGH5PMFA19K7AJG2";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "cbN644Mg7hQHBOxUKTnnrPV4hVCWq7+PozgLE1dj";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
  // private static final String ITEM_ID = "0545010225"; // harry potter
    private static final String ITEM_ID = "B002HH4CFI";

	public void service(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException
    {
      ServletOutputStream out = response.getOutputStream();
      response.setContentType("text/html");
      String mode="whatever";
      
      out.println("<h1>Amazon Test</h1>");
      
      /*
       * Set up the signed requests helper 
       */
      SignedRequestsHelper helper;
      try {
          helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
      } catch (Exception e) {
          e.printStackTrace();
          return;
      }
      
      String requestUrl = null;
      String title = null;
      String detailPageUrl = null;
      String smallImageUrl = null;

      /* The helper can sign requests in two forms - map form and string form */
      
      /*
       * Here is an example in map form, where the request parameters are stored in a map.
       */
      System.out.println("Map form example:");
      Map<String, String> params = new HashMap<String, String>();
      params.put("Service", "AWSECommerceService");
      params.put("Version", "2009-03-31");
      params.put("Operation", "ItemLookup");
      params.put("ItemId", ITEM_ID);
     // params.put("ResponseGroup", "Small,Images");
      params.put("ResponseGroup", "Medium,Offers");
      params.put("AssociateTag", "eugeargetang-20");

      requestUrl = helper.sign(params);
      out.println("Signed Request is \"" + requestUrl + "\"<br><br>");

     // title = fetchTitle(requestUrl);
      
      try {
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          DocumentBuilder db = dbf.newDocumentBuilder();
          Document doc = db.parse(requestUrl);
       //   Node titleNode = doc.getElementsByTagName("Title").item(0);
       //   title = titleNode.getTextContent();
          
          Node detailPageUrlNode = doc.getElementsByTagName("DetailPageURL").item(0);
          detailPageUrl = detailPageUrlNode.getTextContent();
          String imageUrl= null;
          /*
          Node smallImageNode = doc.getElementsByTagName("SmallImage").item(0);
          smallImageUrl = smallImageNode.getTextContent();
          int dotPos = smallImageUrl.lastIndexOf(".jpg");
          smallImageUrl=smallImageUrl.substring(0,(dotPos+4));
          */
          imageUrl = getImageElement(doc, "SmallImage");
          out.println("Small Image text content: "+imageUrl+ "<br><br>");
          out.println("<img src=\""+imageUrl+"\"/><br>");
          
          imageUrl = getImageElement(doc, "MediumImage");
          out.println("Small Image text content: "+imageUrl+ "<br><br>");
          out.println("<img src=\""+imageUrl+"\"/><br>");
          
          out.println("<b>Title:</b> " + getTextElement(doc, "Title") + "<br>");
          System.out.println(getTextElement(doc, "Title"));
          out.println("<b>Artist:</b> "+getTextElement(doc, "Artist")+ "<br>");
          out.println("<b>Author:</b> "+getTextElement(doc, "Author")+ "<br>");
          out.println("<b>Price:</b> "+getTextElement(doc, "FormattedPrice")+ "<br>");
          out.println("<b>Availability:</b> "+getTextElement(doc, "Availability")+ "<br>");
          out.println("<b>Publisher:</b> "+getTextElement(doc, "Publisher")+ "<br>");
          out.println("<b>Publication Date:</b> "+getTextElement(doc, "PublicationDate")+ "<br>");
        
      } catch (Exception e) {
    	  e.printStackTrace();
          //throw new RuntimeException(e);
      }
      
     
      out.println("<br><br>");
     
      out.println("Detail Page URL is: " + detailPageUrl + "<br><br>");
      
    //  out.println("Medium Image is: " + mediumImageNode + "<br><br>");
  
      
    }
	
	/*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static String fetchTitle(String requestUrl) {
        String title = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            Node titleNode = doc.getElementsByTagName("Title").item(0);
            title = titleNode.getTextContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
    }
    
    private static String getImageElement(Document doc, String name)
    {
    	String result = null;
    	 Node imageNode = doc.getElementsByTagName(name).item(0);
         result = imageNode.getTextContent();
         int dotPos = result.lastIndexOf(".jpg");
         if (dotPos>4) result=result.substring(0,(dotPos+4));
         return result;
    }
    
    private static String getTextElement(Document doc, String name)
    {
    	String result = "not found";
  
    	try
    	{
    	
    	 Node node = doc.getElementsByTagName(name).item(0);
         result = node.getTextContent();
    	} catch (Exception e) { System.out.println("getTextElement error: "+name);}
         return result;
    }

}
