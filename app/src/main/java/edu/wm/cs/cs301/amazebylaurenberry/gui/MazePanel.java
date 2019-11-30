package edu.wm.cs.cs301.amazebylaurenberry.gui;

import android.view.View;
import android.util.Log;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;



import edu.wm.cs.cs301.amazebylaurenberry.R;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
//import edu.wm.cs.cs301.amazebylaurenberry.generation.MazeHolder;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Wall;


public class MazePanel extends View {
	private static final String TAG = "Logs";

	private Paint paint = new Paint();
	private Canvas noteTaker = new Canvas();
	private Canvas mainCanvas = new Canvas();
	private Bitmap noteBitMap;

	private Bitmap bitmap1;
	private BitmapShader shader1;

	private Bitmap bitmap2 ;
	private BitmapShader shader2 ;




	public MazePanel(Context context,AttributeSet attrs) {
		super(context,attrs);
		init(attrs,0);

	}
	public MazePanel (Context context){
		super(context);
		init(null,0);
	}
	public MazePanel(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs);
		init(attrs,defStyle);
	}

	private void init(AttributeSet attrs, int defStyle){
		paint.setColor(Color.RED);
		noteBitMap = Bitmap.createBitmap(4000, 4000, Bitmap.Config.ARGB_8888);
		noteTaker.setBitmap(noteBitMap);

		bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.TEMPPP);
		shader1 = new BitmapShader(bitmap1, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

		bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.TEMPPP);
		shader2 = new BitmapShader(bitmap2, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);


	//	Maze maze  = MazeHolder.getData();

		//figure out how to modify^^^

	}

	/**
	 *
	 *  g used to be Graphics g
	 */
	public void update(Canvas c) {
		//paint(g);
		paint(c);
	}

	/**
	 * Method to draw the buffer image on a graphics object that is
	 * obtained from the superclass.
	 * Warning: do not override getGraphics() or drawing might fail.
	 */
	public void update() {
		// paint(getGraphics());
		Log.v(TAG, "Called update method");
		paint(mainCanvas);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mainCanvas = canvas;
		Log.v(TAG, "Called onDraw method");
		mainCanvas.drawBitmap(noteBitMap,0,0, paint);


	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.v(TAG, "Called onMeasure method");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}




	/**
	 * Draws the buffer image to the given graphics object.
	 * This method is called when this panel should redraw itself.
	 * The given graphics object is the one that actually shows 
	 * on the screen.
	 */
	public void paint(Canvas c) {
		/*if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
		}
		else {
			g.drawImage(bufferImage,0,0,null);
		}*/

		if (null == c) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
		}
		else {
			this.invalidate();
		}
	}

	/**
	 * Obtains a graphics object that can be used for drawing.
	 * This MazePanel object internally stores the graphics object 
	 * and will return the same graphics object over multiple method calls. 
	 * The graphics object acts like a notepad where all clients draw 
	 * on to store their contribution to the overall image that is to be
	 * delivered later.
	 * To make the drawing visible on screen, one needs to trigger 
	 * a call of the paint method, which happens 
	 * when calling the update method. 
	 * @return graphics object to draw on, null if impossible to obtain image
	 */
	public Canvas getBufferGraphics() {
		/*// if necessary instantiate and store a graphics object for later use
		if (null == graphics) {
			if (null == bufferImage) {
				bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
				if (null == bufferImage)
				{
					System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
					return null; // still no buffer image, give up
				}
			}
			graphics = (Graphics2D) bufferImage.getGraphics();
			if (null == graphics) {
				System.out.println("Error: creation of graphics for buffered image failed, presumedly container not displayable");
			}
			else {
				// System.out.println("MazePanel: Using Rendering Hint");
				// For drawing in FirstPersonDrawer, setting rendering hint
				// became necessary when lines of polygons
				// that were not horizontal or vertical looked ragged
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			}
		}
		return graphics;*/

		return noteTaker;
	}


	/// everything below refactored in proj5 ///
	
	/**
	 * Method sets the color in the refactored classes for proj5.
	 */
	public void setColor(String color) {
		/*if (color == "White") {
			graphics.setColor(Color.white);
		}
		if (color == "Gray") {
			graphics.setColor(Color.gray);
		}
		if (color == "Yellow") {
			graphics.setColor(Color.yellow);
		}
		if (color == "Red") {
			graphics.setColor(Color.red);
		}*/

		if (color == "White") {
			paint.setColor(Color.WHITE);
		}
		if (color == "Gray") {
			paint.setColor(Color.GRAY);
		}
		if (color == "Yellow") {
			paint.setColor(Color.YELLOW);
		}
		if (color == "Red") {
			paint.setColor(Color.RED);
		}
	}
	
	
	/**
	 * Replaced the draw method in FirstPersonView. Much of the original
	 * draw method was copied over to MazePanel to reduce AWT dependency 
	 * in the FirstPersonView class.
	 * 
	 * @param width
	 * @param height
	 */
	public void FirstPersonViewDraw (int width, int height) {
		/*Graphics g = getBufferGraphics() ;

		if (null == g) {
            System.out.println("FirstPersonDrawer.draw: can't get graphics object to draw on, skipping redraw operation") ;
            return;
        }
        graphics = (Graphics2D) g ;

        drawBackground(g, width, height);
        g.setColor(Color.white);*/

		Canvas g = getBufferGraphics() ;
		
		if (null == g) {
            System.out.println("FirstPersonDrawer.draw: can't get graphics object to draw on, skipping redraw operation") ;
            return;
        }
        noteTaker = (Canvas) g ;
        
        drawBackground(g, width, height);
        setColor("White");
	}



	public void setColorDrawWall(Wall wall) {
		//graphics.setColor(wall.getPanel().getColor());

		paint.setColor(Color.BLUE);
		paint.setShader(shader1);

	}
	


	private void drawBackground(Canvas canvas, int width, int height) {
		/*graphics.setColor(Color.black);
		graphics.fillRect(0, 0, width, height/2);
		graphics.setColor(Color.darkGray);
		graphics.fillRect(0, height/2, width, height/2);
		 */

		paint.setShader(shader2);
		canvas.drawRect(0, 0, width, height/2, paint);
		paint.setShader(null);
		// grey rectangle in lower half of screen
		setColor("black");
		canvas.drawRect(0,height/2, width, height, paint);
	}
	
	
	/**
	 * Called in the drawPolygon method of the FirstPersonView class. Reduces
	 * the dependency of the class on AWT.
	 */
	public void fillPolygon(int[] x, int[]y, int z) {
		//graphics.fillPolygon(x, y, z);

		Path path = new Path();
		path.reset(); // only needed when reusing this path for a new build
		path.moveTo(x[0], y[0]);
		for (int n = 1; n < z; n++) {
			path.lineTo(x[n], y[n]);
		}
		path.lineTo(x[0], y[0]);
		noteTaker.drawPath(path, paint);
	}
	
	
	/**
	 * Graphics code taken from the draw method of the Map class and put in 
	 * MazePanel to reduce AWT dependency.
	 */
	public void mapDraw() {
		/*Graphics g = getBufferGraphics() ;
        // viewers draw on the buffer graphics
        if (null == g) {
            System.out.println("MapDrawer.draw: can't get graphics object to draw on, skipping draw operation") ;
            return;
        }
        graphics = (Graphics2D) g ;*/

		Canvas c = getBufferGraphics() ;
		if (null == c) {
			System.out.println("MapDrawer.draw: can't get graphics object to draw on, skipping draw operation") ;
			return;
		}
		noteTaker = c ;
	}
	
	
	/**
	 * Calls the drawLine method from Graphics to draw the line through the
	 * MazePanel class to reduce the AWT dependency of the Map class.
	 */
	public void drawLine(int startX, int startY, int i, int startY2) {
		//graphics.drawLine(startX, startY, i, startY2);

		noteTaker.drawLine(startX, startY, i, startY2, paint);
	}

	
	/**
	 * Calls the fillOval method from Graphics to fill an oval through the
	 * MazePanel class to reduce the AWT dependency of the Map class.
	 */
	public void fillOval(int i, int j, int diameter, int diameter2) {
		//graphics.fillOval(i, j, diameter, diameter2);

		noteTaker.drawOval(i, j, i+diameter, j+diameter2, paint);
	}
	
	
	/**
	 * Following code taken from Wall class to reduce its AWT dependency.
	 */
    private static final int RGB_DEF = 20;
    private Color col;
    private int x; private int y; private int dx; private int dy; private int dist;
    private boolean partition; private boolean seen;
    
    
	/**
     * Determine and set the color for this wall. Taken from the 
     * Wall class.
     *
     * @param distance to exit
     * @param cc obscure
     */
    public void initColor(final int distance, final int cc) {
    	/*final int d = distance / 4;
        final int rgbValue = calculateRGBValue(d);
        switch (((d >> 3) ^ cc) % 6) {
        case 0:
            setColor(new Color(rgbValue, RGB_DEF, RGB_DEF));
            break;
        case 1:
            setColor(new Color(RGB_DEF, rgbValue, RGB_DEF));
            break;
        case 2:
            setColor(new Color(RGB_DEF, RGB_DEF, rgbValue));
            break;
        case 3:
            setColor(new Color(rgbValue, rgbValue, RGB_DEF));
            break;
        case 4:
            setColor(new Color(RGB_DEF, rgbValue, rgbValue));
            break;
        case 5:
            setColor(new Color(rgbValue, RGB_DEF, rgbValue));
            break;
        default:
            setColor(new Color(RGB_DEF, RGB_DEF, RGB_DEF));
            break;
        }*/
       //don't rly need this method
    }
    
    
    /**
     * Computes an RGB value based on the given numerical value. Taken
     * from the Wall class.
     *
     * @param distance
     *            value to select color
     * @return the calculated RGB value
     */
    private int calculateRGBValue(final int distance) {
        // compute rgb value, depends on distance and x direction
        // 7 in binary is 0...0111
        // use AND to get last 3 digits of distance
        final int part1 = distance & 7;
        final int add = (getExtensionX() != 0) ? 1 : 0;
        final int rgbValue = ((part1 + 2 + add) * 70) / 8 + 80;
        return rgbValue;
    }
    
    
    /**
     * Taken from the Wall class.
     * 
     * @param color, the color to set
     */
    public void setColor(final Color color) {
        col = color;
    }
    
    
    /**
     * Taken from the Wall class.
     * 
     * @return the color
     */
    public Color getColor() {
        return col;
    }
    
    
    /**
     * Equals method that checks if the other object matches in dimensions and
     * content. Taken from the Wall class.
     *
     * @param other
     *            provides fully functional cells object to compare its content
     */
    //not sure if this method is necessary, but it uses a Color object, so I moved
    //it from the Wall class
    public boolean equal(final Object other) {
		// trivial special cases
		/*if (this == other) {
			return true;
		}
		if (null == other) {
			return false;
		}
		if (getClass() != other.getClass()) {
			return false;
		}
		// general case
		final Wall o = (Wall) other; // type cast safe after checking class
		// objects
		// compare all fields
		if ((x != o.x) || (dx != o.dx) || (y != o.y) || (dy != o.dy)) {
			return false;
		}
		if ((dist != o.dist) || (partition != o.partition) || (seen != o.seen)
				|| (col.getRGB() != col.getRGB())) {
			return false;
		}*/
		// all fields are equal, so both objects are equal
		return true;

    }
      
    
    /**
     * Called in the calculateRGBValue method.
     */
    public int getExtensionX() {
        return dx;
    }
    

    
    

	
}
