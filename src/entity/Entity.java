package entity;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * Класс Entity представляет собой базовый объект (сущность), 
 * который участвует в симуляции (например, человек).
 */
public class Entity {

    // Скорость движения сущности
    public double speed;

    // Координаты положения сущности на экране
    public double x, y;

    // Изображения сущности в разных состояниях и анимациях
    public BufferedImage 
        normBro,         // Обычное состояние (здоровый)
        normBro2,        // Вторая фаза анимации здорового
        sickBro,         // Больной
        sickBro2,        // Вторая фаза анимации больного
        immuneBro,       // Иммунный
        immuneBro2,      // Вторая фаза анимации иммунного
        reinfectedBro,   // Повторно заражённый
        reinfectedBro2,  // Вторая фаза повторного заражения
        deadBro,         // Мёртвый
        deadBro2;        // Альтернативное изображение мёртвого (необязательно используется)

    // Текущее состояние сущности:
    // "norm", "sick", "immune", "reinfected", "dead"
    public String state = "norm";

    // Направление движения: "up", "down", "left", "right"
    public String direction = "down";

    // Счётчик кадров спрайта (для анимации)
    public int spriteCounter = 0;

    // Номер текущего кадра спрайта (например, 1 или 2)
    public int spriteNum = 1;

    // Прямоугольная область, используемая для проверки столкновений
    public Rectangle solidArea;

    // Флаги столкновения: указывают, столкнулась ли сущность с чем-то
    public boolean collisionTop = false;
    public boolean collisionBottom = false;
    public boolean collisionLeft = false;
    public boolean collisionRight = false;
}
