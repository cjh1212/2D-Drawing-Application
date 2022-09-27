/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{
    public ArrayList<MyShapes> myShapes = new ArrayList<>();
    public MyShapes temp;
    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    public JPanel line1, line2, topPanel;

    // create the widgets for the firstLine Panel.
    public String[] shape = {"Line", "Rectangle", "Oval"};
    public JComboBox shapes;
    public JButton color1, color2, undo, clear;
    public JLabel shapeLabel;
    
    //create the widgets for the secondLine Panel.
    public JLabel optionLabel, lineWidth, dashLength;
    public JCheckBox filled, useGradient, dashed;
    public JSpinner line, dash;

    // Variables for drawPanel.
    public DrawPanel drawPanel;
    

    // add status label
    public JLabel statusLabel;
    
    
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        line1 = new JPanel();
        line2 = new JPanel();
        topPanel =  new JPanel();
        
        shapes = new JComboBox(shape);
        color1 = new JButton("1st Color...");
        color2 = new JButton("2nd COlor...");
        undo = new JButton("Undo");
        clear = new JButton("Clear");
        shapeLabel = new JLabel("Shape: ");
        
        optionLabel = new JLabel("Options: ");
        lineWidth = new JLabel("Line Width: ");
        dashLength = new JLabel("Dash Length: ");
        filled = new JCheckBox("Filled");
        useGradient = new JCheckBox("Use Gradient");
        dashed = new JCheckBox("Dashed");
    
        SpinnerModel value1 = new SpinnerNumberModel(5,0,100,1);
        SpinnerModel value2 = new SpinnerNumberModel(5,0,100,1);
        dash = new JSpinner(value1);
        line = new JSpinner(value2);
        
        // add widgets to panels
        line1.add(shapeLabel);
        line1.add(shapes);
        line1.add(color1);
        line1.add(color2);
        line1.add(undo);
        line1.add(clear);
        
        line2.add(optionLabel);
        line2.add(filled);
        line2.add(useGradient);
        line2.add(dashed);
        line2.add(lineWidth);
        line2.add(line);
        line2.add(dashLength);
        line2.add(dash);
        
        drawPanel = new DrawPanel();
        statusLabel = new JLabel(" ");

        // add top panel of two panels
        GridLayout topPanelLayout = new GridLayout(2,0);
        topPanel.setLayout(topPanelLayout);
        topPanel.add(line1);
        topPanel.add(line2);
        drawPanel.setBackground(Color.white);

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        this.add(topPanel, BorderLayout.NORTH);
        this.add(drawPanel, BorderLayout.CENTER);
        this.add(statusLabel, BorderLayout.SOUTH);
        
        //add listeners and event handlers
        color1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                color1.setBackground(JColorChooser.showDialog(null,"Choose a color", Color.BLACK));
            }
        });

        color2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                color2.setBackground(JColorChooser.showDialog(null, "Choose a color", Color.white));
            }
        });
        
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.removeAll();
                myShapes.clear();
                drawPanel.repaint();
            }
        });
        
        undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (myShapes.isEmpty()==false){
                    myShapes.remove(myShapes.size()-1);
                    drawPanel.repaint();
                }
            }
        
        });

    }

    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        

        public DrawPanel()
        {
            this.addMouseListener(new MouseHandler());
            this.addMouseMotionListener(new MouseHandler());
            
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            //loop through and draw each shape in the shapes arraylist
            for (MyShapes s: myShapes){
                s.draw(g2d);
            }

        }


    public class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
                Stroke stroke;
                Paint paint; 
                String selected = shapes.getSelectedItem().toString();
                if (dashed.isSelected()){
                    stroke = new BasicStroke((int) line.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10 ,new float[]{((Integer) dash.getValue()).floatValue()}, 0);
                } else{
                    stroke = new BasicStroke((int) line.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                if (useGradient.isSelected()){
                    paint = new GradientPaint(0, 0, color2.getBackground(), 50, 50, color1.getBackground(), true);
                }   else{
                    paint = new GradientPaint(0, 0, color2.getBackground(), 50, 50, color1.getBackground(), false);
                }
                if ("Line".equals(selected)) {
                    temp = new MyLine(event.getPoint(), event.getPoint(), paint, stroke);
                    myShapes.add(temp);
                    System.out.println(myShapes);
                    System.out.println(temp);
                }else if ("Rectangle".equals(selected)) {
                    temp = new MyRectangle(event.getPoint(), event.getPoint(), paint, stroke, filled.isSelected());
                    myShapes.add(temp);
                }else if ("Oval".equals(selected)) {
                    temp = new MyOval(event.getPoint(), event.getPoint(), paint, stroke, filled.isSelected());
                    myShapes.add(temp);
                }

            }

            public void mouseReleased(MouseEvent event)
            {
                temp.setEndPoint(event.getPoint());
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                statusLabel.setText(String.format("(%d, %d)", event.getX(), event.getY()));
                temp.setEndPoint(event.getPoint());
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText(String.format("(%d, %d)", event.getX(), event.getY()));
            }
        }

    }}

