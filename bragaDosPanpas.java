package BrayanMartins;

import robocode.*;
import java.awt.Color;

public class BrayanMartins extends Robot {
    boolean isCheiraParede = false; //é o CheiraParede
    boolean isBlablade = false; //é o Blablade

    //principal - comportamento do robo
    @Override
    public void run() {
        setBodyColor(java.awt.Color.black);
        setGunColor(java.awt.Color.red);
        setRadarColor(java.awt.Color.black);

        //loop
        while (true) {
            //muda com base na vida
            if (getEnergy() > 50) {
                //ta cheio de vida, bora agressivar
                ahead(150); //avança mais pra pressão
                turnGunRight(360); //procura inimigos
                back(100); //recua pra enganar
                turnGunRight(360); //procura dnv
            } else {
                //vida ta baixa, bora virar um ninja e sobreviver
                turnRight(90); //movimento evasivo
                ahead(100); //foge estrategicamente
                turnGunRight(360); //ainda procura inimigos porque a vingança nunca é plena
            }
        }
    }

    //detecta outro robo
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double distancia = e.getDistance();
        double energiaInimigo = e.getEnergy();

        // Detecta se o robô inimigo é o CheiraParede
        if (e.getName().contains("Walls")) {
            isCheiraParede = true;
        }

        // Detecta se o robô inimigo é o Blablade
        if (e.getName().contains("SpinBot")) {
            isBlablade = true;
        }

        if (isCheiraParede) {
            // Estratégia especial contra CheiraParede
            fire(3); // Força máxima porque ele só cheira parede mesmo
            turnRight(90); // Sai do caminho do cheirador
            ahead(200); // Ganha distância porque parede não atira
        } else if (isBlablade) {
            // Estratégia especial contra Blablade
            turnGunRight(360); // Fica girando pra acompanhar o giro dele
            fire(2); // Dá tiros frequentes pra quebrar o ciclo do giro
            back(100); // Mantém distância pra evitar colisões
        } else {
            if (distancia < 50) {
                fire(3); //se tiver perto atira 3 vezes (close-range massacre!)
            } else if (distancia < 200) {
                fire(2); //se tiver medium-range atira 2 vezes
            } else {
                fire(1); //se tiver longe atira uma vez
            }

            //estrategia para inimigos fracos
            if (energiaInimigo < 20) {
                //ta fraco? finaliza com estilo
                fire(3);
                turnRight(30);
                ahead(50); //se aproxima pra garantir a vitoria
            }
        }
    }

    //recebe tiro e faz munwalk
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        if (getEnergy() > 30) {
            turnLeft(90); //munwalk pra confundir
            ahead(50); //munwalk pra desviar
        } else {
            //ta fraco? movimento mais radical
            turnRight(180); //manda aquele giro inesperado
            ahead(100); //foge com elegancia
        }
    }

    //cheira parede
    @Override
    public void onHitWall(HitWallEvent e) {
        //ajusta o comportamento pra evitar dano desnecessario
        if (getEnergy() > 50) {
            back(50); //viu parede
            turnRight(90); //sai pra nao ficar preso
        } else {
            //energia baixa maior evasao
            back(100);
            turnLeft(90);
        }
    }
}
