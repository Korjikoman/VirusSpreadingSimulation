package main;
import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Entity;
import entity.SimulationObject;
import tools.Instruments;

public class CollisionChecker {
	
	SPanel panel;
	
	int maxRow;
	int maxCol;
	
	
	public CollisionChecker(SPanel pan){
		this.panel = pan;
		this.maxCol = pan.maxScreenCol;
		this.maxRow = pan.maxScreenRow;
	
}	
	// COLLISION BETWEEEN OBJECT AND TILES
	public boolean checkTile(Entity entity) {
		// Координаты точек красной зоны
		double entityLeftWorldX = entity.x + entity.solidArea.x; // 
		double entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
		double entityTopWorldY = entity.y + entity.solidArea.y;
		double entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;
		//System.out.println(" Lx"+entityLeftWorldX + "\n Rx"+ entityRightWorldX + "\n Ty"+ entityTopWorldY + "\n By"+ entityBottomWorldY + "\n ");
		
		// 
		int entityLeftCol = (int)entityLeftWorldX / panel.tileSize; // колонка тайлов до leftX
		int entityRightCol =(int) entityRightWorldX / panel.tileSize; // колонка тайлов до rightX
		int entityTopRow = (int)entityTopWorldY / panel.tileSize;
		int entityBottomRow = (int)entityBottomWorldY / panel.tileSize;
		//System.out.println("L"+entityLeftCol + " R"+ entityRightCol + " T"+ entityTopRow + " B"+ entityBottomRow + " ");
		//System.out.println("Direction: " + entity.direction);
		
		int tileNum1, tileNum2;
		
		
		int flag = 0;
		
		// TOP ///////////////////////////////////
		entityTopRow = (int)(entityTopWorldY - entity.speed)/panel.tileSize;
		if (entityTopRow < 0) {entity.collisionTop = true;
		flag = 1;}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			
			
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionTop = true;
				flag = 1;
				
			}
		}
		
		
		entityTopRow = (int)entityTopWorldY / panel.tileSize; // default value
		///////////////////////////////////////////

		// BOTTOM ///////////////////////////////////	
		entityBottomRow = (int)(entityBottomWorldY + entity.speed)/panel.tileSize;
		if (entityBottomRow >= panel.maxScreenRow) {
			entity.collisionBottom = true;
			flag = 1;
			
		}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("DOWN tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionBottom = true;
				flag = 1;
				
			}
		}
		
		entityBottomRow = (int)entityBottomWorldY / panel.tileSize; // default value
		///////////////////////////////////////////
		
		// LEFT ///////////////////////////////////
		entityLeftCol = (int)(entityLeftWorldX - entity.speed)/panel.tileSize;
		if (entityLeftCol < 0) {
			entity.collisionLeft = true;
			flag = 1;
		}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionLeft = true;
				flag = 1;
				
			}
		}
		
		entityLeftCol = (int)entityLeftWorldX / panel.tileSize; // default value
		///////////////////////////////////////////
		
		// RIGHT ///////////////////////////////////
		entityRightCol = (int)(entityRightWorldX + entity.speed)/panel.tileSize;
		if (entityRightCol >= panel.maxScreenCol) {
			entity.collisionRight = true;
			flag = 1;
		}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("RIGHT tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionRight = true;
				flag = 1;
			}
		}
		
		entityRightCol =(int) entityRightWorldX / panel.tileSize; // default value
		
		///////////////////////////////////////////
		if (flag == 1) {
			return true;
		}
		return false;
		
		
	}


	// COLLISION BETWEEEN OBJECTS
	public void checkObjects(SimulationObject simulationObject) {
	    
	    
	    
	    if(!simulationObject.dead) {
	    	ArrayList<SimulationObject> objects = panel.getObjects();
	    	Rectangle objBounds = new Rectangle(
	    	        (int)(simulationObject.x + simulationObject.solidArea.x),
	    	        (int)(simulationObject.y + simulationObject.solidArea.y),
	    	        simulationObject.solidArea.width,
	    	        simulationObject.solidArea.height
	    	    );

	    	    for (SimulationObject other : objects) {
	    	        if (other == simulationObject || other == null) continue;

	    	        Rectangle otherBounds = new Rectangle(
	    	            (int)(other.x + other.solidArea.x),
	    	            (int)(other.y + other.solidArea.y),
	    	            other.solidArea.width,
	    	            other.solidArea.height
	    	        );

	    	        if (objBounds.intersects(otherBounds) && !other.dead) {
	    	           
	    	        	
	    	        	double infNum1 = Instruments.random_number(0.01, 100); // вероятность заражения у нормисов
	    	        	double infNum2 = Instruments.random_number(0.01, 100); // вероятность заражения у нормисов
	    	        	
	    	        	
	    	            	        	
	    	            if (simulationObject.infected && !(other.immune) && (infNum1 < panel.INFECTED_PROBABILITY) ) {
	    	            	other.infected = true;
	    	            	System.out.println("FUCKKKKKKKKKKKKKKKKKKKK");
	    	            	System.out.println("simulationObject.immune" + simulationObject.immune);
	    	            }
	    	            else if (other.infected && !(simulationObject.immune) && (infNum2 < panel.INFECTED_PROBABILITY)) {
	    	            	simulationObject.infected = true;
	    	            	System.out.println("FUCKKKKKKKKKKKKKKKKKKKK");
	    	            	System.out.println("simulationObject.immune" + simulationObject.immune);
	    	            	//System.out.println("Random num = " + randomNum1);
	    	            }
	    	            // если же у него иммунитет, а другой заражен
	    	            else if (simulationObject.infected && other.immune) {
	    	            	if (other.imSys.reInfect()) {
	    	            		// заново заражаем челика
	    	            		other.infected = true;
	    	            		other.immune = true;
	    	            		// отмечаем, что он повторно заболел
	    	            		other.imSys.sick();
	    	            		
	    	            	}
	    	            }
	    	         // если же у него иммунитет, а другой заражен
	    	            else if (other.infected && simulationObject.immune) {
	    	            	if (simulationObject.imSys.reInfect()) {
	    	            		// заново заражаем челика
	    	            		simulationObject.infected = true;
	    	            		simulationObject.immune = true;
	    	            		// отмечаем, что он повторно заболел
	    	            		simulationObject.imSys.sick();
	    	            		
	    	            	}
	    	            }
	    	            
	    	            //HANDLE OBJECT BEHAVIOR AFTER COLLISION
	    	            resolveCollision(simulationObject, other);
	    	        	
	    	        }
	    	    }
	    }
	    
	}
	private void resolveCollision(SimulationObject obj1, SimulationObject obj2) {
	    // Вычисляем векторы нормали между центрами объектов
		double dx = (obj2.x + obj2.solidArea.x + obj2.solidArea.width/2f) - 
	               (obj1.x + obj1.solidArea.x + obj1.solidArea.width/2f);
		double dy = (obj2.y + obj2.solidArea.y + obj2.solidArea.height/2f) - 
	               (obj1.y + obj1.solidArea.y + obj1.solidArea.height/2f);
	    float distance = (float) Math.sqrt(dx*dx + dy*dy);
	    
	    if (distance == 0) return; // Защита от деления на ноль
	    
	    // Нормализуем вектор
	    dx /= distance;
	    dy /= distance;

	    // Вычисляем относительные скорости
	    float v1x = (float) (obj1.speed * Math.sin(Math.toRadians(obj1.angle)));
	    float v1y = (float) (obj1.speed * Math.cos(Math.toRadians(obj1.angle)));
	    float v2x = (float) (obj2.speed * Math.sin(Math.toRadians(obj2.angle)));
	    float v2y = (float) (obj2.speed * Math.cos(Math.toRadians(obj2.angle)));
	    
	    // Проекции скоростей на нормаль
	    double dotProduct1 = v1x*dx + v1y*dy;
	    double dotProduct2 = v2x*dx + v2y*dy;

	    // Обмен компонентами скорости вдоль нормали (упругое столкновение)
	    double newV1x = v1x - dotProduct1 * dx + dotProduct2 * dx;
	    double newV1y = v1y - dotProduct1 * dy + dotProduct2 * dy;
	    double newV2x = v2x - dotProduct2 * dx + dotProduct1 * dx;
	    double newV2y = v2y - dotProduct2 * dy + dotProduct1 * dy;

	    // Обновляем углы движения
	    obj1.angle = (int) Math.toDegrees(Math.atan2(newV1x, newV1y));
	    obj2.angle = (int) Math.toDegrees(Math.atan2(newV2x, newV2y));
	    
	    // Разделяем объекты, чтобы избежать залипания
	    double overlap = (obj1.solidArea.width/2f + obj2.solidArea.width/2f) - distance;
	    if (overlap > 0) {
	    	double separateX = dx * overlap * 0.5f;
	        double separateY = dy * overlap * 0.5f;
	        
	        // Сохраняем старые позиции для проверки коллизий с тайлами
	        double oldX1 = obj1.x;
	        double oldY1 = obj1.y;
	        double oldX2 = obj2.x;
	        double oldY2 = obj2.y;
	        
	        // Пробуем переместить объекты
	        obj1.x -= separateX;
	        obj1.y -= separateY;
	        obj2.x += separateX;
	        obj2.y += separateY;
	        
	        // Проверяем коллизии с тайлами после перемещения
	        if (checkTile(obj1)) {
	            // Если возникла коллизия с тайлом, возвращаем первый объект на место
	            obj1.x = oldX1;
	            obj1.y = oldY1;
	            // И перемещаем только второй объект
	            obj2.x += separateX * 2;
	            obj2.y += separateY * 2;
	            
	            // Проверяем второй объект на коллизии с тайлами
	            if (checkTile(obj2)) {
	                // Если и у второго коллизия, возвращаем оба на место
	                obj2.x = oldX2;
	                obj2.y = oldY2;
	            }
	        } else if (checkTile(obj2)) {
	            // Если только у второго объекта коллизия с тайлом
	            obj2.x = oldX2;
	            obj2.y = oldY2;
	            // Двигаем только первый объект
	            obj1.x -= separateX * 2;
	            obj1.y -= separateY * 2;
	            
	            if (checkTile(obj1)) {
	                // Если и у первого коллизия, возвращаем оба на место
	                obj1.x = oldX1;
	                obj1.y = oldY1;
	            }
	        }
	    }
	    
	    // Обновляем скорости объектов
	    obj1.speed = (float) Math.sqrt(newV1x*newV1x + newV1y*newV1y);
	    obj2.speed = (float) Math.sqrt(newV2x*newV2x + newV2y*newV2y);
	}
		
}