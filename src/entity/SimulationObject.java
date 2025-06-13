package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import immune_system.ImmuneSystem;

import java.awt.*;
import javax.imageio.ImageIO;

import main.SPanel;
import tools.Instruments;


/**
 * Класс SimulationObject наследуется от Entity и представляет собой активный объект симуляции:
 * человек/агент, который может двигаться, заражаться, умирать и выздоравливать.
 */
public class SimulationObject extends Entity {

    private int width;         // Ширина объекта
    private int height;        // Высота объекта
    public double angle;       // Угол направления движения
    private int index;         // Идентификатор (например, индекс в списке)

    // VIRUS-related SETTINGS
    public boolean infected = false;  // Заражен ли
    public boolean immune = false;    // Иммунен ли
    public boolean healthy = true;    // Здоров ли
    public boolean dead = false;      // Мертв ли

    // RECOVER TIMER 
    public Timer timer;               // Таймер, запускающий выздоровление/смерть
    public boolean timerStarted = false; // Был ли запущен таймер

    public SPanel panel;              // Панель, в которой расположен объект

    // IMMUNE SYSTEM
    public ImmuneSystem imSys;       // Иммунная система, связанная с этим объектом

    /**
     * Конструктор объекта симуляции
     */
    public SimulationObject(SPanel sp, float x_pos, float y_pos, int obj_width, int obj_height, int num, boolean is_infected, boolean is_immune, double objVelocity) {
        x = x_pos;
        y = y_pos;
        width = obj_width;
        height = obj_height;
        index = num;
        infected = is_infected;
        immune = is_immune;
        healthy = true;
        speed = objVelocity;

        // Создаем индивидуальную иммунную систему
        imSys = new ImmuneSystem(this);

        this.panel = sp;

        // Устанавливаем прямоугольную зону столкновения
        solidArea = new Rectangle(0,0,width,height);

        // Загружаем изображения объекта
        getObjectImage();

        // Устанавливаем случайный начальный угол движения
        angle = Instruments.random_number(0, 360);

        // Таймер выздоровления / вероятности смерти
        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double random_num = Instruments.random_number(1, 100);
                double death_num = Instruments.random_number(1, 100);

                // Если повезло — выздоравливает и становится иммунным
                if (random_num <= (sp.VACCINE_EFFICIENCY)) {
                    state = "immune";
                    infected = false;
                    immune = true;
                    healthy = false;
                    imSys.sick(); // сообщает иммунной системе
                } else {
                    // Если не повезло — может умереть
                    if (death_num * 2 <= sp.MORALITY) {
                        System.out.println("BAMBY");
                        System.out.println(" infected " + infected + " immune " + immune);
                        System.out.println("BAMBY");
                        infected = false;
                        immune = false;
                        healthy = false;
                        dead = true;
                    }
                }
            }
        });
    }

    // Геттер скорости
    public double getSpeed() {
        return speed;
    }

    // Геттер ширины объекта
    public int getObjWidth () {
        return width;
    }

    /**
     * Основной метод перемещения объекта
     */
    public void move() {
        if (!dead) { // если объект жив

            // Сброс флагов столкновений
            collisionTop = false;
            collisionBottom = false;
            collisionLeft = false;
            collisionRight = false;

            // Проверка столкновений с тайлами и другими объектами
            panel.cChecker.checkTile(this);
            panel.cChecker.checkObjects(this);

            // Обновление состояния в зависимости от заражения/иммунитета
            if (infected && !immune) {
                state = "sick";
            } else if (immune && !infected) {
                state = "immune";
            } else if (immune && infected) {
                state = "reinfected";
            }

            // Обработка столкновений
            if (collisionTop || collisionBottom) {
                angle = 180 - angle; // отражаем по горизонтали
            }

            if (collisionLeft || collisionRight) {
                angle = -angle; // отражаем по вертикали
            }

            // Вычисляем смещение по x и y с учетом текущего угла
            float dx = (float) (speed * Math.sin(Math.toRadians(angle)));
            float dy = (float) (speed * Math.cos(Math.toRadians(angle)));

            // Устанавливаем направление в зависимости от dx/dy
            if (dy < 0 && dx == 0) direction = "up";
            if (dy > 0 && dx == 0) direction = "down";
            if (dy == 0 && dx < 0) direction = "left";
            if (dy == 0 && dx > 0) direction = "right";
            if (dy < 0 && dx < 0) direction = "up-left";
            if (dy < 0 && dx > 0) direction = "up-right";
            if (dy > 0 && dx < 0) direction = "down-left";
            if (dy > 0 && dx > 0) direction = "down-right";

            // Обновляем позицию
            x += dx;
            y += dy;

        } else { // если мертв
            state = "dead";
            timer.stop(); // отключаем таймер выздоровления
        }

        // Обновление анимации (чередование спрайтов)
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    // Получить индекс (номер) объекта
    public int getNum() {
        return index;
    }

    /**
     * Отрисовка объекта на экране
     */
    public void paintObj(Graphics2D g2) {
        BufferedImage image = null;

        // Выбираем спрайт в зависимости от состояния и кадра
        switch(state) {
            case "norm":
                image = (spriteNum == 1) ? normBro : normBro2;
                break;
            case "sick":
                image = (spriteNum == 1) ? sickBro : sickBro2;
                break;
            case "immune":
                image = (spriteNum == 1) ? immuneBro : immuneBro2;
                break;
            case "reinfected":
                image = (spriteNum == 1) ? reinfectedBro : reinfectedBro2;
                break;
            case "dead":
                image = (spriteNum == 1) ? deadBro : deadBro2;
                break;
        }

        // Рисуем изображение в текущей позиции
        g2.drawImage(image, (int)x, (int)y, width, height, null);
    }

    /**
     * Загрузка изображений из ресурсов
     */
    public void getObjectImage() {
        try {
            normBro = ImageIO.read(getClass().getResourceAsStream("/player/normBro.png"));
            normBro2 = ImageIO.read(getClass().getResourceAsStream("/player/normBro2.png"));
            sickBro = ImageIO.read(getClass().getResourceAsStream("/player/sickBro.png"));
            sickBro2 = ImageIO.read(getClass().getResourceAsStream("/player/sickBro2.png"));
            immuneBro = ImageIO.read(getClass().getResourceAsStream("/player/immuneBro.png"));
            immuneBro2 = ImageIO.read(getClass().getResourceAsStream("/player/immuneBro2.png"));
            reinfectedBro = ImageIO.read(getClass().getResourceAsStream("/player/reInfectedBro.png"));
            reinfectedBro2 = ImageIO.read(getClass().getResourceAsStream("/player/reInfectedBro2.png"));
            deadBro = ImageIO.read(getClass().getResourceAsStream("/player/deadBro.png"));
            deadBro2 = ImageIO.read(getClass().getResourceAsStream("/player/deadBro2.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Запуск таймера выздоровления/смерти
     */
    public void startRecover() {
        timer.start();
        timerStarted = true;
    }

    /**
     * Остановка таймера выздоровления/смерти
     */
    public void stopRecover() {
        timer.stop();
        timerStarted = false;
    }
}
