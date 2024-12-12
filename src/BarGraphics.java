import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

class BarGraphics extends JPanel {
	private Board board;

	public BarGraphics(final Board gui, int num) {
		super(null, true);
		board = gui;
		board.add(this);

		setBounds(Board.getGeometry().getBarBounds(num));
		setBackground(Palette.getBarColour());
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
	}

	protected void paintDisk(final Graphics2D g2, final PlayerColor c, final int number, final int totalNumber) {
		final Color diskColour = Palette.getPieceColor(c);
		final Shape diskShape = this.diskEllipse(c, number, totalNumber);
		g2.setColor(Color.BLUE);
		g2.draw(diskShape);
		g2.setColor(diskColour);
		g2.fill(diskShape);
	}

	private Shape diskEllipse(final PlayerColor c, final int k, final int n) {
		final BoardGeometry geo = Board.getGeometry();
		final double diskWidth = geo.getDiskWidth();
		final double regionHeight = 0.45 * geo.getBoardHeight();
		final double overlap = Math.max(-2.0, diskWidth - regionHeight / n);
		double yLoc = (k - 1) * (diskWidth - overlap);
		if (c == PlayerColor.WHITE) {
			yLoc = geo.getBarHeight() - yLoc - diskWidth;
		}
		return new Ellipse2D.Double(0.0, yLoc, diskWidth, diskWidth);
	}
	
	/* public void drawQuestionMark(final Graphics2D g2, final int barNumber) {
	        // Load the image (question mark icon)
	        Image questionMarkImage = Toolkit.getDefaultToolkit().getImage("images/question-mark-icon-free-vector.jpg");
	        
	        // Wait for the image to load
	        MediaTracker tracker = new MediaTracker(this);
	        tracker.addImage(questionMarkImage, 0);
	        try {
	            tracker.waitForAll();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        
	        // Get the bounds of the specified bar using getBarBounds
	        Rectangle barBounds = Board.getGeometry().getBarBounds(barNumber);
	        
	        // The x position is the leftmost position of the bar, adjust y as needed
	        int x = barBounds.x + (barBounds.width / 2) - 15;  // Center the image horizontally on the bar
	        int y = barBounds.y + (barBounds.height / 2) - 15; // Center the image vertically on the bar

	        int width = 5;   // Image width (adjust as needed)
	        int height = 5;  // Image height (adjust as needed)

	        // Draw the image
	        g2.drawImage(questionMarkImage, x, y, width, height, this);
	    }*/
}
