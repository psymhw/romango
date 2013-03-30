package test;

import javafx.scene.shape.Polygon;

class Tree extends Polygon {

        public Tree(double x, double y) {
            super(0, 0, 10, 30, -10, 30);
            this.setTranslateX(x);
            this.setTranslateY(y);
        }
    }
    