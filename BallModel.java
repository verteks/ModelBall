import java.applet.Applet;
import java.awt.*;

/** An applet that displays a simple animation */
public class BallModel extends Applet implements Runnable {

    Thread animator; // The thread that performs the animation0
    Thread test;
    volatile boolean pleaseStop; // A flag to ask the thread to stop

    int t =100; // время анимации
    int Dt=500;// период фиксирования данных
    int ti=Dt;

    Ball ball;

    private class Ball {
        private double x, y, dx, dy, g, r, k,tt;

        public Ball(double x, double y, double r, double k) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.k = k;

        }

        public void setDxDy(double Dx, double Dy) {
            this.dx = Dx;
            this.dy = Dy;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getR() {
            return r;
        }

        public void setG(double g) {
            this.g = g;
        }


        /**
         * This method simply draws the circle at its current position
         */


        /**
         * This method moves (and bounces) the circle and then requests a redraw.
         * The animator thread calls this method periodically.
         */
        void animate(double t) {
            tt=tt+t/1000;
            // Bounce if we've hit an edge.
            Rectangle bounds = getBounds();
            // Определение столкновений
            if ((x - r + dx < 0) || (x + r + dx*(t/1000) > bounds.width))
                dx = -dx*k;
            if ((y - r + dy < 0) || (y + r + dy*(t/1000) > bounds.height))
                dy = -dy*k;

            // двигаем шар.
            if (y + r > bounds.height){y=bounds.height-r;}else{y += (dy+g)*(t/1000);};
            x += dx*(t/1000);

            dy+=g*t/1000;
            System.out.println(tt+": ("+x+";"+y+")->("+dx+";"+dy+")");
            // Просим перерисовки
            repaint();
        }
    }

    public void paint(Graphics g) {
        double x,y,r;
        x=ball.getX();
        y=ball.getY();
        r=ball.getR();
        g.setColor(Color.red);
        g.fillOval((int) (x - r),(int)(y - r), (int)(r * 2), (int)(r * 2));

    }
    /**
     * This method is from the Runnable interface. It is the body of the thread
     * that performs the animation. The thread itself is created and started in
     * the start() method.
     */
    public void run() {
        while (!pleaseStop) { // Loop until we're asked to stop


            ball.animate(t); // Update and request redraw

            try {
                Thread.sleep(t);
            } // Wait t milliseconds
            catch (InterruptedException e) {
            } // Ignore interruptions
        }
    }

    /** Start animating when the browser starts the applet */
    public void start() {

        setSize(400,400);
        animator = new Thread(this); // Create a thread
        animator.setName("A");
        //test = new Thread(this); // Create a thread
        //test.setName("test");
        pleaseStop = false; // Don't ask it to stop now
        animator.start(); // Start the thread.
        //test.start(); // Start the thread.
        // The thread that called start now returns to its caller.
        // Meanwhile, the new animator thread has called the run() method
    }

    /** Stop animating when the browser stops the applet */
    public void stop() {
        // Set the flag that causes the run() method to end
        pleaseStop = true;
    }
    public void init() {
        ball =new Ball(10,10,10,0.5);
        ball.setDxDy(40,40);
        ball.setG(9.8);
    }
}
