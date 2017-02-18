/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cube3d;

import Jama.Matrix;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author asilkaratas
 */
public class Cube3D extends Application
{
    private StackPane root;
    private Pane container;
    private double angle = 0;
    private double z = 3;
    
    @Override
    public void start(Stage primaryStage)
    {
        
        primaryStage.setTitle("Cube 3D");
        
        
        root = new StackPane();
        
        Scene scene = new Scene(root, 500, 500);
        scene.setCamera(new PerspectiveCamera());
        
        primaryStage.setTitle("Cube 3d");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        container = new Pane();
        
        Slider slider = new Slider(0, 360, 0);
        slider.valueProperty().addListener(new ChangeListener<Number>()
        {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                //System.out.println("valaue:" + newValue);
                angle = newValue.doubleValue() * (Math.PI / 180.0);
                drawCube();
            }
        });
        
        Slider sliderZ = new Slider(3, 10, 3);
        sliderZ.valueProperty().addListener(new ChangeListener<Number>()
        {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                //System.out.println("valaue:" + newValue);
                z = newValue.doubleValue();
                drawCube();
            }
        });
        
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(slider, sliderZ, container);
        
        root.getChildren().add(vBox);
        
        drawCube();
    }
    
    
    private void drawCube()
    {
        container.getChildren().clear();
        
        
        double[][] p1 = new double[][]{
            {-1, -1, -1, 1}
        };
        
        double[][] p2 = new double[][]{
            {1, -1, -1, 1}
        };
        
        double[][] p3 = new double[][]{
            {1, -1, 1, 1}
        };
        
        double[][] p4 = new double[][]{
            {1, 1, 1, 1}
        };
        
        double[][] p5 = new double[][]{
            {1, 1, -1, 1}
        };
        
        
        double[][] p6 = new double[][]{
            {-1, 1, -1, 1}
        };
        
        
        double[][] p7 = new double[][]{
            {-1, 1, 1, 1}
        };
        
        double[][] p8 = new double[][]{
            {-1, -1, 1, 1}
        };
        
        
        drawLine(p1, p2);
        drawLine(p1, p6);
        drawLine(p1, p8);
        
        drawLine(p2, p3);
        drawLine(p2, p5);
        
        drawLine(p3, p8);
        drawLine(p3, p4);
        
        drawLine(p4, p5);
        drawLine(p4, p7);
        
        drawLine(p5, p6);
        
        drawLine(p6, p7);
        
        drawLine(p7, p8);
    }
    
    private void drawLine(double[][] point1, double[][] point2)
    {
        double[] p1 = getProjection(point1);
        double[] p2 = getProjection(point2);
        
        Line line = new Line(p1[0], p1[1], p2[0], p2[1]);
        
        container.getChildren().add(line);
    }
    
    private double[] getProjection(double[][] point)
    {
        
        double w = 500;
        double h = 400;
        double tetha = Math.PI/4;
        
        double d = w/2 * Math.cos(tetha/2);
        double cx = w/2;
        double cy = h/2;
        
        double[][] projection = new double[][]{
            {d, 0, cx, 0},
            {0, -d, cy, 0},
            {0, 0, 0, 1},
            {0, 0, 1, 0}
        };
        
        double[][] transform = new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, z},
            {0, 0, 0, 1}
        };
        
        double[][] rotation = new double[][]{
            {Math.cos(angle), 0, -Math.sin(angle), 0},
            {0, 1, 0, 0},
            {Math.sin(angle), 0, Math.cos(angle), 0},
            {0, 0, 0, 1}
        };
        
        
        Matrix mPoint = new Matrix(point).transpose();
        Matrix mProjection = new Matrix(projection);
        Matrix mTransform = new Matrix(transform);
        Matrix mRotation = new Matrix(rotation);
        
        Matrix mResult = mProjection.times(mTransform);
        mResult = mResult.times(mRotation);
        mResult = mResult.times(mPoint);
        
        
        mResult = mResult.transpose();
        
        double[] result = mResult.getArray()[0];
        double qw = result[3];
        
        //System.out.println("qw:" + qw);
        
        mResult = mResult.times(1/qw);
        
        result = mResult.getArray()[0];
        //printPoint(result);
        
        
        return result;
    }
    
    
    private void printPoint(double[] point)
    {
        for(int i = 0; i < point.length; ++i)
        {
            System.out.print(point[i] + " ");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
    
    
}
