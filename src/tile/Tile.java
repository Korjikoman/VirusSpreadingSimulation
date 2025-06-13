package tile;

import java.awt.image.BufferedImage;
/*
 * Класс, представляющий отдельный тайл (ячейку карты)
 */
public class Tile {
	public BufferedImage image;// Изображение тайла (текстура)
	public boolean collision = false;// Флаг, указывающий, есть ли коллизия (столкновение) с этим тайлом
}

