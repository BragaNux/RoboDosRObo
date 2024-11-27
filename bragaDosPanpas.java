package Yasuo1v1NaoPerde;

import robocode.*;
import java.awt.Color;

public class Yasuo1v1NaoPerde extends Robot {
    
    //principal - comportamento do robo
    @Override
    public void run() {
        setBodyColor(java.awt.Color.black);
        setGunColor(java.awt.Color.red);
        setRadarColor(java.awt.Color.black);

        // Loop
        while (true) {
            ahead(100); //anda 100px
            turnGunRight(360); //procura inimigos
            back(100); //recua 100px
            turnGunRight(360); //procura dnv
        }
    }

    //detecta outro robo
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double distancia = e.getDistance();
        if (distancia < 50) {
            fire(3); //se tiver perto atira 3 vezes
        } else if (distancia < 200) {
            fire(2); //se tiver mediumrage atira 2 vezes
        } else {
            fire(1); //se tiver longe atira uma vez
        }
    }

	//recebe tiro e faz munwalk
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(90); //munwalk pra confundir
        ahead(50); //munwalk pra desviar
    }

    //cheira parede
    @Override
    public void onHitWall(HitWallEvent e) {
        back(50); // viu parede
        turnRight(90); // sai pra nao ficar preso
    }
}
