package tutorial;
	 

import javafx.scene.paint.Color;
	 
	/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {
    public Missile(Color fill) {
       // super(5, fill, false);
        super(5, fill);
    }
 
    public Missile(int radius, Color fill) {
    //    super(radius, fill, true);
        super(radius, fill);
    }
}