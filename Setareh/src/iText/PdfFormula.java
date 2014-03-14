package iText;

import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

public class PdfFormula extends Phrase
{
  private String formula;
  private ArrayList<Element> parts = new ArrayList<Element>();
  private float rise=-5;
  
  public PdfFormula(String formula, Font elementFont, Font countFont)
  {
    this.formula=formula;
    parseFormula();
    Element e;
    
    for(int i=0; i<parts.size(); i++)
    {
      e = parts.get(i);
      e.element.setFont(elementFont);
      e.elementCount.setFont(countFont);
      e.elementCount.setTextRise(rise);
      
      add(e.element);
      if (!e.singlet) add(e.elementCount);
    }
  }
  
  private void parseFormula()
  {
    Element e;
    final int CHARACTER=0;
    final int INTEGER=1;
    
    char elementChar=0;
    StringBuffer countBuff = new StringBuffer();
    
    boolean first=true;
    int mode = CHARACTER;
    
    if (formula.equals(""))  //Check for empty string.
      return;
    if (!Character.isLetterOrDigit(formula.charAt(0)))//For those stupid approximate weights stuck in mol_formulas!
      return;
    
    for (int i=0; i<formula.length(); i++)
    {
      if (Character.isDigit(formula.charAt(i)))
      {
        countBuff.append(formula.charAt(i));
      }
      else
      {
        if (!first) 
        {
          parts.add(new Element(elementChar, countBuff.toString()));
          countBuff = new StringBuffer();
        }
        elementChar=formula.charAt(i);
        first=false;
      }
    }
    
    parts.add(new Element(elementChar, countBuff.toString()));
  }
  
 
  private class Element
  {
    public Chunk element;
    public boolean singlet=false;
    public Chunk elementCount;
    public Element(char elementChar, String countStr)
    {
      element = new Chunk(elementChar);
      elementCount = new Chunk(countStr);
      if ("".equals(elementCount)) singlet=true;
    }
    
    
  }
  
}
