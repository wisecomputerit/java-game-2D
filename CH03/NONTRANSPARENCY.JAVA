import java.awt.*;
import java.applet.*;

public class NonTransparency extends Applet
{
   Image I;

   public void init()
   {
      setBackground(Color.black);
      I = getImage(getCodeBase(),"Images/1.jpg");
   }

   public void paint(Graphics g)
   {
      g.drawImage(I,25,25,150,150,this);
   }
}