package de.flxnet.pixelwall.panels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

public class Panel {

	@Expose
	@Getter
	private String name;
	
	@Expose
	@Getter @Setter
	private URL imageSource;
	
	@Expose
	@Getter @Setter
	private PanelLocation location;
	
	@Getter @Setter
	private BufferedImage image;
	
	@Getter
	private boolean imageLoaded = false;
	
	@Getter
	private BufferedImage[] cutImages;
	
	@Getter
	private int rows;

	@Getter
	private int columns;

	private int reminderX;
	private int reminderY;
	
	private static final int WIDTH = 128;
	private static final int HEIGHT = 128;
	
	@Getter
	private PanelImageRender[] tileRenders;
	
	@Getter @Setter
	private boolean rendered = false;
	
	@Expose
	@Getter @Setter
	private boolean itemFramesVisible = false;
	
	public Panel(String name, URL imageSource, PanelLocation location) {
		this.name = name;
		this.imageSource = imageSource;
		this.location = location;
	}
	
	// STEP 1
	public boolean loadImage() {
		try {
			image = ImageIO.read(imageSource);
			if(image != null) {
				imageLoaded = true;
			}
		} catch(Exception ex) {
			imageLoaded = false;
		}
		return imageLoaded;
	}
	
	// STEP 2
	public void calculateDimensions() {	
		int originalWidth = image.getWidth();
		int originalHeight = image.getHeight();
		
		columns = (int) Math.ceil(originalWidth / WIDTH);
		rows = (int) Math.ceil(originalHeight / HEIGHT);
		
		reminderX = originalWidth % WIDTH;
		reminderY = originalHeight % HEIGHT;

		if (reminderX > 0) columns++;
		if (reminderY > 0) rows++;
	}

	// STEP 3
	public void splitImage() {
		int imageCount = getImageCount();
		cutImages = new BufferedImage[imageCount];
		tileRenders = new PanelImageRender[imageCount];

		int imageX;
		int imageY = reminderY == 0 ? 0 : (reminderY - HEIGHT) / 2;
		for (int i = 0; i < rows; i++) {
			imageX = reminderX == 0 ? 0 : (reminderX - WIDTH) / 2;
			for (int j = 0; j < columns; j++) {
				cutImages[i * columns + j] = makeSubImage(image, imageX, imageY);
				imageX += WIDTH;
			}
			imageY += HEIGHT;
		}

		image = null;
	}
	
	// STEP 4
	public boolean renderImage() {
		for(int i = 0; i < cutImages.length; i++) {
			PanelImageRender render = new PanelImageRender(cutImages[i], itemFramesVisible, location.getDirection());
			tileRenders[i] = render;
			boolean finish = render.finish();
			if(!finish) rendered = false;
		}
		ArrayUtils.reverse(tileRenders);
		rendered = true;
		return rendered;
	}
	
	public void update(URL url, PanelLocation panelLocation) {
		if(url != null) imageSource = url;
		location = panelLocation;
		rendered = false;
	}
	
	public void move(int x, int y, int z) {
		location.setX(location.getX() + x);
		location.setX(location.getY() + y);
		location.setX(location.getZ() + z);
	}
	
	private BufferedImage makeSubImage(BufferedImage originalImage, int x, int y) {
		BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = newImage.getGraphics();
		graphics.drawImage(originalImage, -x, -y, null);
		graphics.dispose();
		return newImage;
	}
	
	public BufferedImage getImageAt(int i) {
		return cutImages[i];
	}

	public int getColumnAt(int i) {
		return i % columns;
	}

	public int getRowAt(int i) {
		return i / columns;
	}
	
	public int getImageCount() {
		return columns * rows;
	}
	
	@Override
	public String toString() {
		return "Tile[source=" + imageSource + ", loaded=" + this.imageLoaded + "]";
	}
	
}
