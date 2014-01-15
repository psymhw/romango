package tutorial;

import junit.framework.TestCase;
import com.opensymphony.xwork2.ActionSupport;
public class HelloActionTest extends TestCase
{
  public void testhelloAction() throws Exception
  {
    HelloAction index = new HelloAction();
    String result = index.execute();
    assertTrue("Expected a success result!", ActionSupport.SUCCESS.equals(result));
    assertTrue("Expected the default message!", HelloAction.MESSAGE.equals(index.getMessage()));
    }
}