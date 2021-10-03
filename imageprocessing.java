//this program is to get the graph of relation between time and no_of_threads.

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.*;

public class imageprocessing {
    static int width;
    static int height;
    static int no_of_grids = 50;
    static List<Integer> no_of_threads = new ArrayList<>();
    static List<Long> time_of_execution = new ArrayList<>();
    //n^2 threads will be created for n grids .
    //this program creates a graph of thread vs time  and returns the negetive image 
    //calculated using ("no_of_grids" * no_of_grids) number of grids .

    public static void main(String args[]) throws IOException, InterruptedException {
        File f = null;
        // read image
        read_file();
        // Get image width and height
        width = read_file().getWidth();
        height = read_file().getHeight();
        ArrayList<Thread> th ;
        for (int grid = 1; grid <= no_of_grids; grid++) {

            long start_time = System.currentTimeMillis();

            BufferedImage tempimage = read_file();
            th = new ArrayList<>();
            for (int i = 0; i < grid; i++) {
                for (int j = 0; j < grid; j++) {
                    Thread object = new Thread(new MultithreadingDemo(i, j, height, width, tempimage, grid));
                    th.add(object);// Inbuilt Function from arraylist Package
                    object.start();// Runs “RUN” Function in the thread
                }
            }
            for (Thread thread : th) {
                thread.join();
            }
            // write image
            if(grid == no_of_grids)
                write_file(grid, tempimage);

            long end_time = System.currentTimeMillis();
            time_of_execution.add(end_time-start_time);
            no_of_threads.add(grid*grid);            
        }
        System.out.println(no_of_threads);
        System.out.println(time_of_execution);     
    }

    //function which reads the file and returns buffer image.
    static BufferedImage read_file(){
        try {
            // file where your image resides  
            File f = new File("E:\\Inp.jpg");//set your input path here  
            BufferedImage img = ImageIO.read(f);  
            return img;          
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    //function which writes the file .
    static void write_file(int grid ,BufferedImage tempimage){
        try {
             
            // set your output path here 
            File f = new File("E:\\Out using "+grid+"X"+grid+".jpg");// path of the output file
            ImageIO.write(tempimage, "jpg", f);
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}

class MultithreadingDemo implements Runnable {
    int i, j, height, width, no_of_threads;
    BufferedImage img;

    public MultithreadingDemo(int i, int j, int height, int width, BufferedImage img, int no_of_threads) {
        this.i = i;
        this.j = j;
        this.height = height;
        this.width = width;
        this.img = img;
        this.no_of_threads = no_of_threads;
    }

    @Override
    public void run() {
        int x = 0, y = 0;
        try {
            for (x = (int) (i * (float) (width / ((double) no_of_threads))); x < (int) ((i + 1)
                    * (float) (width / ((double) no_of_threads))); x++) {
                for (y = (int) (j * (float) (height / ((double) no_of_threads))); y < (int) ((j + 1)
                        * (float) (height / ((double) no_of_threads))); y++) {

                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    // subtract RGB from 255
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    // set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }
        } catch (Exception e) {
            // Throwing an exception
            e.printStackTrace();
        }
    }

}

