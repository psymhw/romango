package test;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

   /* a cloud is like an ellipse */
    class Cloud extends Ellipse {

        public Cloud(double centerX, double centerY) {
            super(0, 0, 50, 25);
            this.setTranslateX(centerX);
            this.setTranslateY(centerY);
            this.setFill(Color.WHITESMOKE);
            this.setOpacity(0.5);
        }
    }