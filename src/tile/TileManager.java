package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import main.SPanel;

public class TileManager {
	SPanel panel;
	public Tile[] tile;
	public int mapTileNum[][];
	private static final int TILE_NUM = 10; 
	
	
	public TileManager(SPanel sp) {
		this.panel = sp;
		
		tile = new Tile[TILE_NUM];
		
		mapTileNum = new int[panel.maxScreenCol][panel.maxScreenRow];
		
		getTileImage();
		
		
	}
	
	
	
	
	public void loadMap(String filePath) {
		System.out.println("LOADING MAP --> " + filePath);
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			while(col < panel.maxScreenCol && row < panel.maxScreenRow) {
				// read a row
				String line = br.readLine();
				
				while(col < panel.maxScreenCol) {
					String numbers[] = line.split(" ");
					// string to int
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num;
					col++;
					
				}
				if (col == panel.maxScreenCol) {
					col = 0;
					row++;
				}
			}
			br.close();
		}catch(Exception e) {
			
		}
		
		
	}
	
	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/land.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[1].collision = true;
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		
		while(col < panel.maxScreenCol && row < panel.maxScreenRow) {
			int tileNum = mapTileNum[col][row];
			
			g2.drawImage(tile[tileNum].image, x, y, panel.tileSize, panel.tileSize, null);
			col ++;
			x += panel.tileSize;
			if (col == panel.maxScreenCol) {
				col = 0;
				x = 0;
				row ++;
				y += panel.tileSize;
			}
			
		}
		
	}
}
